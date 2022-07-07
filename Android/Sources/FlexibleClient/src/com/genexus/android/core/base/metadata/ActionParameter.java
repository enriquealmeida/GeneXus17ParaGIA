package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.IAssignableExpression;
import com.genexus.android.core.base.services.Services;

public class ActionParameter implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String mName;
	private final String mValue;
	private final Expression mExpression;
	private DataItem mValueDefinition;

	/**
	 * Creates a "named" parameter (unless name is null, in which case it's a "value" one).
	 */
	public ActionParameter(String name, String value, Expression expression)
	{
		mName = name;
		mValue = value;
		mExpression = expression;
	}

	/**
	 * Creates a "value" parameter.
	 */
	public ActionParameter(String value)
	{
		this(null, value, null);
	}

	/**
	 * Creates a "value" parameter from an expression.
	 */
	public ActionParameter(Expression expression)
	{
		this(null, null, expression);
	}

	public String getName() { return mName; }
	public void setName(String name) { mName = name; }

	public String getValue()
	{
		return Services.Language.getExpressionTranslation(mValue);
	}

	public Expression getExpression()
	{
		return mExpression;
	}

	@Override
	public String toString()
	{
		if (Services.Strings.hasValue(mName))
			return String.format("%s = %s", mName, mValue);
		else if (mExpression != null)
			return mExpression.toString();
		else
			return mValue;
	}

	private static final String ATT_OR_VAR_REGEX = "&?[a-zA-Z](\\w|\\.)*";

	public boolean isAssignable()
	{
		// Attributes, variables, and sdt fields are assignable.
		return (mValue != null && mValue.matches(ATT_OR_VAR_REGEX)) ||
			   (mExpression != null && mExpression instanceof IAssignableExpression);
	}

	public static List<String> getValues(Iterable<ActionParameter> parameters)
	{
		ArrayList<String> values = new ArrayList<>();

		if (parameters != null)
		{
			for (ActionParameter parameter : parameters)
				values.add(parameter.getValue());
		}

		return values;
	}

	public void setValueDefinition(DataItem definition)
	{
		mValueDefinition = definition;
	}

	public DataItem getValueDefinition()
	{
		return mValueDefinition;
	}
}
