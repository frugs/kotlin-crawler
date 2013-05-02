package com.frugs.dungeoncrawler.event


public interface Interruptible extends Chainable {
    List<? extends Interrupter> getInterruptedBy()
    long getTimeIssued()
}