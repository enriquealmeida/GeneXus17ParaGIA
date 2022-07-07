package com.genexus.android.core.activities;

import android.app.Activity;
import android.content.Intent;

import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.IDataView;

public class DataViewHelper
{
	private final IDataViewDefinition mView;

	public static DataViewHelper fromIntent(Intent intent)
	{
		String dataViewId = intent.getStringExtra(IntentParameters.DATA_VIEW);

		if (!Services.Strings.hasValue(dataViewId))
			throw new IllegalArgumentException("Detail Intent was not properly set up -- DataView name is missing.");

		IDataViewDefinition dataView = Services.Application.getDefinition().getDataView(dataViewId);
		if (dataView == null)
        	throw new IllegalArgumentException(String.format("Data View with name '%s' does not exist.", dataViewId));

        return new DataViewHelper(dataView);
	}

	public DataViewHelper(IDataViewDefinition view)
	{
		mView = view;
	}

	public IDataViewDefinition getDefinition()
	{
		return mView;
	}
	
	public static void setTitle(Activity activity, IDataView fromDataView, CharSequence title)
	{
		if (activity == null)
			return;

		if (activity instanceof GenexusActivity)
		{
			((GenexusActivity)activity).setTitle(title, fromDataView);
		}
		else
			activity.setTitle(title);
	}
}
