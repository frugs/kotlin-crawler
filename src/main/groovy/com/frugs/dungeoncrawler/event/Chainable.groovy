package com.frugs.dungeoncrawler.event


public interface Chainable extends Event {
    Event getChain()
}