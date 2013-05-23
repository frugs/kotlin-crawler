package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.util.MockMaterial
import com.frugs.dungeoncrawler.event.EventManager
import org.junit.Test
import com.frugs.dungeoncrawler.game.Player
import com.jme3.math.Vector3f
import com.jme3.math.FastMath

class PlayerMoveTest {

    private val player = Player(MockMaterial)
    private val tpf = 1/60.toFloat()

    Test fun playerMove_shouldMovePlayerToDestination() {
        val destination = Vector3f.UNIT_Z.mult(10.0)!!
        val event = PlayerMove(destination, player)

        EventManager.queueEvent(event)

        (0..100).forEach { EventManager.update(tpf) }
        assert(player.getLocalTranslation() == destination, "Player didn't reach destination. Player ended up at ${player.getLocalTranslation()}")
    }

    Test fun playerMove_shouldRotatePlayerToFaceDestination_givenAngleOfHalfPi() {
        val destination = Vector3f.UNIT_X.mult(10.0)!!.negate()!!
        val event = PlayerMove(destination, player, true)

        EventManager.queueEvent(event)

        (0..100).forEach { EventManager.update(tpf) }
        assert(player.facingDirection.isRoughlyTheSameAs(Vector3f.UNIT_X.negate()!!),
            "Player isn't facing destination. Player ended facing at ${player.facingDirection}")
    }

    Test fun playerMove_shouldRotatePlayerToFaceDestination_givenAngleOfPi() {
        val destination = Vector3f.UNIT_Z.mult(10.0)!!.negate()!!
        val event = PlayerMove(destination, player, true)

        EventManager.queueEvent(event)

        (0..100).forEach { EventManager.update(tpf) }
        assert(player.facingDirection.isRoughlyTheSameAs(Vector3f.UNIT_Z.negate()!!),
            "Player isn't facing destination. Player ended facing at ${player.facingDirection}")
    }

    fun Vector3f.isRoughlyTheSameAs(vector: Vector3f, accuracy: Float = 0.000002.toFloat()): Boolean {
        fun Float.isRoughlyTheSameAs(float: Float): Boolean = FastMath.abs(this - float) < accuracy
        return x.isRoughlyTheSameAs(vector.x) && y.isRoughlyTheSameAs(vector.y) && z.isRoughlyTheSameAs(vector.z)
    }
}
