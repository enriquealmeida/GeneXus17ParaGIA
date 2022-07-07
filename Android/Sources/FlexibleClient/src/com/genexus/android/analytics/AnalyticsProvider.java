package com.genexus.android.analytics;

import android.app.Activity;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;

/**
 * Interface for Analytics providers.
 */
public interface AnalyticsProvider
{
	void onActivityStart(Activity activity);
	void onActivityStop(Activity activity);

	void onComponentStart(Activity activity, IViewDefinition definition);
	void onAction(UIContext context, ActionDefinition action);

	void onException(Exception e);
}
