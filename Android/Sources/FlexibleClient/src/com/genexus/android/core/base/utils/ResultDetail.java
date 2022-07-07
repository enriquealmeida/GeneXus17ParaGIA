package com.genexus.android.core.base.utils;

import java.io.Serializable;

/**
 * Generic class for returning a boolean result plus some extra info.
 */
public class ResultDetail<T> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final boolean mResult;
	private final String mMessage;
	private final T mData;

	public static final ResultDetail<?> TRUE = ResultDetail.ok();
	public static final ResultDetail<?> FALSE = ResultDetail.error();

	private ResultDetail(boolean result, String message, T data)
	{
		mResult = result;
		mMessage = message;
		mData = data;
	}

	public boolean getResult() { return mResult; }
	public String getMessage() { return mMessage; }
	public T getData() { return mData; }

	public static ResultDetail<Void> ok()
	{
		return ok(null);
	}

	public static <T> ResultDetail<T> ok(T data)
	{
		return new ResultDetail<>(true, Strings.EMPTY, data);
	}

	public static ResultDetail<Void> error()
	{
		return error(Strings.EMPTY);
	}

	public static ResultDetail<Void> error(String message)
	{
		return error(message, null);
	}

	public static <T> ResultDetail<T> error(String message, T data)
	{
		if (message == null)
			message = Strings.EMPTY;

		return new ResultDetail<>(false, message, data);
	}
}
