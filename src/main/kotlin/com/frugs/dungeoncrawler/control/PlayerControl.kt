package com.frugs.dungeoncrawler.control

import com.jme3.input.controls.AnalogListener
import com.frugs.dungeoncrawler.event.EventManager
import com.jme3.renderer.Camera
import com.jme3.input.InputManager
import com.frugs.dungeoncrawler.game.Player
import com.frugs.dungeoncrawler.event.player.PlayerMove
import com.frugs.dungeoncrawler.event.player.PlayerStop
import com.frugs.dungeoncrawler.action.PlayerAction
import com.frugs.dungeoncrawler.action.PlayerAction.*
import com.jme3.math.Vector3f
import com.jme3.math.Plane
import com.jme3.math.Ray
import com.google.inject.Inject


class PlayerControl [Inject] (
    val player: Player,
    val eventManager: EventManager,
    val cam: Camera,
    val inputManager: InputManager
) : AnalogListener {

    override fun onAnalog(p0: String?, p1: Float, p2: Float) {
        val action = PlayerAction.valueOf(p0)

        when (action) {
            MOVE_MOUSE_LOCATION -> eventManager.queueEvent(PlayerMove(currentMouseFloorLocation(), player, true))
            STOP -> eventManager.queueEvent(PlayerStop())
            else -> throw UnsupportedOperationException("Cannot process non-player action: '${p0}'")
        }
    }

    private fun currentMouseFloorLocation(): Vector3f {
        val origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0)
        val direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3)!!.subtract(origin)!!.normalize()
        val loc = Vector3f.ZERO
        val xzPlane = Plane(Vector3f.UNIT_Y.negate(), 0.0)
        Ray(origin, direction).intersectsWherePlane(xzPlane, loc)
        return loc
    }

}
