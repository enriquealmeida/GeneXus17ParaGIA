package com.genexus.android.core.activities;

import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.fragments.IDataView;

public interface IGxActivity extends IGxBaseActivity
{
	ActivityModel getModel();
	ActivityController getController();
	Iterable<IDataView> getDataViews();
	Iterable<IDataView> getActiveDataViews(boolean includeNoLayoutDataViews);

	IViewDefinition getMainDefinition();
	void refreshData(RefreshParameters params);

	void setReturnResult();
}
