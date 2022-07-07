package com.genexus.android.core.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.RecyclerListener;
import android.widget.EditText;
import android.widget.ListView;

import com.genexus.android.R;
import com.genexus.android.ListViewUtils;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.CellDefinition;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.ILayoutActionDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.UIActionHelper;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.grids.GridAdapter;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.grids.GridItemLayout;
import com.genexus.android.core.controls.grids.ISupportsEditableControls;
import com.genexus.android.core.controls.grids.ISupportsMultipleSelection;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.KeyboardUtils;

public class GxListView extends ListView implements IGridView,
													IGxThemeable,
													IGxControlRuntime,
													ISupportsMultipleSelection,
													ISupportsEditableControls,
													RecyclerListener {
	private final Coordinator mCoordinator;
	private final GridDefinition mDefinition;
	private final ListViewHelper mHelper;
	private final GridAdapter mAdapter;

	private boolean mInSelectionMode;
	private ActionDefinition mInSelectionForAction;
	private boolean mWithSelection;

	private Drawable mDefaultSelector;
	private Drawable mDefaultDivider;

	private ThemeClassDefinition mThemeClass;

	public GxListView(Context context, Coordinator coordinator, GridDefinition definition)
	{
		super(context);

		mCoordinator = coordinator;
		mDefinition = definition;
		mHelper = new ListViewHelper(this, mCoordinator, mDefinition);
		mAdapter = new GridAdapter(getContext(), mHelper, mDefinition);

		// prepareAdapter
		mAdapter.setSelectionMode(mInSelectionMode);
		mAdapter.adjustSizeWithMarginPadding(mDefinition);

		if (mDefinition.hasInverseLoad())
		{
			setStackFromBottom(true);
			setTranscriptMode(TRANSCRIPT_MODE_NORMAL);
		}

		// Store default drawables from style, if they need to be restored later.
		mDefaultSelector = getSelector();
		mDefaultDivider = getDivider();

		// Although descendant views may receive focus, the ListView itself doesn't need it.
		setFocusable(false);

		setRecyclerListener(this);

		// Important: BEFORE setAdapter().
		mHelper.showFooter(true, Strings.EMPTY, mThemeClass);

		// Fix transparency for background color.
		// See: http://android-developers.blogspot.com/2009/01/why-is-my-list-black-android.html
		setCacheColorHint(0);

		// Set the separator from theme class, keep default from style.
		updateSeparator(new GxHorizontalSeparator(context, definition));

		// set adapter, must be done after showFooters
		setAdapter(mAdapter);

	}

	public boolean handlesClicksOn(GridItemLayout itemView)
	{
		return (mDefinition.getDefaultAction() != null ||
				mHelper.hasDifferentLayoutWhenSelected(itemView.getEntity()));
	}

	@Override
	public boolean performItemClick(View view, int position, long id)
	{
		// Base handling of item click is IGNORED in case we are selecting items with a current
		// selection already; otherwise the default action would be ignored for
		// grids with selection.
		boolean continueAfterBase = (mInSelectionMode && !mWithSelection);

		if (super.performItemClick(view, position, id))
		{
			if (!continueAfterBase)
				return true;
		}

		class ClearSelectionHelper
		{
			private boolean selectionChangedExecuted = false;
			private boolean defaultActionExecuted = false;

			private void check() {
				if (selectionChangedExecuted && defaultActionExecuted &&
					mDefinition.getSelectionType() == GridDefinition.SelectionType.KeepWhileExecuting) {
					Services.Device.runOnUiThread(mAdapter::clearSelection);
				}
			}

			public void onSelectionChangedExecuted() {
				selectionChangedExecuted = true;
				check();
			}

			public void onDefaultActionExecuted() {
				defaultActionExecuted = true;
				check();
			}
		}
		final ClearSelectionHelper clearSelectionHelper = new ClearSelectionHelper();

		// Note: cannot use mAdapter.getEntity() here because index might be shifted by footer/header view.
		Entity item = (Entity)getAdapter().getItem(position);
		if (item != null && mDefinition.getShowSelector() == GridDefinition.ShowSelector.None) {
			mAdapter.selectIndex(position, true, clearSelectionHelper::onSelectionChangedExecuted);
		}

		return mHelper.runDefaultAction(item, clearSelectionHelper::onDefaultActionExecuted);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params)
	{
		mHelper.adjustMargins(params);
		super.setLayoutParams(params);
	}

	@Override
	public void update(ViewData data)
	{
		if (mDefinition.hasInverseLoad() && getAdapter() != null)
		{
			// Keep scrolling position (only necessary when adding ABOVE).
			int addedItems = data.getCount() - mAdapter.getCount();
			int index = getFirstVisiblePosition() + addedItems;
			View topView = getChildAt(getHeaderViewsCount());
			int top = (topView == null) ? 0 : topView.getTop();

			if (mHelper.isFooterViewVisible())
				index += getHeaderViewsCount();

			internalUpdate(data);
			setSelectionFromTop(index, top);
		}
		else
		{
			internalUpdate(data);

			//set position to top if data say that
			if (data.getMoveToTop())
			{
				this.post(new Runnable()
				{
					@Override
					public void run()
					{
						setSelection(0);
					}
				});
			}
		}
	}

	private void internalUpdate(ViewData data)
	{
		setFastScrollEnabled(data.getUri());

		mAdapter.setData(data);
		updateSelection(data);

		// Important: AFTER setAdapter().
		// removeFooterView() cannot be called before setting adapter, and addFooterView()
		// has already been called.
		mHelper.showFooter(data.isMoreAvailable(), data.getStatusMessage(), mThemeClass);
	}



	private void setFastScrollEnabled(GxUri uri)
	{
		if (uri != null && uri.getOrder() != null && uri.getOrder().getEnableAlphaIndexer())
		{
			setFastScrollEnabled(true);
			//TODO : see how to fix alpha index with fill_parent
			// http://code.google.com/p/android/issues/detail?id=9054
			// http://stackoverflow.com/questions/2912082/section-indexer-overlay-is-not-updating-as-the-adapters-data-changes
			//ViewGroup.LayoutParams params = this.getLayoutParams();
			//params.height = LayoutParams.WRAP_CONTENT;
			//params.width = LayoutParams.WRAP_CONTENT;
			//this.setLayoutParams(params);
		}
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		applyClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mThemeClass;
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass)
	{
		mHelper.setThemeClass(themeClass);
		updateSeparator(new GxHorizontalSeparator(getContext(), mDefinition, themeClass));
		updateSelector(themeClass);
		mAdapter.notifyDataSetChanged();
	}

	private void updateSeparator(GxHorizontalSeparator separator)
	{
		setHeaderDividersEnabled(false);
		setFooterDividersEnabled(false);

		if (separator.isVisible())
		{
			if (!separator.isDefault())
			{
				setDivider(separator.getDrawable());
				setDividerHeight(separator.getHeight());
			}
			else
				setDivider(mDefaultDivider);
		}
		else
		{
			setDivider(null);
			setDividerHeight(0);
		}
	}

	private void updateSelector(ThemeClassDefinition themeClass)
	{
		if (mDefinition.getDefaultAction() != null)
		{
			if (themeClass != null)
			{
				if ((themeClass.getThemeGridEvenRowClass() != null && themeClass.getThemeGridEvenRowClass().hasHighlightedBackground()) ||
					(themeClass.getThemeGridOddRowClass() != null && themeClass.getThemeGridOddRowClass().hasHighlightedBackground()))
				{
					// Remove the default selector if the row classes have highlighted background.
					setSelector(android.R.color.transparent);
				}
				else
				{
					// Use the default selector to act as highlighted background color.
					setSelector(mDefaultSelector);
				}
			}
		}
		else
		{
			// Disable selector if the grid doesn't have a default action.
			setSelector(android.R.color.transparent);
		}
	}

	@Override
	public void addListener(GridEventsListener listener)
	{
		mHelper.setListener(listener);
		setOnScrollListener(mOnScrollList);
	}

	private final OnScrollListener mOnScrollList = new OnScrollListener()
	{
		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3)
		{
			mHelper.onScroll();
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) { }
	};

	@Override
	public void onMovedToScrapHeap(View view)
	{
		try
		{
			mFakeGainFocus = true;
			View focusedChild = view.findFocus();
			if (focusedChild != null)
			{
				KeyboardUtils.hideKeyboard(focusedChild);
				focusedChild.clearFocus();
			}
		}
		finally
		{
			mFakeGainFocus = false;
		}

		mHelper.discardItemView(view);
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		try
		{
			super.dispatchDraw(canvas);
		}
		catch (IndexOutOfBoundsException ignored)
		{
			// Possibly a weird bug in HeaderViewListAdapter.isEnabled(). Swallow this exception.
			// See http://stackoverflow.com/questions/8431342/listview-random-indexoutofboundsexception-on-froyo
			// and http://stackoverflow.com/questions/25471000/headerviewlistadapter-randomly-crashes
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		mHelper.beginOnMeasure();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mHelper.endOnMeasure();
	}

	private boolean mFakeGainFocus;

	@Override
	protected void layoutChildren()
	{
		try
		{
			View focusedView = findFocus();
			if (focusedView instanceof EditText)
				mFakeGainFocus = true;

			super.layoutChildren();
		}
		finally
		{
			mFakeGainFocus = false;
		}
	}

	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect)
	{
		//noinspection SimplifiableIfStatement
		if (mFakeGainFocus)
			return true;

		return super.requestFocus(direction, previouslyFocusedRect);
	}

	/**
	 * Calculates the height in pixels necessary to fit the current grid data:
	 * size of items (including their margins, padding, separators and group headers)
	 * plus grid padding and margin.
	 */
	public int calculateAutoHeight()
	{
		int itemCount = mAdapter.getCount();
		if (itemCount == 0)
			return 0;

		int totalItemHeight = 0;
		int maxPaddingMarginsGrid = getGridMaxPaddingMarginsOddEven(mDefinition);
		if (mDefinition.getOddItemLayouts().size() > 1)
		{
			int position = 0;
			// Iterate over each item, because they may have different item layouts (possibly with different heights).
			for (Entity dataItem : mAdapter.getData().getEntities())
			{
				boolean odd = position % 2 == 0;
				TableDefinition itemLayout = mAdapter.getLayoutFor(dataItem, odd);
				totalItemHeight += calculateItemAutoHeight(itemLayout, maxPaddingMarginsGrid);
				position++;
			}
		}
		else
		{
			// Simply multiply the number of items by the height of the only possible layout.
			int oddCount = (itemCount + 1) / 2;
			int evenCount = itemCount - oddCount;
			totalItemHeight = oddCount * calculateItemAutoHeight(mDefinition.getDefaultItemLayout(true), maxPaddingMarginsGrid);
			totalItemHeight += evenCount * calculateItemAutoHeight(mDefinition.getDefaultItemLayout(false), maxPaddingMarginsGrid);
		}

		// Add space for "Break by" separators, if used.
		if (mDefinition.hasBreakBy())
		{
			// We create a group header TextView to measure it _for each time the header appears_
			// since a long text may produce word wrap, and hence a different height.
			GridItemLayout dummyItem = (GridItemLayout)LayoutInflater.from(getContext()).inflate(R.layout.grid_item_with_break, this, false);
			GxTextView dummyHeader = dummyItem.findViewById(R.id.grid_item_header_text);
			mHelper.applyGroupHeaderClass(dummyHeader);

			int headerWidth = ((CellDefinition)mDefinition.getParent()).getAbsoluteWidth();

			for (int i = 0; i < itemCount; i++)
			{
				if (mAdapter.isGroupHeaderVisible(i))
				{
					// Set the group header text to measure the view.
					dummyHeader.setText(mAdapter.getGroupHeaderText(i));
					dummyHeader.measure(MeasureSpec.makeMeasureSpec(headerWidth, MeasureSpec.AT_MOST), MeasureSpec.UNSPECIFIED);
					int headerHeight = dummyHeader.getMeasuredHeight();

					totalItemHeight += headerHeight;
				}
			}
		}

		// Add space for separators between items.
		int separatorHeight = getDividerHeight();
		int totalSeparatorHeight = separatorHeight * (itemCount - 1);

		int gridPaddingHeight = 0;
		if (mDefinition.getThemeClass() != null)
			gridPaddingHeight = mDefinition.getThemeClass().getPadding().getTotalVertical();

		//temp, TODO, check this, margins should not be necessary but works in an example.
		int gridMarginsHeight = 0;
		if (mDefinition.getThemeClass() != null)
			gridMarginsHeight = mDefinition.getThemeClass().getMargins().getTotalVertical();

		// Height of the grid is: padding of the grid + item heights + separator heights.
		return gridMarginsHeight + gridPaddingHeight + totalItemHeight + totalSeparatorHeight;
	}

	private static int calculateItemAutoHeight(TableDefinition itemLayout, int maxPaddingMarginsGrid)
	{
		int rowHeight = itemLayout.getAbsoluteHeight();

		int maxPaddingMargins = 0;
		if (itemLayout.getThemeClass() != null)
		{
			// Add top/bottom padding from theme class.
			maxPaddingMargins += itemLayout.getThemeClass().getPadding().getTotalVertical();

			// Add top/bottom margins from theme class.
			// This must be considered for items (because the whole item view, including margins, is part of the grid)
			// However, not for the grid itself, because its margins are "outside".
			maxPaddingMargins += itemLayout.getThemeClass().getMargins().getTotalVertical();
		}
		maxPaddingMargins = Math.max(maxPaddingMarginsGrid, maxPaddingMargins);

		rowHeight += maxPaddingMargins;
		return rowHeight;
	}

	private static int getGridMaxPaddingMarginsOddEven(GridDefinition gridDefinition) {
		int maxPaddingMarginsGridResult = 0;
		int maxPaddingMarginsGrid = 0;
		if (gridDefinition.getThemeClass()!=null)
		{
			// Padding in Even
			if (gridDefinition.getThemeClass().getThemeGridEvenRowClass()!=null)
			{
				maxPaddingMarginsGrid += gridDefinition.getThemeClass().getThemeGridEvenRowClass().getPadding().getTotalVertical();
				maxPaddingMarginsGrid += gridDefinition.getThemeClass().getThemeGridEvenRowClass().getMargins().getTotalVertical();
			}

			maxPaddingMarginsGridResult = maxPaddingMarginsGrid;

			// Padding in Odd
			maxPaddingMarginsGrid = 0;
			if (gridDefinition.getThemeClass().getThemeGridOddRowClass()!=null)
			{
				maxPaddingMarginsGrid += gridDefinition.getThemeClass().getThemeGridOddRowClass().getPadding().getTotalVertical();
				maxPaddingMarginsGrid += gridDefinition.getThemeClass().getThemeGridOddRowClass().getMargins().getTotalVertical();

			}
			maxPaddingMarginsGridResult = Math.max(maxPaddingMarginsGrid, maxPaddingMarginsGridResult);

		}
		return maxPaddingMarginsGridResult;
	}

	@Override
	public void saveEditValues()
	{
		mHelper.saveEditValues();
	}

	@Override
	public void setSelectionMode(boolean enabled, ActionDefinition forAction)
	{
		mInSelectionMode = enabled;

		// Notify adapter, needed to redraw views (to add or remove checkbox).
		mAdapter.setSelectionMode(enabled);

		if (enabled)
		{
			setChoiceMode(CHOICE_MODE_MULTIPLE_MODAL);
			setMultiChoiceModeListener(new MultiChoiceModeListener());
			mInSelectionForAction = forAction;
		}
		else
		{
			setChoiceMode(CHOICE_MODE_NONE);
			clearChoices();
		}
	}

	private void updateSelection(ViewData data)
	{
		for (int i = 0; i < data.getEntities().size(); i++)
			setItemChecked(i, data.getEntities().get(i).isSelected());
	}

	@Override
	public void setItemSelected(int position, boolean selected)
	{
		setItemChecked(position, selected);
	}

	private class MultiChoiceModeListener implements AbsListView.MultiChoiceModeListener
	{
		private List<ILayoutActionDefinition> mActions;

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu)
		{
			updateTitle(mode);
			mWithSelection = true;

			// Get the list of actions that apply to this multiple selection to build menu
			// (or show a single action if selection was started for a particular one).
			mActions = new ArrayList<>();
			if (mInSelectionForAction != null)
			{
				// Find the LAYOUT action given its associated event, to get UI properties (icon, caption).
				for (ILayoutActionDefinition layoutAction : mDefinition.getMultipleSelectionActions())
				{
					if (layoutAction.getEvent() == mInSelectionForAction)
					{
						mActions.add(layoutAction);
						break;
					}
				}
			}
			else
				mActions.addAll(mDefinition.getMultipleSelectionActions());

			for (int i = 0; i < mActions.size(); i++)
			{
				ILayoutActionDefinition action = mActions.get(i);
				MenuItem item = menu.add(Menu.NONE, i, Menu.NONE, action.getCaption());
				UIActionHelper.setMenuItemImage(getContext(), item, action, null);
			}

			return true;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
		{
			updateTitle(mode);
		}

		private void updateTitle(ActionMode mode)
		{
			int count = getCheckedItemCount();
			String title = String.format(Services.Strings.getResource(R.string.GXM_SelectedItems), count);
			mode.setTitle(title);
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu)
		{
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item)
		{
			// Perform the multiple selection action.
			ILayoutActionDefinition action = mActions.get(item.getItemId());
			mHelper.runExternalAction(action.getEvent());
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode)
		{
			mWithSelection = false;
		}
	}

	@Override
	public Value getPropertyValue(String name) {
		if (GridHelper.PROPERTY_SELECTED_INDEX.equalsIgnoreCase(name))
			return Value.newInteger(mAdapter.getSelectedIndex() + 1);
		return null;
	}

	@Override
	public Value callMethod(String name, List<Value> parameters) {
		if (GridHelper.METHOD_SELECT.equalsIgnoreCase(name) && parameters.size() == 1) {
			int selectedIndex = parameters.get(0).coerceToInt() - 1;

			// If the item to select has not been loaded in the grid, this method has no effect.
			if (selectedIndex >= getCount()) {
				return null;
			}

			ListViewUtils.smoothScrollToPosition(this , selectedIndex);
			mAdapter.selectIndex(selectedIndex, true);
		}
		else if (GridHelper.METHOD_DESELECT.equalsIgnoreCase(name) && parameters.size() == 1) {
			int deselectedIndex = parameters.get(0).coerceToInt() - 1;

			// If the item to deselect has not been loaded in the grid, this method has no effect.
			if (deselectedIndex >= getCount()) {
				return null;
			}

			mAdapter.deselectIndex(deselectedIndex, true);
		}
		return null;
	}
}
