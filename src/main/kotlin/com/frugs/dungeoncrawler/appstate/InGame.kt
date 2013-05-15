package com.frugs.dungeoncrawler.appstate

import com.jme3.app.state.AbstractAppState
import com.jme3.input.InputManager
import com.jme3.scene.Node
import com.frugs.dungeoncrawler.game.Player
import com.google.inject.Inject
import com.jme3.app.state.AppStateManager
import com.jme3.app.Application
import com.frugs.dungeoncrawler.DungeonCrawler

class InGame [Inject] (
    val rootNode: Node,
    val inputManager: InputManager
): AbstractAppState() {
    var player: Player? = null

    var enabled: Boolean = false
        get() = $enabled
        set(state: Boolean) {
            $enabled = state
            if (state) {
                rootNode.attachChild(player)
//                inputManager.addListener(playerControl, PlayerAction.ids)
            } else if (initialized) {
                rootNode.detachChild(player)
//                inputManager.removeListener(playerControl)
            }
        }

//    var playerControl: PlayerControl? = null

    override public fun initialize(stateManager: AppStateManager?, app: Application?) {
        super.initialize(stateManager, app)
        val simpleApp = app as DungeonCrawler
        player = Player(simpleApp.materials.get("unshaded")!!)
//        playerControl = PlayerControl(player, simpleApp.eventManager, simpleApp.camera, inputManager)
    }

    override public fun isEnabled(): Boolean = enabled
}
