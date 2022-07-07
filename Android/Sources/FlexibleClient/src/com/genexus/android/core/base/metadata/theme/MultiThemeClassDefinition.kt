package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.utils.Strings

class MultiThemeClassDefinition
private constructor(theme: ThemeDefinition, private val strategy: PrecedenceStrategy) :
    ThemeClassDefinition(theme, null) {

    private val list = mutableListOf<ThemeClassDefinition>()

    private enum class PrecedenceStrategy {
        Css, // normal precedence for multiple classes used by user
        FirstInList, // first in list, used for class references like gx-readonly-class
        // which need to get priority no matter where they are declared in the theme
    }

    override fun getProperty(name: String): Any? {
        return when (strategy) {
            PrecedenceStrategy.Css -> getPropertyCss(name)
            PrecedenceStrategy.FirstInList -> getPropertyFirst(name)
        }
    }

    private fun getPropertyCss(name: String): Any? {
        var bestResult: ThemeValueWithSource? = null
        for (themeClass in list) {
            val result = themeClass.getPropertyWithSource(name)
            if (result != null && (bestResult == null || result.isAfter(mTheme, bestResult)))
                bestResult = result
        }
        return bestResult?.value ?: super.getProperty(name)
    }

    private fun getPropertyFirst(name: String): Any? {
        for (themeClass in list) {
            val result = themeClass.getProperty(name)
            if (result != null)
                return result
        }
        return super.getProperty(name)
    }

    companion object {
        /**
         * Creates a new definition that represent a class with an added behaviour, i.e readonly
         * @return a combined class if it has a class for the added behaviour or null if not
         */
        @JvmStatic
        fun classReference(mainClass: ThemeClassDefinition, addedBehaviorClassPropertyName: String): ThemeClassDefinition? {
            val readonlyClassName = mainClass.optStringProperty(addedBehaviorClassPropertyName)
            if (Strings.hasValue(readonlyClassName)) {
                val addedBehaviorClass = mainClass.theme.getClass(readonlyClassName)
                if (addedBehaviorClass != null) {
                    val multi = MultiThemeClassDefinition(mainClass.theme, PrecedenceStrategy.FirstInList)
                    multi.list.add(addedBehaviorClass)
                    multi.list.add(mainClass)
                    return multi
                }
            }
            return null
        }

        /**
         * Creates a new definition that represent the composite classes
         */
        @JvmStatic
        fun create(className: String, theme: ThemeDefinition): ThemeClassDefinition {
            val multi = MultiThemeClassDefinition(theme, PrecedenceStrategy.Css)
            multi.name = className
            multi.list.addAll(
                className
                    .split(' ')
                    .mapNotNull { theme.getClass(it) }
            ) // just in case, themes may return null for some classes
            return multi
        }
    }
}
