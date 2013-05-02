package com.frugs.dungeoncrawler.event


public interface ChainableCopy extends Event {
    Event getChain()
}