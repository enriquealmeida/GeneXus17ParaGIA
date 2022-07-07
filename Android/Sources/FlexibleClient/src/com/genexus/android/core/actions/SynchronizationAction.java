package com.genexus.android.core.actions;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.loader.SyncManager;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.synchronization.SynchronizationSendHelper;

public class SynchronizationAction extends Action
{
	private final String mExecuteNamespace;
	private final String mExecuteMethod;
	private final String mReturnValue;
	
	private static final String EXECUTE_NAMESPACE = "@executeNamespace";
	private static final String EXECUTE_METHOD = "@executeMethod";

	private static final String SEND_METHOD = "send";
	private static final String RECEIVE_METHOD = "receive";
	private static final String STATUS_METHOD = "serverstatus";
	private static final String METHOD_RESETOFFLINEDATABASE = "ResetOfflineDatabase";
	private static final String CHECKPOINT_METHOD = "SetSendCheckpoint";

	protected SynchronizationAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
		mExecuteNamespace = definition.optStringProperty(EXECUTE_NAMESPACE);
		mExecuteMethod = definition.optStringProperty(EXECUTE_METHOD);
		mReturnValue = definition.optStringProperty("@returnValue");
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return (Services.Strings.hasValue(definition.optStringProperty(EXECUTE_NAMESPACE))
				&& Services.Strings.hasValue(definition.optStringProperty(EXECUTE_METHOD)));
	}

	@Override
	public boolean Do()
	{
		Object result = null;
		//Call Send or receive
		
		Services.Log.debug(" Sync method " + mExecuteNamespace + " " + mExecuteMethod);
		if (mExecuteMethod.equalsIgnoreCase(SEND_METHOD))
		{
			Services.Log.debug("callOfflineReplicator (Sync.Send) from Action Do ");
			result = SynchronizationSendHelper.callOfflineReplicator();
		}
		if (mExecuteMethod.equalsIgnoreCase(RECEIVE_METHOD))
		{
			Services.Log.debug("callSynchronizer (Sync.Receive) from Action Do ");
			result = Services.Sync.callSynchronizer(true, true);
		}
		if (mExecuteMethod.equalsIgnoreCase(STATUS_METHOD))
		{
			result = Services.Sync.callSynchronizerCheck();
		}
		if (mExecuteMethod.equalsIgnoreCase(METHOD_RESETOFFLINEDATABASE))
		{
			SyncManager.resetSyncDatabase(Services.Application.get());
		}
		if (mExecuteMethod.equalsIgnoreCase(CHECKPOINT_METHOD))
		{
			result = Services.Sync.saveCheckPoint();
		}

		// Check return value
		if (result != null)
			setOutputValue(mReturnValue, Value.newValue(result));

		return true;
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}
}
