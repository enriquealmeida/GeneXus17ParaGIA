package com.genexus.android.core.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.genexus.android.core.actions.ActionParametersHelper;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.fragments.LayoutFragmentActivity;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;

public class SearchBox extends SearchView implements IGxEditThemeable, IGxControlRuntime
{

	public static final String OBJECT_NAME_SEARCH = "GeneXus.SD.Search";
	public static final String EVENT_SEARCH_TEXT_CHANGED = "SearchTextChanged";

	//private IGxEdit mControl;
	private Coordinator mCoordinator;
	private LayoutItemDefinition mDefinition;
	private Context mContext;
	private Activity mParentActivity;

	// Search Box definition
	private boolean mSearchDynamic = false;
	private boolean mSearchAutoComplete = false;
	private String mSearchResultPanel = "";
	private boolean mSearchInResultPanel = false;

	//ej: "@SearchBoxResultsPanelParam": "&vMin, &vMax , 'text'"
	private String mSearchResultPanelParameters = "";

	private SearchBox(Context context, Activity activity)
	{
		super(context);
		mContext = context;
		mParentActivity = activity;

		// also need the definition at least.
	}

	public SearchBox(Context context, Coordinator coordinator, LayoutItemDefinition def)
	{
		super(context);
		mContext = context;
		//mControl = this;
		mCoordinator = coordinator;
		mDefinition = def;

		// from: http://stackoverflow.com/a/30249187/462007
		// allow back keyboard to work... not sure why
		this.setFocusableInTouchMode(true);

		this.setOnQueryTextListener(this.mSearchOnQueryTextListener);
		this.setOnSuggestionListener(this.mSearchOnSuggestionListener);

		this.setOnSearchClickListener(this.mSearchOnSearchClickListener);
		this.setOnQueryTextFocusChangeListener(this.mSearchOnQueryTextFocusChangeListener);

		//read definition values
		mSearchDynamic = mDefinition.getControlInfo().optStringProperty("@SearchBoxSearchType").equalsIgnoreCase("Dynamic");
		mSearchAutoComplete = mDefinition.getControlInfo().getBooleanProperty("@SearchBoxSearchAutocomplete", false);
		mSearchResultPanel = MetadataLoader.getAttributeName(mDefinition.getControlInfo().optStringProperty("@SearchBoxResultsPanel"));
		mSearchResultPanelParameters =  mDefinition.getControlInfo().optStringProperty("@SearchBoxResultsPanelParam");

		if (mSearchDynamic && Strings.hasValue(mSearchResultPanel))
		{
			this.setIconifiedByDefault(true);
		}
		else
		{
			this.setIconifiedByDefault(false);
		}
	}

