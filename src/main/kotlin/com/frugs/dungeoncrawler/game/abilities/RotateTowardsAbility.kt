package com.frugs.dungeoncrawler.game.abilities

import com.frugs.dungeoncrawler.game.ReadWriteLocked
import com.frugs.dungeoncrawler.game.MovingSpatial
import com.jme3.math.Vector3f
import kotlin.concurrent.write
import com.jme3.math.FastMath
import com.frugs.dungeoncrawler.util.Radians
import com.frugs.dungeoncrawler.game.RotatingSpatial

trait RotateTowardsAbility : ReadWriteLocked, RotatingSpatial {

    //return value is true if we've got more to rotate
    public fun rotateTowardsDestination(destination: Vector3f, tpf: Float): Boolean = lock.write <Boolean> {
        val destinationDirection = destination.subtract(getLocalTranslation())!!.normalize()
        val modCrossProduct = FastMath.asin(facingDirection.cross(destinationDirection)!!.y)
        val angleToDestination = if (modCrossProduct != 0.toFloat() || facingDirection == destinationDirection) modCrossProduct
        else FastMath.PI

        val angularVelocity = angularSpeed * tpf * FastMath.sign(angleToDestination)
        val rotation = Radians.smallerAbsolute(angularVelocity, angleToDestination)

        rotate(0.0, rotation, 0.0)
        rotation != modCrossProduct
    }
}
