package com.genexus.android.core.providers;

/**
 * Exception used to indicate a failed Entity Storage operation.
 */
class EntityStorageException extends Exception
{
	public EntityStorageException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
