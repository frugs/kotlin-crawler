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

[Singleton]
class Player [Inject] ([Named("unshaded")] mat: Material): Node("player") {
    {
        val dome = createDome(mat)
        attachChild(dome)
    }

    public var facingDirection: Vector3f = Vector3f.UNIT_Z
        get() =  try {
            lock.readLock().lock()
            $facingDirection
        } finally { lock.readLock().unlock() }
        private set(direction: Vector3f) = try {
            lock.writeLock().lock()
            $facingDirection = direction
        } finally { lock.writeLock().unlock() }

    var speed: Float = 8.0
    var angularSpeed: Float = (FastMath.DEG_TO_RAD * 10.0 * 60.0).toFloat()

    val lock = ReentrantReadWriteLock()

    public override fun rotate(rot: Quaternion?): Spatial? = lock.write <Spatial?> {
        facingDirection = rot!!.toRotationMatrix()!!.mult(facingDirection)!!
        super.rotate(rot)
    }

    public override fun rotate(xAngle: Float, yAngle: Float, zAngle: Float): Spatial? = lock.write <Spatial?> {
        rotate(Quaternion.ZERO.fromAngles(xAngle, yAngle, zAngle))
    }

    public override fun move(offset: Vector3f?): Spatial? = lock.write <Spatial?> { super.move(offset) }
    public override fun getLocalTranslation(): Vector3f? = lock.read <Vector3f?> { super.getLocalTranslation() }
    public override fun setLocalTranslation(localTranslation: Vector3f?): Unit {
        lock.write { super.setLocalTranslation(localTranslation) }
    }

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

    //return value is true if we've got more to rotate
    public fun rotateTowardsDestination(destination: Vector3f, tpf: Float): Boolean = lock.write <Boolean> {
        val angleToDestination = FastMath.asin(facingDirection.cross(destination.subtract(getLocalTranslation())!!.normalize())!!.y)
        val angularVelocity = angularSpeed * tpf * FastMath.sign(angleToDestination)
        val rotation = Radians.smallerAbsolute(angularVelocity, angleToDestination)

        rotate(0.0, rotation, 0.0)
        rotation != angleToDestination
    }

    private fun createDome(mat: Material): Geometry {
        val dome = Geometry("Dome", Dome(Vector3f.ZERO, 2, 4, 1.5, true))
        dome.rotate(FastMath.HALF_PI, 0.0, 0.0)
        mat.setColor("Color", ColorRGBA.Blue)
        //material = mat
        dome.setMaterial(mat)
        return dome
    }
}