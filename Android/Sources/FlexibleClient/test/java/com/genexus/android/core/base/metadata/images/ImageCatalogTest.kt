package com.genexus.android.core.base.metadata.images

import com.genexus.android.core.base.metadata.theme.ThemeOptions
import com.genexus.android.core.base.services.Services
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.mock
import java.util.stream.Stream

class ImageCatalogTest {
    @ParameterizedTest(name = "Densities {0}, screen {1}, expected index {2}")
    @MethodSource("provide")
    fun `Given two densities for the same image, when getting the one to use, then get the correct one`
    (densities: Array<Double>, screenDensity: Double, expectedIndex: Int) {
        // Arrange
        Services.Themes = mock()
        Services.Language = mock()

        val collection = ImageCollection("English", "White", true, "")
        val images = densities.map {
            ImageFile(collection, "Image", ImageFile.Type.Internal, "Example/Path", ThemeOptions(), it.toFloat(), false)
        }
        images.forEach { collection.add(it) }
        val catalog = ImageCatalog().apply { add(collection) }

        // Act
        val image = catalog.getImage("Image", screenDensity.toFloat())

        // Assert
        assertThat(images.indexOf(image)).isEqualTo(expectedIndex)
    }

    private companion object {
        @Suppress("UNUSED")
        @JvmStatic
        fun provide(): Stream<Arguments> =
            Stream.of(
                Arguments.of(arrayOf(1.0, 2.0), 1.0, 0), // exact in first position
                Arguments.of(arrayOf(1.0, 1.0), 1.0, 0), // exact duplicated
                Arguments.of(arrayOf(1.0, 1.5, 2.0), 2.0, 2), // exact in last position
                Arguments.of(arrayOf(1.0, 2.0, 3.0), 2.5, 2), // closest prefer higher
                Arguments.of(arrayOf(2.0, 3.0), 1.5, 0), // closest
                Arguments.of(arrayOf(Double.NaN, Double.NaN), 2.0, 0), // unknown
                Arguments.of(arrayOf(2.0, 3.0), 2.75, 1), // closest
                Arguments.of(arrayOf(2.0, 3.0), 2.25, 0), // closest
            )
    }
}
