package com.genexus.android.core.actions;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.genexus.android.core.base.application.IProcedure;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ProcedureDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.controls.IGxGridControl;
import com.genexus.android.core.utils.Cast;

class MultipleSelectionAction extends ActionWithOutput
{
	private final ActionDefinition.MultipleSelectionInfo mInfo;
	private final IGxGridControl mGrid;
	private final ProcedureDefinition mTargetProcedure;

	private OutputResult mOutput;

	MultipleSelectionAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
		mInfo = definition.getMultipleSelectionInfo();
		mGrid = Cast.as(IGxGridControl.class, findControl(mInfo.getGrid()));

		String callTarget = MetadataLoader.getObjectName(definition.optStringProperty("@call"));
		mTargetProcedure = Services.Application.getDefinition().getProcedure(callTarget);

		// TODO: Enable this when "selection on demand" works.
		// Action needs a continuation if grid selection mode = "on action" and it's a "for each SELECTED line" action.
		// Otherwise Do() already has all the information needed and can proceed.
		// mSelectionOnDemand = (mInfo.useSelection() && mGrid != null && mGrid.getDefinition().getShowSelector() == GridDefinition.SELECTION_ON_ACTION);
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return "foreachlineproc".equals(definition.optStringProperty(ActionHelper.STATEMENT_NAME));
	}

	@Override
	public boolean Do()
	{
		if (mTargetProcedure == null)
			return false;

		if (mGrid != null)
		{
			// This action is always the confirmation step, even if the selection mode is "on action".
			// Therefore, just perform the server call.
			IApplicationServer server = getApplicationServer(mTargetProcedure);
			IProcedure procedure = server.getProcedure(mTargetProcedure.getName());

			List<PropertiesObject> values = getMultiCallParameters(server, mGrid.getData());
			mOutput = procedure.executeMultiple(values);

			if (mOutput.isOk())
			{
				// Either clear selection (if selection mode = "always") or end selection (if "on demand").
				Services.Device.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						mGrid.setSelectionMode(false, null);
					}
				});
			}

			return mOutput.isOk();
		}
		else
		{
			Services.Log.error(String.format("Grid '%s' not found on UI context.", mInfo.getGrid()));
			return false;
		}
	}

	private List<PropertiesObject> getMultiCallParameters(IApplicationServer server, EntityList entities)
	{
		// Get the list of entities that make up the action (all or selected), and evaluate field expressions for each one.
		List<PropertiesObject> allValues = new ArrayList<>();

		for (Entity entity : entities)
		{
			if (!mInfo.useSelection() || entity.isSelected())
			{
				// Set "CurrentItem" so that selection can be evaluated over SDTs.
				entities.setCurrentItem(entity);

				ResultDetail<PropertiesObject> prepareResult = CallGxObjectAction.prepareCallParameters(server, this, getDefinition(), mTargetProcedure, entity);
				PropertiesObject itemValues = prepareResult.getData();
				allValues.add(itemValues);
			}
		}

		return allValues;
	}

	IGxGridControl getGrid()
	{
		return mGrid;
	}

	@Override
	public Activity getActivity()
	{
		return super.getActivity();
	}

	@Override
	public OutputResult getOutput()
	{
		return mOutput;
	}
}
