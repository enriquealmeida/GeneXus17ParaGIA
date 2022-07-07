package com.genexus.android.controls.grids.magazineviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Configuration;
import android.database.DataSetObserver;

import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.genexus.android.core.activities.ActivityController;
import com.genexus.android.core.base.controls.IGxControlNotifyEvents;
import com.genexus.android.core.base.controls.IGxControlPreserveState;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.controls.grids.GridAdapter;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.grids.IGridSite;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.controls.grids.magazineviewer.FlipperOptions.FlipperLayoutType;
import com.genexus.android.controls.grids.viewpager.AutoGrowViewPager;
import com.genexus.android.controls.grids.viewpager.GxCirclePageIndicator;
import com.genexus.android.controls.grids.viewpagers.R;

public class GxMagazineViewer extends android.widget.LinearLayout implements IGridView, IGxThemeable, IGridSite, IGxControlRuntime, IGxControlNotifyEvents, IGxControlPreserveState {
	public static final String NAME = "SDMagazineViewer";
	private static final String SPECIFIC = "specific";
	private GridDefinition mDefinition;
	private FlipperAdapter mAdapter;
	private Coordinator mCoordinator;
	protected FlipperOptions mFlipperOptions;
	private GridHelper mHelper;
	private GridAdapter mGridAdapter;
	private Size mSize;
	private GxCirclePageIndicator mCirclePageIndicator;
	private ViewPager mViewPager;
	private PageChangeListener mPageChangeListener;

	private boolean mHasMoreData = false;
	private boolean mRecalculatePageOnRotation = false;
	private int mCurrentFirstItem = 0;
	private int mCurrentPagePortrait = 0;
	private int mCurrentPageLandscape = 0;
	private boolean mSetProgrammatically = false;
	private ThemeClassDefinition mThemeClass;

	private static final int REQUEST_THRESHOLD = 2;
	private static final String EVENT_PAGE_CHANGED = "PageChanged";
	private static final String PROPERTY_CURRENT_PAGE = "CurrentPage";
	private static final String PROPERTY_SELECTED_INDEX = "SelectedIndex";
	private static final String PROPERTY_SHOWPAGECONTROLLER = "ShowPageController";

	private static final String METHOD_ENSURE_VISIBLE = "EnsureVisible";

	private static final String STATE_CURRENT_PAGE_LANDSCAPE = "CurrentPageLandscape";
	private static final String STATE_CURRENT_PAGE_PORTRAIT = "CurrentPagePortrait";
	private static final String STATE_CURRENT_FIRST_ITEM = "CurrentFirstItem";
	private static final String STATE_RECALCULATE_PAGE = "RecalculatePage";

	public GxMagazineViewer(Context context, Coordinator coordinator, GridDefinition def) {
		super(context);
		mCoordinator = coordinator;
		setLayoutDefinition(def);
	}

	public GxMagazineViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void initView() {
		setOrientation(LinearLayout.VERTICAL);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// auto grow work for linear laoyut and relative layout.
		// not ok in relative layout with above rule, so we using linear layout in that case
		// and only relative in transparent controller.
		if (mFlipperOptions.isTransparentFooter()) {
			// if color is transparent set footer as part of the content
			RelativeLayout relativeContainer = new RelativeLayout(getContext());
			inflater.inflate(R.layout.simple_circles_relative, relativeContainer);
			this.addView(relativeContainer, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		} else {
			// if not set it to draw above indicator, normal case
			inflater.inflate(R.layout.simple_circles, this);
		}

		mPageChangeListener = new PageChangeListener();
		mViewPager = findViewById(R.id.autogrowpager);

		if (mDefinition.gridUserControlSupportAutoGrow() && mDefinition.hasAutoGrow()) {
			// set autogrow to view pager.
			AutoGrowViewPager myviewpager = (AutoGrowViewPager) mViewPager;
			myviewpager.setAutoGrow(true);
		}

		mCirclePageIndicator = findViewById(R.id.indicator);
		mCirclePageIndicator.setOnPageChangeListener(mPageChangeListener);

		mGridAdapter = new GridAdapter(getContext(), mHelper, mDefinition);
		finishViewInitialization(true);
	}

	protected void finishViewInitialization(boolean registerDataSetObserver) {
		if (mGridAdapter == null || mFlipperOptions == null || mHelper == null || mViewPager == null)
			throw new IllegalStateException("Cannot finishViewInitialization without calling initView first");

		int previousPage = mViewPager.getCurrentItem();

		FlipDataSource flipDataSource = new FlipDataSource(getContext(), mFlipperOptions, mGridAdapter, mHelper);

		mAdapter = new FlipperAdapter(flipDataSource, mSize);

		if (registerDataSetObserver) {
			mGridAdapter.registerDataSetObserver(new DataSetObserver() {

				@Override
				public void onChanged() {
					super.onChanged();
					mAdapter.notifyDataSetChanged();
				}

			});
		}

		mViewPager.setAdapter(mAdapter);
		mCirclePageIndicator.setViewPager(mViewPager);
		mCirclePageIndicator.setOptions(mFlipperOptions);

		int currentPage = mViewPager.getCurrentItem();
		if (currentPage != previousPage)
			mPageChangeListener.onPageSelected(currentPage);
	}

	// Get current page depending on current orientation
	private int getCurrentPage() {
		int currentPage;

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			currentPage = mCurrentPagePortrait;
		} else {
			currentPage = mCurrentPageLandscape;
		}

		return currentPage;
	}

