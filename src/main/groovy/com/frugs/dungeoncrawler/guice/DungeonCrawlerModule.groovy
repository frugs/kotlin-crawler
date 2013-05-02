package com.frugs.dungeoncrawler.guice

import com.frugs.dungeoncrawler.DungeonCrawler
import com.frugs.dungeoncrawler.event.EventManager
import com.google.inject.AbstractModule
import com.jme3.app.state.AppStateManager
import com.jme3.asset.AssetManager
import com.jme3.audio.AudioRenderer
import com.jme3.input.InputManager
import com.jme3.renderer.Camera
import com.jme3.renderer.ViewPort
import com.jme3.scene.Node

class DungeonCrawlerModule extends AbstractModule {

    private DungeonCrawler app

    DungeonCrawlerModule(DungeonCrawler app) {
        this.app = app
    }

    @Override
    protected void configure() {
        bind(Camera).toInstance(app.camera)
        bind(EventManager).toInstance(app.eventManager)
        bind(InputManager).toInstance(app.inputManager)
        bind(AppStateManager).toInstance(app.stateManager)
        bind(Node).toInstance(app.rootNode)
        bind(AudioRenderer).toInstance(app.audioRenderer)
        bind(ViewPort).toInstance(app.guiViewPort)
        bind(AssetManager).toInstance(app.assetManager)
    }
}
