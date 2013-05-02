package com.frugs.dungeoncrawler.game

import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import groovy.transform.CompileStatic

@CompileStatic
class Player extends Geometry {

    final float speed = 8.0f
    final float angularSpeed = FastMath.DEG_TO_RAD * 3.0f

    Vector3f facingDirection

    Player(Material mat) {
        this(mat, Vector3f.UNIT_Z)
    }

    Player(Material mat, Vector3f facingDirection) {
        super("Box", new Box(Vector3f.ZERO, 1, 1, 1))  // create cube geometry from the shape
        this.facingDirection = facingDirection.normalize()
        mat.setColor("Color", ColorRGBA.Blue)   // set color of material to blue
        material = mat                 // set the cube's material
        rotate(0f, 45f * FastMath.DEG_TO_RAD, 0f)
    }

    @Override
    synchronized Spatial rotate(Quaternion rotation) {
        facingDirection = rotation.toRotationMatrix().mult(facingDirection)
        super.rotate(rotation)
    }

    @Override
    synchronized Spatial move(Vector3f offset) {
        super.move(offset)
    }

    //return value is whether we've got more to go or not
    boolean moveTowardsDestination(Vector3f destination, float tpf) {
       // GParsStm.atomicWithBoolean {
            def remainingTravel = destination.subtract(localTranslation)
            def displacement = remainingTravel.normalize().mult(speed).mult(tpf)

            if (remainingTravel.length() == 0) {
                return false
            }

            if (displacement.length() < remainingTravel.length()) {
                move(displacement)
                return true
            } else {
                move(remainingTravel)
                return false
            }
        //}
    }

    //return value is whether we've finished rotating or not
    boolean rotateTowardsDirection(Vector3f normalisedDirection, float tpf) {
     //   GParsStm.atomicWithBoolean {
            def angleToDestination = normalisedDirection.angleBetween(facingDirection)

            if (angleToDestination == 0) {
                return true
            }

            def refAngle = normalisedDirection.angleBetween(normalisedDirection.cross(Vector3f.UNIT_Y))
            int directionOfRotation = refAngle > angleToDestination ? 1 : -1
            float rotation = angularSpeed * tpf

            if (angularSpeed < angleToDestination) {
                rotate(0.0f, rotation * directionOfRotation, 0.0f)
                return false
            } else {
                rotate(0.0f, rotation * directionOfRotation, 0.0f)
                return true
            }
      //  }
    }
}
