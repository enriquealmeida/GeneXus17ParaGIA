package com.genexus.android.core.externalobjects;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.controls.ProgressDialogFactory;

public class ProgressIndicatorAPIOffline
{
	private static ProgressIndicatorAPI sProgressIndicatorInstance = new ProgressIndicatorAPI(null);

	private static void initialize()
	{
		// TODO: this condition is necessary???
		if (sProgressIndicatorInstance.getAction()==null
				|| (ActivityHelper.getCurrentActivity()!=null && !sProgressIndicatorInstance.getActivity().equals(ActivityHelper.getCurrentActivity()))
		)
		{
			UIContext uiContext = new UIContext(ActivityHelper.getCurrentActivity(), null, null, Connectivity.Offline);
			ActionDefinition def = new ActionDefinition(null);
			ApiAction action = new ApiAction(uiContext, def, null, true);
			sProgressIndicatorInstance = new ProgressIndicatorAPI(action);
		}
	}

	// static methods to call inside of offline procedures

	// missing parameters

	public static void show()
	{
		List<String> values = new ArrayList<>();
		showIndicator(values);
	}

	public static void showWithTitle(String title)
	{
		List<String> values = new ArrayList<>();
		values.add(title);
		showIndicator(values);
	}

	public static void showWithTitleAndDescription(String title, String description)
	{
		List<String> values = new ArrayList<>();
		values.add(title);
		values.add(description);
		showIndicator(values);
	}

	private static void showIndicator(List<String> values)
	{
		initialize();
		final ProgressDialogFactory.ProgressViewProvider provider = sProgressIndicatorInstance.getCurrentProvider();
		sProgressIndicatorInstance.showIndicator(provider, values);
	}

	public static void hide()
	{
		hideIndicator();
	}

	private static void hideIndicator()
	{
		initialize();

		final ProgressDialogFactory.ProgressViewProvider provider = sProgressIndicatorInstance.getCurrentProvider();
		sProgressIndicatorInstance.hideIndicator(provider);

	}

	//Properties
	public static void setType(Byte type)
	{
		initialize();
		sProgressIndicatorInstance.getCurrentIndicator().Type = type;
		final ProgressDialogFactory.ProgressViewProvider provider = sProgressIndicatorInstance.getCurrentProvider();
		sProgressIndicatorInstance.updateIndicator(provider);
	}

	public static Byte getType()
	{
		initialize();
		Byte result = Byte.valueOf(Integer.toString(sProgressIndicatorInstance.getCurrentIndicator().Type) );
		return result;

	}

	public static void setTitle(String title)
	{
		initialize();
		sProgressIndicatorInstance.getCurrentIndicator().Title = title;
		final ProgressDialogFactory.ProgressViewProvider provider = sProgressIndicatorInstance.getCurrentProvider();
		sProgressIndicatorInstance.updateIndicator(provider);
	}

	public static String getTitle()
	{
		initialize();
		return sProgressIndicatorInstance.getCurrentIndicator().Title;

	}

	public static void setDescription(String description)
	{
		initialize();
		sProgressIndicatorInstance.getCurrentIndicator().Description = description;
		final ProgressDialogFactory.ProgressViewProvider provider = sProgressIndicatorInstance.getCurrentProvider();
		sProgressIndicatorInstance.updateIndicator(provider);
	}

	public static String getDescription()
	{
		initialize();
		return sProgressIndicatorInstance.getCurrentIndicator().Description;
	}

	public static void setMaxValue(Integer maxValue)
	{
		initialize();
		sProgressIndicatorInstance.getCurrentIndicator().MaxValue = maxValue;
		final ProgressDialogFactory.ProgressViewProvider provider = sProgressIndicatorInstance.getCurrentProvider();
		sProgressIndicatorInstance.updateIndicator(provider);
	}

	public static Integer getMaxValue()
	{
		initialize();
		return sProgressIndicatorInstance.getCurrentIndicator().MaxValue;
	}

	public static void setValue(Integer value)
	{
		initialize();
		sProgressIndicatorInstance.getCurrentIndicator().Value = value;
		final ProgressDialogFactory.ProgressViewProvider provider = sProgressIndicatorInstance.getCurrentProvider();
		sProgressIndicatorInstance.updateIndicator(provider);
	}

	public static Integer getValue()
	{
		initialize();
		return sProgressIndicatorInstance.getCurrentIndicator().Value;

	}
}
