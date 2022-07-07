package com.genexus.android.core.layers;

import androidx.annotation.NonNull;

import com.genexus.android.json.NodeObject;
import com.genexus.android.core.base.application.IBusinessComponent;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.IServiceDataResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Implementation of the IBusinessComponent interface that uses a remote server
 * (via a REST interface) to perform reads and updates.
 */
class RemoteBusinessComponent implements IBusinessComponent
{
	private final String mName;
	private final StructureDefinition mDefinition;
	private Entity mBoundEntity;
	private int mMode;
	private boolean mSuccess;
	private EntityList mMessages;

	public RemoteBusinessComponent(String name, StructureDefinition definition)
	{
		mName = name;
		mDefinition = definition;
		mMode = DisplayModes.INSERT;
		mMessages = LocalUtils.retrieveBCMessages(mName);
		mSuccess = mMessages != null && mMessages.size() == 0;
	}

	@Override
	public void initialize(Entity entity)
	{
		mBoundEntity = entity;
		mMode = DisplayModes.INSERT;

		if (mDefinition != null)
		{
			if (!loadDefaultsForBC(mBoundEntity))
				mBoundEntity.initialize();
		}
	}

	@Override
	public boolean success() {
		return mSuccess;
	}

	@Override
	public EntityList getMessages() {
		return mMessages == null ? new EntityList() : mMessages;
	}

	@Override
	public OutputResult load(Entity entity, List<String> key)
	{
		if (mDefinition == null)
			return RemoteUtils.outputNoDefinition(mName);

		mBoundEntity = entity;
		mMode = DisplayModes.EDIT; // Could be DELETE, will be determined later.

		entity.setKey(key);
		OutputResult result = loadEntity(mBoundEntity);
		mSuccess = result.isOk();
		return result;
	}

	@Override
	public OutputResult save(Entity entity)
	{
		if (mDefinition == null)
			return RemoteUtils.outputNoDefinition(mName);

		mBoundEntity = entity;
		ServiceResponse response;

		if (mMode == DisplayModes.INSERT)
			response = callService(entity, Entity.OPERATION_INSERT);
		else if (mMode == DisplayModes.EDIT)
			response = callService(entity, Entity.OPERATION_UPDATE);
		else
			throw new IllegalArgumentException(String.format("Unknown mode: %s", mMode));

		if (response != null && response.getResponseOk())
		{
			entity.resetDirties();
		}

		OutputResult result = parseResponse(response);
		mSuccess = result.isOk();
		return result;
	}

	@Override
	public OutputResult delete()
	{
		if (mDefinition == null)
			return RemoteUtils.outputNoDefinition(mName);

		mMode = DisplayModes.DELETE;
		ServiceResponse response = callService(mBoundEntity, Entity.OPERATION_DELETE);
		OutputResult result = parseResponse(response);
		mSuccess = result.isOk();
		return result;
	}

	@Override
	public OutputResult insert(Entity entity)
	{
		return callServiceWithOperationMode(entity, Entity.OPERATION_INSERT);
	}

	@NonNull
	private OutputResult callServiceWithOperationMode(Entity entity, int operationMode) {
		if (mDefinition == null)
			return RemoteUtils.outputNoDefinition(mName);

		mBoundEntity = entity;
		ServiceResponse response = callService(entity, operationMode);

		if (response != null && response.getResponseOk()) {
			entity.resetDirties();
		}

		OutputResult result = parseResponse(response);
		mSuccess = result.isOk();
		return result;
	}

	@Override
	public OutputResult insertOrUpdate(Entity entity)
	{
		return callServiceWithOperationMode(entity, Entity.OPERATION_INSERT_UPDATE);
	}

	@Override
	public OutputResult update(Entity entity)
	{
		return callServiceWithOperationMode(entity, Entity.OPERATION_UPDATE);
	}

	private boolean loadDefaultsForBC(Entity entity)
	{
		JSONObject data = Services.HttpService.getEntityDefaultsBC(mDefinition.getName());
		if (data != null)
		{
			entity.deserialize(new NodeObject(data));
			entity.setProperty("gx_md5_hash", Strings.EMPTY);
			return true;
		}

		return false;
	}

	private OutputResult loadEntity(Entity entity)
	{
		IServiceDataResult result = Services.HttpService.getEntityDataBC(mDefinition.getName(), entity.getKey());

		if (!result.isOk())
			return RemoteUtils.translateOutput(result);

		if (result.getData().length() != 0)
		{
			try
			{
				JSONObject jsonBC = result.getData().getJSONObject(0);
				entity.deserialize(new NodeObject(jsonBC));
				return OutputResult.ok();
			}
			catch (JSONException e)
			{
				// Should never happen, or isOk() would have returned false.
				return OutputResult.error(e.getMessage());
			}
		}
		else
			return OutputResult.error("Internal error: loadEntity returned nothing.");
	}

	private ServiceResponse callService(Entity entity, int operation)
	{
		if (entity == null)
			throw new IllegalArgumentException("No entity provided.");

		INodeObject data = entity.serialize(Entity.JSONFORMAT_INTERNAL);
		String uri = mDefinition.getName();

		// Call service.
		ServiceResponse response;
		List<String> entityKey = entity.getKey();

		switch (operation)
		{
			case Entity.OPERATION_INSERT :
				response = Services.HttpService.insertEntityData(uri, entityKey, data);
				break;

			case Entity.OPERATION_INSERT_UPDATE :
				response = Services.HttpService.insertOrUpdateEntityData(uri, entityKey, data);
				break;

			case Entity.OPERATION_UPDATE :
				response = Services.HttpService.updateEntityData(uri, entityKey, data);
				break;

			case Entity.OPERATION_DELETE :
				response = Services.HttpService.deleteEntityData(uri, entityKey);
				break;

			default :
				throw new IllegalArgumentException(String.format("Unknown operation: %s", operation));
		}

		if (response != null && response.getResponseOk()) {
			// Reload data (e.g. autonumbers) in case of insert or delete.
			if (operation == Entity.OPERATION_INSERT || operation == Entity.OPERATION_INSERT_UPDATE || operation == Entity.OPERATION_UPDATE )
				entity.deserialize(response.Data);
		}

		return response;
	}

	private OutputResult parseResponse(ServiceResponse response) {
		OutputResult result = RemoteUtils.translateOutput(response);
		LocalUtils.saveBCMessages(result.getMessages(), mName);
		return result;
	}

	@Override
	public String getName()
	{
		return mDefinition.getName();
	}
}
