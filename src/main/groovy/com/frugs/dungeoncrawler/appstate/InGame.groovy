package com.frugs.dungeoncrawler.appstate

import com.frugs.dungeoncrawler.DungeonCrawler
import com.frugs.dungeoncrawler.control.PlayerControl
import com.frugs.dungeoncrawler.control.PlayerControl.PlayerAction
import com.frugs.dungeoncrawler.game.Player
import com.frugs.event.EventManager
import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.input.InputManager
import com.jme3.renderer.Camera
import com.jme3.scene.Node
import groovy.transform.CompileStatic

@CompileStatic
class InGame extends AbstractAppState {

    Player player
    Node rootNode
    EventManager eventManager
    InputManager inputManager
    PlayerControl playerControl
    Camera cam
    int frameNo = 0

    InGame() {
        super(enabled: false)
    }

    @Override
    void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app)
        SimpleApplication simpleApp = app as DungeonCrawler
        player = new Player(simpleApp.materials.unshaded)
        rootNode = simpleApp.rootNode
        eventManager = simpleApp.eventManager
        inputManager = simpleApp.inputManager
        cam = simpleApp.camera
        playerControl = new PlayerControl(player, eventManager, cam, inputManager)
        enabled = true
    }

    @Override
    void setEnabled(boolean enabled) {
        super.setEnabled(enabled)
        if (enabled) {
            rootNode.attachChild(player)
            inputManager.addListener(playerControl, PlayerAction.ids)
        } else {
            rootNode.detachChild(player)
            inputManager.removeListener(playerControl)
        }
    }

    @Override
    void update(float tpf) {
        super.update(tpf)

//        if (frameNo == 0) {
//            eventManager.queueEvent(new PlayerMove(Vector3f.UNIT_X.mult(10.0f), player))
//        }
//
//        if (frameNo == 600) {
//            eventManager.queueEvent(new PlayerMove(Vector3f.ZERO, player))
//        }
//
//        frameNo++
    }
}
