package com.frugs.dungeoncrawler.event

import org.junit.Test

class EventManagerTest {

    val eventManager = EventManager()
    var counter: Long = 0

    inner class ConcurrentEvent : Event {
        override fun process(tpf: Float) {
            val run = counter
            Thread.sleep(1000 * counter)
            counter = run + counter
        }
    }

    inner class TestInterrupter(public override val timeIssued: Long) : Interrupter {
        override public fun process(tpf: Float) { counter-- }
    }

    inner class TestInterruptible(public override val timeIssued: Long) : Interruptible {
        override public fun interruptibleBy(interruptors: List<Interrupter>): Boolean = interruptors.any { it is TestInterrupter }
        override public fun process(tpf: Float) { counter++ }
        override public val chain = TestInterruptible(timeIssued)
    }


    inner class TestSelfInterruptible(override public val timeIssued: Long) : Interruptible, Interrupter {
        override public fun interruptibleBy(interruptors: List<Interrupter>) = interruptors.any { it is TestSelfInterruptible }
        override public fun process(tpf: Float) { counter += timeIssued }
        override public val chain =  TestSelfInterruptible(timeIssued)
    }

    inner class TestChain : Chainable {
        override public val chain = if (counter < 5) TestChain() else ImpotentEvent()
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
        assert(counter == 2.toLong())
    }

    Test fun update_shouldNotProcessInterruptiblesInQueue_givenInterrupterInQueue() {
        listOf(TestInterruptible(0), TestInterruptible(1), TestInterruptible(3), TestInterrupter(4)).forEach { eventManager.queueEvent(it) }
        eventManager.update(1.0) //init queue
        counter = 1

        eventManager.update(1.0)
        assert(counter == 0.toLong(), "Only TestInterrupter should have been processed, decrementing counter from 1 to 0")
    }

    Test fun update_shouldProcessOnlyLatestSelfInterruptible_givenSeveralInQueue() {
        listOf(TestSelfInterruptible(0), TestSelfInterruptible(1), TestSelfInterruptible(2)).forEach { eventManager.queueEvent(it) }
        eventManager.update(1.0) //init queue
        counter = 0

        eventManager.update(1.0)
        assert(counter == 3.toLong())
    }

    Test fun update_shouldProcessEventChains() {
        eventManager.queueEvent(TestChain())
        eventManager.update(1.0)
        counter = 0

        (1..10).forEach { eventManager.update(1.0) }
        assert(counter == 5.toLong(), "TestChain should chain itself until counter is 5, then chain an ImpotentEvent")
    }
}