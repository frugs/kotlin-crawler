package com.frugs.dungeoncrawler.event


public interface Interruptable extends Event {
    List<? extends Interrupter> getInterruptedBy()
    long getTimeIssued()
}