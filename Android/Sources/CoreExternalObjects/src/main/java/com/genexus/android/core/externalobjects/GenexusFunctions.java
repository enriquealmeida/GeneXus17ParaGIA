package com.genexus.android.core.externalobjects;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.FormatHelper;
import com.genexus.GXutil;

class GenexusFunctions {
	public static String gxToString(Action action, String value) {
		return parameterToString(action.getDefinition().getParameter(0), value);
	}

	public static String gxToString(Action action, String value, Integer length, Integer decimals) {
		if (length != null) {
			// Assume numeric datatype if length (possibly with decimals) is passed in.
			BigDecimal numericValue = Services.Strings.tryParseDecimal(value);
			if (numericValue != null) {
				if (decimals == null)
					decimals = 0;

				return GXutil.str(numericValue, length, decimals);
			}
		}

		// Generic conversion for all other datatypes.
		return parameterToString(action.getDefinition().getParameter(0), value);
	}

	public static String gxFormat(Action action, List<String> values) {
		// Argument 0 is format string.
		String formatString = values.get(0);

		// Arguments 1..n are the parameters.
		String[] params = new String[9];
		Arrays.fill(params, Strings.EMPTY);
		for (int i = 1; i < action.getDefinition().getParameters().size() && i < values.size(); i++)
			params[i - 1] = parameterToString(action.getDefinition().getParameter(i), values.get(i));

		return GXutil.format(formatString, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]);
	}

	private static String parameterToString(ActionParameter parameter, String value) {
		// The parameter may be a constant expression (in which case its string value is returned)
		// or a variable/attribute/SDT field (in which case we get its type to use a proper conversion).
		DataItem definition = parameter.getValueDefinition();
		if (definition != null && value != null)
			return FormatHelper.formatValue(value, definition).toString();
		else
			return value;
	}
}
