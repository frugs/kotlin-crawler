package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.util.MockMaterial
import com.frugs.dungeoncrawler.event.EventManager
import org.junit.Test
import com.frugs.dungeoncrawler.game.Player
import com.jme3.math.Vector3f

class PlayerMoveTest {

    val player = Player(MockMaterial())
    val eventManager = EventManager()
    val tpf = 1/60.toFloat()

    Test fun playerMove_shouldMovePlayerToDestination() {
        val destination = Vector3f.UNIT_Z.mult(10.0)!!
        val event = PlayerMove(destination, player)

        eventManager.queueEvent(event)

        (0..100).forEach { eventManager.update(tpf) }
        assert(player.getLocalTranslation() == destination, "Player didn't reach destination. Player ended up at ${player.getLocalTranslation()}")
    }

    Test fun playerMove_shouldRotatePlayerToFaceDestination() {
        val destination = Vector3f.UNIT_Z.mult(10.0)!!.negate()!!
        val event = PlayerMove(destination, player, true)

        eventManager.queueEvent(event)

        (0..100).forEach { eventManager.update(tpf) }
        println(player.getLocalTranslation())
        assert(player.facingDirection == Vector3f.UNIT_X.negate(), "Player isn't facing destination. Player ended facing at ${player.facingDirection}")
    }
}
