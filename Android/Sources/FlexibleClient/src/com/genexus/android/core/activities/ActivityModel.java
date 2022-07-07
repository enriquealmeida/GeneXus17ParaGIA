package com.genexus.android.core.activities;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.controllers.DataViewModel;

public class ActivityModel
{
	private String mMainDataViewName;
	private DataViewModel mMain;

	private boolean mInSelectionMode;
	private UIContext mUIContext;

	ActivityModel()
	{
    	mMainDataViewName = "<None>";
	}

	/**
	 * Initializes a new ActivityModel using the activity intent bundle for parameters.
	 * @param bundle Data of the intent received by the activity.
	 */
	boolean initializeFrom(Activity activity, Bundle bundle)
	{
        if (bundle != null)
        {
        	mUIContext = UIContext.base(activity, Connectivity.fromBundle(bundle));

        	mMainDataViewName = bundle.getString(IntentParameters.DATA_VIEW);
       		short mode = bundle.getShort(IntentParameters.MODE, DisplayModes.VIEW);
	       	List<String> mainParameters = IntentHelper.getList(bundle, IntentParameters.PARAMETERS);
	       	Map<String, String> mainNamedParameters = IntentHelper.getMap(bundle, IntentParameters.BC_FIELD_PARAMETERS);
	       	mInSelectionMode = bundle.getBoolean(IntentParameters.IS_SELECTING);

	        IViewDefinition mainDefinition = Services.Application.getDefinition().getView(mMainDataViewName);
	       	if (mainDefinition != null) {
	       		Connectivity connectivity = null;
	       		if (mainDefinition instanceof IDataViewDefinition)
			        connectivity = Connectivity.getConnectivitySupport(bundle, (IDataViewDefinition) mainDefinition);
		        else if (mainDefinition instanceof DashboardMetadata)
	       			connectivity = Connectivity.fromBundle(bundle);

		        mUIContext = UIContext.base(activity, connectivity);
		        ComponentParameters params = new ComponentParameters(mainDefinition, mode, mainParameters, mainNamedParameters);
		        mMain = createDataView(mUIContext, params);
		        return true;
	       	}
        }

        return false;
	}

	public UIContext getUIContext()
	{
		return mUIContext;
	}

	public DataViewModel getMain()
	{
		return mMain;
	}

	DataViewModel createDataView(UIContext context, ComponentParameters params)
	{
		if (mMain != null && mMain.getParams().Object == params.Object)
		{
			if (!mMain.getParams().Parameters.equals(params.Parameters))
			{
				mMain = null;
				mMain = createDataView(context, params);
			}

			return mMain;
		}

		return new DataViewModel(params, context.getConnectivitySupport());
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public String getName()
	{
		return mMainDataViewName;
	}

	public boolean getInSelectionMode() { return mInSelectionMode; }
}
