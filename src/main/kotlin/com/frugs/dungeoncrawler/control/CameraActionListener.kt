package com.frugs.dungeoncrawler.control

import com.frugs.dungeoncrawler.scene.RtsCameraNode
import com.jme3.input.controls.AnalogListener
import com.jme3.math.Vector3f
import com.google.inject.Inject
import com.frugs.dungeoncrawler.action.CameraAction
import com.frugs.dungeoncrawler.action.CameraAction.*


class CameraActionListener [Inject] (val rtsCameraNode: RtsCameraNode): AnalogListener {

    override fun onAnalog(name: String?, value: Float, tpf: Float) {
        val action = CameraAction.valueOf(name!!)
        val adjustedSpeed = Vector3f.UNIT_XYZ.mult(tpf * 60)!!.mult(rtsCameraNode.speed)

        when (action) {
            MOVE_UP -> rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed))
            MOVE_DOWN -> rtsCameraNode.move(Vector3f.UNIT_Z.mult(adjustedSpeed)!!.negate())
            MOVE_RIGHT -> rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed)!!.negate())
            MOVE_LEFT -> rtsCameraNode.move(Vector3f.UNIT_X.mult(adjustedSpeed))
            else -> throw UnsupportedOperationException("Cannot process non-camera action: '${name}'")
        }
    }
}