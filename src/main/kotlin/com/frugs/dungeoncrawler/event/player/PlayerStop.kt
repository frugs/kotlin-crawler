package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.event.Interrupter

class PlayerStop(override public val timeIssued: Long = System.currentTimeMillis()) : Interrupter {
    override public fun process(tpf: Float) {}
}