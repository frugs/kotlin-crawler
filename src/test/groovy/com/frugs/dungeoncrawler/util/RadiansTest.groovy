package com.frugs.dungeoncrawler.util

import com.jme3.math.FastMath
import groovy.transform.CompileStatic
import org.junit.Test

@CompileStatic
class RadiansTest {

    @Test
    void normaliseAngle_shouldNormalise_NegativeAngles() {
        assert Radians.normaliseAngle(FastMath.PI * -1) == FastMath.PI
    }

    @Test
    void normaliseAngle_shouldNormalise_AnglesGreaterThan2Pi() {
        assert Radians.normaliseAngle(FastMath.PI * 4.5f).round(6) == FastMath.HALF_PI.round(6)
    }
}
