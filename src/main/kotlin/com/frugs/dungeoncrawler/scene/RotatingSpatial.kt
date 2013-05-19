package com.frugs.dungeoncrawler.game

import com.jme3.math.Vector3f
import com.jme3.scene.Spatial
import com.frugs.dungeoncrawler.scene.LocatedSpatial

trait RotatingSpatial : LocatedSpatial {
    val angularSpeed: Float
    var facingDirection: Vector3f

    fun rotate(xAngle: Float, yAngle: Float, zAngle: Float): Spatial?
}
