package com.frugs.dungeoncrawler.event

import com.jme3.app.state.AbstractAppState
import sun.nio.ch.Interruptible
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EventManager : AbstractAppState() {

    private val executor: ExecutorService = Executors.newCachedThreadPool()

    private var nextEvents: MutableList<Event> = linkedListOf()
    private var eventQueue: MutableList<Event> = linkedListOf()

    public var enabled: Boolean = true
        get() = $enabled
        set(state: Boolean) { $enabled = state }

    public fun queueEvent(event: Event) { synchronized<Unit>(this) { nextEvents.add(event) } }

    override public fun update(tpf: Float) {
        fun processQueue() {
            val runners = eventQueue.map { event -> EventRunner(this, tpf, event) }
            executor.invokeAll(runners)
        }

        super.update(tpf)

        performInterrupts()
        processQueue()
        loadNextEvents()
    }

    private fun loadNextEvents() {
        synchronized(this) {
            eventQueue = nextEvents
            nextEvents = linkedListOf()
        }
    }

    private fun performInterrupts() {
        val interruptors: List<Interrupter> = eventQueue.filter { it is Interrupter }.map { it as Interrupter }
        eventQueue.removeAll(eventQueue.filter { it is Interruptible && it.shouldBeInterrupted(interruptors) })
    }

    override public fun isEnabled() = enabled
}