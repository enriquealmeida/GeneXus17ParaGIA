package com.genexus.android.core.actions;

import com.genexus.android.layout.ControlHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.controls.IGxControl;

class GetControlPropertyAction extends Action
{
	private final ActionParameter mAssignTarget;
	private final String mControl;
	private final String mProperty;

	public GetControlPropertyAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
		mAssignTarget = ActionHelper.getAssignmentLeft(definition);
		mControl = definition.optStringProperty(ActionHelper.ASSIGN_CONTROL);
		mProperty = definition.optStringProperty(ActionHelper.ASSIGN_CONTROL_PROPERTY);
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return ActionHelper.hasProperties(definition, ActionHelper.ASSIGN_LEFT_VARIABLE, ActionHelper.ASSIGN_CONTROL, ActionHelper.ASSIGN_CONTROL_PROPERTY);
	}

	@Override
	public boolean Do()
	{
		if (Strings.hasValue(mControl) && Strings.hasValue(mProperty) && mAssignTarget != null)
		{
			// Find the control to update properties.
			IGxControl control = findControl(mControl);
			if (control != null)
			{
				Value value = ControlHelper.getProperty(ExecutionContext.inAction(this), control, mProperty);
				setOutputValue(mAssignTarget, value);
			}
		}

		// Never fail. Ignore wrong control, property, or variable.
		return true;
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}
}
