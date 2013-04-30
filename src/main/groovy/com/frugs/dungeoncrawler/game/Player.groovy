package com.frugs.dungeoncrawler.game

import com.frugs.dungeoncrawler.util.Radians
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
    Spatial rotate(float xAngle, float yAngle, float zAngle) {
        assert xAngle == 0.0f && zAngle == 0.0f //rotations in other dimensions not supported yet
        Quaternion rotation = Quaternion.ZERO.fromAngles(xAngle, yAngle, zAngle)
        rotate(rotation)
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
}
