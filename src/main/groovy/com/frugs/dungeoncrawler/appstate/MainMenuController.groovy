package com.frugs.dungeoncrawler.appstate

import com.frugs.dungeoncrawler.gui.MainMenuController
import com.jme3.app.Application
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.asset.AssetManager
import com.jme3.audio.AudioRenderer
import com.jme3.input.InputManager
import com.jme3.niftygui.NiftyJmeDisplay
import com.jme3.renderer.ViewPort
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.Screen
import de.lessvoid.nifty.screen.ScreenController
import groovy.transform.CompileStatic

@CompileStatic
class MainMenuController extends AbstractAppState implements ScreenController{

    private AppStateManager stateManager
    private AssetManager assetManager
    private InputManager inputManager
    private AudioRenderer audioRenderer
    private ViewPort guiViewPort

    private NiftyJmeDisplay niftyJmeDisplay
    private Nifty nifty
    private Screen screen

    @Override
    void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty
        this.screen = screen
    }

    void startGame() {
        stateManager.getState(InGame).enabled = true
        stateManager.getState(RtsCamera).enabled = true
        enabled = false
    }

    @Override
    void onStartScreen() {}

    @Override
    void onEndScreen() {}

    @Override
    void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app)
        this.stateManager = stateManager
        assetManager = app.assetManager
        inputManager = app.inputManager
        audioRenderer = app.audioRenderer
        guiViewPort = app.guiViewPort
        enabled = true
    }

    @Override
    void setEnabled(boolean enabled) {
        super.setEnabled(enabled)

        if (enabled) {
            niftyJmeDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort)
            niftyJmeDisplay.nifty.fromXml("Interface/MainMenu.xml", "start", this)
            guiViewPort.addProcessor(niftyJmeDisplay)
        } else if (initialized) {
            niftyJmeDisplay.nifty.exit() //questionable, refactor when there are more screens
        }
    }
}
