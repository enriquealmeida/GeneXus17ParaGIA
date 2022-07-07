package com.genexus.android.core.actions;

import java.util.ArrayList;

import android.content.Intent;

import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.ExpressionFormatHelper;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.ui.navigation.Navigation;
import com.genexus.android.core.ui.navigation.NavigationHandled;
import com.genexus.android.core.ui.navigation.UIObjectCall;

class CallWebPanelAction extends Action
{
	private boolean mWaitForResult;
	
	public CallWebPanelAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
	}

	@Override
	public boolean Do()
	{
		mWaitForResult = true;
		String webUrl = getUrl();

		Intent intent = IntentFactory.getIntentForWebApplication(getContext(), webUrl);
		UIObjectCall call = new UIObjectCall(getContext(), new ComponentParameters(webUrl));

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

	@Override
	public boolean catchOnActivityResult()
	{
		return mWaitForResult;
	}
	
	private String getUrl()
	{
		ArrayList<String> urlParameters = new ArrayList<>();
		for (ActionParameter parameter : getDefinition().getParameters())
		{
			Value value = getParameterValue(parameter);
			if (value != null) {
				String url = ExpressionFormatHelper.toUrlParameterString(value);
				urlParameters.add(Services.HttpService.uriEncode(url));
			}
		}

		String link = Services.Application.get().link(getDefinition().getGxObject());
		if (urlParameters.size() > 0)
			link += Strings.QUESTION + Services.Strings.join(urlParameters, Strings.COMMA);
		
		return link;
	}
}
