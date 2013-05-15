package com.frugs.dungeoncrawler.appstate

import com.jme3.app.state.AbstractAppState
import com.jme3.renderer.Camera
import com.jme3.input.InputManager
import com.jme3.scene.Node
import com.frugs.dungeoncrawler.scene.RtsCameraNode
import com.jme3.app.state.AppStateManager
import com.jme3.app.Application
import com.jme3.math.Vector3f
import com.google.inject.Inject
import com.frugs.dungeoncrawler.control.CameraActionListener
import com.frugs.dungeoncrawler.action.CameraAction

class RtsCamera [Inject] (
    val cam: Camera,
    val rtsCameraNode: RtsCameraNode,
    val rootNode: Node,
    val inputManager: InputManager,
    val cameraActionListener: CameraActionListener
): AbstractAppState() {
    private val screenEdgeOffset = 10

    public var enabled: Boolean = false
        get() = $enabled
        set(state: Boolean) {
            $enabled = state
            if (state) {
                rootNode.attachChild(rtsCameraNode)
                inputManager.addListener(cameraActionListener, *CameraAction.values().map { it.id }.toArray() as Array<String?>)
            } else if (initialized) {
                rootNode.detachChild(rtsCameraNode)
                inputManager.removeListener(cameraActionListener)
            }
        }

    override public fun initialize(stateManager: AppStateManager?, app: Application?) {
        super.initialize(stateManager, app)
    }

    override public fun update(tpf: Float) {
        super.update(tpf)
        val adjustedSpeed = Vector3f.UNIT_XYZ.mult((tpf * 60.0).toFloat())!!.mult(rtsCameraNode.speed)
        val cursorPosition = inputManager.getCursorPosition()!!

        if (cursorPosition.y + screenEdgeOffset > cam.getHeight()) {
            rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed))
        }
        if (cursorPosition.y - screenEdgeOffset < 0) {
            rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed)!!.negate())
        }
        if (cursorPosition.x + screenEdgeOffset > cam.getWidth()) {
            rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed)!!.negate())
        }
        if (cursorPosition.x - screenEdgeOffset < 0) {
            rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed))
        }
    }

    override public fun isEnabled(): Boolean = enabled
}