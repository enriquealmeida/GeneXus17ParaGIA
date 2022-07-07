package com.genexus.android.core.controls.media;

import android.net.Uri;

public interface IGxMediaEdit {

	/**
	 * Called when the user selects a media file.
	 *
	 * @param mediaUri Uri to the selected media file.
	 */
	void onMediaChanged(Uri mediaUri, boolean successful, boolean fireValueChangedEvent);

	/**
	 * Called when media needs to be copied to the app's directory
	 *
	 * @param value Boolean to turn loading ON or OFF
	 */
	void setLoadingForCopying(boolean value);

	/**
	 * Returns the current control type in order to display the according prompt options
	 * @return The control type
	 */
	String getControlType();
}
