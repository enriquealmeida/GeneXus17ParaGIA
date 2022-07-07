package com.genexus.android.core.actions;

import android.view.Menu;

/**
 * Interface that controls may implement if they want to add custom items to the action bar.
 */
public interface ICustomMenuManager
{
	/**
	 * Called during Activity.onCreateOptionsMenu().
	 */
	void onCustomCreateOptionsMenu(Menu menu);
}
