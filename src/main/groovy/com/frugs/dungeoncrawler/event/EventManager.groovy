package com.frugs.dungeoncrawler.event

import com.google.inject.Inject
import com.jme3.app.Application
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import groovy.transform.CompileStatic

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Singleton
@CompileStatic
class EventManager extends AbstractAppState{

    @Inject
    private EventManager() {}

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
        def interruptables = []
        eventQueue.each { Event event -> event
            if (event instanceof Interrupter) {
                interrupters << event as Interrupter
            }
            if (event instanceof Interruptable) {
                interruptables << event
            }
        }

        interruptables.each { Interruptable interruptable ->
            def interrupterExists = interrupters.any { Interrupter interrupter ->
                interruptable.interruptedBy.any { interrupter.class == it && interruptable.timeIssued < interrupter.timeIssued }
            }
            if (interrupterExists) {
                eventQueue.remove(interruptable)
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
