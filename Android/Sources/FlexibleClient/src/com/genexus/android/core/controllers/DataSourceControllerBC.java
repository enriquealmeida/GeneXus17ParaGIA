package com.genexus.android.core.controllers;

import java.util.ArrayList;

import com.genexus.android.R;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.application.IBusinessComponent;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.VariableDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ListUtils;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.utils.TaskRunner;

import static com.genexus.android.core.base.metadata.enums.DisplayModes.INSERT;
import static com.genexus.android.core.base.model.Entity.JSONFORMAT_INTERNAL;

public class DataSourceControllerBC implements IDataSourceControllerInternal
{
	public static final String INSERT_BC_PARAM_NAME = "InitialValues";

	private final DataViewController mParent;
	private final DataSourceModel mModel;
	private final ComponentParameters mParams;
	private final int mId;

	private IApplicationServer mServer;
	private StructureDefinition mStructure;
	private IBusinessComponent mBusinessComponent;
	private Entity mBCEntity;

	private IDataSourceBoundView mView;
	private TaskRunner.BaseTask<?> mCurrentLoadTask;
	private ViewData mCurrentResult;
	
	public DataSourceControllerBC(DataViewController parent, DataSourceModel model, StructureDefinition bc)
	{
		mParent = parent;
		mModel = model;
		mId = DataSourceController.createDataSourceId();

		mParams = parent.getComponentParams();
		initBusinessComponent(bc);
	}
	
	private void initBusinessComponent(StructureDefinition bc)
	{
		mStructure = bc;
		mServer = Services.Application.getApplicationServer(getParent().getModel().getConnectivity());
		mBusinessComponent = mServer.getBusinessComponent(bc.getName());
		mBCEntity = EntityFactory.newEntity(bc);
		addVariablesToBC(mBCEntity);
	}

	private void startLoading()
	{
		TaskRunner.execute(new TaskRunner.BaseTask<ViewData>() {
			@Override
			public void onPreExecute()
			{
				mCurrentLoadTask = this;
			}

			@Override
			public ViewData doInBackground()
			{
				return loadBusinessComponent();
			}

			@Override
			public void onPostExecute(ViewData result)
			{
				if (mView != null && !isCancelled())
					mView.update(result);

				mCurrentResult = result;
				mCurrentLoadTask = null;
			}
		});
	}
	
	private ViewData loadBusinessComponent()
	{
		if (mParams.Mode == INSERT)
		{
			mBusinessComponent.initialize(mBCEntity);
			loadNamedParameters(mBCEntity, mParams);
			return ViewData.customData(mBCEntity, DataRequest.RESULT_SOURCE_SERVER);
		}
		else
		{
			Entity entity = EntityFactory.newEntity(mStructure);
			OutputResult loadResult = mBusinessComponent.load(entity, mParams.Parameters);

			if (loadResult.isOk())
			{
				mBCEntity = entity;
				addVariablesToBC(mBCEntity);
				return ViewData.customData(mBCEntity, DataRequest.RESULT_SOURCE_SERVER);
			}
			else
			{
				// If we cannot 'GET' the entity, then we won't be able to update/delete it either.
				String message = String.format(Services.Strings.getResource(R.string.GXM_InvalidMode), DisplayModes.getString(mParams.Mode), loadResult.getErrorText());
				return new ViewData(null, null, DataRequest.RESULT_SOURCE_SERVER, new EntityList(), false, DataRequest.ERROR_SERVER, message, false);
			}
		}
	}

	private static void loadNamedParameters(Entity entity, ComponentParameters params)
	{
		if (params.Mode == INSERT &&
			params.NamedParameters.size() == 1 &&
			params.NamedParameters.firstKey().equals(INSERT_BC_PARAM_NAME)) {
			String bcJson = params.NamedParameters.firstEntry().getValue();
			entity.deserialize(Services.Serializer.createNode(bcJson), JSONFORMAT_INTERNAL);
		} else {
			for (DataItem def : entity.getLevel().Items) {
				String result = params.NamedParameters.get(def.getName());
				if (Strings.hasValue(result))
					entity.setProperty(def.getName(), result);
			}
		}
	}

	private void addVariablesToBC(Entity bcEntity)
	{
		ArrayList<VariableDefinition> variables = new ArrayList<>();
		
		// Variables from the Form.
		if (mParent.getComponentParams() != null && mParent.getComponentParams().Object != null)
			variables.addAll(mParent.getComponentParams().Object.getVariables());
		
		// Variables from the Data Provider. Although the DP is not called as of now,
		// they still must have a definition to work properly in the client.
		if (mModel != null && mModel.getDefinition() != null)
			variables.addAll(ListUtils.itemsOfType(mModel.getDefinition().getDataItems(), VariableDefinition.class));
		
		if (variables.size() != 0)
			bcEntity.setExtraMembers(variables);

		// Get the &Mode variable ready for usage in events.
		DisplayModes.setVariable(bcEntity, mParams.Mode);
	}
	
	public IApplicationServer getServer()
	{
		return mServer;
	}
	
	public IBusinessComponent getBusinessComponent()
	{
		return mBusinessComponent;
	}

	public Entity getBCEntity()
	{
		return mBCEntity;
	}
	
	public short getMode()
	{
		return mParams.Mode;
	}
	
	@Override
	public int getId()
	{
		return mId;
	}

	@Override
	public String getName()
	{
		return mStructure.getName();
	}

	@Override
	public IDataViewController getParent()
	{
		return mParent;
	}

	@Override
	public IDataSourceDefinition getDefinition()
	{
		// This is a shim. The DataSourceModel is not actually used.
		return mModel.getDefinition();
	}

	@Override
	public DataSourceModel getModel()
	{
		// This is a shim. The DataSourceModel is not actually used.
		return mModel;
	}

	@Override
	public void onRequestMoreData() { }

	@Override
	public void onResume()
	{
		if (mCurrentLoadTask != null || mCurrentResult != null)
			return; // Already loading (or already loaded).
		
		startLoading();
	}

	@Override
	public void onRefresh(RefreshParameters params)
	{
		if (mCurrentLoadTask != null)
			return; // Already loading, refresh is ignored.

		if (mParams.Mode == INSERT)
			return; // Doesn't make sense to refresh when inserting.
		
		startLoading(); 
	}

	@Override
	public void onPause()
	{
		if (mCurrentLoadTask != null)
			mCurrentLoadTask.cancel();
	}

	@Override
	public void attach(IDataSourceBoundView view)
	{
		mView = view;
	}

	@Override
	public void detach()
	{
		mView = null;
	}

	@Override
	public IDataSourceBoundView getBoundView()
	{
		return mView;
	}
}
