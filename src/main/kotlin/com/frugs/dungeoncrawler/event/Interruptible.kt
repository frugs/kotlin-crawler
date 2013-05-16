package com.frugs.dungeoncrawler.event


trait Interruptible : Chainable {
    public val timeIssued: Long
    public fun interruptibleBy(interruptors: List<Interrupter>): Boolean
    final public fun shouldBeInterrupted(interruptors: List<Interrupter>): Boolean =
        interruptibleBy(interruptors) && timeIssued < interruptors.sortBy { it.timeIssued }.last!!.timeIssued
}