package com.genexus.android.core.actions;

import androidx.annotation.NonNull;

import com.genexus.android.layout.ControlHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.fragments.LayoutFragment;
import com.genexus.android.core.utils.Cast;

class SetControlPropertyAction extends Action
{
	private final String mControl;
	private final String mProperty;
	private final ActionParameter mValue;

	public SetControlPropertyAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);

		mControl = definition.optStringProperty(ActionHelper.ASSIGN_CONTROL);
		mProperty = definition.optStringProperty(ActionHelper.ASSIGN_CONTROL_PROPERTY);
		mValue = ActionHelper.getAssignmentRight(definition);
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return ActionHelper.hasProperties(definition, ActionHelper.ASSIGN_CONTROL, ActionHelper.ASSIGN_CONTROL_PROPERTY, ActionHelper.ASSIGN_RIGHT_VALUE);
	}

	@Override
	public @NonNull ThreadPreference getThreadPreference()
	{
		if (mValue.getExpression().needsBackgroundThread())
			return ThreadPreference.BACKGROUND_THREAD;
		else
			return ThreadPreference.MAIN_THREAD;
	}

	@Override
	public boolean Do()
	{
		if (Services.Strings.hasValue(mControl) &&
			Services.Strings.hasValue(mProperty) &&
			mValue != null)
		{
			// Find the control to update properties.
			IGxControl control = findControl(mControl);
			if (control != null)
			{
				// mValue is an expression (e.g. True, "Textblock.SubClass", &Variable1...)
				Value propertyValue = getParameterValue(mValue);
				if (propertyValue != Value.UNKNOWN)
				{
					// Store for reapplying later (if activity is recreated).
					LayoutFragment fragment = Cast.as(LayoutFragment.class, getContext().getDataView());
					if (fragment != null)
						fragment.getRuntimeProperties().putProperty(mControl, mProperty, propertyValue);

					ControlHelper.setProperty(ExecutionContext.inAction(this), control, mProperty, propertyValue);
				}
			}
		}

		// Never fail. Ignore wrong control, property, or value.
		return true;
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}

	@Override
	public boolean catchOnActivityResult()
	{
		return false;
	}
}
