package com.genexus.android.core.common;

/**
 * Utility class that helps converting a Genexus datetime picture into a Java SimpleDateFormat pattern.
 * <p/>
 * Example:
 * Genexus Picture:                99/99/99 99:99
 * Java SimpleDateFormat pattern:  d/M/yy H:mm
 */
class DateTimeFormatter {
	// from https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
	// use full pattern to show date month correctly dd/MM/yy HH:mm
	// https://www.javatpoint.com/java-simpledateformat

	/**
     * Converts a Genexus time picture to a Java SimpleDateFormat pattern.
     */
    public static String getDatePattern(String datePicture, String preferredDateFormatPattern) {
        String dateFormat;

        switch (datePicture) {
            case "99/99/99":
                dateFormat = preferredDateFormatPattern.replaceFirst("[y]+", "yy");
                break;
            case "99/99/9999":
                dateFormat = preferredDateFormatPattern.replaceFirst("[y]+", "yyyy");
                break;
            default:
                dateFormat = preferredDateFormatPattern;
                break;
        }
		// use full pattern to show date month correctly dd/MM/yy
		dateFormat = dateFormat.replaceFirst("[d]+", "dd");
		dateFormat = dateFormat.replaceFirst("[M]+", "MM");

		return dateFormat;
    }

    /**
     * Convert a Genexus time picture to a Java SimpleDateFormat pattern.
     */
    public static String getTimePattern(String timePicture, boolean is24HourFormat) {
        String timeFormat;

        // Depending on the picture, we either show just hh, hh:mm (default) or hh:mm:ss.
        switch (timePicture) {
            case "99":
                timeFormat = "";
                break;
            case "99:99":
            default:
                timeFormat = ":mm";
                break;
            case "99:99:99":
                timeFormat = ":mm:ss";
                break;
			case "99:99:99.999":
				timeFormat = ":mm:ss.SSS";
				break;
        }

	   // Display either 24 hours or AM/PM format.
        if (is24HourFormat) {
            timeFormat = "HH" + timeFormat;
        } else {
            timeFormat = "hh" + timeFormat + " a";
        }

        return timeFormat;
    }
}
