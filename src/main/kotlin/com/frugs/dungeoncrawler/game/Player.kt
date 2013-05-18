package com.frugs.dungeoncrawler.game

import com.jme3.scene.Node
import com.jme3.material.Material
import com.jme3.math.Vector3f
import com.jme3.math.FastMath
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Dome
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.scene.Spatial
import com.frugs.dungeoncrawler.util.Radians
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write
import kotlin.concurrent.read
import com.google.inject.Inject
import com.google.inject.name.Named
import com.google.inject.Singleton
import com.frugs.dungeoncrawler.game.abilities.MoveTowardsAbility
import com.frugs.dungeoncrawler.game.abilities.RotateTowardsAbility
import com.frugs.dungeoncrawler.scene.ReadWriteLockedMovingRotatingNode

[Singleton]
class Player [Inject] ([Named("unshaded")] mat: Material): ReadWriteLockedMovingRotatingNode("player"), MoveTowardsAbility, RotateTowardsAbility {
    {
        fun createDome(mat: Material): Geometry {
            val dome = Geometry("Dome", Dome(Vector3f.ZERO, 2, 4, 1.5, true))
            dome.rotate(FastMath.HALF_PI, 0.0, 0.0)
            mat.setColor("Color", ColorRGBA.Blue)
            dome.setMaterial(mat)
            return dome
        }

        val dome = createDome(mat)
        attachChild(dome)
        facingDirection = Vector3f.UNIT_Z
    }

    override val speed: Float = 8.0
    override val angularSpeed: Float = (FastMath.DEG_TO_RAD * 10.0 * 60.0).toFloat()
}