package com.genexus.android.core.activities;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.model.Entity;

public interface IGxDashboardActivity extends IGxBaseActivity
{
	UIContext getUIContext();
	IViewDefinition getDashboardDefinition();
	Entity getData();
}
