package com.genexus.android.core.base.metadata.images

import kotlin.math.abs

class ImageCollection(
    val language: String,
    val theme: String,
    val isDefault: Boolean,
    val baseDirectory: String
) {
    private val images = mutableListOf<ImageFile>()
    fun add(file: ImageFile) {
        images.add(file)
    }

    fun get(imageName: String, screenDensity: Float) =
        images
            .filter { imageName.equals(it.name, ignoreCase = true) }
            .maxByOrNull { Points(it, screenDensity) }
}

private class Points(imageFile: ImageFile, screenDensity: Float) : Comparable<Points> {
    override fun compareTo(other: Points): Int {
        // options has priority over any density
        (optionsPoints - other.optionsPoints).let { if (it != 0) return it }

        if (!densityType.isClose || !other.densityType.isClose) {
            // different density type
            (-densityType.ordinal + other.densityType.ordinal).let { if (it != 0) return it }
        }

        // same density type
        return when (densityType) {
            ScaleType.Exact -> 0
            ScaleType.Lower, ScaleType.Higher -> {
                if (distance == other.distance)
                    densityType.ordinal - other.densityType.ordinal // prefer higher
                else
                    -distance.compareTo(other.distance)
            }
            ScaleType.Unknown -> 0
        }
    }

    enum class ScaleType {
        // Priority
        Exact, // 1. exact as screenDensity
        Lower, // 2. highest of the lower then screenDensity
        Higher, // 3. lowest of the higher than screenDensity,
        Unknown; // 4. unknown scale

        val isClose get() = this == Lower || this == Higher
    }

    private val optionsPoints = imageFile.options.currentPoints
    private val densityType: ScaleType
    private val density = imageFile.density
    private val distance = abs(density - screenDensity)

    init {
        densityType = when {
            density.isNaN() -> ScaleType.Unknown // not specified density
            density == screenDensity -> ScaleType.Exact
            density < screenDensity -> ScaleType.Lower
            density > screenDensity -> ScaleType.Higher
            else -> ScaleType.Unknown
        }
    }
}
