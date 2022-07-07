package com.genexus.android.uitesting.visual

import kotlin.math.abs

class Bitmap(private val bitmap: android.graphics.Bitmap) {
    fun sameAs(another: android.graphics.Bitmap?): Boolean {
        if (another == null)
            return false

        val width = bitmap.width
        val height = bitmap.height
        val width2 = another.width
        val height2 = another.height
        if (width != width2 || height != height2)
            return false

        var diff = 0L
        for (y in 0 until height)
            for (x in 0 until width)
                diff += pixelDiff(bitmap.getPixel(x, y), another.getPixel(x, y)).toLong()

        val maxDiff = width * height * 3L * 255
        return 100 * diff / maxDiff <= TOLERANCE_PERCENTAGE
    }

    private fun pixelDiff(rgb1: Int, rgb2: Int): Int {
        val r1 = rgb1 shr 16 and 0xff
        val g1 = rgb1 shr 8 and 0xff
        val b1 = rgb1 and 0xff
        val r2 = rgb2 shr 16 and 0xff
        val g2 = rgb2 shr 8 and 0xff
        val b2 = rgb2 and 0xff
        return abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2)
    }

    companion object {
        private const val TOLERANCE_PERCENTAGE = 4
    }
}
