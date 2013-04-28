package com.frugs.dungeoncrawler.appstate

import com.frugs.dungeoncrawler.control.CameraActionListener
import com.frugs.dungeoncrawler.control.CameraActionListener.CameraAction
import com.frugs.dungeoncrawler.control.PlayerControl
import com.frugs.dungeoncrawler.scene.RtsCameraNode
import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.input.InputManager
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import com.jme3.scene.Node
import groovy.transform.CompileStatic

@CompileStatic
class RtsCamera extends AbstractAppState {

    private Camera cam
    private InputManager inputManager
    private Node rootNode

    private RtsCameraNode rtsCameraNode
    private CameraActionListener cameraActionListener

    private int screenEdgeOffset = 10

    @Override
    void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app)
        SimpleApplication simpleApp = app as SimpleApplication
        cam = simpleApp.camera
        inputManager = simpleApp.inputManager
        rootNode = simpleApp.rootNode
        rtsCameraNode = new RtsCameraNode(cam)
        cameraActionListener = new CameraActionListener(rtsCameraNode)
        enabled = false
    }

    @Override
    void setEnabled(boolean enabled) {
        super.setEnabled(enabled)
        if (enabled) {
            rootNode.attachChild(rtsCameraNode)
            inputManager.addListener(cameraActionListener, CameraAction.ids)
        } else if (initialized) {
            rootNode.detachChild(rtsCameraNode)
            inputManager.removeListener(cameraActionListener)
        }
    }

    @Override
    void update(float tpf) {
        super.update(tpf)
        def adjustedSpeed = Vector3f.UNIT_XYZ.mult(tpf * 60).mult(rtsCameraNode.speed)

        if (inputManager.cursorPosition.y + screenEdgeOffset > cam.height) {
            rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed))
        }
        if (inputManager.cursorPosition.y - screenEdgeOffset < 0) {
            rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed).negate())
        }
        if (inputManager.cursorPosition.x + screenEdgeOffset > cam.width) {
            rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed).negate())
        }
        if (inputManager.cursorPosition.x - screenEdgeOffset < 0) {
            rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed))
        }
    }
}
