package com.genexus.android.core.controls.grids;

import com.genexus.android.core.base.metadata.ActionDefinition;

public interface ISupportsMultipleSelection
{
	/**
	 * Starts or ends selection mode for the grid.
	 * @param forAction Starts mode for a specific action (can be null).
	 */
	void setSelectionMode(boolean enabled, ActionDefinition forAction);

	/**
	 * Selects or deselects the specified item.
	 */
	void setItemSelected(int position, boolean selected);
}
