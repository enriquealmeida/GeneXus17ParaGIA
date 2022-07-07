package com.genexus.android.core.base.metadata.loader;

import com.genexus.android.R;
import com.genexus.android.core.base.services.Services;

public class LoadException extends Exception
{
	private static final long serialVersionUID = 1L;

	private final String mErrorContext;
	private final Exception mDetailException;

	private LoadException(String errorContext, String detailMessage, Exception exception)
	{
		super(detailMessage, exception);
		mErrorContext = errorContext;
		mDetailException = exception;
	}

	public Exception getDetail()
	{
		return mDetailException;
	}

	public static LoadException from(String errorContext, Exception exception)
	{
		if (exception instanceof LoadException)
		{
			// Don't nest messages if we nest catches, just use the inner exception and the combined message.
			errorContext = ((LoadException)exception).mErrorContext + ", " + errorContext;
			exception = ((LoadException)exception).mDetailException;
		}

		String message = String.format(Services.Strings.getResource(R.string.GXM_ErrorLoadingMetadata), errorContext);
		Services.Log.warning(message, exception);

		return new LoadException(errorContext, message, exception);
	}
}
