package com.frugs.dungeoncrawler.event

import groovy.transform.CompileStatic

import java.util.concurrent.Callable

@CompileStatic
class EventRunner implements Callable {

    private final Event event
    private final float tpf
    private EventManager manager

    EventRunner(Event event, float tpf, EventManager manager) {
        this.manager = manager
        this.event = event
        this.tpf = tpf
    }

    def call() {
        event.process(tpf)

        if (event instanceof Chainable) {
            Event chain = (event as Chainable).chain
            manager.queueEvent(chain)
        }
    }
}
