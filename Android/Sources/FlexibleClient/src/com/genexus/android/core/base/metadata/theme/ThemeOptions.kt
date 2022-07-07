package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.model.PropertiesObject
import com.genexus.android.core.base.services.Services

class ThemeOptions : PropertiesObject() {

    /**
     * Calculates the matching points for the options
     * @return a score of how many options match for that tokens, -1 if they don't match
     */
    val currentPoints: Int get() {
        var points = 0

        // iterate using tokens option because all must be met,
        // if there are optionsSetByUser that aren't in the token options it doesn't matter,
        // it is still a match but with less points.
        for (name in propertyNames) {
            val optionSetByUserValue = Services.Application.definition.getOption(name)
            if (optionSetByUserValue == null) {
                // option must be set to match
                points = -1
                break
            }
            val optionInTokenFilterValue = getStringProperty(name, null)!!
            if (optionInTokenFilterValue.equals(optionSetByUserValue, ignoreCase = true)) {
                points++
            } else {
                // option must be the same to match
                points = -1
                break
            }
        }
        return points
    }
}
