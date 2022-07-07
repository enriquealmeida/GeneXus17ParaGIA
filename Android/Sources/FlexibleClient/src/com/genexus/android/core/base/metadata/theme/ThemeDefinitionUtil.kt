package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.utils.Strings

/**
 * The user can set the class in slitly different ways but at the end they refer to the same set of classes,
 * this function will normalize the name so it can be used as a key for a NamedMap
 */
fun normalizeClassName(name: String): String {
    return Strings.trimAll(name)
        .split(' ') // split composite classes
        .filter { it.isNotBlank() } // remove extra spaces
        .sorted() // sort composite classes, since order doesn't matter for functionally but it will allow reuse of cache for different orders
        .joinToString(separator = " ") // combine it again to a single string
}

/**
 * @param name it should be called with a normalized class name
 * @return true if the it has more than one class
 */
fun isCompositeClass(name: String) = name.contains(' ')

private fun <T> ThemeDefinition.getImportedPropertyLoop(className: String, getProperty: (ThemeClassDefinition) -> T): T? {
    var result: T? = null
    for (importedTheme in importedStylesThemes.reversed()) { // reverse list to respect precedence, last definition wins
        val classDefinition = importedTheme.getClass(className)
        if (classDefinition != null)
            result = getProperty(classDefinition)
        else
            result = importedTheme.getImportedPropertyLoop(className, getProperty)
        if (result != null)
            break
    }
    return result
}

/**
 * Get the value of the property looking in the imported themes
 * @param className is the name of the class to look for
 * @return value or null if not found
 */
fun ThemeDefinition.getImportedProperty(className: String, propertyName: String): Any? {
    return getImportedPropertyLoop(className) { it.getPropertyRaw(propertyName) }
}

/**
 * Get the value of the property looking in the imported themes
 * @param className is the name of the class to look for
 * @return value with source where it was found or null if not found
 */
fun ThemeDefinition.getImportedPropertyWithSource(className: String, propertyName: String): ThemeValueWithSource? {
    return getImportedPropertyLoop(className) { it.getPropertyRawWithSource(propertyName) }
}
