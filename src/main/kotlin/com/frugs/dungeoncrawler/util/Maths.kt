package com.frugs.dungeoncrawler.util

import com.jme3.math.FastMath

public fun Float.sign(): Float = if (this < 0) -1.toFloat() else 1.toFloat()

public fun Float.normaliseAngle(): Float = StrictMath.abs(this) % FastMath.TWO_PI

public fun smallerAbsolute(angle1: Float, angle2: Float): Float =
    if (StrictMath.abs(angle1) < StrictMath.abs(angle2)) angle1 else angle2