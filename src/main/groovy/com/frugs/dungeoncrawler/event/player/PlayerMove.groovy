package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.event.Event
import com.frugs.dungeoncrawler.event.EventManager
import com.frugs.dungeoncrawler.game.Player
import com.frugs.dungeoncrawler.event.Interruptible
import com.frugs.dungeoncrawler.event.Interrupter
import com.jme3.math.Vector3f
import groovy.transform.CompileStatic
import groovyx.gpars.stm.GParsStm

@CompileStatic
class PlayerMove implements Interruptible, Interrupter {

    private boolean endOfChain = true
    final long timeIssued

    Vector3f destination
    Player player
    EventManager eventManager

    PlayerMove(Vector3f destination, Player player, long timeIssued) {
        this.eventManager = eventManager
        this.destination = destination
        this.player = player
        this.timeIssued = timeIssued
    }

    PlayerMove(Vector3f destination, Player player) {
        this(destination, player, System.currentTimeMillis())
    }

    @Override
    void process(float tpf) {
        GParsStm.atomic {
            def moved = player.moveTowardsDestination(destination, tpf)
            def rotated = player.rotateTowardsDirection(destination.normalize(), tpf)
            endOfChain = moved || rotated
        }
    }

    @Override
    List<? extends Interrupter> getInterruptedBy() {
        [PlayerMove, PlayerStop] as List<? extends Interrupter>
    }

    @Override
    Event getChain() {
        endOfChain ? new PlayerMove(destination, player, timeIssued) : new PlayerStop(player)
    }
}
