package com.frugs.util

import com.jme3.app.SimpleApplication
import com.jme3.system.AppSettings
import groovy.transform.CompileStatic
import org.junit.Assert

@CompileStatic
class TestSimpleApplication extends SimpleApplication {

    Closure simpleInit = {}
    Closure update = {}

    Closure succeedIf = { true }
    Closure failIf = { false }

    Closure error = {}

    TestSimpleApplication() {
        AppSettings appSettings = new AppSettings(true)
        appSettings.frameRate = 60
        appSettings.setResolution(1024, 768)
        showSettings = false
        settings = appSettings
    }

    @Override
    void simpleInitApp() {
        simpleInit.call()
    }

    @Override
    void update() {
        super.update()
        update.call()

        if (succeedIf.call()) {
            stop()
        }

        if (failIf.call()) {
            Assert.fail(error.call() as String)
        }
    }
}
