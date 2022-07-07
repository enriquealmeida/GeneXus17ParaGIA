package com.genexus.android.core.actions;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.loader.WorkWithMetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.Cast;

class ActionHelper
{
	static final String ASSIGN_LEFT_VARIABLE = "@assignVariable";
	static final String ASSIGN_LEFT_EXPRESSION = "assignExpression";
	static final String ASSIGN_RIGHT_VALUE = "@assignValue";
	static final String ASSIGN_RIGHT_EXPRESSION = "expression";
	static final String ASSIGN_CONTROL = "@assignControl";
	static final String ASSIGN_CONTROL_PROPERTY = "@assignProperty";

	static final String METHOD_CALL_TARGET = "targetExpression";
	static final String METHOD_CALL_METHOD_NAME = "@method";

	static final String STATEMENT_NAME = "@statementName";

	/**
	 * Returns whether the action definition has values for all the specified properties.
	 * Useful to decide whether the action is of a particular type.
	 */
	static boolean hasProperties(ActionDefinition definition, String... properties)
	{
		for (String property : properties)
		{
			if (definition.getProperty(property) == null)
				return false;
		}

		return true;
	}

	static ActionParameter getAssignmentLeft(ActionDefinition action)
	{
		return newAssignmentParameter(action, ASSIGN_LEFT_VARIABLE, ASSIGN_LEFT_EXPRESSION);
	}

	static ActionParameter getAssignmentRight(ActionDefinition action)
	{
		return newAssignmentParameter(action, ASSIGN_RIGHT_VALUE, ASSIGN_RIGHT_EXPRESSION);
	}

	static ActionParameter getParameter(String expressionKey, ActionDefinition action)
	{
		INodeObject assignExpression = Cast.as(INodeObject.class, action.getProperty(expressionKey));
		return WorkWithMetadataLoader.newActionParameter(Strings.EMPTY, Strings.EMPTY, assignExpression);
	}

	static ActionParameter newAssignmentParameter(ActionDefinition action, String valueKey, String expressionKey)
	{
		// Read expression (new format) or value (old format).
		String assignValue = action.optStringProperty(valueKey);
		INodeObject assignExpression = Cast.as(INodeObject.class, action.getProperty(expressionKey));

		return WorkWithMetadataLoader.newActionParameter(Strings.EMPTY, assignValue, assignExpression);
	}
}