	// Set current page depending on current orientation
	private void setCurrentPage(int currentPage) {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mCurrentPagePortrait = currentPage;
		} else {
			mCurrentPageLandscape = currentPage;
		}
	}


	private class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			// If the page was changed by the user, mark that the current page should be recalculated in case the activity is re-created.
			if (!mSetProgrammatically) {
				// Update values
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					mCurrentPagePortrait = position;
				} else {
					mCurrentPageLandscape = position;
				}

				mCurrentFirstItem = position * mFlipperOptions.getItemsPerPage();
				mRecalculatePageOnRotation = true;

				// Select index before firing event, since the event uses the item context.
				mGridAdapter.selectIndex(mCurrentFirstItem, true);
				firePageChangedEvent();
			} else
				mSetProgrammatically = false;

			notifyPageChangedEvent();
			requestMoreDataIfNeeded();
		}
	}

	private boolean firePageChangedPending = false;

	private void firePageChangedEvent() {
		ActionDefinition event = mDefinition.getEventHandler(EVENT_PAGE_CHANGED);
		int currentPage = getCurrentPage();
		int selectedIndex = currentPage * mFlipperOptions.getItemsPerPage();

		Services.Log.debug("GxMagazineViewer firePageChanged currentpage " + currentPage + " selectedIndex " + selectedIndex);
		if (event != null) {
			try {
				firePageChangedPending = false;
				mHelper.runAction(event, mGridAdapter.getEntity(selectedIndex), new Anchor(this));
			} catch (IllegalStateException ex) {
				Services.Log.debug("GxMagazineViewer, firePageChangedEvent could not be performed yet");
				firePageChangedPending = true;
			}

		}
	}

	private void notifyPageChangedEvent() {
		ActivityController activityController = ActivityController.from(mCoordinator.getUIContext().getActivity());
		if (activityController != null)
			activityController.notify(EventType.GRID_PAGE_CHANGED);
	}

	private boolean requestMoreDataIfNeeded() {
		// Get more data when near the end of the current set.
		boolean getMoreData = mHasMoreData && (getCurrentPage() + REQUEST_THRESHOLD >= mAdapter.getCount());
		if (getMoreData) {
			mHelper.requestMoreData();
		}

		return getMoreData;
	}

	@Override
	public void addListener(GridEventsListener listener) {
		mHelper = new GridHelper(this, mCoordinator, mDefinition, false);
		mHelper.setListener(listener);
	}

	protected void setControlInfo(ControlInfo info) {
		mFlipperOptions = new FlipperOptions();
		ArrayList<Integer> layout = new ArrayList<>();

		try {
			String rowsPerColumn = info.optStringProperty("@SDMagazineViewerRowsPerColumn");
			String[] cols = Services.Strings.split(rowsPerColumn, ' ');
			for (String col : cols)
				layout.add(Integer.parseInt(col));
		} catch (NumberFormatException e) {
			Services.Log.warning("Invalid SDMagazineViewerRowsPerColumn", e);
		}

		mFlipperOptions.setLayout(layout);
		mFlipperOptions.setItemsPerPage(info.optIntProperty("@SDMagazineViewerItemsPerPage"));
		mFlipperOptions.setHeaderText(info.optStringProperty("@SDMagazineViewerHeaderText"));
		mFlipperOptions.setShowFooter(info.optBooleanProperty("@SDMagazineViewerShowFooter"));

		String pageLayoutType = info.optStringProperty("@SDMagazineViewerPageLayout");
		if (pageLayoutType.compareToIgnoreCase(SPECIFIC) == 0)
			mFlipperOptions.setLayoutType(FlipperLayoutType.Specific);
		else
			mFlipperOptions.setLayoutType(FlipperLayoutType.Random);

		ThemeClassDefinition indicatorClass = Services.Themes.getThemeClass(info.optStringProperty("@SDMagazineViewerPageControllerClass"));
		if (indicatorClass != null)
			mFlipperOptions.setFooterThemeClass(indicatorClass);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		mHelper.adjustMargins(params);
		super.setLayoutParams(params);
	}

	@Override
	public void update(ViewData data) {
		mGridAdapter.setData(data);
		mAdapter.notifyDataSetChanged();
		mHasMoreData = data.isMoreAvailable();

		boolean validPageInAdapter = mAdapter.getCount() > 0 && getCurrentPage() >= 0 && getCurrentPage() < mAdapter.getCount();
		if (validPageInAdapter) {
			// Update ViewPager to currentPage if it's not the same as the current one.
			if (getCurrentPage() != mViewPager.getCurrentItem()) {
				mSetProgrammatically = true;
				mViewPager.setCurrentItem(getCurrentPage(), false);
			} else if (firePageChangedPending) {
				// this not get fired. if control is recreated. Se how to fix it.
				Services.Log.debug("firePageChangedEvent pending");
				firePageChangedEvent();
			}
		}

		if (!requestMoreDataIfNeeded()) {
			// Update the indicator when we finish getting all the data.
			mCirclePageIndicator.notifyDataSetChanged();

			//if only has one page, not show page controller.
			if (mAdapter.getCount() == 1) {
				calculateNewSize(false);
				updateControlController();
			}
		}
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		mThemeClass = themeClass;
		applyClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemeClass;
	}

	private void setLayoutDefinition(LayoutItemDefinition layoutItemDefinition) {
		mDefinition = (GridDefinition) layoutItemDefinition;
		if (mDefinition != null)
			setControlInfo(mDefinition.getControlInfo());
	}

	@Override
	public void setAbsoluteSize(Size size) {
		// Remove footer size from item size.
		mSize = new Size(size.getWidth(), size.getHeight() - mFlipperOptions.getFooterHeight());
		initView();
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		mHelper.setThemeClass(themeClass);
	}

	@Override
	public void setPropertyValue(String name, Value value) {

		if (PROPERTY_CURRENT_PAGE.equalsIgnoreCase(name) && value != null) {
			if (mAdapter != null) {
				int page = value.coerceToInt();
				page = Math.max(1, page);

				int itemCount = mAdapter.getCount();
				if (itemCount > 0) {
					page = Math.min(page, itemCount);
					Services.Log.debug("GxMagazineViewer set current item " + page + " total " + itemCount);
					mViewPager.setCurrentItem(page - 1, false);
				}

				setCurrentPage(page - 1);
			}
		} else if (PROPERTY_SELECTED_INDEX.equalsIgnoreCase(name)) {
			Integer selectedIndex = value.coerceToInt() - 1;
			mGridAdapter.selectIndex(selectedIndex, true);
		} else if (PROPERTY_SHOWPAGECONTROLLER.equalsIgnoreCase(name)) {
			Boolean showPageController = Services.Strings.tryParseBoolean(String.valueOf(value));

			calculateNewSize(showPageController);
			updateControlController();
		}
	}

	private void calculateNewSize(Boolean showPageController) {
		//calculate the new size.
		Size oldSize = mSize;
		int dif = mFlipperOptions.getFooterHeight();
		//set show footer.
		mFlipperOptions.setShowFooter(showPageController);
		dif = dif - mFlipperOptions.getFooterHeight();
		mSize = new Size(oldSize.getWidth(), oldSize.getHeight() + dif);
	}

	private void updateControlController() {
		// update the control.
		mCirclePageIndicator.setOptions(mFlipperOptions);
		mAdapter.setNewSize(mSize);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public Value getPropertyValue(String name) {
		if (PROPERTY_CURRENT_PAGE.equalsIgnoreCase(name)) {
			return Value.newInteger(getCurrentPage() + 1);
		} else if (PROPERTY_SELECTED_INDEX.equalsIgnoreCase(name)) {
			return Value.newInteger(mGridAdapter.getSelectedIndex() + 1);
		}
		return null;
	}

	@Override
	public Value callMethod(String name, List<Value> parameters) {
		if (METHOD_ENSURE_VISIBLE.equalsIgnoreCase(name) && parameters.size() == 1) {
			// Index number is expected as an integer value (>= 1).
			int index = parameters.get(0).coerceToInt();
			if (index > 0) {
				ensureVisible(index, false);
			}
		} else if (GridHelper.METHOD_SELECT.equalsIgnoreCase(name) && parameters.size() == 1) {
			// Index number is expected as an integer value (>= 1).
			int index = parameters.get(0).coerceToInt();

			boolean firstItemOfNewPage = isFirstItemOfNewPage(index);

			ensureVisible(index, false);
			mGridAdapter.selectIndex(index - 1, false);
			//is first item of a page, select it. If was already selected (by page change) and unselected (by selectIndex)
			if (firstItemOfNewPage)
				mGridAdapter.selectIndex(index - 1, false);
		}
		return null;
	}

	// Displays the page containing the item with such index.
	private void ensureVisible(int index, boolean suppressEvents) {
		int pageNumber = index / mFlipperOptions.getItemsPerPage();
		if (index % mFlipperOptions.getItemsPerPage() != 0)
			pageNumber++;

		if (suppressEvents)
			mSetProgrammatically = true;

		mViewPager.setCurrentItem(pageNumber - 1, false);
		setCurrentPage(pageNumber - 1);
	}

	private boolean isFirstItemOfNewPage(int index) {
		int pageNumber = index / mFlipperOptions.getItemsPerPage();
		if (index % mFlipperOptions.getItemsPerPage() != 0)
			pageNumber++;

		int currentPage = getCurrentPage() + 1;

		return pageNumber != currentPage && (index % mFlipperOptions.getItemsPerPage() == 1);

	}

	@Override
	public void notifyEvent(EventType type) {
		if (type == EventType.ACTIVITY_DESTROYED && mViewPager != null)
			mViewPager.setAdapter(null); // Destroy all views.
	}

	@Override
	public String getControlId() {
		return mDefinition.getName();
	}

	@Override
	public void saveState(Map<String, Object> state) {
		state.put(STATE_RECALCULATE_PAGE, mRecalculatePageOnRotation);
		state.put(STATE_CURRENT_FIRST_ITEM, mCurrentFirstItem);
		state.put(STATE_CURRENT_PAGE_PORTRAIT, mCurrentPagePortrait);
		state.put(STATE_CURRENT_PAGE_LANDSCAPE, mCurrentPageLandscape);
	}

	@Override
	public void restoreState(Map<String, Object> state) {
		mRecalculatePageOnRotation = (Boolean) state.get(STATE_RECALCULATE_PAGE);
		mCurrentFirstItem = (Integer) state.get(STATE_CURRENT_FIRST_ITEM);
		mCurrentPagePortrait = (Integer) state.get(STATE_CURRENT_PAGE_PORTRAIT);
		mCurrentPageLandscape = (Integer) state.get(STATE_CURRENT_PAGE_LANDSCAPE);

		if (mRecalculatePageOnRotation) {
			// Calculate the new currentPage according to the previous currentFirstItem
			setCurrentPage(mCurrentFirstItem / mFlipperOptions.getItemsPerPage());

			// Calculate the new currentFirstItem of the new page
			mCurrentFirstItem = getCurrentPage() * mFlipperOptions.getItemsPerPage();

			mRecalculatePageOnRotation = false;

			if (getCurrentPage() != mViewPager.getCurrentItem()) {
				mViewPager.setCurrentItem(getCurrentPage(), false);
			}
		}
	}
}
