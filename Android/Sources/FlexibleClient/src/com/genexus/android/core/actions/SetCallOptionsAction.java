package com.genexus.android.core.actions;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.navigation.CallOptionsHelper;

public class SetCallOptionsAction extends Action
{
	private final String mTargetObject;
	private final String mOption;
	private final String mValue;

	public SetCallOptionsAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);

		mTargetObject = MetadataLoader.getObjectName(definition.optStringProperty("@optionTarget"));
		mOption = definition.optStringProperty("@optionName");
		String value = definition.optStringProperty("@optionValue");
		mValue = Services.Language.getExpressionTranslation(value);
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return (definition.getProperty("@optionTarget") != null);
	}

	@Override
	public boolean Do()
	{
		Value optionValue = getParameterValue(new ActionParameter(mValue));
		if (optionValue != Value.UNKNOWN)
			CallOptionsHelper.setCallOption(mTargetObject, mOption, optionValue.coerceToString());
		return true; // Never fail, ignore wrong options.
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}
}
