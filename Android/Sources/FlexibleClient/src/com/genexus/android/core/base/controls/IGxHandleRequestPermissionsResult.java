package com.genexus.android.core.base.controls;

import androidx.annotation.NonNull;

public interface IGxHandleRequestPermissionsResult
{
	/**
	 * Notifies the control that a request for permissions that it (presumably) asked for has finished, with the specified results.
	 * @return True if the control successfully handled the result, otherwise false.
	 */
	boolean handleOnRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
