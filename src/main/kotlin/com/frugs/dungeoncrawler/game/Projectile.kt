package com.frugs.dungeoncrawler.game

import com.jme3.material.Material
import com.jme3.scene.Node
import com.frugs.dungeoncrawler.game.abilities.MoveTowardsAbility
import com.frugs.dungeoncrawler.game.abilities.RotateTowardsAbility
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Sphere
import com.jme3.math.FastMath
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import java.util.concurrent.locks.ReentrantReadWriteLock
import com.frugs.dungeoncrawler.scene.ReadWriteLockedMovingRotatingNode

class Projectile(val mat: Material, location: Vector3f, val destination: Vector3f): ReadWriteLockedMovingRotatingNode("projectile"), MoveTowardsAbility, RotateTowardsAbility {
    {
        fun createSphere(mat: Material): Geometry {
            val dome = Geometry("Dome", Sphere(25, 25, 0.7))
            mat.setColor("Color", ColorRGBA.Orange)
            dome.setMaterial(mat)
            return dome
        }

        val sphere = createSphere(mat)
        attachChild(sphere)
        facingDirection = destination.subtract(location)!!.normalize()!!
        setLocalTranslation(location)
    }

    override val speed = 20.0.toFloat()
    override val angularSpeed = FastMath.PI
}
