package com.frugs.dungeoncrawler.event

trait Interrupter: Event {
    public val timeIssued: Long
}