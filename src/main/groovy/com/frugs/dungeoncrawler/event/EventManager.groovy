package com.frugs.dungeoncrawler.event

import com.jme3.app.Application
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import groovy.transform.CompileStatic

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@CompileStatic
class EventManager extends AbstractAppState{

    EventManager() {}

    private List<Event> eventQueue = []
    private List<Event> nextEvents = []
    private ExecutorService threadPool = Executors.newCachedThreadPool()

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

    private void processQueue(float tpf) {
        def runners = eventQueue.collect { Event event -> new EventRunner(event, tpf, this) }
        threadPool.invokeAll(runners)
    }

    private void loadNextEvents() {
        eventQueue = nextEvents
        nextEvents = []
    }
}
