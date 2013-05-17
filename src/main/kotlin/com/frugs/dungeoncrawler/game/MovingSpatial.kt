package com.frugs.dungeoncrawler.game

import com.jme3.scene.Spatial
import com.jme3.math.Vector3f

trait MovingSpatial {
    var speed: Float
    var angularSpeed: Float

    fun getLocalTranslation(): Vector3f?
    fun move(offset: Vector3f?): Spatial?
}
