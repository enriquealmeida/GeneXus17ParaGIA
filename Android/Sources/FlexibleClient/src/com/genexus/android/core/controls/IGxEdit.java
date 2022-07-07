package com.genexus.android.core.controls;

import android.content.Intent;

import com.genexus.android.core.usercontrols.IGxUserControl;

public interface IGxEdit extends IGxUserControl
{
	/**
	 * Returns the value associated with this control in string format
	 * The program will call this method to set the actual value for the control.
	 * @return
	 */
	String getGxValue();

	/**
	 * Set the value associated with the control. The control is responsible to store the actual value and set
	 * this value in the required type to the underline control implementation
	 * @param value
	 */
	void setGxValue(String value);

	/**
	 * The program use this as a memento, so return the value previously stored using setGxTag
	 * @return
	 */
	String getGxTag();

	/***
	 * Just store this memento to return in a subsequent call to getGxTag
	 * @param tag
	 */
	void setGxTag(String tag);

	/***
	 * Sometimes your View call some Intents for results, this result is handled in the container Activity. The container activity is
	 * going to call your View with the Intent result. Typically this is used to implement picker controls.
	 * @param data
	 */
	void setValueFromIntent(Intent data);

	/***
	 * Set the enabled state of this view. The interpretation of the enabled state varies by subclass.
	 * @param enabled
	 */
	void setEnabled(boolean enabled);

	/**
	 * Returns whether the control is in an "editable" state (i.e. not disabled for user input).
	 */
	boolean isEditable();

	/***
	 * Get the control to use in the views of items for this controls
	 * default implementation should return this
	 * @return
	 */
	IGxEdit getViewControl();

	/***
	 * Get the control to use in the edit of items for this controls
	 * default implementation should return this
	 * @return
	 */
	IGxEdit getEditControl();
}
