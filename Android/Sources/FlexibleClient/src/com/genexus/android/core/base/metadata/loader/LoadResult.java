package com.genexus.android.core.base.metadata.loader;

import com.genexus.android.core.base.utils.Strings;

public class LoadResult
{
	private final int mCode;
	private final Throwable mError;
	private final String mData;

	public static final int RESULT_OK = 0;
	public static final int RESULT_UPDATE = 1;
	public static final int RESULT_ERROR = 2;
	public static final int RESULT_INVALID_APP_URL = 3;

	private LoadResult(int code, Throwable error, String data)
	{
		mCode = code;
		mError = error;
		mData = data;
	}

	public static LoadResult result(int code)
	{
		return new LoadResult(code, null, null);
	}

	public static LoadResult result(int code, String data)
	{
		return new LoadResult(code, null, data);
	}

	public static LoadResult error(Throwable ex)
	{
		return new LoadResult(RESULT_ERROR, ex, null);
	}

	public int getCode()
	{
		return mCode;
	}

	public String getErrorMessage()
	{
		if (mError != null)
		{
			if (mError.getMessage() != null && mError.getMessage().length() > 0)
				return mError.getMessage();
			else
				return mError.toString();
		}
		else
			return Strings.EMPTY;
	}

	public Throwable getErrorDetail()
	{
		if (mError instanceof LoadException)
			return ((LoadException)mError).getDetail();

		return mError;
	}

	public String getData() {
		return mData;
	}
}
