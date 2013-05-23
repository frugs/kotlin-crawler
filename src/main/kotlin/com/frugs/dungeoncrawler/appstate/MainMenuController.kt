package com.frugs.dungeoncrawler.appstate

import com.jme3.app.state.AbstractAppState
import de.lessvoid.nifty.screen.ScreenController
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.Screen
import com.jme3.app.state.AppStateManager
import com.jme3.asset.AssetManager
import com.jme3.input.InputManager
import com.jme3.audio.AudioRenderer
import com.jme3.renderer.ViewPort
import com.google.inject.Inject
import com.jme3.app.Application
import com.jme3.niftygui.NiftyJmeDisplay
import com.frugs.dungeoncrawler.event.EventManager

class MainMenuController [Inject] (
    private val stateManager: AppStateManager,
    private val assetManager: AssetManager,
    private val inputManager: InputManager,
    private val audioRenderer: AudioRenderer,
    private val guiViewPort: ViewPort
): AbstractAppState(), ScreenController {

    private val niftyJmeDisplay = NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort)

    public var enabled: Boolean = false
        get() = $enabled
        set(state: Boolean) {
            $enabled = state
            if (state) {
                niftyJmeDisplay.getNifty()!!.fromXml("Interface/MainMenu.xml", "start", this)
                guiViewPort.addProcessor(niftyJmeDisplay)
            } else if (initialized) {
                niftyJmeDisplay.getNifty()!!.exit() //questionable, refactor when there are more screens
            }
        }

    public override fun bind(p0: Nifty?, p1: Screen?) {}

    fun startGame() {
        stateManager.getState(javaClass<InGame>())!!.enabled = true
        stateManager.getState(javaClass<RtsCamera>())!!.enabled = true
        EventManager.enabled = true
        enabled = false
    }

    fun initialize(stateManager: AppStateManager, app: Application) {
        super<AbstractAppState>.initialize(stateManager, app)
        enabled = true
    }

    override fun onStartScreen() {}

    override fun onEndScreen() {}

    public override fun isEnabled() = enabled
}