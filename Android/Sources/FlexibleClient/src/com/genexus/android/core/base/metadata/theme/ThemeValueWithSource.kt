package com.genexus.android.core.base.metadata.theme

import java.lang.IllegalArgumentException

/**
 * Value of a theme class property with the information where it was declared
 */
class ThemeValueWithSource(var value: Any, val theme: ThemeDefinition, val position: Int) {
    /**
     * Used to check precedence, if this wins over other
     * @param rootTheme for the comparison to start, this and other must be on rootTheme or the imported ones
     * @param other value to compare with
     * @return true if this value was declared after, in the same theme or the imported ones
     */
    fun isAfter(rootTheme: ThemeDefinition, other: ThemeValueWithSource): Boolean {
        if (theme == other.theme)
            return position > other.position
        else
            return themePosition(rootTheme) < other.themePosition(rootTheme) // less themePosition means it is after in the import tree
    }

    /**
     * Gets the position of theme in the imported tree of rootTheme,
     * position starts at the end, 0 means it is the same theme, then it starts counting from the last import
     * @return the position in reverse pre-order traversal
     * @throws IllegalArgumentException if theme is not part of the imported tree of rootTheme
     */
    private fun themePosition(rootTheme: ThemeDefinition): Int {
        var position = 0
        fun visit(t: ThemeDefinition): Int? {
            if (t == rootTheme) // check current first since it has precedence
                return position
            position++
            return t.importedStylesThemes
                .reversed() // last import has precedence
                .mapNotNull { visit(it) } // lazy so it only continues until first not null
                .firstOrNull() // returns null if not found
        }
        return visit(theme) ?: throw IllegalArgumentException("${rootTheme.name} doesn't contain ${theme.name}")
    }
}
