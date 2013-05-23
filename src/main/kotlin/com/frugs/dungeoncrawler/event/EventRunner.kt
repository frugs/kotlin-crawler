package com.frugs.dungeoncrawler.event

import java.util.concurrent.Callable


class EventRunner(val tpf: Float, val event: Event) : Callable<Unit> {

    override public fun call() {
        event.process(tpf)
        if (event is Chainable) EventManager.queueEvent(event.chain)
    }
}