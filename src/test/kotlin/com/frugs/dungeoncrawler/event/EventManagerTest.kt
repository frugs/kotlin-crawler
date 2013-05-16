package com.frugs.dungeoncrawler.event

import org.junit.Test
import kotlin.test.fail

class EventManagerTest {

    val eventManager = EventManager()
    var counter: Long = 0

    inner class ConcurrentEvent : Event {
        override fun process(tpf: Float) {
            val run = counter
            Thread.sleep(1000 * run)
            counter = run + run
        }
    }

    inner class TestInterrupter(public override val timeIssued: Long) : Interrupter {
        override public fun process(tpf: Float) { counter-- }
    }

    inner class TestInterruptible(public override val timeIssued: Long) : Interruptible {
        override public fun interruptibleBy(interruptors: List<Interrupter>): Boolean = interruptors.any { it is TestInterrupter }
        override public fun process(tpf: Float) { counter++ }
        override public val chain: Event
            get() = TestInterruptible(timeIssued)
    }

    inner class TestSelfInterruptible(override public val timeIssued: Long) : Interruptible, Interrupter {
        override public fun interruptibleBy(interruptors: List<Interrupter>) = interruptors.any { it is TestSelfInterruptible }
        override public fun process(tpf: Float) { counter += timeIssued }
        override public val chain: Event
            get() = TestSelfInterruptible(timeIssued)
    }

    inner class TestChain : Chainable {
        override public val chain: Event
            get() = if (counter < 5) TestChain() else ImpotentEvent()
        override public fun process(tpf: Float) { counter++ }
    }

    inner class ImpotentEvent : Event {
        override public fun process(tpf: Float) {}
    }

    Test fun update_shouldProcessEventsConcurrently_ThenWaitForAllEventsToFinishProcessingBeforeExiting() {
        listOf(ConcurrentEvent(), ConcurrentEvent(), ConcurrentEvent()).forEach { eventManager.queueEvent(it) }
        eventManager.update(1.0) //init queue
        counter = 1

        eventManager.update(1.0)
        assert(counter == 2.toLong(), "Deliberately thread-unsafe code, should have incremented counter from 1 to 2 three times. Counter was actually $counter")
    }

    Test fun update_shouldNotProcessInterruptiblesInQueue_givenInterrupterInQueue() {
        listOf(TestInterruptible(0), TestInterruptible(1), TestInterruptible(3), TestInterrupter(4)).forEach { eventManager.queueEvent(it) }
        eventManager.update(1.0) //init queue
        counter = 1

        eventManager.update(1.0)
        assert(counter == 0.toLong(), "Only TestInterrupter should have been processed, decrementing counter from 1 to 0. Counter was actually $counter")
    }

    Test fun update_shouldProcessOnlyLatestSelfInterruptible_givenSeveralInQueue() {
        listOf(TestSelfInterruptible(0), TestSelfInterruptible(1), TestSelfInterruptible(2)).forEach { eventManager.queueEvent(it) }
        eventManager.update(1.0) //init queue
        counter = 0

        eventManager.update(1.0)
        assert(counter == 2.toLong(), "Only TestSelfInterruptible(2) should have been process, incrementing counter from 0 to 2. Counter was actually $counter")
    }

    Test fun update_shouldProcessEventChains() {
        eventManager.queueEvent(TestChain())
        eventManager.update(1.0)
        counter = 0

        (1..10).forEach { eventManager.update(1.0) }
        assert(counter == 5.toLong(), "TestChain should chain itself until counter is 5, then chain an ImpotentEvent. Counter was actually $counter")
    }
}