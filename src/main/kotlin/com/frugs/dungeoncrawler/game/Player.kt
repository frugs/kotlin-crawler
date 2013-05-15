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

class Player(mat: Material): Node("player") {
    {
        val dome = createDome(mat)
        attachChild(dome)
    }

    var facingDirection: Vector3f = Vector3f.UNIT_Z
    var speed: Float = 8.0
    var angularSpeed: Float = (FastMath.DEG_TO_RAD * 10.0 * 60.0).toFloat()

    public override fun rotate(rot: Quaternion?): Spatial? {
        facingDirection = rot!!.toRotationMatrix()!!.mult(facingDirection)!!
        return super.rotate(rot)
    }

    //return value is true if we've got more to go
    public fun moveTowardsDestination(destination: Vector3f, tpf: Float): Boolean {
        val remainingTravel = destination.subtract(getLocalTranslation())!!
        val displacement = remainingTravel.normalize()!!.mult(speed)!!.mult(tpf)!!

        if (remainingTravel.length() == 0.0.toFloat()) {
            return false
        }

        val stillMoving = displacement.length() < remainingTravel.length()
        if (stillMoving) move(displacement) else move(remainingTravel)
        return stillMoving
    }

    //return value is true if we've got more to rotate
    public fun rotateTowardsDestination(destination: Vector3f, tpf: Float): Boolean {
        val angleToDestination = FastMath.asin(facingDirection.cross(destination.subtract(getLocalTranslation())!!.normalize())!!.y)
        val angularVelocity = angularSpeed * tpf * FastMath.sign(angleToDestination)
        val rotation = Radians.smallerAbsolute(angularVelocity, angleToDestination)

        rotate(0.0, rotation, 0.0)
        return rotation != angleToDestination
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