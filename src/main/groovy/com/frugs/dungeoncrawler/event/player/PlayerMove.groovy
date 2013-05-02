package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.event.Event
import com.frugs.dungeoncrawler.event.Interrupter
import com.frugs.dungeoncrawler.event.Interruptible
import com.frugs.dungeoncrawler.game.Player
import com.frugs.dungeoncrawler.util.Radians
import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import groovy.transform.CompileStatic

@CompileStatic
class PlayerMove implements Interruptible, Interrupter {

    private static final Vector3f MARGIN = Vector3f.UNIT_XYZ.mult(0.1f)
    private boolean endOfChain = true
    final long timeIssued

    Vector3f destination
    Player player

    PlayerMove(Vector3f destination, Player player, long timeIssued) {
        this.destination = destination
        this.player = player
        this.timeIssued = timeIssued
    }

    PlayerMove(Vector3f destination, Player player) {
        this.destination = destination
        this.player = player
        this.timeIssued = System.currentTimeMillis()
    }

    @Override
    void process(float tpf) {
        Vector3f normalisedDirection = destination.subtract(player.localTranslation).normalize()
        float angleToDestination = normalisedDirection.angleBetween(player.facingDirection)

        if (angleToDestination > 0.01f) {
            def refVector = normalisedDirection.cross(Vector3f.UNIT_Y)
            def refAngle = normalisedDirection.angleBetween(refVector)
            float rotationAngle = Radians.smallerAbsolute(angleToDestination % FastMath.PI, player.angularSpeed)
            def angularVelocity = refAngle > angleToDestination ? rotationAngle : FastMath.TWO_PI - rotationAngle
            Quaternion rotation = Quaternion.ZERO.fromAngleNormalAxis(angularVelocity, Vector3f.UNIT_Y)
            player.rotate(rotation)
            endOfChain = false
        }

        if (!reachedDestination()) {
            player.move(normalisedDirection.mult(player.speed).mult(tpf))
            endOfChain = false
        }
    }

    @Override
    Event getChain() {
        if(endOfChain) {
            new PlayerStop(System.currentTimeMillis(), player)
        } else {
            new PlayerMove(destination, player, timeIssued)
        }
    }

    @Override
    List<? extends Interrupter> getInterruptedBy() {
        [PlayerMove, PlayerStop] as List<? extends Interrupter>
    }

    private boolean reachedDestination() {
        Vector3f difference = absolute(player.localTranslation.subtract(destination))
        difference.x < MARGIN.x && difference.y < MARGIN.y && difference.z < MARGIN.z
    }

    private static Vector3f absolute(Vector3f vector) {
        float x = vector.x > 0.0f ? vector.x : -vector.x
        float y = vector.y > 0.0f ? vector.y : -vector.y
        float z = vector.z > 0.0f ? vector.z : -vector.z

        new Vector3f(x, y, z)
    }
}
