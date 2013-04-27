package com.frugs.dungeoncrawler.scene

import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.scene.CameraNode
import com.jme3.scene.Spatial
import groovy.transform.CompileStatic

import static com.jme3.scene.control.CameraControl.ControlDirection.SpatialToCamera

@CompileStatic
class RtsCameraNode extends CameraNode {

    Vector3f speed = new Vector3f(0.25f, 0.25f, 0.25f)

    RtsCameraNode(Camera camera) {
        super("RtsCamNode", camera)
        controlDir = SpatialToCamera
        localTranslation = new Vector3f(0f, 40.0f, -15.0f)
        lookAt(Vector3f.ZERO, Vector3f.UNIT_Y)
    }

    @Override
    synchronized Spatial move(Vector3f offset) {
        return super.move(offset)
    }
}
