package com.genexus.android.core.actions;

import android.app.Activity;

import com.genexus.android.core.base.application.IBusinessComponent;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ParametersStringUtil;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.layers.LocalUtils;

import java.util.List;
import java.util.Map;

class CallBCAction extends ActionWithOutput
{
	private final StructureDefinition mStructureDefinition;
	private final String mBCVariableName;

	private OutputResult mOutput;

	CallBCAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
		mStructureDefinition = Services.Application.getDefinition().getBusinessComponent(definition.getGxObject());
		mBCVariableName = definition.optStringProperty("@bcVariable");
	}

	private short getMode()
	{
		// Only batch modes (exclude view).
		if (getDefinition().optStringProperty("@bcMode").equalsIgnoreCase("Delete"))
			return DisplayModes.DELETE;
		else if (getDefinition().optStringProperty("@bcMode").equalsIgnoreCase("Update"))
			return DisplayModes.EDIT;
		else // Default Mode
			return DisplayModes.INSERT;
	}

	@Override
	public boolean catchOnActivityResult()
	{
		return false; // It's a batch call.
	}

	@Override
	public boolean Do()
	{
		mOutput = callBcBatch(mStructureDefinition, getBCParameters());
		return mOutput.isOk();
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

	private Map<String, String> getBCParameters()
	{
		return ActionParametersHelper.getParametersForBC(this);
	}

	private OutputResult callBcBatch(StructureDefinition structureDef, Map<String, String> parameters)
	{
		if (structureDef == null)
			return OutputResult.error(String.format("Structure definition for '%s' not found.", getDefinition().getGxObject()));

		IApplicationServer server = getApplicationServer(structureDef);
		IBusinessComponent businessComponent = server.getBusinessComponent(structureDef.getName());

		Entity entity = null;
		short mode = getMode();
		OutputResult result = null;

		if (mode == DisplayModes.INSERT)
		{
			entity = EntityFactory.newEntity(structureDef);
			entity.initialize();
			result = saveBusinessComponent(server, businessComponent, entity, parameters);
		}
		else if (mode == DisplayModes.EDIT || mode == DisplayModes.DELETE)
		{
			// Read entity to update or delete.
			entity = EntityFactory.newEntity(structureDef);
			List<String> key = ParametersStringUtil.getKeyValuesFromFieldValues(parameters, structureDef);

			// TODO: What, if any, message should be shown when the entity to update/delete is not in the server?
			OutputResult loadResult = businessComponent.load(entity, key);

			if (loadResult.isOk())
			{
				if (mode == DisplayModes.EDIT)
					result = saveBusinessComponent(server, businessComponent, entity, parameters);
				else
					result = businessComponent.delete();
			}
			else
				result = loadResult; // Return the load error as the error.
		}

		if (result != null && result.isOk())
		{
			// Assign variable with BC and return.
			setOutputValue(mBCVariableName, Value.newBC(entity));
		}

		LocalUtils.saveBCMessages(result.getMessages(), mBCVariableName);
		return result;
	}

	private static OutputResult saveBusinessComponent(IApplicationServer server, IBusinessComponent businessComponent, Entity entity, Map<String, String> parameters)
	{
		ResultDetail<Void> setValuesResult = setBCFieldValues(server, businessComponent.getName(), entity, parameters);
		if (!setValuesResult.getResult())
			return OutputResult.error(setValuesResult.getMessage()); // Don't even try to save

		return businessComponent.save(entity);
	}

	private static ResultDetail<Void> setBCFieldValues(IApplicationServer mServer, String objectName, Entity entity, Map<String, String> parameters)
	{
		if (parameters == null)
			return ResultDetail.ok(); // Nothing to do

		for (DataItem def : entity.getLevel().Items)
		{
			String valueString = parameters.get(def.getName());
    		if (Services.Strings.hasValue(valueString))
    		{
    			entity.setProperty(def.getName(), valueString);

				ResultDetail<Void> subResult = BCMethodHandler.uploadBlobsFromContainer(mServer, objectName, def.getMaximumUploadSizeMode(), entity, def);
				if (!subResult.getResult())
					return subResult; // Abort on failure
    		}
		}

		return ResultDetail.ok(); // All ok
	}
}
