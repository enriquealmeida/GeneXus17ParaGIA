package com.genexus.android.core.ui.navigation.controllers;

import java.util.ArrayList;
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
import com.genexus.android.core.ui.navigation.NavigationController;
import com.genexus.android.core.ui.navigation.NavigationHandled;
import com.genexus.android.core.ui.navigation.UIObjectCall;

/**
 * Class for supporting multiple navigation controllers at once (e.g. tabs and slider menu).
 */
public class CompositeNavigationController implements NavigationController
{
	private final NavigationController[] mControllers;

	public CompositeNavigationController(NavigationController... controllers)
	{
		mControllers = controllers;
	}

	@Override
	public boolean start(ComponentParameters mainParams, LayoutFragmentActivityState previousState)
	{
		for (NavigationController controller : mControllers)
			if (controller.start(mainParams, previousState))
				return true;

		return false;
	}

	@Override
	public @NonNull
	NavigationHandled handle(UIObjectCall call, Intent callIntent)
	{
		for (NavigationController controller : mControllers)
		{
			NavigationHandled handled = controller.handle(call, callIntent);
			if (handled != NavigationHandled.NOT_HANDLED)
				return handled;
		}

		return NavigationHandled.NOT_HANDLED;
	}

	@Override
	public boolean showTarget(String targetName)
	{
		for (NavigationController controller : mControllers)
			if (controller.showTarget(targetName))
				return true;

		return false;
	}

	@Override
	public boolean hideTarget(String targetName)
	{
		for (NavigationController controller : mControllers)
			if (controller.hideTarget(targetName))
				return true;

		return false;
	}

	@Override
	public boolean isTargetVisible(String targetName)
	{
		for (NavigationController controller : mControllers)
			if (!controller.isTargetVisible(targetName))
				return false;

		// default return true
		return true;
	}

	@Override
	public Pair<View, Boolean> onPreCreate(Bundle savedInstanceState, ComponentParameters mainParams)
	{
		Pair<View, Boolean> result = null;
		for (NavigationController controller : mControllers)
			result = controller.onPreCreate(savedInstanceState, mainParams);
		return result;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		for (NavigationController controller : mControllers)
			controller.onCreate(savedInstanceState);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState)
	{
		for (NavigationController controller : mControllers)
			controller.onPostCreate(savedInstanceState);
	}

	@Override
	public void onResume()
	{
		for (NavigationController controller : mControllers)
			controller.onResume();
	}

	@Override
	public void onPostResume()
	{
		for (NavigationController controller : mControllers)
			controller.onPostResume();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		for (NavigationController controller : mControllers)
			controller.onConfigurationChanged(newConfig);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		for (NavigationController controller : mControllers)
			controller.onSaveInstanceState(outState);
	}

	@Override
	public void saveActivityState(LayoutFragmentActivityState outState)
	{
		for (NavigationController controller : mControllers)
			controller.saveActivityState(outState);
	}

	@Override
	public void onPause()
	{
		for (NavigationController controller : mControllers)
			controller.onPause();
	}

	@Override
	public void onStop() {
		for (NavigationController controller : mControllers)
			controller.onStop();
	}

	@Override
	public void onStart() {
		for (NavigationController controller : mControllers)
			controller.onStart();
	}

	@Override
	public boolean setTitle(IDataView fromDataView, CharSequence title)
	{
		for (NavigationController controller : mControllers)
		{
			if (controller.setTitle(fromDataView, title))
				return true;
		}

		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		for (NavigationController controller : mControllers)
			if (controller.onOptionsItemSelected(item))
				return true;

		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		for (NavigationController controller : mControllers)
			if (controller.onKeyUp(keyCode, event))
				return true;

		return false;
	}
	
	@Override
	public boolean onBackPressed()
	{
		for (NavigationController controller : mControllers)
			if (controller.onBackPressed())
				return true;

		return false;
	}

	@Override
	public List<IInspectableComponent> getActiveComponents() {
		List<IInspectableComponent> activeComponents = new ArrayList<>();
		for (NavigationController controller : mControllers) {
			activeComponents.addAll(controller.getActiveComponents());
		}
		return activeComponents;
	}

	@Override
	public DashboardItem getMenuItemDefinition(String itemName)
	{
		for (NavigationController controller : mControllers)
			if (controller.getMenuItemDefinition(itemName)!= null)
				return controller.getMenuItemDefinition(itemName);

		return null;
	}
}
