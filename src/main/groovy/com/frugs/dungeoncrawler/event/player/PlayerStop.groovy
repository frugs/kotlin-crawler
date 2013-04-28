package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.game.Player
import com.frugs.dungeoncrawler.event.Interrupter
import groovy.transform.CompileStatic

@CompileStatic
class PlayerStop implements Interrupter {

    final long timeIssued

    PlayerStop(long timeIssued, Player player) {
        this.timeIssued = timeIssued
    }

    PlayerStop(Player player) {
        this.timeIssued = System.currentTimeMillis()
    }

    @Override
    void process(float tpf) {}
}
