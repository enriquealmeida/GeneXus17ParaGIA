package com.genexus.android.core.ui.navigation;

import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.DashboardItem;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.IInspectableComponent;
import com.genexus.android.core.fragments.LayoutFragmentActivityState;

/**
 * A Navigation Controller is the object responsible for handling fragment instantiation
 * (and related operations such as managing call targets, replacing or stacking history, &c)
 * inside a single activity. Examples are: sliding menu, tabbed interface, split.
 */
public interface NavigationController
{
	boolean start(ComponentParameters mainParams, LayoutFragmentActivityState previousState);
	@NonNull NavigationHandled handle(UIObjectCall call, Intent callIntent);
	boolean showTarget(String targetName);
	boolean hideTarget(String targetName);
	boolean isTargetVisible(String targetName);


	// Life-cycle events.
	Pair<View, Boolean> onPreCreate(Bundle savedInstanceState, ComponentParameters mainParams);
	void onCreate(Bundle savedInstanceState);
	void onPostCreate(Bundle savedInstanceState);
	void onResume();
	void onPostResume();
	void onConfigurationChanged(Configuration newConfig);
	void onSaveInstanceState(Bundle outState);
	void onPause();
	void onStop();
	void onStart();

	void saveActivityState(LayoutFragmentActivityState outState);

	// Misc events.
	boolean setTitle(IDataView fromDataView, CharSequence title);
	boolean onOptionsItemSelected(MenuItem item);
	boolean onKeyUp(int keyCode, KeyEvent event);
	boolean onBackPressed();

	List<IInspectableComponent> getActiveComponents();

	// info
	DashboardItem getMenuItemDefinition(String itemName);

}
