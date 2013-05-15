package com.frugs.dungeoncrawler.scene

import com.jme3.scene.CameraNode
import com.jme3.renderer.Camera
import com.jme3.scene.control.CameraControl.ControlDirection
import com.jme3.math.Vector3f
import com.google.inject.Inject
import com.google.inject.Singleton

class RtsCameraNode [Inject] [Singleton] (
    val camera: Camera
): CameraNode("RtsCamNode", camera) {
    {
        setControlDir(ControlDirection.SpatialToCamera)
        setLocalTranslation(Vector3f(0.0, 40.0, -15.0.toFloat()))
        lookAt(Vector3f.ZERO, Vector3f.UNIT_Y)
    }

    val speed = Vector3f(0.25, 0.25, 0.25)
}