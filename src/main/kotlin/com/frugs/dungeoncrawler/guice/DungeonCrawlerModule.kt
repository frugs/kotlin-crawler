package com.frugs.dungeoncrawler.guice

import com.google.inject.AbstractModule
import com.frugs.dungeoncrawler.DungeonCrawler
import com.jme3.renderer.ViewPort
import com.jme3.app.state.AppStateManager
import com.jme3.asset.AssetManager
import com.jme3.input.InputManager
import com.jme3.audio.AudioRenderer
import com.jme3.scene.Node
import com.jme3.renderer.Camera
import com.jme3.material.Material
import com.google.inject.name.Names

class DungeonCrawlerModule(val app: DungeonCrawler): AbstractModule() {

    protected override fun configure() {
        for (entry in app.materials) {
            bind(javaClass<Material>())!!.annotatedWith(Names.named(entry.key))!!.toInstance(entry.value)
        }

        bind(javaClass<Node>())!!.toInstance(app.getRootNode())
        bind(javaClass<Camera>())!!.toInstance(app.getCamera())
        bind(javaClass<AppStateManager>())!!.toInstance(app.getStateManager()!!)
        bind(javaClass<AssetManager>())!!.toInstance(app.getAssetManager()!!)
        bind(javaClass<InputManager>())!!.toInstance(app.getInputManager()!!)
        bind(javaClass<AudioRenderer>())!!.toInstance(app.getAudioRenderer()!!)
        bind(javaClass<ViewPort>())!!.toInstance(app.getGuiViewPort()!!)
    }
}
