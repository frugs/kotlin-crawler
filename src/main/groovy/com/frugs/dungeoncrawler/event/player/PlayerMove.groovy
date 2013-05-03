package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.event.Event
import com.frugs.dungeoncrawler.event.EventManager
import com.frugs.dungeoncrawler.event.Interrupter
import com.frugs.dungeoncrawler.event.Interruptible
import com.frugs.dungeoncrawler.game.Player
import com.jme3.math.Vector3f
import groovy.transform.CompileStatic

@CompileStatic
class PlayerMove implements Interruptible, Interrupter {

    final long timeIssued
    private final boolean rotate

    private boolean endOfChain = true
    private boolean rotated = false

    Vector3f destination
    Player player
    EventManager eventManager

    PlayerMove(Vector3f destination, Player player, boolean rotate, long timeIssued) {
        this.rotate = rotate
        this.eventManager = eventManager
        this.destination = destination
        this.player = player
        this.timeIssued = timeIssued
    }

    PlayerMove(Vector3f destination, Player player, boolean rotate) {
        this(destination, player, rotate, System.currentTimeMillis())
    }

    @Override
    void process(float tpf) {
        def moved = player.moveTowardsDestination(destination, tpf)
        rotated = rotate ? player.rotateTowardsDestination(destination, tpf) : false
        endOfChain = moved || rotated
    }

    @Override
    List<? extends Interrupter> getInterruptedBy() {
        [PlayerMove, PlayerStop] as List<? extends Interrupter>
    }

    @Override
    Event getChain() {
        endOfChain ? new PlayerMove(destination, player, rotated, timeIssued) : new PlayerStop(player)
    }
}
