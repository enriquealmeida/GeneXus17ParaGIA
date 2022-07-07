package com.genexus.android.controls.grids.viewpager;

import java.util.List;
import java.util.Map;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.genexus.android.core.base.controls.IGxControlPreserveState;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.grids.IGridSite;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.controls.grids.magazineviewer.FlipperOptions;
import com.genexus.android.controls.grids.viewpagers.R;

public class GxViewPager extends android.widget.LinearLayout
	implements IGridView, IGxThemeable, IGridSite, IGxControlRuntime, IGxControlPreserveState
{
	public static final String NAME = "SDPagedGrid";
	private static final String PROPERTY_CURRENT_PAGE = "CurrentPage";
	private static final String EVENT_PAGE_CHANGED = "PageChanged";
	private static final String STATE_CURRENT_PAGE = "CurrentPage";

	private GridDefinition mDefinition;
	private GxViewPagerAdapter mAdapter;
	private Coordinator mCoordinator;

	private GridHelper mHelper;
    private Size mSize;
	private ViewPager mViewPager;
	private GxCirclePageIndicator mCirclePageIndicator;

	private boolean mHasMoreData = false;
	private int mCurrentPage = 0;
	protected FlipperOptions mFlipperOptions  = new FlipperOptions();
	private ThemeClassDefinition mThemeClass;

	private static final int REQUEST_THRESHOLD = 2;

	public GxViewPager(Context context, Coordinator coordinator, GridDefinition def)
	{
		super(context);
		mCoordinator = coordinator;
		setLayoutDefinition(def);
	}

	public GxViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	private void initView()
	{
		mHelper.setBounds(mSize.getWidth(), mSize.getHeight());

		setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutparms = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutparms);

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// auto grow work for linear layout and relative layout.
		// not ok in relative layout with above rule, so we using linear layout in that case
		// and only relative in transparent controller.
		if (mFlipperOptions.isTransparentFooter())
		{
			// if color is transparent set footer as part of the content
			RelativeLayout relativeContainer = new RelativeLayout(getContext());
			inflater.inflate(R.layout.simple_circles_relative, relativeContainer);
			this.addView(relativeContainer, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		} else {
			// if not set it to draw above indicator, normal case
			inflater.inflate(R.layout.simple_circles, this);
		}

		mViewPager = findViewById(R.id.autogrowpager);

		mCirclePageIndicator = findViewById(R.id.indicator);
		mCirclePageIndicator.setOnPageChangeListener(new PageChangeListener());

		// Configure visual properties.
		mCirclePageIndicator.setOptions(mFlipperOptions);
	}

	public int getCurrentPage()
	{
		return mCurrentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		mCurrentPage = currentPage;
	}

	private class PageChangeListener extends ViewPager.SimpleOnPageChangeListener
	{
		@Override
		public void onPageSelected(int position)
		{
			mCurrentPage = position;
			mAdapter.setCurrentItem(position);
			firePageChangedEvent(position);
			requestMoreDataIfNeeded();
		}
	}

	private void firePageChangedEvent(int position)
	{
		ActionDefinition event = mDefinition.getEventHandler(EVENT_PAGE_CHANGED);

		if (event != null && position >= 0 && position < mAdapter.getCount())
			mHelper.runAction(event, mAdapter.getEntity(position), new Anchor(this));
	}

	@Override
	public void addListener(GridEventsListener listener)
	{
		mHelper = new GridHelper(this, mCoordinator, mDefinition);
		mHelper.setListener(listener);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params)
	{
		mHelper.adjustMargins(params);
		super.setLayoutParams(params);
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
	public void update(ViewData data)
	{
		prepareAdapter();
		mAdapter.setData(data);
		mHasMoreData = data.isMoreAvailable();

		boolean validPageInAdapter = mAdapter.getCount() > 0 && mCurrentPage >= 0 && mCurrentPage < mAdapter.getCount();

		// Update ViewPager to currentPage if we have the page and it's not the same as the current one.
		if (validPageInAdapter && mCurrentPage != mViewPager.getCurrentItem()) {
			mViewPager.setCurrentItem(mCurrentPage, false);
			mCirclePageIndicator.notifyDataSetChanged();
		}

		if (!requestMoreDataIfNeeded()) {
			// Update the indicator when we finish getting all the data.
			mCirclePageIndicator.notifyDataSetChanged();
		}
	}

	private void prepareAdapter()
	{
		if (mAdapter == null)
		{
			mAdapter = new GxViewPagerAdapter(getContext(), this, mHelper, mDefinition);
			mViewPager.setAdapter(mAdapter);
			mCirclePageIndicator.setViewPager(mViewPager);
			mAdapter.selectIndex(mViewPager.getCurrentItem(), true);
		}
	}

	public void onItemClick(View view)
	{
		Entity entity = mAdapter.getEntity(mCurrentPage);
		mHelper.runDefaultAction(entity);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		applyClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemeClass;
	}

	protected void setControlInfo(ControlInfo info)
	{
		mFlipperOptions = new FlipperOptions();
		mFlipperOptions.setShowFooter(info.optBooleanProperty("@SDPagedGridShowPageController"));

		ThemeClassDefinition indicatorClass = Services.Themes.getThemeClass(info.optStringProperty("@SDPagedGridPageControllerClass"));
		if (indicatorClass != null)
			mFlipperOptions.setFooterThemeClass(indicatorClass);
	}

	@Override
	public void setAbsoluteSize(Size size)
	{
		mSize = new Size(size.getWidth(), size.getHeight() - mFlipperOptions.getFooterHeight());
		initView();
	}

	private void setLayoutDefinition(LayoutItemDefinition layoutItemDefinition)
	{
		mDefinition = (GridDefinition) layoutItemDefinition;
		if (mDefinition != null)
			setControlInfo(mDefinition.getControlInfo());
	}

	@Override
	public void setPropertyValue(String name, Value value)
	{
		if (PROPERTY_CURRENT_PAGE.equalsIgnoreCase(name) && value != null) {
			int page = MathUtils.constrain(value.coerceToInt() - 1, 0, mAdapter.getCount() - 1);
			mViewPager.setCurrentItem(page);
		}
	}

	@Override
	public Value getPropertyValue(String name) {
		if (PROPERTY_CURRENT_PAGE.equalsIgnoreCase(name)) {
			return Value.newInteger(mCurrentPage + 1);
		}
		else if (GridHelper.PROPERTY_SELECTED_INDEX.equalsIgnoreCase(name)) {
			int index = mAdapter == null ?
					0 :                                // Grid's data has not been loaded yet
					mAdapter.getSelectedIndex() + 1;   // Indexes in Genexus start at 1.
			return Value.newInteger(index);
		}
		return null;
	}

	@Override
	public Value callMethod(String methodName, List<Value> parameters)
	{
		if (GridHelper.METHOD_SELECT.equalsIgnoreCase(methodName) && parameters.size() == 1) {
			// Index number is expected as an integer value (>= 1).
			int index = parameters.get(0).coerceToInt() - 1;

			setCurrentPage(index);
			mViewPager.setCurrentItem(index, false);

			// If the data has not been loaded yet (e.g. when executing Select on ClientStart).
			if (mAdapter != null) {
				mAdapter.selectIndex(index, true);
			}
		}
		return null;
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		mHelper.setThemeClass(themeClass);
	}

	@Override
	public String getControlId() {
		return mDefinition.getName();
	}

	@Override
	public void saveState(Map<String, Object> state) {
		state.put(STATE_CURRENT_PAGE, getCurrentPage());
	}

	@Override
	public void restoreState(Map<String, Object> state) {
		int currentPage = (Integer) state.get(STATE_CURRENT_PAGE);
		setCurrentPage(currentPage);
		mViewPager.setCurrentItem(mCurrentPage, false);
	}
}
