package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import java.util.Calendar;

import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class ConstantDateExpression extends ConstantExpression
{
	static final String TYPE = "date";

	public ConstantDateExpression(INodeObject node)
	{
		super(node);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		return parseGxDate(getConstant());
	}

	public static @NonNull Value parseGxDate(String str)
	{
		if (!Strings.hasValue(str))
			return Value.UNKNOWN;

		if (!str.startsWith("#") || !str.endsWith("#"))
			return Value.UNKNOWN;

		Expression.Type type = Type.UNKNOWN;
		str = str.substring(1, str.length() - 1);

		// Separate into date and time parts (both are optional).
		String datePart = null;
		String timePart = null;
		int blankPosition = str.indexOf(Strings.SPACE);
		if (blankPosition != -1)
		{
			datePart = str.substring(0, blankPosition);
			timePart = str.substring(blankPosition + 1, str.length());
		}
		else
		{
			// Only date or time. Which one?
			if (str.contains("-"))
				datePart = str;
			else
				timePart = str;
		}

		// Calendar object to create the date, component by component.
		// Clear it because it's set to "now" by default.
		Calendar calendar = Calendar.getInstance();
		calendar.clear();

		// Parse date components.
		if (Strings.hasValue(datePart))
		{
			String[] dateNumbers = datePart.split("-", -1);
			type = Type.DATE;

			if (dateNumbers.length > 0)
			{
				int year = Services.Strings.tryParseInt(dateNumbers[0], 0);
				if (year < 100)
					year += 2000;

				calendar.set(Calendar.YEAR, year);
			}

			if (dateNumbers.length > 1)
				calendar.set(Calendar.MONTH, Services.Strings.tryParseInt(dateNumbers[1], 1) - 1); // Remember, month is 0-based.

			if (dateNumbers.length > 2)
				calendar.set(Calendar.DAY_OF_MONTH, Services.Strings.tryParseInt(dateNumbers[2], 1));
		}

		// Parse time components.
		if (Strings.hasValue(timePart))
		{
			type = (type == Type.DATE ? Type.DATETIME : Type.TIME);

			// May end in A/P (for AM/PM).
			int hourOffset = 0;
			String lastChar = timePart.substring(timePart.length() - 1);
			if (lastChar.equalsIgnoreCase("A") || lastChar.equalsIgnoreCase("P"))
			{
				timePart = timePart.substring(0, timePart.length() - 1);
				if (lastChar.equalsIgnoreCase("P"))
					hourOffset = 12;
			}

			String[] timeNumbers = timePart.split(":", -1);

			if (timeNumbers.length > 0)
			{
				int hour = Services.Strings.tryParseInt(timeNumbers[0], 0);
				hour += hourOffset;
				calendar.set(Calendar.HOUR, hour);
			}

			if (timeNumbers.length > 1)
				calendar.set(Calendar.MINUTE, Services.Strings.tryParseInt(timeNumbers[1], 0));

			if (timeNumbers.length > 2)
				calendar.set(Calendar.SECOND, Services.Strings.tryParseInt(timeNumbers[1], 0));
		}

		return new Value(type, calendar.getTime());
	}
}
