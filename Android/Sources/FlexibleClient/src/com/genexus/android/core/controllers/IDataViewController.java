package com.genexus.android.core.controllers;

import com.genexus.android.core.actions.CompositeAction;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.ActivityController;
import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.fragments.IDataView;

public interface IDataViewController {
	ComponentId getId();

	ActivityController getParent();

	DataViewModel getModel();

	Entity getEntity();

	Iterable<IDataSourceController> getDataSources();

	IDataSourceController getDataSource(int id);

	IDataViewDefinition getDefinition();

	ComponentParameters getComponentParams();

	void onFragmentStart(IDataView dataView);

	void attachDataController(IDataSourceBoundView view);

	void runAction(UIContext context, ActionDefinition action, Entity data);

	void runBlockingStart(CompositeAction action, ActionDefinition actionDefinition);

	boolean handleSelection(Entity entity);

	boolean isStarted();
}
