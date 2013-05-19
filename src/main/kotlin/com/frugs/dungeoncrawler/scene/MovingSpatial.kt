package com.frugs.dungeoncrawler.game

import com.jme3.scene.Spatial
import com.jme3.math.Vector3f
import com.frugs.dungeoncrawler.scene.LocatedSpatial

trait MovingSpatial : LocatedSpatial {
    val speed: Float

    fun move(offset: Vector3f?): Spatial?
}
