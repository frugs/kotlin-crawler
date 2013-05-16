package com.frugs.dungeoncrawler.event


trait Event {
    public fun process(tpf: Float): Unit
}