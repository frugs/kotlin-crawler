package com.frugs.dungeoncrawler.scene

import com.jme3.scene.Node
import com.frugs.dungeoncrawler.game.ReadWriteLocked
import java.util.concurrent.locks.ReentrantReadWriteLock
import com.jme3.math.Quaternion
import com.jme3.scene.Spatial
import kotlin.concurrent.write
import com.frugs.dungeoncrawler.game.MovingSpatial
import com.frugs.dungeoncrawler.game.RotatingSpatial
import com.jme3.math.Vector3f
import kotlin.concurrent.read

abstract class ReadWriteLockedMovingRotatingNode (name: String): Node(name), ReadWriteLocked, MovingSpatial, RotatingSpatial {

    override val lock = ReentrantReadWriteLock()

    override public var facingDirection: Vector3f = Vector3f.UNIT_Z
        get() =  try {
            lock.readLock().lock()
            $facingDirection
        } finally { lock.readLock().unlock() }
        protected set(direction: Vector3f) = try {
            lock.writeLock().lock()
            $facingDirection = direction.normalize()!!
        } finally { lock.writeLock().unlock() }


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
}
