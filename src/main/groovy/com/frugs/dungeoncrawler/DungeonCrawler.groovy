package com.frugs.dungeoncrawler

import com.frugs.dungeoncrawler.appstate.InGame
import com.frugs.dungeoncrawler.appstate.MainMenuController
import com.frugs.dungeoncrawler.appstate.RtsCamera
import com.frugs.dungeoncrawler.control.CameraActionListener.CameraAction
import com.frugs.dungeoncrawler.control.PlayerControl.PlayerAction
import com.frugs.dungeoncrawler.event.EventManager
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.jme3.app.FlyCamAppState
import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger
import com.jme3.material.Material
import com.jme3.system.AppSettings
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import java.awt.*

import static groovy.transform.TypeCheckingMode.SKIP

@Singleton
@CompileStatic
class DungeonCrawler extends SimpleApplication {

    RtsCamera rtsCamera
    InGame inGame
    EventManager eventManager
    MainMenuController mainMenuController
    Map<String, Material> materials

    @TypeChecked(SKIP)
    public static void main(String[] args) {
        Injector injector = Guice.createInjector()

        DungeonCrawler app = injector.getInstance(DungeonCrawler)
        app.start() // start the game
    }

    @Inject
    private DungeonCrawler(RtsCamera rtsCamera, InGame inGame, MainMenuController mainMenuController, EventManager eventManager) {
        AppSettings appSettings = new AppSettings(true)
        appSettings.frameRate = 60

//        GraphicsDevice device = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice
//        DisplayMode displayMode = device.displayModes[0] // note: there are usually several, let's pick the first
//        appSettings.setResolution(displayMode.width, displayMode.height)
//        appSettings.frequency = displayMode.refreshRate
//        appSettings.depthBits = displayMode.bitDepth
//        appSettings.fullscreen = device.fullScreenSupported

        showSettings = false
        settings = appSettings
        this.rtsCamera = rtsCamera
        this.inGame = inGame
        this.mainMenuController = mainMenuController
        this.eventManager = eventManager
    }

    @Override
    void simpleInitApp() {
        initAssets()
        initKeyBindings()
        initAppStates()
    }

    private void initAssets() {
        materials = [
            unshaded: new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
        ]
    }

    @Override
    void simpleUpdate(float tpf) {
    }

    private void initAppStates() {
        stateManager.detach(stateManager.getState(FlyCamAppState))
        stateManager.attach(rtsCamera)
        stateManager.attach(inGame)
        stateManager.attach(eventManager)
        stateManager.attach(mainMenuController)
    }

    private void initKeyBindings() {
        Map keyBindings = [
            (CameraAction.MOVE_UP.id): KeyInput.KEY_UP,
            (CameraAction.MOVE_LEFT.id): KeyInput.KEY_LEFT,
            (CameraAction.MOVE_DOWN.id): KeyInput.KEY_DOWN,
            (CameraAction.MOVE_RIGHT.id): KeyInput.KEY_RIGHT,
            (PlayerAction.STOP.id): KeyInput.KEY_S
        ]

        Map mouseBindings = [
            (PlayerAction.MOVE_MOUSE_LOCATION.id): MouseInput.BUTTON_RIGHT,
        ]

        keyBindings.each { String name, int keyBinding ->
            inputManager.addMapping(name, new KeyTrigger(keyBinding))
        }

        mouseBindings.each { String name, int mouseBinding ->
            inputManager.addMapping(name, new MouseButtonTrigger(mouseBinding))
        }
    }
}
