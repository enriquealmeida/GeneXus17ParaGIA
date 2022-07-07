package com.genexus.android.core.layers;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.application.MessageLevel;
import com.genexus.android.core.base.application.OutputMessage;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.IServiceDataResult;

class RemoteUtils
{
	public static OutputResult outputNoDefinition(String objectName)
	{
		return OutputResult.error(messageNoDefinition(objectName));
	}

	public static String messageNoDefinition(String objectName)
	{
		return String.format("The definition for object '%s' was not found in the application.", objectName);
	}

	public static OutputResult translateOutput(IServiceDataResult response)
	{
		if (response.isOk())
			return OutputResult.ok();

		// Process single error.
		OutputMessage msg = new OutputMessage(MessageLevel.ERROR, response.getErrorMessage());
		return new OutputResult(response.getErrorType(), msg);
	}

	public static OutputResult translateOutput(ServiceResponse response)
	{
		List<OutputMessage> messages = new ArrayList<>();

		if (Services.Strings.hasValue(response.WarningMessage))
			messages.add(new OutputMessage(MessageLevel.WARNING, response.WarningMessage));

		if (Services.Strings.hasValue(response.ErrorMessage))
			messages.add(new OutputMessage(MessageLevel.ERROR, response.ErrorMessage));

		if (response.getResponseOk())
		{
			if (response.Data != null)
			{
				// Special "message" field.
				String messageStr = response.Data.optString("message");
				if (Services.Strings.hasValue(messageStr))
					messages.add(new OutputMessage(MessageLevel.ERROR, messageStr));

				// Read the contents of the "Messages" special output variable.
				INodeCollection messageNodes = response.Data.optCollection("Messages");
				if (messageNodes.length() == 0)
					messageNodes = response.Data.optCollection("messages");

				for (INodeObject messageNode : messageNodes)
				{
					// Get text from gxmessage or Description
					String msgText = messageNode.optString("gxmessage");
					if (!Services.Strings.hasValue(msgText))
						msgText = messageNode.optString("Description");

					String type = messageNode.optString("Type");
					MessageLevel msgLevel = CommonUtils.translateMessageLevel(type);

					messages.add(new OutputMessage(msgLevel, msgText));
				}
			}
		}

		return new OutputResult(response.StatusCode, messages);
	}
}
