package com.genexus.android.core.common

import com.genexus.android.core.base.metadata.ApplicationDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.metadata.theme.ThemeDefinition
import com.genexus.android.core.base.metadata.theme.ThemeTokensDefaultOptionsDefinition
import com.genexus.android.core.base.metadata.theme.ThemeTokensDefinition
import com.genexus.android.core.base.services.IApplication
import com.genexus.android.core.base.services.Services
import com.genexus.android.json.NodeObject
import com.google.common.truth.Truth.assertThat
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ThemesManagerTest {
    private val manager = ThemesManager()
    private val appDefinition = ApplicationDefinition()

    @Before
    fun setup() {
        val app = mock<IApplication>()
        whenever(app.definition).thenReturn(appDefinition)
        whenever(app.get()).thenReturn(mock())
        Services.Application = app
    }

    @Test
    fun `Given a definition with a token, when asked for a property, then it returns the value`() {
        val theme = ThemeDefinition("", true).apply {
            setAsLoaded()
            putClass(
                ThemeClassDefinition(this, null).apply {
                    name = "Class"
                    setProperty("Property", "\$token")
                }
            )

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Simple\" } }"
                        )
                    )
                )
            )

            tokensDefaultOptions = ThemeTokensDefaultOptionsDefinition(mock())
        }
        appDefinition.putTheme(theme)
        manager.setCurrentTheme(mock(), "")

        val themeClass = manager.getThemeClass("Class")
        val property = themeClass?.getProperty("Property")

        assertThat(property).isEqualTo("Simple")
    }

    @Test
    fun `Given a definition, when setting the default option, then it returns the default option value`() {
        // Arrange
        val theme = ThemeDefinition("", true).apply {
            setAsLoaded()
            putClass(
                ThemeClassDefinition(this, null).apply {
                    name = "Class"
                    setProperty("Property", "\$token")
                }
            )

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Simple\" } }"
                        )
                    )
                )
            )

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Default\" }, \"Options\" : { \"option\" : \"1\" } }"
                        )
                    )
                )
            )

            tokensDefaultOptions = ThemeTokensDefaultOptionsDefinition(mock())
        }
        appDefinition.putTheme(theme)
        manager.setCurrentTheme(mock(), "")

        // Act
        appDefinition.setDefaultOption("option", "1")
        val themeClass = manager.getThemeClass("Class")
        val property = themeClass?.getProperty("Property")

        // Assert
        assertThat(property).isEqualTo("Default")
    }

    @Test
    fun `Given a definition, when setting the default option and a normal option, then it returns the normal option value`() {
        // Arrange
        val theme = ThemeDefinition("", true).apply {
            setAsLoaded()
            putClass(
                ThemeClassDefinition(this, null).apply {
                    name = "Class"
                    setProperty("Property", "\$token")
                }
            )

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Simple\" } }"
                        )
                    )
                )
            )

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Default\" }, \"Options\" : { \"option\" : \"1\" } }"
                        )
                    )
                )
            )

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Normal\" }, \"Options\" : { \"option\" : \"2\" } }"
                        )
                    )
                )
            )

            tokensDefaultOptions = ThemeTokensDefaultOptionsDefinition(mock())
        }
        appDefinition.putTheme(theme)
        manager.setCurrentTheme(mock(), "")

        // Act
        appDefinition.setDefaultOption("option", "1")
        appDefinition.setOption("option", "2")
        val themeClass = manager.getThemeClass("Class")
        val property = themeClass?.getProperty("Property")

        // Assert
        assertThat(property).isEqualTo("Normal")
    }

    @Test
    fun `Given a definition with an import, when setting the default option, then it returns the default option value`() {
        // Arrange
        val themeImported = ThemeDefinition("", true).apply {
            setAsLoaded()
            putClass(
                ThemeClassDefinition(this, null).apply {
                    name = "Class"
                    setProperty("Property", "\$token")
                }
            )

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Simple\" } }"
                        )
                    )
                )
            )

            tokensDefaultOptions = ThemeTokensDefaultOptionsDefinition(mock())
        }
        val theme = ThemeDefinition("", true).apply {
            setAsLoaded()

            putTokens(
                ThemeTokensDefinition(
                    NodeObject(
                        JSONObject(
                            "{ \"Values\" : { \"token\" : \"Default\" }, \"Options\" : { \"option\" : \"1\" } }"
                        )
                    )
                )
            )

            putImportedStylesTheme(themeImported)

            tokensDefaultOptions = ThemeTokensDefaultOptionsDefinition(mock())
        }
        appDefinition.putTheme(theme)
        manager.setCurrentTheme(mock(), "")

        // Act
        appDefinition.setDefaultOption("option", "1")
        val themeClass = manager.getThemeClass("Class")
        val property = themeClass?.getProperty("Property")

        // Assert
        assertThat(property).isEqualTo("Default")
    }
}
