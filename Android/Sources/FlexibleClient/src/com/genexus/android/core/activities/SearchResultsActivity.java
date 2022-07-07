package com.genexus.android.core.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.UIActionHelper;
import com.genexus.android.core.controls.SearchBox;
import com.genexus.android.core.controls.SearchBoxDefinition;
import com.genexus.android.core.fragments.BaseFragment;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.LayoutFragmentActivityState;

public class SearchResultsActivity extends GenexusActivity
{

	private SearchBox mSearchView;
	private ActivityController mController;

	private SearchBoxDefinition mSearchBoxDefinition;

	// state
	private String mSearchCurrentValue;
	private boolean mDestroyComponent = false;

	private static final String STATE_KEY_DESTROY = "Gx::SearchPattern::Destroy";


	@Override
	protected boolean initializeController(ActivityController controller)
	{
		Intent intent = getIntent();
		// read intent special data?

		// get searchBox definition, see how
		mSearchBoxDefinition = (SearchBoxDefinition)intent.getSerializableExtra(IntentParameters.SEARCH_BOX_DEFINITION);

		Services.Log.debug(" dynamic: " + mSearchBoxDefinition.isSearchBoxDynamic() + " ");
		mSearchCurrentValue = mSearchBoxDefinition.getSearchBoxCurrentValue();

		mController = controller;
		return super.initializeController(controller);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// initialize something

	}

	@Override
	protected boolean initializeView(ActivityController controller, Bundle savedInstanceState, LayoutFragmentActivityState previousState)
	{
		// restore state
		if (previousState!=null)
		{
			mDestroyComponent = previousState.getBooleanProperty(STATE_KEY_DESTROY, false);
		}
		return super.initializeView(controller, savedInstanceState, previousState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Services.Log.debug(" SearchResultsActivity onCreateOptionsMenu ");
		// when recreate options menu
		if (mSearchView!=null)
		{
			mSearchCurrentValue = mSearchView.getGxValue();
		}
		super.onCreateOptionsMenu(menu);

		// IDataViewDefinition definition = getDefaultDataView(workWithName);
		if (mDestroyComponent && !haveSearchViewInActivity())
		{
			Services.Log.debug(" SearchResultsActivity onCreateOptionsMenu not add search, replaced ");
		}
		else
		{
			addSearchToActionBar(menu);
		}

		// only if if search pattern yet
		return true;
	}

	private void addSearchToActionBar(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.standardsearch, menu);

		MenuItem searchItem = menu.findItem(R.id.menusearch2);

		UIActionHelper.setStandardMenuItemImage(mController.getActivity(), searchItem, ActionDefinition.StandardAction.SEARCH);

		// Only if action bar is visible, if not put them as menu
		if (ActivityHelper.hasActionBar(mController.getActivity()))
			searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		Services.Log.debug(" addSearchToActionBar SearchBox create ");
		mSearchView = SearchBox.create(mController.getActivity(), searchItem, mSearchBoxDefinition);

		mSearchView.setLastRunQueryString(mSearchCurrentValue);
		mSearchView.setGxValue(mSearchCurrentValue);

		// special case, set the theme to the stand alone search box in action bar
		if (Strings.hasValue(mSearchBoxDefinition.getThemeClassName()))
		{
			ThemeClassDefinition classDefinition = Services.Themes.getThemeClass(mSearchBoxDefinition.getThemeClassName());
			if (classDefinition!=null)
				mSearchView.applyEditClass(classDefinition);
		}

		if (Strings.hasValue(mSearchCurrentValue))
			mSearchView.clearFocus();
	}


	private boolean haveSearchViewInActivity()
	{
		boolean result = false;
		if (mSearchBoxDefinition!=null)
		{
			for (IDataView dataView : this.getDataViews())
			{
				if (dataView.getDefinition() != null)
				{
					if (dataView.getDefinition().getObjectName().equalsIgnoreCase(mSearchBoxDefinition.getSearchResultPanel()))
						return true;
				}
			}
		}
		return result;
	}

	// destroyComponent
	@Override
	public void destroyComponent(BaseFragment fragment)
	{
		super.destroyComponent(fragment);
		Services.Log.debug(" SearchResultsActivity destroyComponent ");
		mDestroyComponent = true;
	}

	//save state
	@Override
	protected void saveActivityState(LayoutFragmentActivityState outState)
	{
		super.saveActivityState(outState);
		outState.setProperty(STATE_KEY_DESTROY, mDestroyComponent );
	}


}
