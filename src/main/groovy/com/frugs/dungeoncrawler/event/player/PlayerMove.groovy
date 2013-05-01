package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.event.EventManager
import com.frugs.dungeoncrawler.game.Player
import com.frugs.dungeoncrawler.event.Interruptable
import com.frugs.dungeoncrawler.event.Interrupter
import com.jme3.math.Vector3f
import groovyx.gpars.agent.Agent
import groovy.transform.CompileStatic

@CompileStatic
class PlayerMove implements Interruptable, Interrupter {

    private static final Vector3f MARGIN = Vector3f.UNIT_XYZ.mult(0.1f)
    private boolean endOfChain = true
    final long timeIssued

    Vector3f destination
    Agent<Player> player
    EventManager eventManager

    PlayerMove(Vector3f destination, Agent<Player> player, EventManager eventManager, long timeIssued) {
        this.eventManager = eventManager
        this.destination = destination
        this.player = player
        this.timeIssued = timeIssued
    }

    PlayerMove(Vector3f destination, Agent<Player> player, EventManager eventManager) {
        this(destination, player, eventManager, System.currentTimeMillis())
    }

    @Override
    void process(float tpf) {

        player << { Player player ->
            def moved = player.moveTowardsDestination(destination, tpf)
            def rotated = player.rotateTowardsDirection(destination.normalize(), tpf)

            if (moved || rotated) {
                new PlayerMove(destination, new Agent<Player>(player), eventManager, timeIssued)
            } else {
                eventManager.queueEvent(new PlayerStop(System.currentTimeMillis(), player))
            }
        }
    }

    @Override
    List<? extends Interrupter> getInterruptedBy() {
        [PlayerMove, PlayerStop] as List<? extends Interrupter>
    }
}
