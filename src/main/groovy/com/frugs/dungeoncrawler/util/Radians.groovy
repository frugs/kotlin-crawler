package com.frugs.dungeoncrawler.util

import com.jme3.math.FastMath

class Radians {
    static float normaliseAngle(float angle) {
        StrictMath.abs(angle) % FastMath.TWO_PI
    }

    static float largerAbsolute(float angle1, float angle2) {
        StrictMath.abs(angle1) > StrictMath.abs(angle2) ? angle1 : angle2
    }
}
