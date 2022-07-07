package com.genexus.android.core.externalobjects;

import java.util.List;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.ProgressDialogFactory;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

public class ProgressIndicatorAPI extends ExternalApi
{
	public static final String OBJECT_NAME = "GeneXus.Common.UI.Progress";

	private static final String METHOD_SHOW = "Show";
	private static final String METHOD_HIDE = "Hide";
	private static final String METHOD_SHOW_SPECIAL = "ShowWithTitle"; // Also: ShowWithTitleAndDescription

	private static final String PREFIX_SET_PROPERTY = "set";
	private static final String SET_PROPERTY_TYPE = "setType";
	private static final String SET_PROPERTY_TITLE = "setTitle";
	private static final String SET_PROPERTY_DESCRIPTION = "setDescription";
	private static final String SET_PROPERTY_MAX_VALUE = "setMaxValue";
	private static final String SET_PROPERTY_VALUE = "setValue";

	private static final String SET_PROPERTY_CLASS = "setClass";

	private ProgressDialogFactory progressDialogFactory = null;

    public ProgressIndicatorAPI(ApiAction action) {
		super(action);
		progressDialogFactory = new ProgressDialogFactory();

	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters)
	{
		ProgressDialogFactory.ProgressViewProvider currentProgressProvider = progressDialogFactory.getViewProvider();
		//ProgressDialogFactory.ProgressIndicatorData data = getCurrentIndicator();
		List<String> parameterValues = toString(parameters);

		// Methods
		if (METHOD_SHOW.equalsIgnoreCase(method) || Strings.starsWithIgnoreCase(method, METHOD_SHOW_SPECIAL))
		{
			showIndicator(currentProgressProvider, parameterValues);

			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_HIDE.equalsIgnoreCase(method))
		{
			hideIndicator(currentProgressProvider);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (Strings.starsWithIgnoreCase(method, PREFIX_SET_PROPERTY) && parameterValues.size() == 1)
		{
			// Property setter.
			String value = parameterValues.get(0);

			if (SET_PROPERTY_TYPE.equalsIgnoreCase(method))
				currentProgressProvider.setProgressType( getActivity(), Services.Strings.tryParseInt(value, ProgressDialogFactory.TYPE_INDETERMINATE));
			else if (SET_PROPERTY_TITLE.equalsIgnoreCase(method))
				currentProgressProvider.setTitle( getActivity(), value);
			else if (SET_PROPERTY_DESCRIPTION.equalsIgnoreCase(method))
				currentProgressProvider.setMessage( getActivity(), value);
			else if (SET_PROPERTY_MAX_VALUE.equalsIgnoreCase(method))
				currentProgressProvider.setMaxValue(getActivity(), Services.Strings.tryParseInt(value, 0));
			else if (SET_PROPERTY_VALUE.equalsIgnoreCase(method))
				currentProgressProvider.setValue(getActivity(), Services.Strings.tryParseInt(value, 0));
			else if (SET_PROPERTY_CLASS.equalsIgnoreCase(method))
			{
				// TODO: SET_PROPERTY_CLASS
				// set from theme
				ThemeClassDefinition indicatorClass = Services.Themes.getThemeClass(value);
				if (indicatorClass != null) {
					progressDialogFactory.setThemeClass(getActivity(), indicatorClass);
				}

			}
			// Refresh changed settings into current dialog, if any.
			// currentProgressProvider.updateIndicator(data);

			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else
			return ExternalApiResult.failureUnknownMethod(this, method);
	}

	// methods for use in static methods from ProgressIndicator in procedures
	public void showIndicator(ProgressDialogFactory.ProgressViewProvider currentProgressProvider, List<String> parameterValues)
	{
		// parameters
		String title = null;
		if (parameterValues.size() >= 1)
			title = parameterValues.get(0);

		String description = null;
		if (parameterValues.size() >= 2)
			description = parameterValues.get(1);

		currentProgressProvider.showProgressIndicator(getActivity(), title, description);

		ProgressDialogFactory.ProgressIndicatorData progressData = getCurrentIndicator();
		if (progressData!=null && this.getAction().getParentComposite()!=null) {
			progressData.EventName = this.getAction().getParentComposite().getDefinition().getName();
		}
	}

	public void hideIndicator(ProgressDialogFactory.ProgressViewProvider currentProgressProvider)
	{
		currentProgressProvider.hideProgressIndicator(getActivity());
	}

	public void updateIndicator(ProgressDialogFactory.ProgressViewProvider currentProgressProvider)
	{
		currentProgressProvider.updateProgressIndicator(getActivity());
	}


	// Methods of instance of API data. use in API offline
	public ProgressDialogFactory.ProgressIndicatorData getCurrentIndicator()
	{
		return getCurrentIndicator(getActivity());
	}

	public ProgressDialogFactory.ProgressViewProvider  getCurrentProvider()
	{
		ProgressDialogFactory progressDialogFactory = new ProgressDialogFactory();
		ProgressDialogFactory.ProgressViewProvider currentProgressProvider = progressDialogFactory.getViewProvider();
		return currentProgressProvider;
	}

	public static ProgressDialogFactory.ProgressIndicatorData  getCurrentIndicator(Activity activity)
	{
		ProgressDialogFactory progressDialogFactory = new ProgressDialogFactory();
		ProgressDialogFactory.ProgressViewProvider currentProgressProvider = progressDialogFactory.getViewProvider();

		return currentProgressProvider.getCurrentIndicatorData(activity);
	}

}
