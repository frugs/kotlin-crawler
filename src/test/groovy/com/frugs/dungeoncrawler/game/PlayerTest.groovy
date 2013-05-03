package com.frugs.dungeoncrawler.game

import com.jme3.material.Material
import com.jme3.math.FastMath
import com.jme3.math.Vector3f
import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test

@CompileStatic
class PlayerTest {

    private final float tpf = 1

    Material mat = [
        setColor: { def a, def b -> }
    ] as Material
    Player player = new Player(mat)

    @Before
    void setUp() {
        player.facingDirection = Vector3f.UNIT_Z
        player.angularSpeed = FastMath.HALF_PI
        player.localTranslation = Vector3f.UNIT_XYZ
    }

    @Test
    void rotateTowardsDirection_shouldRotateClockwise_givenQuarterPi() {
        Vector3f quarterPi = player.localTranslation.add(Vector3f.UNIT_X.negate().add(Vector3f.UNIT_Z).normalize())
        player.rotateTowardsDestination(quarterPi, tpf)
        assertRotatedClockwise()
    }

    @Test
    void rotateTowardsDirection_shouldRotateClockwise_givenHalfPi() {
        Vector3f halfPi = player.localTranslation.add(Vector3f.UNIT_X.negate())
        player.rotateTowardsDestination(halfPi, tpf)
        assertRotatedClockwise()
    }

    @Test
    void rotateTowardsDirection_shouldRotateClockwise_givenThreeQuarterPi() {
        Vector3f threeQuarterPi = player.localTranslation.add(Vector3f.UNIT_X.negate().add(Vector3f.UNIT_Z.negate()).normalize())
        player.rotateTowardsDestination(threeQuarterPi, tpf)
        assertRotatedClockwise()
    }

    @Test
    void rotateTowardsDirection_shouldRotateClockwise_givenVerySmallAngle() {
        Vector3f verySmallAngle = player.localTranslation.add(Vector3f.UNIT_Z.add(Vector3f.UNIT_X.negate().mult(0.01f)).normalize())
        player.rotateTowardsDestination(verySmallAngle, tpf)
        assertRotatedClockwise()
    }

    @Test
    void rotateTowardsDirection_shouldRotateAntiClockwise_givenQuarterPi() {
        Vector3f quarterPi = player.localTranslation.add(Vector3f.UNIT_X.add(Vector3f.UNIT_Z).normalize())
        player.rotateTowardsDestination(quarterPi, tpf)
        assertRotatedAntiClockwise()
    }

    @Test
    void rotateTowardsDirection_shouldRotateAntiClockwise_givenHalfPi() {
        Vector3f halfPi = player.localTranslation.add(Vector3f.UNIT_X)
        player.rotateTowardsDestination(halfPi, tpf)
        assertRotatedAntiClockwise()
    }

    @Test
    void rotateTowardsDirection_shouldRotateAntiClockwise_givenThreeQuarterPi() {
        Vector3f threeQuarterPi = player.localTranslation.add(Vector3f.UNIT_X.add(Vector3f.UNIT_Z.negate()).normalize())
        player.rotateTowardsDestination(threeQuarterPi, tpf)
        assertRotatedAntiClockwise()
    }

    @Test
    void rotateTowardsDirection_shouldRotateAntiClockwise_givenVerySmallAngle() {
        Vector3f verySmallAngle = player.localTranslation.add(Vector3f.UNIT_Z.add(Vector3f.UNIT_X.mult(0.01f)).normalize())
        player.rotateTowardsDestination(verySmallAngle, tpf)
        assertRotatedAntiClockwise()
    }

    void assertRotatedClockwise() {
        assert player.facingDirection.x < 0, "Player rotated anti-clockwise to $player.facingDirection"
    }

    void assertRotatedAntiClockwise() {
        assert player.facingDirection.x > 0, "Player rotated clockwise to $player.facingDirection"
    }
}
