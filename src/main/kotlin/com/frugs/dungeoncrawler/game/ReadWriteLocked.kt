package com.frugs.dungeoncrawler.game

import java.util.concurrent.locks.ReentrantReadWriteLock


trait ReadWriteLocked {
    val lock: ReentrantReadWriteLock
}