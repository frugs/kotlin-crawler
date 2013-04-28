package com.frugs.dungeoncrawler.event


public interface Interruptable extends Chainable {
    List<? extends Interrupter> getInterruptedBy()
    long getTimeIssued()
}