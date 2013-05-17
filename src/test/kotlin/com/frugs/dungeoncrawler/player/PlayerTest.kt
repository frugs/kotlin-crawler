package com.frugs.event.player

import com.jme3.material.Material
import com.frugs.dungeoncrawler.game.Player
import com.jme3.math.ColorRGBA
import org.junit.Before
import com.jme3.math.Vector3f
import com.jme3.math.FastMath
import org.junit.Test

class PlayerTest {
    private val tpf = 1.0.toFloat()

    inner class MockMaterial: Material() {
        override public fun setColor(name: String?, value: ColorRGBA?) {}
    }

    private val mat = MockMaterial()
    val player = Player(mat)

    Before fun setUp() {
        player.angularSpeed = FastMath.HALF_PI
        player.setLocalTranslation(Vector3f.UNIT_XYZ)
    }

    Test fun rotateTowardsDirection_shouldRotateClockwise_givenQuarterPi() {
        val quarterPi = player.getLocalTranslation()!!.add(Vector3f.UNIT_X.negate()!!.add(Vector3f.UNIT_Z)!!.normalize())!!
        player.rotateTowardsDestination(quarterPi, tpf)
        assertRotatedClockwise()
    }

    Test fun rotateTowardsDirection_shouldRotateClockwise_givenHalfPi() {
        val halfPi = player.getLocalTranslation()!!.add(Vector3f.UNIT_X.negate())!!
        player.rotateTowardsDestination(halfPi, tpf)
        assertRotatedClockwise()
    }

    Test fun rotateTowardsDirection_shouldRotateClockwise_givenThreeQuarterPi() {
        val threeQuarterPi = player.getLocalTranslation()!!.add(Vector3f.UNIT_X.negate()!!.add(Vector3f.UNIT_Z.negate()!!)!!.normalize())!!
        player.rotateTowardsDestination(threeQuarterPi, tpf)
        assertRotatedClockwise()
    }

    Test fun rotateTowardsDirection_shouldRotateClockwise_givenVerySmallAngle() {
        val verySmallAngle = player.getLocalTranslation()!!.add(Vector3f.UNIT_Z.add(Vector3f.UNIT_X.negate()!!.mult(0.01))!!.normalize())!!
        player.rotateTowardsDestination(verySmallAngle, tpf)
        assertRotatedClockwise()
    }

    Test fun rotateTowardsDirection_shouldRotateAntiClockwise_givenQuarterPi() {
        val quarterPi = player.getLocalTranslation()!!.add(Vector3f.UNIT_X.add(Vector3f.UNIT_Z)!!.normalize())!!
        player.rotateTowardsDestination(quarterPi, tpf)
        assertRotatedAntiClockwise()
    }

    Test fun rotateTowardsDirection_shouldRotateAntiClockwise_givenHalfPi() {
        val halfPi = player.getLocalTranslation()!!.add(Vector3f.UNIT_X)!!
        player.rotateTowardsDestination(halfPi, tpf)
        assertRotatedAntiClockwise()
    }

    Test fun rotateTowardsDirection_shouldRotateAntiClockwise_givenThreeQuarterPi() {
        val threeQuarterPi = player.getLocalTranslation()!!.add(Vector3f.UNIT_X.add(Vector3f.UNIT_Z.negate()!!)!!.normalize())!!
        player.rotateTowardsDestination(threeQuarterPi, tpf)
        assertRotatedAntiClockwise()
    }

    Test fun rotateTowardsDirection_shouldRotateAntiClockwise_givenVerySmallAngle() {
        val verySmallAngle = player.getLocalTranslation()!!.add(Vector3f.UNIT_Z.add(Vector3f.UNIT_X.mult(0.01))!!.normalize())!!
        player.rotateTowardsDestination(verySmallAngle, tpf)
        assertRotatedAntiClockwise()
    }

 fun assertRotatedClockwise() {
        assert(player.facingDirection.x < 0, "Player did not rotate clockwise; rotated anti-clockwise to $player.facingDirection")
    }

 fun assertRotatedAntiClockwise() {
        assert(player.facingDirection.x > 0, "Player did not rotate anti-clockwise; rotated clockwise to $player.facingDirection")
    }
}