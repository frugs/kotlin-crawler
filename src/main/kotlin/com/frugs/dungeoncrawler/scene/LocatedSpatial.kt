package com.frugs.dungeoncrawler.scene

import com.jme3.math.Vector3f
import com.jme3.scene.Spatial

trait LocatedSpatial {
    fun getLocalTranslation(): Vector3f?
    fun attachChild(child: Spatial?): Unit
}
