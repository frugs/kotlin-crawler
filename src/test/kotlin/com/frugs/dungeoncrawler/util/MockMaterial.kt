package com.frugs.dungeoncrawler.util

import com.jme3.material.Material
import com.jme3.math.ColorRGBA

class MockMaterial {
    class object: Material() {
        override public fun setColor(name: String?, value: ColorRGBA?) {
        }
    }
}
