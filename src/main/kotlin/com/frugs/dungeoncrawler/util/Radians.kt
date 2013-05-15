package com.frugs.dungeoncrawler.util

import com.jme3.math.FastMath


class Radians {
    class object {
        public fun normaliseAngle(angle: Float): Float = StrictMath.abs(angle) % FastMath.TWO_PI

        public fun smallerAbsolute(angle1: Float, angle2: Float): Float =
            if (StrictMath.abs(angle1) < StrictMath.abs(angle2)) angle1 else angle2
    }
}