package com.genexus.android.core.actions;

import java.util.Collections;
import java.util.List;

import android.content.Intent;

import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IPatternMetadata;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.navigation.Navigation;
import com.genexus.android.core.ui.navigation.NavigationHandled;
import com.genexus.android.core.ui.navigation.UIObjectCall;
import com.genexus.android.core.utils.Cast;

public class CallDashboardAction extends Action
{
	private boolean mWaitForResult;

	public CallDashboardAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
		mWaitForResult = true;
	}

	@Override
	public boolean Do()
	{
		mWaitForResult = true;

		Intent intent = getIntentForAction();
		ComponentParameters params = new ComponentParameters(getObject(), getMode(), getObjectParameters());
		UIObjectCall call = new UIObjectCall(getContext(), params);

		NavigationHandled handled = Navigation.handle(call, intent);
		if (handled != NavigationHandled.NOT_HANDLED)
		{
			mWaitForResult = (handled == NavigationHandled.HANDLED_WAIT_FOR_RESULT);
			return true;
		}

		getActivity().startActivityForResult(intent, RequestCodes.ACTION);
		return true;
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}

	private Intent getIntentForAction()
	{
		return IntentFactory.getDashboard(getContext(), getObject(), getObjectParameters(), getMode());
	}

	@Override
	public boolean catchOnActivityResult() { return mWaitForResult; }

	private IViewDefinition getObject()
	{
		IPatternMetadata obj = Services.Application.getDefinition().getPattern(getDefinition().getGxObject());
		return Cast.as(DashboardMetadata.class, obj);
	}

	private List<String> getObjectParameters()
	{
		// Dashboards do not take arguments, for now.
		return Collections.emptyList();
	}

	private short getMode()
	{
		return DisplayModes.VIEW;
	}
}
