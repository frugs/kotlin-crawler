package com.frugs.dungeoncrawler.event

trait Chainable : Event {
    public val chain: Event
}
