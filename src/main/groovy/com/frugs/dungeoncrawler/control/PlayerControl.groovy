package com.frugs.dungeoncrawler.control

import com.frugs.dungeoncrawler.game.Player
import com.frugs.event.EventManager
import com.frugs.event.player.PlayerMove
import com.frugs.event.player.PlayerStop
import com.jme3.input.InputManager
import com.jme3.input.controls.AnalogListener
import com.jme3.math.Plane
import com.jme3.math.Ray
import com.jme3.math.Vector3f
import com.jme3.renderer.Camera
import groovy.transform.CompileStatic

import static com.frugs.dungeoncrawler.control.PlayerControl.PlayerAction.*

@CompileStatic
class PlayerControl implements AnalogListener{

    Player player
    EventManager eventManager
    Camera cam
    InputManager inputManager

    PlayerControl(Player player, EventManager eventManager, Camera cam, InputManager inputManager) {
        this.player = player
        this.eventManager = eventManager
        this.cam = cam
        this.inputManager = inputManager
    }

    @Override
    void onAnalog(String name, float value, float tpf) {
        PlayerAction action = PlayerAction.valueOf(name)

        if (!action) {
            throw new UnsupportedOperationException("Cannot process non-player action: '${name}'")
        }

        switch (action) {
            case MOVE_MOUSE_LOCATION:
                eventManager.queueEvent(new PlayerMove(currentMouseFloorLocation, player))
                break
            case STOP:
                eventManager.queueEvent(new PlayerStop(player))
                break
        }
    }

    private Vector3f getCurrentMouseFloorLocation() {
        def origin = cam.getWorldCoordinates(inputManager.cursorPosition, 0.0f)
        def direction = cam.getWorldCoordinates(inputManager.cursorPosition, 0.3f).subtract(origin).normalize()
        Vector3f loc = Vector3f.ZERO
        Plane xzPlane = new Plane(Vector3f.UNIT_Y.negate(), 0.0f)
        new Ray(origin, direction).intersectsWherePlane(xzPlane, loc)
        loc
    }

    enum PlayerAction {
        MOVE_MOUSE_LOCATION,
        STOP

        String id

        PlayerAction() {
            id = this.toString()
        }

        static String[] getIds() {
            (values() as List<PlayerAction>).id as String[]
        }
    }
}
