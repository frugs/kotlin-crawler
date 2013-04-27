package com.frugs.event.player

import com.frugs.dungeoncrawler.game.Player
import com.frugs.event.EventManager
import com.frugs.util.TestSimpleApplication
import com.google.inject.Guice
import com.google.inject.Injector
import com.jme3.material.Material
import com.jme3.math.Vector3f

class PlayerMoveTest extends TestSimpleApplication {

    static Injector injector = Guice.createInjector()

    EventManager eventManager1

    static void main(String[] args) {
        tests.each { test, method ->
            method(new PlayerMoveTest(error: { "$test failed" }))
        }
    }

    static Map<String, Closure> tests = [
        process_shouldMovePlayerTowardsDestination: { PlayerMoveTest testApp ->
            float result
            Material mat
            Player player
            testApp.eventManager1 = injector.getInstance(EventManager)

            testApp.simpleInit = {
                mat = new Material(testApp.assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
                player = new Player(mat)
                assert player.localTranslation == Vector3f.ZERO
                testApp.stateManager.attach(testApp.eventManager1)
                testApp.eventManager1.queueEvent(new PlayerMove(Vector3f.UNIT_X.mult(20.0f), player))
            }
            testApp.update = {
                result = player.localTranslation.x
            }
            testApp.succeedIf = {
                player.localTranslation.x > 19.8f
            }

            testApp.start()
        }
    ]
}
