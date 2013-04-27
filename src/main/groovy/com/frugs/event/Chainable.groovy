package com.frugs.event


public interface Chainable extends Event {
    Event getChain()
}