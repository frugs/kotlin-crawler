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

[Singleton]
class Player [Inject] ([Named("unshaded")] mat: Material): Node("player"), MoveTowardsAbility, RotateTowardsAbility {
    {
        val dome = createDome(mat)
        attachChild(dome)
    }

    override public var facingDirection: Vector3f = Vector3f.UNIT_Z
        get() =  try {
            lock.readLock().lock()
            $facingDirection
        } finally { lock.readLock().unlock() }
        private set(direction: Vector3f) = try {
            lock.writeLock().lock()
            $facingDirection = direction
        } finally { lock.writeLock().unlock() }

    override var speed: Float = 8.0
    override var angularSpeed: Float = (FastMath.DEG_TO_RAD * 10.0 * 60.0).toFloat()

    override val lock = ReentrantReadWriteLock()

    public override fun rotate(rot: Quaternion?): Spatial? = lock.write <Spatial?> {
        facingDirection = rot!!.toRotationMatrix()!!.mult(facingDirection)!!
        super<Node>.rotate(rot)
    }

    public override fun rotate(xAngle: Float, yAngle: Float, zAngle: Float): Spatial? = lock.write <Spatial?> {
        rotate(Quaternion.ZERO.fromAngles(xAngle, yAngle, zAngle))
    }

    public override fun move(offset: Vector3f?): Spatial? = lock.write <Spatial?> { super<Node>.move(offset) }
    public override fun getLocalTranslation(): Vector3f? = lock.read <Vector3f?> { super<Node>.getLocalTranslation() }
    public override fun setLocalTranslation(localTranslation: Vector3f?): Unit {
        lock.write { super<Node>.setLocalTranslation(localTranslation) }
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