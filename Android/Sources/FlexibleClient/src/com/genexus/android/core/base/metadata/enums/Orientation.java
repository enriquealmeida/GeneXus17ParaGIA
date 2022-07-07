package com.genexus.android.core.base.metadata.enums;

import com.genexus.android.core.base.utils.Strings;

public enum Orientation
{
	UNDEFINED,
	PORTRAIT,
	LANDSCAPE;

	@Override
	public String toString()
	{
		switch(this)
		{
			case PORTRAIT : return "Portrait";
			case LANDSCAPE : return "Landscape";
			default : return "Undefined";
		}
	}

	public static Orientation parse(String value)
	{
		if (Strings.hasValue(value))
		{
			if (value.equalsIgnoreCase("Portrait"))
				return PORTRAIT;
			else if (value.equalsIgnoreCase("Landscape"))
				return LANDSCAPE;
		}

		return UNDEFINED; // Empty or "Any".
	}

	public static Orientation opposite(Orientation orientation)
	{
		if (orientation == PORTRAIT)
			return LANDSCAPE;
		else if (orientation == LANDSCAPE)
			return PORTRAIT;
		else
			return UNDEFINED;
	}
}
