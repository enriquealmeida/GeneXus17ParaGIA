package com.genexus.android.core.actions;

import java.util.HashMap;
import java.util.List;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionDefinition.DependencyInfo;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.utils.Strings;

class DependentValuesAction extends Action
{
	private final String mService;
	private final List<String> mInput;
	private final List<String> mOutput;

	@SuppressWarnings("unchecked")
	public DependentValuesAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
		mService = definition.optStringProperty(DependencyInfo.SERVICE);
		mInput = (List<String>)definition.getProperty(DependencyInfo.SERVICE_INPUT);
		mOutput = (List<String>)definition.getProperty(DependencyInfo.SERVICE_OUTPUT);
	}

	public static boolean isAction(ActionDefinition action)
	{
		return Strings.hasValue(action.optStringProperty(DependencyInfo.SERVICE));
	}

	@Override
	public boolean Do()
	{
		HashMap<String, String> inputValues = new HashMap<>();
		for (String inputName : mInput)
		{
			Value inputValue = getParameterValue(new ActionParameter(inputName));
			inputValues.put(inputName, inputValue != null ? inputValue.coerceToString() : Strings.EMPTY);
		}

		// Call service (local or remote).
		List<String> output = getDefaultApplicationServer().getDependentValues(mService, inputValues);

		for (int i = 0; i < mOutput.size() && i < output.size(); i++)
			setOutputValue(mOutput.get(i), Value.newString(output.get(i)));

		return true;
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}
}
