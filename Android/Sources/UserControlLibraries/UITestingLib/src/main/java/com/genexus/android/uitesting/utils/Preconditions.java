package com.genexus.android.uitesting.utils;

import androidx.annotation.NonNull;

public class Preconditions {
    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
	public static @NonNull <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

	/**
	 * Checks whether a String variable passed as a parameter to the calling
	 * method is a DateTime value or not.
	 *
	 * @param value a String that could be in DateTime format
	 * @return true if the value matches the DateTime regular expression, false otherwise
	 */
    public static boolean isDateTime(String value) {
		return value.matches("^((\\d{1,2})/(\\d{1,2})/(\\d{2}|\\d{4})[,]\\s([0-1]\\d|[2][0-3]):[0-5]\\d)(:[0-5]\\d)?\\s(AM|am|aM|Am|PM|pm|pM|Pm)");
	}
}
