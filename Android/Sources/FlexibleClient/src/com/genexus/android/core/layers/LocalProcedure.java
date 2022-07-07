package com.genexus.android.core.layers;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.application.IProcedure;
import com.genexus.android.core.base.application.MessageLevel;
import com.genexus.android.core.base.application.OutputMessage;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.metadata.ProcedureDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.PropertiesObject;
import com.artech.base.services.IGxProcedure;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.Cast;

public class LocalProcedure implements IProcedure
{
	private final String mName;
	private final IGxProcedure mImplementation;

	public LocalProcedure(String name)
	{
		mName = name;
		mImplementation = GxObjectFactory.getProcedure(name);
	}

	public LocalProcedure(@NonNull String packageName, String name)
	{
		mName = name;
		mImplementation = GxObjectFactory.getProcedure(packageName, name);
	}


	public IGxProcedure getImplementation()
	{
		return mImplementation;
	}

	public boolean isPrinterReport()
	{
		return mImplementation instanceof com.genexus.reports.GXReportText;
	}

	@Override
	public OutputResult execute(PropertiesObject parameters)
	{
		if (mImplementation != null)
		{
			LocalUtils.beginTransaction();
			
			try {
				mImplementation.execute(parameters);
			}
			finally {
				LocalUtils.endTransaction();
			}
			LocalBusinessComponent.postSendBCToServer();
			return translateOutput(parameters);
		}
		else
			return LocalUtils.outputNoImplementation(mName);
	}

	@Override
	public OutputResult executeMultiple(List<PropertiesObject> parameters)
	{
		if (mImplementation != null)
		{
			for (PropertiesObject item : parameters)
			{
				LocalUtils.beginTransaction();
				try
				{
					mImplementation.execute(item);
				}
				finally
				{
					LocalUtils.endTransaction();
				}
				// TODO: Accumulate procedure warnings/errors. Not right now, to maintain compatibility with online behavior.
			}
			// TODO: Return procedure warnings/errors. Not right now, to maintain compatibility with online behavior.
			LocalBusinessComponent.postSendBCToServer();
			return OutputResult.ok();
		}
		else
			return LocalUtils.outputNoImplementation(mName);
	}

	private OutputResult translateOutput(PropertiesObject parameters)
	{
		// See if there are any output parameters of type "Messages".
		ObjectParameterDefinition outputParameter = getOutputParameter(Services.Application.getDefinition().getProcedure(mName));

		if (outputParameter == null)
			return OutputResult.ok(); // No output means the call is successful, unless it crashes or something.

		// Since this is a collection SDT, it should have been converted to a collection of Entities.
		Object outputValue = parameters.getProperty(outputParameter.getName());
		List<?> procedureMessages = Cast.as(List.class, outputValue);
		if (procedureMessages != null)
		{
			ArrayList<OutputMessage> messages = new ArrayList<>();
			for (Object objProcedureMessage : procedureMessages)
			{
				Entity procedureMessage = Cast.as(Entity.class, objProcedureMessage);
				if (procedureMessage != null)
				{
					MessageLevel msgLevel = CommonUtils.translateMessageLevel(procedureMessage.optStringProperty("Type"));
					String msgText = procedureMessage.optStringProperty("Description");
					messages.add(new OutputMessage(msgLevel, msgText));
				}
			}

			return new OutputResult(messages);
		}
		else
		{
			Services.Log.warning(String.format("Could not read output messages after calling procedure '%s'.", mName));
			return OutputResult.ok();
		}
	}

	private static ObjectParameterDefinition getOutputParameter(ProcedureDefinition procedure)
	{
		final String MESSAGES_NAME = "Messages";

		if (procedure == null)
			return null;

		for (ObjectParameterDefinition parameter : procedure.getOutParameters())
			if (MESSAGES_NAME.equalsIgnoreCase(parameter.getName()))
				return parameter;

		return null;
	}

	@Override
	public OutputResult executeReplicator(PropertiesObject parameters)
	{
		return execute(parameters);
	}
}
