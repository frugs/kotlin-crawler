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

    class TestInterruptable implements Interruptable {
        List<? extends Interrupter> interruptedBy = [TestInterrupter] as List<? extends Interrupter>
        long timeIssued

        TestInterruptable(long timeIssued) {
            this.timeIssued = timeIssued
        }

        @Override
        void process(float tpf) {
            counter++
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

    class TestSelfInterruptable implements Interruptable, Interrupter {
        long timeIssued
        List<? extends Interrupter> interruptedBy = [TestSelfInterruptable] as List<? extends Interrupter>

        TestSelfInterruptable(long timeIssued) {
            this.timeIssued = timeIssued
        }

        @Override
        void process(float tpf) {
            counter += timeIssued
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
    void update_shouldNotProcessInterruptablesInQueue_givenInterrupterInQueue() {
        [new TestInterruptable(0), new TestInterruptable(1), new TestInterruptable(3), new TestInterrupter(4)].each { Event event ->
            eventManager.queueEvent(event)
        }
        eventManager.update(1) //init queue
        counter = 1

        eventManager.update(1)
        assert counter == 0
    }

    @Test
    void update_shouldProcessOnlyLatestSelfInterruptable_givenSeveralInQueue() {
        [new TestSelfInterruptable(0), new TestSelfInterruptable(1), new TestSelfInterruptable(2), new TestSelfInterruptable(3)].each { Event event ->
            eventManager.queueEvent(event)
        }
        eventManager.update(1) //init queue
        counter = 0

        eventManager.update(1)
        assert counter == 3
    }
}
