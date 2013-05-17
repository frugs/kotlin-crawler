package com.frugs.dungeoncrawler.game.abilities

import com.frugs.dungeoncrawler.game.ReadWriteLocked
import com.jme3.math.Vector3f
import kotlin.concurrent.write
import com.frugs.dungeoncrawler.game.MovingSpatial

trait MoveTowardsAbility : MovingSpatial, ReadWriteLocked {

    //return value is true if we've got more to go
    public fun moveTowardsDestination(destination: Vector3f, tpf: Float): Boolean = lock.write <Boolean> {
        val remainingTravel = destination.subtract(getLocalTranslation())!!
        val displacement = remainingTravel.normalize()!!.mult(speed)!!.mult(tpf)!!

        if (remainingTravel.length() == 0.0.toFloat()) false
        else {
            val stillMoving = displacement.length() < remainingTravel.length()
            if (stillMoving) move(displacement) else move(remainingTravel)
            stillMoving
        }
    }
}
