package com.frugs.dungeoncrawler.game.abilities

import com.frugs.dungeoncrawler.scene.LocatedSpatial
import com.frugs.dungeoncrawler.game.ReadWriteLocked
import com.frugs.dungeoncrawler.game.Projectile
import kotlin.concurrent.read

trait FireProjectileAbility : LocatedSpatial, ReadWriteLocked {

    open public fun fireProjectile(projectile: Projectile): Unit = lock.read {
        attachChild(projectile)
    }
}
