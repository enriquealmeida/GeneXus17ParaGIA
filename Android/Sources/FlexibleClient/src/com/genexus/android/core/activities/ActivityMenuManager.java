package com.genexus.android.core.activities;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.Events;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.filter.SearchDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.UIActionHelper;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.controllers.IDataSourceBoundView;
import com.genexus.android.core.controllers.IDataSourceController;
import com.genexus.android.core.controllers.IDataSourceControllerInternal;
import com.genexus.android.core.controls.GxSearchView;
import com.genexus.android.core.controls.actiongroup.ActionBarMerger;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.ui.Anchor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

class ActivityMenuManager
{
	private final ActivityController mController;
	private final ActionBarMerger mActionBarHelper;

	private ArrayList<IDataView> mActiveDataViews;
	private ActiveGrids mActiveGrids;
	private GxSearchView mSearchView;
	private IDataView mUpButtonHandler;

	ActivityMenuManager(ActivityController controller, ActivityActionGroupManager actionGroups)
	{
		mController = controller;
		mActionBarHelper = actionGroups.getActionBar();
	}

	public void onCreateOptionsMenu(Menu menu)
	{
		prepare();
		initialize(menu);
		addStandardActions(menu);
		addDataViewActions(menu);
	}

	private void prepare()
	{
		mActiveDataViews = new ArrayList<>();
		for (IDataView dataview : mController.getGxActivity().getActiveDataViews(false))
		{
			if (dataview.isDataReady())
				mActiveDataViews.add(dataview);
		}

		mActiveGrids = new ActiveGrids(mActiveDataViews);

		// Show UP button if a DataView will handle it.
		for (IDataView dataView : mActiveDataViews)
		{
			if (dataView.getDefinition().getEvent(Events.UP) != null)
				mUpButtonHandler = dataView;
		}
	}

	private void initialize(Menu menu)
	{
		if (menu.size() == 0)
		{
			MenuInflater inflater = mController.getActivity().getMenuInflater();
			inflater.inflate(R.menu.standardmenu, menu);
		}
	}

	private void addStandardActions(Menu menu)
	{
		// 1) Search. Show if any dataview has search fields.
		MenuItem searchItem = menu.findItem(R.id.menusearch);
		if (searchItem != null)
		{
			UIActionHelper.setStandardMenuItemImage(mController.getActivity(), searchItem, ActionDefinition.StandardAction.SEARCH);
			setItemVisible(searchItem, mActiveGrids.HasSearch, MenuItem.SHOW_AS_ACTION_ALWAYS);
			if (mActiveGrids.HasSearch)
			{
				mSearchView = GxSearchView.create(mController.getActivity(), searchItem);
				mSearchView.setOnSearchClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) { runActionWithGridChooser(mPrepareSearchHandler); }
				});

