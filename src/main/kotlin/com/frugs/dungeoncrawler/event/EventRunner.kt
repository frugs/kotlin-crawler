package com.frugs.dungeoncrawler.event

import java.util.concurrent.Callable


class EventRunner(val eventManager: EventManager, val tpf: Float, val event: Event) : Callable<Unit> {

    override public fun call() {
        event.process(tpf)
        if (event is Chainable) eventManager.queueEvent(event.chain)
    }
}