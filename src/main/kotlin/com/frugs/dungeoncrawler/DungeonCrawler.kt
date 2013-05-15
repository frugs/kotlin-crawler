package com.frugs.dungeoncrawler

import com.jme3.system.AppSettings
import java.awt.GraphicsEnvironment
import com.jme3.app.SimpleApplication
import java.awt.DisplayMode
import java.awt.GraphicsDevice
import com.jme3.material.Material
import com.jme3.app.FlyCamAppState
import com.frugs.dungeoncrawler.appstate.MainMenuController
import com.frugs.dungeoncrawler.guice.DungeonCrawlerModule
import com.google.inject.Guice
import com.frugs.dungeoncrawler.appstate.InGame
import com.frugs.dungeoncrawler.appstate.RtsCamera

class DungeonCrawler(): SimpleApplication() {
    {
        fun getAppSettingsForDeviceAndDisplayMode(device: GraphicsDevice, displayMode: DisplayMode): AppSettings {
            val appSettings = AppSettings(true)
            appSettings.setFrameRate(60)
            appSettings.setResolution(displayMode.getWidth(), displayMode.getHeight())
            appSettings.setFrequency(displayMode.getRefreshRate())
            appSettings.setFullscreen(device.isFullScreenSupported())
            return appSettings
        }

        fun getDefaultAppSettings(): AppSettings {
            val appSettings = AppSettings(true)
            appSettings.setFrameRate(60)
            return appSettings
        }

        val device = GraphicsEnvironment.getLocalGraphicsEnvironment()?.getDefaultScreenDevice()
        val displayMode = device?.getDisplayModes()?.sortBy { it.getWidth() }?.last
        val appSettings = if (device != null && displayMode != null)
            getAppSettingsForDeviceAndDisplayMode(device, displayMode) else
            getDefaultAppSettings()

        showSettings = false
        setSettings(appSettings)
    }

    public val materials: MutableMap<String, Material>
        get() = hashMapOf(
            Pair("unshaded", Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"))
        )

    public override fun simpleInitApp() {
        fun attachAppStates() {
            val injector = Guice.createInjector(DungeonCrawlerModule(this))!!
            val stateManager = getStateManager()!!
            val mainMenuController = injector.getInstance(javaClass<MainMenuController>())
            val inGame = injector.getInstance(javaClass<InGame>())
            val rtsCamera = injector.getInstance(javaClass<RtsCamera>())

            stateManager.detach(getStateManager()?.getState(javaClass<FlyCamAppState>()))
            stateManager.attach(mainMenuController)
            stateManager.attach(inGame)
            stateManager.attach(rtsCamera)
        }

        attachAppStates()
    }
}