				// Temporary workaround: Remove flags that inhibit keyboard suggestions, because offline search does not have app suggestions yet.
				if (mController.getModel().getUIContext() != null && mController.getModel().getUIContext().getConnectivitySupport() == Connectivity.Offline)
				{
					final int MASK_ENABLE_SUGGESTIONS = ~(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
					mSearchView.setInputType(mSearchView.getInputType() & MASK_ENABLE_SUGGESTIONS);
				}
			}
		}

		// 2) Filter. Show if any dataview has filter/order.
		MenuItem filterItem = menu.findItem(R.id.menufilter);
		if (filterItem != null)
		{
			filterItem.setTitle(mActiveGrids.getFilterActionText());
			UIActionHelper.setStandardMenuItemImage(mController.getActivity(), filterItem, ActionDefinition.StandardAction.FILTER);
			setItemVisible(filterItem, mActiveGrids.hasFilterAction(), MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		// 4) UP button.
		if (mUpButtonHandler != null)
		{
			ActionBar actionBar = SherlockHelper.getActionBar(mController.getActivity());
			if (actionBar != null)
				actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	void onSearchRequested()
	{
		if (mSearchView != null)
			mSearchView.show();
	}

	private void addDataViewActions(Menu menu)
	{
		mActionBarHelper.initializeMenu(menu, mActiveDataViews);
	}

	private void setItemVisible(MenuItem item, boolean visible, int actionBarMode)
	{
		item.setVisible(visible);

		// Only if action bar is visible, if not put them as menu
		if (ActivityHelper.hasActionBar(mController.getActivity()))
			item.setShowAsAction(actionBarMode);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		int itemId = item.getItemId();
		if (itemId == android.R.id.home)
		{
			if (mUpButtonHandler != null)
			{
				ActionDefinition upEvent = mUpButtonHandler.getDefinition().getEvent(Events.UP);
				mUpButtonHandler.runAction(upEvent, Anchor.fromViewId(mController.getActivity(), itemId));
			}

			return true;
		}
		else if (itemId == R.id.menusearch)
		{
			Services.Log.debug("onOptionsItemSelected() called for R.id.menusearch: This should never happen.");
			return true;
		}
		else if (itemId == R.id.menufilter)
		{
			showFilter();
			return true;
		}
		else
			return mActionBarHelper.onOptionsItemSelected(itemId);
	}

	/**
	 * Runs an action that applies over a single grid, by making the user choose a grid
	 * first if more than one is available.
	 */
	private void runActionWithGridChooser(final IActionWithGridChooser action)
	{
		// Build a list of the grids that can have the action applied on.
		ArrayList<IDataSourceController> grids = new ArrayList<>();
		for (IDataSourceController dataSource : mActiveGrids.DataSources)
			if (action.isApplicable(dataSource))
				grids.add(dataSource);

		if (grids.size() == 0)
			return;

		if (grids.size() == 1)
		{
			action.run(grids.get(0));
			return;
		}

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mController.getActivity());
		dialogBuilder.setTitle(action.getName());

		String[] names = new String[grids.size()];
		for (int i = 0; i < grids.size(); i++)
			names[i] = grids.get(i).getName();

		dialogBuilder.setItems(names, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				IDataSourceController dataSource = mActiveGrids.DataSources.get(which);
				action.run(dataSource);
			}
		});

		dialogBuilder.show();
	}

	private void showFilter()
	{
		runActionWithGridChooser(mFilterHandler);
	}

	@SuppressWarnings("checkstyle:MemberName")
	private static class ActiveGrids
	{
		private final ArrayList<IDataSourceController> DataSources;
		private boolean HasSearch;
		private boolean HasFilter;
		private boolean HasOrderChoice;

		public ActiveGrids(Iterable<IDataView> dataViews)
		{
			DataSources = new ArrayList<>();
			for (IDataView dataView : dataViews)
			{
				if (dataView.getController() != null)
				{
					for (IDataSourceController dataSource : dataView.getController().getDataSources())
					{
						IDataSourceDefinition definition = dataSource.getDefinition();
						if (dataSource.getDefinition().isCollection())
						{
							// Even in an active DataView, a bound view may not be active (e.g. if it's not shown).
							IDataSourceBoundView dataSourceView = ((IDataSourceControllerInternal)dataSource).getBoundView();
							if (dataSourceView != null && dataSourceView.isActive())
							{
								DataSources.add(dataSource);
								SearchDefinition search = definition.getFilter().getSearch();

								HasSearch |= (search != null);
								HasFilter |= definition.getFilter().hasAdvancedFilter();
								HasOrderChoice |= (definition.getOrders().size() > 1);
							}
						}
					}
				}
			}
		}

		public boolean hasFilterAction()
		{
			return (HasFilter || HasOrderChoice);
		}

		public String getFilterActionText()
		{
			if (!hasFilterAction())
				return Strings.EMPTY;

			// Set button text (filters/order/order & filters).
			if (!HasFilter && HasOrderChoice)
				return Services.Strings.getResource(R.string.GXM_Order);
			else if (HasFilter && !HasOrderChoice)
				return Services.Strings.getResource(R.string.GXM_Filter);
			else
				return Services.Strings.getResource(R.string.GXM_FilterAndOrder);
		}
	}

	private interface IActionWithGridChooser
	{
		String getName();
		boolean isApplicable(IDataSourceController dataSource);
		void run(IDataSourceController dataSource);
	}

	private final IActionWithGridChooser mPrepareSearchHandler = new IActionWithGridChooser()
	{
		@Override
		public String getName()
		{
			return Services.Strings.getResource(R.string.GX_BtnSearch);
		}

		@Override
		public boolean isApplicable(IDataSourceController dataSource)
		{
			return (dataSource.getDefinition().getFilter().getSearch() != null);
		}

		@Override
		public void run(IDataSourceController dataSource)
		{
			// Prepare search on the selected grid.
			SearchHelper.prepare(dataSource);

			if (mSearchView != null)
				mSearchView.setQueryHint(dataSource.getDefinition().getFilter().getSearch().getCaption());
		}
	};

	private final IActionWithGridChooser mFilterHandler = new IActionWithGridChooser()
	{
		@Override
		public String getName()
		{
			return mActiveGrids.getFilterActionText();
		}

		@Override
		public boolean isApplicable(IDataSourceController dataSource)
		{
			return (dataSource.getDefinition().getFilter().hasAdvancedFilter() ||
					dataSource.getDefinition().getOrders().size() > 1);
		}

		@Override
		public void run(IDataSourceController dataSource)
		{
			ActivityLauncher.callFilters(mController.getModel().getUIContext(), dataSource);
		}
	};
}
