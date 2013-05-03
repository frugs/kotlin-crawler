package com.frugs.dungeoncrawler.game

import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Dome
import groovy.transform.CompileStatic
import groovyx.gpars.stm.GParsStm

@CompileStatic
class Player extends Node {

    float speed = 8.0f
    float angularSpeed = FastMath.DEG_TO_RAD * 3.0f * 60

    Vector3f facingDirection

    Player(Material mat) {
        super("player")
        Geometry dome = createDome(mat)
        attachChild(dome)
        this.facingDirection = Vector3f.UNIT_Z
    }

    @Override
    synchronized Spatial rotate(Quaternion rotation) {
        facingDirection = rotation.toRotationMatrix().mult(facingDirection)
        super.rotate(rotation)
    }

    @Override
    synchronized Spatial move(Vector3f offset) {
        super.move(offset)
    }

    //return value is true if we've got more to go
    boolean moveTowardsDestination(Vector3f destination, float tpf) {
        GParsStm.atomicWithBoolean {
            def remainingTravel = destination.subtract(localTranslation)
            def displacement = remainingTravel.normalize().mult(speed).mult(tpf)

            if (remainingTravel.length() == 0) {
                return false
            }

            def stillMoving = displacement.length() < remainingTravel.length()
            stillMoving ? move(displacement) : move(remainingTravel)
            stillMoving
        }
    }

    //return value is true if we've got more to rotate
    boolean rotateTowardsDestination(Vector3f destination, float tpf) {
        GParsStm.atomicWithBoolean {
            def normalisedDirection = destination.subtract(localTranslation).normalize()
            def angleToDestination = normalisedDirection.angleBetween(facingDirection)

            boolean stillRotating = angularSpeed * tpf < angleToDestination
            float rotation = stillRotating ? angularSpeed * tpf : angleToDestination
            rotation = getClockwiseOrAntiClockwise(rotation, normalisedDirection)

            rotate(0.0f, rotation, 0.0f)
            stillRotating
        }
    }

    private float getClockwiseOrAntiClockwise(float rotation, Vector3f normalisedDirection) {
        def clockwise = Quaternion.ZERO.fromAngleNormalAxis(rotation, Vector3f.UNIT_Y)
        def proposedClockwiseVector = normalisedDirection.subtract(clockwise.toRotationMatrix().mult(facingDirection))

        def antiClockwise = Quaternion.ZERO.fromAngleNormalAxis(FastMath.TWO_PI - rotation, Vector3f.UNIT_Y)
        def proposedAntiClockwiseVector = normalisedDirection.subtract(antiClockwise.toRotationMatrix().mult(facingDirection))

        proposedAntiClockwiseVector.length() > proposedClockwiseVector.length() ? rotation : FastMath.TWO_PI - rotation
    }

    private Geometry createDome(Material mat) {
        Geometry dome = new Geometry("Dome", new Dome(Vector3f.ZERO, 2, 4, 1.5f, true))
        dome.rotate(FastMath.HALF_PI, 0.0f, 0.0f)
        mat.setColor("Color", ColorRGBA.Blue)
        material = mat
        dome.material = mat
        dome
    }
}
