package com.frugs.dungeoncrawler.event

import com.google.inject.Guice
import com.google.inject.Injector
import org.junit.Test

class EventManagerTest {

    Injector injector = Guice.createInjector()
    EventManager eventManager = injector.getInstance(EventManager)
    int counter

    class ConcurrentEvent implements Event {
        @Override
        void process(float tpf) {
            int run = counter
            Thread.sleep(100 * counter)
            counter = run + counter
        }
    }

    class TestInterruptible implements Interruptible {
        List<? extends Interrupter> interruptedBy = [TestInterrupter] as List<? extends Interrupter>
        long timeIssued

        TestInterruptible(long timeIssued) {
            this.timeIssued = timeIssued
        }

        @Override
        void process(float tpf) {
            counter++
        }

        @Override
        Event getChain() {
            new TestInterruptible(timeIssued)
        }
    }

    class TestInterrupter implements Interrupter {
        long timeIssued

        TestInterrupter(long timeIssued) {
            this.timeIssued = timeIssued
        }

        @Override
        void process(float tpf) {
            counter--
        }
    }

    class TestSelfInterruptible implements Interruptible, Interrupter {
        long timeIssued
        List<? extends Interrupter> interruptedBy = [TestSelfInterruptible] as List<? extends Interrupter>

        TestSelfInterruptible(long timeIssued) {
            this.timeIssued = timeIssued
        }

        @Override
        void process(float tpf) {
            counter += timeIssued
        }

        @Override
        Event getChain() {
            new TestSelfInterruptible(timeIssued)
        }
    }

    class TestChain implements Chainable {

        @Override
        Event getChain() {
            if (counter < 5) {
                new TestChain()
            } else {
                [process: {}] as Event
            }
        }

        @Override
        void process(float tpf) {
            counter++
        }
    }

    @Test
    void update_shouldProcessEventsConcurrently_ThenWaitForAllEventsToFinishProcessingBeforeExiting() {
        [new ConcurrentEvent(), new ConcurrentEvent(), new ConcurrentEvent()].each { Event event ->
            eventManager.queueEvent(event)
        }
        eventManager.update(1) //init queue
        counter = 1

        eventManager.update(1)
        assert counter == 2
    }

    @Test
    void update_shouldNotProcessInterruptiblesInQueue_givenInterrupterInQueue() {
        [new TestInterruptible(0), new TestInterruptible(1), new TestInterruptible(3), new TestInterrupter(4)].each { Event event ->
            eventManager.queueEvent(event)
        }
        eventManager.update(1) //init queue
        counter = 1

        eventManager.update(1)
        assert counter == 0
    }

    @Test
    void update_shouldProcessOnlyLatestSelfInterruptible_givenSeveralInQueue() {
        [new TestSelfInterruptible(0), new TestSelfInterruptible(1), new TestSelfInterruptible(2), new TestSelfInterruptible(3)].each { Event event ->
            eventManager.queueEvent(event)
        }
        eventManager.update(1) //init queue
        counter = 0

        eventManager.update(1)
        assert counter == 3
    }

    @Test
    void update_shouldProcessEventChains() {
        eventManager.queueEvent(new TestChain())
        eventManager.update(1)
        counter = 0

        (1..10).each {
            eventManager.update(1)
        }
        assert counter == 5
    }
}
