package com.genexus.android.core.activities;

import java.util.LinkedHashMap;
import java.util.Map;

import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.controllers.DataViewController;
import com.genexus.android.core.fragments.IDataView;

class ActivityControllerState
{
	private final LinkedHashMap<ComponentId, DataViewController> mControllers;

	public ActivityControllerState()
	{
		mControllers = new LinkedHashMap<>();
	}

	public void save(Map<IDataView, DataViewController> controllers)
	{
		for (DataViewController dataView : controllers.values())
		{
			dataView.detachController();
			mControllers.put(dataView.getId(), dataView);
		}
	}

	public DataViewController restoreController(ComponentId id, ComponentParameters params, ActivityController parent, IDataView dataView)
	{
		DataViewController restored = mControllers.get(id);
		if (restored != null)
		{
			mControllers.remove(id);

			// We might be asked to restore a controller when it's not possible (for example if a component
			// has different objects in different layouts). In that case, DO NOT restore it.
			if (restored.getDefinition() != params.Object)
				return null;

			restored.attachController(parent, dataView);
		}

		return restored;
	}
}
