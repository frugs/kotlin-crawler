package com.frugs.dungeoncrawler.control

import com.frugs.dungeoncrawler.scene.RtsCameraNode
import com.jme3.input.controls.AnalogListener
import com.jme3.math.Vector3f
import groovy.transform.CompileStatic

import static CameraActionListener.CameraAction.*

@CompileStatic
class CameraActionListener implements AnalogListener {

    RtsCameraNode rtsCameraNode

    CameraActionListener(RtsCameraNode rtsCameraNode) {
        this.rtsCameraNode = rtsCameraNode
    }

    @Override
    void onAnalog(String name, float value, float tpf) {
        CameraAction action = CameraAction.valueOf(name)
        def adjustedSpeed = Vector3f.UNIT_XYZ.mult(tpf * 60).mult(rtsCameraNode.speed)

        if (!action) {
            throw new UnsupportedOperationException("Cannot process non-camera action: '${name}'")
        }

        switch (action) {
            case MOVE_UP:
                rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed))
                break
            case MOVE_DOWN:
                rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed).negate())
                break
            case MOVE_RIGHT:
                rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed).negate())
                break
            case MOVE_LEFT:
                rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed))
                break
        }
    }

    enum CameraAction {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_DOWN,
        MOVE_UP

        String id

        CameraAction() {
            id = this.toString()
        }

        static String[] getIds() {
            (values() as List<CameraAction>).id as String[]
        }
    }
}
