package com.frugs.dungeoncrawler.game.abilities

import com.frugs.dungeoncrawler.game.ReadWriteLocked
import com.frugs.dungeoncrawler.game.MovingSpatial
import com.jme3.math.Vector3f
import kotlin.concurrent.write
import com.jme3.math.FastMath
import com.frugs.dungeoncrawler.game.RotatingSpatial
import com.frugs.dungeoncrawler.util.sign
import com.frugs.dungeoncrawler.util.smallerAbsolute

trait RotateTowardsAbility : ReadWriteLocked, RotatingSpatial {

    //return value is true if we've got more to rotate
    public fun rotateTowardsDestination(destination: Vector3f, tpf: Float): Boolean = lock.write <Boolean> {
        val destinationDirection = destination.subtract(getLocalTranslation())!!.normalize()
        val sign = FastMath.asin(facingDirection.cross(destinationDirection)!!.y).sign()
        val angleToDestination = facingDirection.angleBetween(destinationDirection) * sign

        val angularVelocity = angularSpeed * tpf * sign
        val rotation = smallerAbsolute(angularVelocity, angleToDestination)

        rotate(0.0, rotation, 0.0)
        rotation != angleToDestination
    }
}
