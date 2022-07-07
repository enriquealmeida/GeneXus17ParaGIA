package com.genexus.android.core.base.metadata.enums;

import android.view.Gravity;

import com.genexus.android.core.base.metadata.Properties;

public final class Alignment
{
	public static final int NONE = Gravity.NO_GRAVITY;

	public static final int TOP = Gravity.TOP;
	public static final int CENTER_VERTICAL = Gravity.CENTER_VERTICAL;
	public static final int BOTTOM = Gravity.BOTTOM;

	public static final int VERTICAL_MASK = Gravity.VERTICAL_GRAVITY_MASK;
	
	public static final int LEFT = Gravity.LEFT;
	public static final int RIGHT = Gravity.RIGHT;

	public static final int START = Gravity.START;
	public static final int END = Gravity.END;

	public static final int CENTER_HORIZONTAL = Gravity.CENTER_HORIZONTAL;

	public static final int HORIZONTAL_MASK = Gravity.HORIZONTAL_GRAVITY_MASK;
	
	public static final int CENTER = Gravity.CENTER;
	
	private Alignment() { } // To prevent instantiation
	
	private static final String IMAGE_POSITION_ABOVE_TEXT = "Above Text";
	private static final String IMAGE_POSITION_BELOW_TEXT = "Below Text";
	private static final String IMAGE_POSITION_BEFORE_TEXT = "Before Text";
	private static final String IMAGE_POSITION_AFTER_TEXT = "After Text";
	private static final String IMAGE_POSITION_BEHIND_TEXT = "Behind Text";
	
	public static final String ADS_POSITION_BOTTOM = "Bottom";
	public static final String ADS_POSITION_TOP = "Top";
	
	public static int parseImagePosition(String jsonValue, int platformDefault)
	{
		if (Properties.PLATFORM_DEFAULT.equalsIgnoreCase(jsonValue))
			return platformDefault;
		
		return parseImagePosition(jsonValue);
	}
	
	public static int parseImagePosition(String jsonValue)
	{
		if (IMAGE_POSITION_ABOVE_TEXT.equals(jsonValue))
			return Alignment.TOP;
		
		if (IMAGE_POSITION_BELOW_TEXT.equals(jsonValue))
			return Alignment.BOTTOM;
	
		if (IMAGE_POSITION_BEFORE_TEXT.equals(jsonValue))
			return Alignment.START;

		if (IMAGE_POSITION_AFTER_TEXT.equals(jsonValue))
			return Alignment.END;
				
		if (IMAGE_POSITION_BEHIND_TEXT.equals(jsonValue))
			return Alignment.CENTER;
					
		return Alignment.NONE;
	}
}
