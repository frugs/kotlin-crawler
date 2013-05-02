package com.frugs.dungeoncrawler.appstate

import com.frugs.dungeoncrawler.DungeonCrawler
import com.frugs.dungeoncrawler.control.PlayerControl
import com.frugs.dungeoncrawler.control.PlayerControl.PlayerAction
import com.frugs.dungeoncrawler.game.Player
import com.frugs.dungeoncrawler.event.EventManager
import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.input.InputManager
import com.jme3.scene.Node
import groovy.transform.CompileStatic

import javax.inject.Inject

@CompileStatic
class InGame extends AbstractAppState {

    Player player
    Node rootNode
    InputManager inputManager
    PlayerControl playerControl

    @Inject
    InGame(Node rootNode, InputManager inputManager) {
        this.rootNode = rootNode
        this.inputManager = inputManager
    }

    @Override
    void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app)
        SimpleApplication simpleApp = app as DungeonCrawler
        player = new Player(simpleApp.materials.unshaded)
        playerControl = new PlayerControl(player, simpleApp.eventManager, simpleApp.camera, inputManager)
        enabled = false
    }

    @Override
    void setEnabled(boolean enabled) {
        super.setEnabled(enabled)
        if (enabled) {
            rootNode.attachChild(player)
            inputManager.addListener(playerControl, PlayerAction.ids)
        } else if (initialized) {
            rootNode.detachChild(player)
            inputManager.removeListener(playerControl)
        }
    }
}