	public static SearchBox create(Activity activity, MenuItem searchItem, SearchBoxDefinition searchBoxDefinition)
	{
		SearchBox searchView = new SearchBox( SherlockHelper.getActionBarThemedContext(activity), activity);
		searchView.setIconifiedByDefault(true);

		SearchManager searchManager = (SearchManager)activity.getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));

		searchView.setOnQueryTextListener(searchView.mSearchOnQueryTextListener);
		searchView.setOnSuggestionListener(searchView.mSearchOnSuggestionListener);

		searchView.setOnSearchClickListener(searchView.mSearchOnSearchClickListener);
		searchView.setOnQueryTextFocusChangeListener(searchView.mSearchOnQueryTextFocusChangeListener);

		searchView.setIconified(false);

		searchItem.setActionView(searchView);

		searchView.mSearchDynamic = searchBoxDefinition.isSearchBoxDynamic();

		// is in searchPanel true
		searchView.mSearchInResultPanel = true;

		// keep SEARCH_BOX_DEFINITION to update it ?

		return searchView;
	}

	private final Handler mHandler = new Handler(Looper.myLooper());
	private String mQueryString = "";

	private final SearchView.OnQueryTextListener mSearchOnQueryTextListener = new SearchView.OnQueryTextListener()
	{
		@Override
		public boolean onQueryTextChange(String newText)
		{
			//Services.Log.debug(" query text change, value : " + newText);
			//Services.Log.debug(" query text change, oldValue : " + mQueryString);
			//Services.Log.debug(" is dynamic call refresh, dynamic : " + mSearchDynamic + " , in SearchResultPanel "+ mSearchInResultPanel);

			if (mSearchDynamic && !newText.equalsIgnoreCase(mQueryString))
			{
				mQueryString = newText;
				mHandler.removeCallbacksAndMessages(null);

				mHandler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						//Put your call to the server here (with mQueryString)
						Services.Log.debug(" call panel SearchTextChanged event with query " + mQueryString);
						callPanelSearchTextChanged(mQueryString);
					}
				}, 350); //delay to wait user end typing.
				return true;
			}
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String query)
		{
			String currentValue = getGxValue();
			Services.Log.debug("click search query submit, value : " + currentValue);

			// show search result in a new activity, if definition say that.
			//Services.Log.debug("call to panel : " + mSearchResultPanel);
			if (callPanel(currentValue)) return true;

			//Services.Log.debug("call to panel failed call panel SearchTextChanged event ");
			callPanelSearchTextChanged(currentValue);
			return false;
		}
	};

	public boolean callPanel(String currentValue)
	{
		if (Strings.hasValue(mSearchResultPanel) && (mContext instanceof LayoutFragmentActivity))
		{
			LayoutFragmentActivity myActivity = (LayoutFragmentActivity) mContext;
			List<String> parameters = new ArrayList<>();
			parameters.add(currentValue);

			Services.Log.debug("call to panel : " + mSearchResultPanel + " with parm " + currentValue);

			// add extra parameters
			if (Strings.hasValue(mSearchResultPanelParameters))
			{
				Services.Log.debug("call to panel , add extra parameters: " + mSearchResultPanelParameters);
				if (myActivity.getMainFragment()!=null)
				{
					// get main entity
					Entity contextEntity = myActivity.getMainFragment().getContextEntity();
					String[] paramList = mSearchResultPanelParameters.split(",", -1);
					List<ActionParameter> actionParameters = new ArrayList<>();

					// create an ActionParameter for each parameter
					for (String extraParam : paramList)
					{
						ActionParameter actionParam = new ActionParameter(extraParam, extraParam, null);
						actionParameters.add(actionParam);
					}

					List<String> valuesParameters = ActionParametersHelper.getParametersForDataView(actionParameters, contextEntity);
					// Add parameters values to parameters call
					Services.Log.debug("call to panel , add extra parameters values: " + valuesParameters.toString());
					parameters.addAll(valuesParameters);
				}

			}
			String currentThemeClassName = "";
			if (mThemeClass!=null)
				currentThemeClassName = mThemeClass.getName();
			ActivityLauncher.callSearchResult(myActivity.getUIContext(), mSearchResultPanel, parameters
			, new SearchBoxDefinition(mDefinition, currentValue, currentThemeClassName));
			return true;
		}

		return false;
	}

	public void callPanelSearchTextChanged(String currentValue)
	{
		if (mParentActivity!=null && mParentActivity instanceof LayoutFragmentActivity)
		{
			LayoutFragmentActivity myActivity = (LayoutFragmentActivity) mParentActivity;

			Services.Log.debug("call SearchTextChanged event");
			ExternalObjectEvent event = new ExternalObjectEvent(OBJECT_NAME_SEARCH, EVENT_SEARCH_TEXT_CHANGED);
			// sent result to context activity, to the coordinator where the event is defined.
			Coordinator coordinator = event.getFormCoordinatorForEvent(myActivity);
			// raise SearchTextChanged event with value calculated
			event.fire(Arrays.asList(currentValue), coordinator, null);
		}
	}

	private final SearchView.OnSuggestionListener mSearchOnSuggestionListener = new SearchView.OnSuggestionListener()
	{
		@Override
		public boolean onSuggestionSelect(int position) { return false; }

		@Override
		public boolean onSuggestionClick(int position)
		{
			//hide();
			return false;
		}
	};

	private final SearchView.OnClickListener mSearchOnSearchClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			//on get focus and dynamic , on search click get raise. call to panel if not there
			String currentValue = getGxValue();
			//Services.Log.debug("click search, value : " + currentValue);
			//Services.Log.debug("isdynamic : " + mSearchDynamic + " isInPanel: " + mSearchInResultPanel);
			if (v instanceof SearchView && mSearchDynamic && !mSearchInResultPanel)
			{
				SearchView mySearchView = (SearchView) v;
				//Services.Log.debug("search view set iconified true ");
				mySearchView.setIconified(true);
			}
			if (mSearchDynamic && !mSearchInResultPanel)
			{
				if (callPanel(currentValue)) return;

			}
		}
	};

	private final View.OnFocusChangeListener mSearchOnQueryTextFocusChangeListener = new View.OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			//Services.Log.debug("focus change, value : " + hasFocus);
			if (hasFocus)
			{
				// from: http://stackoverflow.com/a/30401449
				final View view = v;
				view.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						// needed to show keyboard in the explicit searchbox at startup.
						// Dont do this in search panel, cause keyboard show after every search
						if (!mSearchDynamic && !mSearchInResultPanel)
						{
							showInputMethod(view.findFocus());
						}
					}
				}, 200);
			}
		}
	};

	private void showInputMethod(View view) {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.showSoftInput(view, 0);
		}
	}


	public void show()
	{
		setIconified(false);
	}

	public void hide()
	{
		setQuery("", false);
		setIconified(true);
	}

	@Override
	public String getGxValue()
	{
		return this.getQuery().toString();
	}

	@Override
	public void setGxValue(String value)
	{
		this.setQuery(value, false);
	}

	public void setLastRunQueryString(String value)
	{
		mQueryString = value;
	}

	@Override
	public String getGxTag()
	{
		return (String)this.getTag();
	}

	@Override
	public void setGxTag(String data)
	{
		this.setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data)
	{
	}

	@Override
	public boolean isEditable()
	{
		return true;
	}

	@Override
	public IGxEdit getViewControl()
	{
		return this;
	}

	@Override
	public IGxEdit getEditControl()
	{
		return this;
	}

	// IGxRuntime methods
	private static final String METHOD_DOSEARCH = "DoSearch";

	@Override
	public Value callMethod(String name, List<Value> parameters)
	{
		if (name.equalsIgnoreCase(METHOD_DOSEARCH))
		{
			String currentValue = getGxValue();
			Services.Log.debug("do search : " + currentValue);

			// show search result in a new activity, if definition say that.
			//Services.Log.debug("call to panel : " + mSearchResultPanel);
			callPanel(currentValue);
		}
		return null;
	}

	private ThemeClassDefinition mThemeClass;

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		// keep the last theme set
		mThemeClass = themeClass;

		// apply text color
		SearchAutoComplete searchSrcTextView = findViewById(androidx.appcompat.R.id.search_src_text);
		if (searchSrcTextView!=null)
		{
			ThemeUtils.setFontProperties(searchSrcTextView, themeClass);
		}

		if (mSearchInResultPanel)
		{
			// apply background properties
			// Override to pass layout item definition (GxLinearLayout does not always correspond to a layout item).
			ThemeUtils.setBackgroundBorderProperties(this, themeClass, BackgroundOptions.defaultFor(mDefinition));


		}
	}
}
