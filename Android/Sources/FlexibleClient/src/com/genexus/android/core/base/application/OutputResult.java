package com.genexus.android.core.base.application;

import java.util.Collections;
import java.util.List;

public class OutputResult
{
	private OutputMessages mMessages;
	private int mStatusCode;

	public OutputResult(int statusCode, List<OutputMessage> messages)
	{
		mStatusCode = statusCode;

		mMessages = new OutputMessages();
		if (messages != null)
			mMessages.addAll(messages);
	}

	public OutputResult(int statusCode, OutputMessage singleMessage)
	{
		this(statusCode, Collections.singletonList(singleMessage));
	}

	public OutputResult(List<OutputMessage> messages)
	{
		this(0, messages);
	}

	public static OutputResult ok()
	{
		return new OutputResult(null);
	}

	public static OutputResult error(String errorMessage)
	{
		OutputMessage msg = new OutputMessage(MessageLevel.ERROR, errorMessage);
		return new OutputResult(Collections.singletonList(msg));
	}

	/*
	public static OutputResult error(Exception e)
	{
		String errorMessage = e.getMessage();
		if (!Services.Strings.hasValue(errorMessage))
			errorMessage = e.getClass().toString();

		return error(errorMessage);
	}
	*/

	public boolean isOk() { return !mMessages.hasErrors(); }

	public String getErrorText()
	{
		return mMessages.getErrorText();
	}

	public String getWarningText()
	{
		return mMessages.getWarningText();
	}

	/**
	 * Gets the special status code (such as DataRequest.ERROR_AUTHENTICATION, DataRequest.ERROR_AUTHORIZATION)
	 * associated to this output.
	 */
	public int getStatusCode()
	{
		return mStatusCode;
	}

	/**
	 * Gets the (read-only) list of messages associated to this output.
	 */
	public List<OutputMessage> getMessages()
	{
		return Collections.unmodifiableList(mMessages);
	}
}
