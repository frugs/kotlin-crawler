package com.frugs.dungeoncrawler.event

import com.jme3.app.Application
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import groovyx.gpars.GParsPool

import static groovy.transform.TypeCheckingMode.SKIP

@CompileStatic
class EventManager extends AbstractAppState{

    EventManager() {}

    private List<Event> eventQueue = []
    private List<Event> nextEvents = []

    void queueEvent(Event event) {
        nextEvents << event
    }

    @Override
    void update(float tpf) {
        super.update(tpf)
        performInterrupts()
        processQueue(tpf)
        loadNextEvents()
    }

    @Override
    void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app)
        enabled = true
    }

    @Override
    void setEnabled(boolean enabled) {
        super.setEnabled(enabled)
    }

    private void performInterrupts() {
        def interrupters = []
        def interruptibles = []
        eventQueue.each { Event event -> event
            if (event instanceof Interrupter) {
                interrupters << event as Interrupter
            }
            if (event instanceof Interruptible) {
                interruptibles << event
            }
        }

        interruptibles.each { Interruptible interruptible ->
            def interrupterExists = interrupters.any { Interrupter interrupter ->
                interruptible.interruptedBy.any { interrupter.class == it && interruptible.timeIssued < interrupter.timeIssued }
            }
            if (interrupterExists) {
                eventQueue.remove(interruptible)
            }
        }
    }

    @TypeChecked(SKIP)
    private void processQueue(float tpf) {
        def runners = eventQueue.collect { Event event -> new EventRunner(event, tpf, this) }
        GParsPool.withPool {
            runners.eachParallel { EventRunner runner -> runner.call() }
        }
    }

    private void loadNextEvents() {
        eventQueue = nextEvents
        nextEvents = []
    }
}
