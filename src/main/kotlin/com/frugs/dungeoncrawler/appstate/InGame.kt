package com.frugs.dungeoncrawler.appstate

import com.jme3.app.state.AbstractAppState
import com.jme3.input.InputManager
import com.jme3.scene.Node
import com.frugs.dungeoncrawler.game.Player
import com.google.inject.Inject
import com.jme3.app.state.AppStateManager
import com.jme3.app.Application
import com.frugs.dungeoncrawler.DungeonCrawler
import com.frugs.dungeoncrawler.control.PlayerControl
import com.frugs.dungeoncrawler.action.PlayerAction

class InGame [Inject] (
    val rootNode: Node,
    val inputManager: InputManager,
    val player: Player,
    val playerControl: PlayerControl
): AbstractAppState() {

    var enabled: Boolean = false
        get() = $enabled
        set(state: Boolean) {
            $enabled = state
            if (state) {
                rootNode.attachChild(player)
                inputManager.addListener(playerControl, *PlayerAction.ids)
            } else if (initialized) {
                rootNode.detachChild(player)
                inputManager.removeListener(playerControl)
            }
        }

    override public fun initialize(stateManager: AppStateManager?, app: Application?) {
        super.initialize(stateManager, app)
    }

    override public fun isEnabled(): Boolean = enabled
}
