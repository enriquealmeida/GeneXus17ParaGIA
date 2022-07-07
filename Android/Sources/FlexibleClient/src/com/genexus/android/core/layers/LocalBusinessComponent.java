package com.genexus.android.core.layers;

import com.artech.base.services.IGxBusinessComponent;
import com.genexus.android.core.base.application.IBusinessComponent;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.GXRuntimeException;
import com.genexus.internet.MsgList;

import java.util.List;

class LocalBusinessComponent implements IBusinessComponent
{
	private final String mName;
	private final IGxBusinessComponent mImplementation;
	private boolean mSuccess;
	private EntityList mMessages;

	public LocalBusinessComponent(String name)
	{
		mName = name;
		mImplementation = GxObjectFactory.getBusinessComponent(name);
		mMessages = LocalUtils.retrieveBCMessages(mName);
		mSuccess = mMessages != null && mMessages.size() == 0;
	}

	@Override
	public void initialize(Entity entity)
	{
		if (mImplementation != null)
			mImplementation.initentity(entity);
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
		if (mImplementation != null)
		{
			entity.setKey(key);
			
			LocalUtils.beginTransaction();
		
			try
			{
				mImplementation.loadbcfromkey(entity);
			}
			finally
			{
				LocalUtils.endTransaction();
			}

			mSuccess = mImplementation.success();
			if (mSuccess)
				return OutputResult.ok();
			else
				return parseResult(false, mImplementation.getmessages());
		}
		else
			return LocalUtils.outputNoImplementation(mName);
	}

	@Override
	public OutputResult save(Entity entity)
	{
		if (mImplementation != null)
		{
			LocalUtils.beginTransaction();
		
			try
			{
				mImplementation.savebcfromentity(entity);
				mSuccess = mImplementation.success();
				if (mSuccess)
				{
					LocalUtils.commit();
					entity.resetDirties();
				}
			}
			catch (GXRuntimeException e)
			{
				// Catch all GXRuntimeException
				// Pending definitive solution getting the correct message from the BC itself in the standards.
				MsgList mList = new MsgList();
				if (e.getTargetException()!=null)
				{
					if (e.getTargetException().getMessage().contains("error code 19: constraint failed"))
						mList.addItem("SQL constraint failed.", 1, "");
					else	
						mList.addItem(e.getTargetException().getMessage(), 1, "");
				}		
				else				
					mList.addItem(e.getMessage(), 1, "");
				return LocalUtils.translateOutput(false, mList);
			}
			finally
			{
				LocalUtils.endTransaction();
			}

			// Send BC To server in offline
			postSendBCToServer();
			return parseResult(mSuccess, mImplementation.getmessages());
		}
		else
			return LocalUtils.outputNoImplementation(mName);
	}

	@Override
	public OutputResult delete()
	{
		if (mImplementation != null)
		{
			LocalUtils.beginTransaction();
			try
			{
				mImplementation.delete();
				mSuccess = mImplementation.success();
				if (mSuccess)
					LocalUtils.commit();
			}
			finally
			{
				LocalUtils.endTransaction();
			}

			// Send BC To server in offline
			postSendBCToServer();
			return parseResult(mSuccess, mImplementation.getmessages());
		}
		else
			return LocalUtils.outputNoImplementation(mName);
	}

	@Override
	public OutputResult insert(Entity entity)
	{
		// TODO: implement real insert
		return save(entity);
	}

	@Override
	public OutputResult insertOrUpdate(Entity entity)
	{
		// TODO: implement real insertOrUpdate
		return save(entity);
	}

	@Override
	public OutputResult update(Entity entity)
	{
		// TODO: implement real Update
		return save(entity);
	}

	public static void postSendBCToServer()
	{
		if (Services.Application.get().isOfflineApplication()
				&& Services.Application.get().getSynchronizerSendAutomatic() 
				&& Services.HttpService.isOnline()
			 	)
		{
			// if the is some pending events send them.
			Services.Log.debug("sendPendingsToServerInBackground (Sync.Send) from Post BC or post procedure call ");
			Services.Sync.sendPendingsToServerInBackground();
			//SynchronizationHelper.sendPendingsToServerDummy();
		}
	}

	private OutputResult parseResult(boolean success, MsgList msgList) {
		OutputResult result = LocalUtils.translateOutput(success, msgList);
		LocalUtils.saveBCMessages(result.getMessages(), mName);
		return result;
	}

	@Override
	public String getName()
	{
		return mName;
	}
}
