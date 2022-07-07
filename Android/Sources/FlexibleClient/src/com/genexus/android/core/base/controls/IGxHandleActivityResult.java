package com.genexus.android.core.base.controls;

import android.content.Intent;

public interface IGxHandleActivityResult
{
	/**
	 * Notifies the control that an activity it (presumably) has launched has finished, with the specified code and data.
	 * @return True if the control successfully handled the result, false otherwise.
	 */
	boolean handleOnActivityResult(int requestCode, int resultCode, Intent data);
}
