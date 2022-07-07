package com.genexus.android.core.ui;

import android.view.View;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;

/**
 * Extended version of the Coordinator interface with methods that should only be called
 * by those that <b>set up</b> the object, not by those that will call it later.
  */
public interface CoordinatorAdvanced extends Coordinator
{
	/**
	 * Registers a new control with the given definition
	 */
	void addControl(View control, LayoutItemDefinition definition);
	
}
