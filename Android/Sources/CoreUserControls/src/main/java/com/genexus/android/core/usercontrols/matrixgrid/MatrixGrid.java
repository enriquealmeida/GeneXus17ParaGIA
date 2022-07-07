package com.genexus.android.core.usercontrols.matrixgrid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.*;

import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.base.utils.Triplet;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.controls.grids.GridAdapter;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.grids.GridItemLayout;
import com.genexus.android.core.controls.grids.IGridSite;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;
import com.genexus.android.core.usercontrols.R;

@SuppressLint("ViewConstructor")
@SuppressWarnings("deprecation")
public class MatrixGrid extends LinearLayout implements ScrollViewListener, IGridView, IGridSite, IGxThemeable, IGxControlRuntime
{
	public static final String NAME = "MatrixGrid";
	/*
	 * For each grid item the control will hold a ViewHolder with its rect and
	 * its view This rect is used to be intersected with the current visible
	 * area of the content area
	 */
	public static class ViewHolder
	{
		private GxLinearLayout mView;
		private Rect mRect;
		private RecyclerKey mRecyclerKey;

		public Rect getRect()
		{
			return mRect;
		}

		public GxLinearLayout getView()
		{
			return mView;
		}

		public void setView(GxLinearLayout view)
		{
			mView = view;
		}

		public void setTypeAndRect(int itemType, Rect rect)
		{
			mRect = rect;
			mRecyclerKey = new RecyclerKey(itemType, rect.width(), rect.height());
		}

		public RecyclerKey getRecyclerKey()
		{
			return mRecyclerKey;
		}
	}

	private static class RecyclerKey
	{
		private final Triplet<Integer, Integer, Integer> mKeyValues;

		public RecyclerKey(int viewType, int viewWidth, int viewHeight)
		{
			mKeyValues = new Triplet<>(viewType, viewWidth, viewHeight);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;

			if (!(obj instanceof RecyclerKey))
				return false;

			RecyclerKey other = (RecyclerKey)obj;
			return mKeyValues.equals(other.mKeyValues);
		}

		@Override
		public int hashCode()
		{
			return mKeyValues.hashCode();
		}

		@Override
		public String toString()
		{
			return mKeyValues.toString();
		}
	}

	// Composition, see init comments or grid.xml to understand the control
	// layout.
	private TableLayout frozenTableHeader;
	private TableLayout contentHeaderTable;
	private TableLayout frozenTable;
	private ObservableVerticalScrollView frozenColumnView;
	private ObservableHorizontalScrollView headerScrollView;
	private GxAbsoluteLayout contentTable;
	private MatrixTwoDScrollView contentScrollView;
	// This flag is used to avoid send scrolling messages while scrolling
	// programatically
	private boolean mForcingScroll = false;
	// Because we load the header and first column on update data we need to
	// control if we already load them because pagination can occur
	private boolean mFirstUpdate = true;
	// Grid helpers
	private final GridHelper mHelper;
	private final Context mContext;
	private final Coordinator mCoordinator;
	private GridAdapter mAdapter;
	// Grid control definition, some definition is from metadata and some come
	// on update data.
	private final ScheduleGridDefinition mDefinition;
	private ThemeClassDefinition mThemeClass;
	private Rect mVisibleRect = new Rect();
	// ViewHolder for each data item
	private final ArrayList<ViewHolder> mViews = new ArrayList<>();
	// Recyclable views by view type and dimensions.
	private final ViewRecycler<RecyclerKey, GxLinearLayout> mRecycleBin = new ViewRecycler<>();

	private Size mSize;
	private boolean mDataArrived;
	private boolean mGlobalLayoutOccurred;
	private RowAndColumn mPendingScrollToRowAndColumn;

	public MatrixGrid(Context context, Coordinator coor, GridDefinition def) {
		super(context);
		mContext = context;
		mCoordinator = coor;
		mDefinition = new ScheduleGridDefinition(context, def);
		mHelper = new GridHelper(this, coor, def);
		mMatrixClass = new MatrixGridThemeClass(null);

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.grid, this, true);
		init();
	}

	private void prepareAdapter()
	{
		if (mAdapter == null)
			mAdapter = new GridAdapter(getContext(), mHelper, mDefinition.getGrid());
	}

	private void init() {
		// The Layout :
		// frozenTableHeader | headerScrollView ( contentHeaderTable )
		// ---------------------------------------------------------------------------------------
		// frozenColumnView( frozenTable ) | contentScrollView ( contentTable )
		//

		// The frozen column
		frozenColumnView = findViewById(R.id.frozenColumn);
		frozenColumnView.setScrollViewListener(this);
		ViewCompat.setOverScrollMode(frozenColumnView, ViewCompat.OVER_SCROLL_NEVER);
		frozenTable = findViewById(R.id.frozenTable);
		frozenTableHeader = findViewById(R.id.frozenTableHeader);

		// The header
		headerScrollView = findViewById(R.id.contentTableHeaderHorizontalScrollView);
		headerScrollView.setScrollViewListener(this);
		ViewCompat.setOverScrollMode(headerScrollView, ViewCompat.OVER_SCROLL_NEVER);
		contentHeaderTable = findViewById(R.id.contentTableHeader);
		contentHeaderTable.setHorizontalScrollBarEnabled(false);

		// The content
		contentScrollView = findViewById(R.id.contentTableHorizontalScrollView);
		contentScrollView.setScrollViewListener(this);
		ViewCompat.setOverScrollMode(contentScrollView, ViewCompat.OVER_SCROLL_ALWAYS);
		contentScrollView.setHorizontalScrollBarEnabled(false); // Only show the scroll bar on the header table (so that there aren't two).
		contentTable = findViewById(R.id.contentTable);

		// do not use a background, so inherit from it parent.
		contentTable.setBackgroundColor(Color.TRANSPARENT);

	}

	/***
	 * Create the first column (frozen) by traversing the entities and creating
	 * a Cell with a text view inside for each item
	 *
	 * @param list
	 */
	protected void populateFrozenColumn(EntityList list)
	{
		TableLayout.LayoutParams parms = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

		int rowNumber = 0;
		for (Entity entity : list)
		{
			TableRow row = new TableRow(getContext());
			row.setLayoutParams(parms);
			View frozenCell = createMarginCell(entity, rowNumber);
			rowNumber++;
			row.addView(frozenCell);
			frozenTable.addView(row);
		}

		if (mMatrixClass.getYAxisTableClass() != null)
			ThemeUtils.setBackgroundBorderProperties(frozenTable, mMatrixClass.getYAxisTableClass(), BackgroundOptions.DEFAULT);

		frozenTableHeader.setMinimumWidth(mDefinition.getYAxisWidth());
	}

	private int mStartSelection;

	private View createMarginCell(Entity entity, int rowNumber)
	{
		int marginCellWidth = mDefinition.getYAxisWidth();
		int rowHeight = mDefinition.getCellHeight();

		GxLinearLayout frozenCell = new GxLinearLayout(getContext());
		frozenCell.setOrientation(LinearLayout.VERTICAL);

		// Add Title Label
		GxTextView title = new GxTextView(getContext(), (LayoutItemDefinition) null);
		title.setText(entity.optStringProperty(mDefinition.getYValuesFieldTitle()));
		title.applyClass(mMatrixClass.getYAxisTitleLabelClass());
		title.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		// Add Description Label
		GxTextView desc = new GxTextView(getContext(), (LayoutItemDefinition) null);
		desc.setText(entity.optStringProperty(mDefinition.getYValuesFieldDescription()));
		desc.applyClass(mMatrixClass.getYAxisDescriptionLabelClass());
		desc.setPadding(5, 0, 0, 0);
		desc.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		frozenCell.addView(title);
		frozenCell.addView(desc);

		// Add the entire row for this frozen cell
		GxLinearLayout row = new GxLinearLayout(getContext());
		row.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		ThemeClassDefinition cls = (rowNumber % 2 == 0) ? mMatrixClass.getRowTableEvenClass() : mMatrixClass.getRowTableOddClass();

		if (mDefinition.isSelectedRow(rowNumber))
		{
			cls = mMatrixClass.getSelectedRowClass();
			rowHeight = mDefinition.getSelectedRowHeight();
		}

		int rowOffset = 0;
		if (mDefinition.hasSelectedRow() && mDefinition.getSelectedRowIndex() < rowNumber)
			rowOffset = mDefinition.getSelectedRowExtraHeight();

		row.applyClass(cls);
		contentTable.addViewInLayout(row,
			new AbsoluteLayout.LayoutParams(mDefinition.getTotalContentWidth(),
						rowHeight,
						0,
						(rowNumber * mDefinition.getCellHeight()) + rowOffset));

		frozenCell.setVerticalGravity(Gravity.CENTER_VERTICAL);
		frozenCell.setMinimumWidth(marginCellWidth);
		frozenCell.setMinimumHeight(rowHeight);
		frozenCell.setLayoutParams(new TableRow.LayoutParams(marginCellWidth, rowHeight));
		frozenCell.applyClass(cls);

		return frozenCell;
	}

	/***
	 * Create the header (frozen) by traversing the entities and creating a Cell
	 * with a text view inside for each item
	 *
	 * @param list
	 */
	protected void populateHeader(EntityList list)
	{
		TableLayout.LayoutParams parms = new TableLayout.LayoutParams(
				mDefinition.getCellWidth(),
				TableLayout.LayoutParams.WRAP_CONTENT);

		parms.setMargins(1, 1, 1, 1);
		parms.weight = 1;
		TableRow row = new TableRow(getContext());
		row.setLayoutParams(parms);

		for (Entity entity : list)
		{
			View frozenCell = createHeaderCell(entity);
			TableRow.LayoutParams cellparms = new TableRow.LayoutParams(mDefinition.getCellWidth(), mDefinition.getXAxisHeight()); // set height as Params instead of setMinimumHeight because it works with lower numbers
			row.addView(frozenCell, cellparms);
		}

		if (mMatrixClass.getXAxisTableClass() != null)
			ThemeUtils.setBackgroundBorderProperties(contentHeaderTable, mMatrixClass.getXAxisTableClass(), BackgroundOptions.DEFAULT);

		contentHeaderTable.addView(row);
	}

	private View createHeaderCell(Entity entity)
	{
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);

		GxTextView title = new GxTextView(getContext(), (LayoutItemDefinition) null);
		if (Strings.hasValue(mDefinition.getXValuesFieldTitle()))
			title.setText(entity.optStringProperty(mDefinition.getXValuesFieldTitle()));

		title.setPadding(5, 0, 5, 0);
		title.applyClass(mMatrixClass.getXAxisLabelClass());

		GxTextView description = new GxTextView(getContext(), (LayoutItemDefinition) null);
		if (Strings.hasValue(mDefinition.getXValuesFieldDescription()))
			description.setText(entity.optStringProperty(mDefinition.getXValuesFieldDescription()));

		description.setPadding(5, 0, 5, 0);
		description.applyClass(mMatrixClass.getXAxisLabelClass());

		layout.addView(title);
		layout.addView(description);
		return layout;
	}

	/***
	 * The site give the control the actual size. Save for further use later.
	 */
	@Override
	public void setAbsoluteSize(Size size)
	{
		mSize = size;
	}

	/***
	 * Scroll between header, column and content must be in sync, so scroll
	 * programatically: manual scroll on header &gt; programatically on content
	 * manual scroll on frozencolumn &gt; programatically on content manual scroll
	 * on content &gt; programatically on header and frozencolumn
	 */
	@Override
	public void onScrollChanged(View scrollView, int x, int y, int oldX, int oldY)
	{
		if (scrollView == headerScrollView && !mForcingScroll)
		{
			mForcingScroll = true;
			contentScrollView.scrollTo(x, contentScrollView.getScrollY());
			mForcingScroll = false;

			updateVisibleViews();
		}

		if (scrollView == frozenColumnView && !mForcingScroll)
		{
			mForcingScroll = true;
			contentScrollView.scrollTo(contentScrollView.getScrollX(), y);
			mForcingScroll = false;

			updateVisibleViews();
		}

		if (scrollView == contentScrollView && !mForcingScroll)
		{
			mForcingScroll = true;
			if (x != oldX)
				headerScrollView.scrollTo(x, headerScrollView.getScrollY());
			if (y != oldY)
				frozenColumnView.scrollTo(frozenColumnView.getScrollX(), y);
			mForcingScroll = false;

			updateVisibleViews();
		}
	}

	/***
	 * Traverse all rectangles from the data and intersects with the visible
	 * area of the content.
	 *
	 */
	private void updateVisibleViews()
	{
		if (contentTable.getLocalVisibleRect(mVisibleRect))
		{
			// Take a bigger rectangle so that we have more intersections with the data rects so
			// we are adding views in advance because will have more hits with the given rect.
			mVisibleRect.left -= 30;
			mVisibleRect.right += 30;
			mVisibleRect.top -= 30;
			mVisibleRect.bottom += 30;
			// Traverse the rects using the ViewHolder, if the View for the holder is null it means is not present
			// on the visible area.
			for (int i = 0; i < mViews.size(); i++)
			{
				ViewHolder holder = mViews.get(i);
				Rect f = holder.getRect();
				if (mVisibleRect.intersects(f.left, f.top, f.right, f.bottom))
				{
					// The rect should be visible, if not created yet try to add
					// using addView
					if (holder.getView() == null)
						addView(i, holder);
				}
				else
				{
					// The view is not visible anymore, so we can reuse it in another view.
					removeView(holder);
				}
			}
		}
	}

	@Override
	public void addListener(GridEventsListener listener)
	{
		mHelper.setListener(listener);
	}

	@Override
	public void update(final ViewData data)
	{
		// If it is the first data set, we need to create Header and first
		// column definition based on data
		// We should do this in other event
		if (mFirstUpdate)
		{
			mDefinition.updateSize(mCoordinator, mSize);
			contentTable.setMinimumHeight(mDefinition.getTotalContentHeight());
			contentTable.setMinimumWidth(mDefinition.getTotalContentWidth());

			// GridLines lines = new GridLines(mDefinition.getCellHeight(), mDefinition.getCellWidth(), mDefinition.getRowCount(), mDefinition.getColumnCount());
			// contentTable.setBackgroundDrawable(lines);

			// cannot populate data without X and Y definition
			if (mDefinition.getXAxis()==null || mDefinition.getYAxis() ==null ) {
				Services.Log.error("Cannot populate Matrix Grid , do not have X And Y axis data");
				return;
			}

			populateHeader(mDefinition.getXAxis());
			populateFrozenColumn(mDefinition.getYAxis());

			mFirstUpdate = false;
		}

		// Create ViewHolders for each data item.
		prepareAdapter();
		mAdapter.setData(data);
		populateData(data);

		mDataArrived = true;

		ThemeClassDefinition selectedClassDef = mMatrixClass.getSelectedCellClass();
		Bitmap bitmapSelector = null;
		if (selectedClassDef != null)
		{
			Drawable draw = Services.Images.getStaticImage(mContext,
					selectedClassDef.getBackgroundImage(), true);
			if (draw instanceof BitmapDrawable)
				bitmapSelector = ((BitmapDrawable) draw).getBitmap();
		}

		if (bitmapSelector == null)
			bitmapSelector = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bgrow);

		if (mDefinition.hasSelectedRow())
		{
			contentScrollView.setIndicatorBitmap(bitmapSelector);
			contentScrollView.setSelectedRow(mStartSelection, mDefinition.getSelectedRowHeight());
		}
		else
			contentScrollView.setIndicatorBitmap(null);

		// Add only visible views to the content area. If the update is before the layout then visible rect
		// is going to be empty so we subscribe to do the updatevisibleviews on layout ready.
		if (!mGlobalLayoutOccurred)
		{
			getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
			{
				@Override
				public void onGlobalLayout()
				{
					MatrixGrid.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					updateVisibleAreaAfterDataArrived(data);
					mGlobalLayoutOccurred = true;
				}
			});
		}
		else
			updateVisibleAreaAfterDataArrived(data);
	}

	private void updateVisibleAreaAfterDataArrived(ViewData data)
	{
		if (mPendingScrollToRowAndColumn != null)
		{
			scrollToRowAndColumn(mPendingScrollToRowAndColumn);
			mPendingScrollToRowAndColumn = null;
		}
		else
		{
			// We only call this if we don't have a pending scroll because
			// scrollToRowAndColumn() indirectly updates the grid.
			updateVisibleViews();
		}

		if (data.isMoreAvailable())
			mHelper.requestMoreData();
	}

	/***
	 * Traverse data and save and create a Holder with its rect for each data
	 * item.
	 *
	 * @param data
	 */
	private void populateData(ViewData data)
	{
		if (mDefinition.hasSelectedRow())
			mStartSelection = (mDefinition.getSelectedRowIndex() * mDefinition.getCellHeight());

		int position = 0;
		for (Entity entitiy : data.getEntities())
		{
			// xFrom -> xTo
			float xFrom = mDefinition.getXDataValue(entitiy.optStringProperty(mDefinition.getXFromFieldName()));
			float xTo = xFrom;
			if (mDefinition.getXToFieldName() != null)
				xTo = mDefinition.getXDataValue(entitiy.optStringProperty(mDefinition.getXToFieldName()));

			// yFrom -> yTo
			float yFrom = mDefinition.getYDataValue(entitiy.optStringProperty(mDefinition.geYFromFieldName()));
			float yTo = yFrom;
			if (mDefinition.getYToFieldName() != null)
				yTo = mDefinition.getYDataValue(entitiy.optStringProperty(mDefinition.getYToFieldName()));

			// xFrom, xTo, yFrom, yTo to pixels using a resolver
			Rect rect = mDefinition.createCellRect(xFrom, xTo, yFrom, yTo, mStartSelection);
			if (mDefinition.hasSelectedRow() && rect.top == mStartSelection)
				entitiy.setIsSelected(true);

			// Save the holder, used for further intersection with the visible area when needed.
			ViewHolder holder;

			// Create a new one if we didn't have enough.
			if (position >= mViews.size())
			{
				holder = new ViewHolder();
				mViews.add(holder);
			}
			else
			{
				holder = mViews.get(position);
				removeView(holder);
			}

			holder.setTypeAndRect(mAdapter.getItemViewType(position), rect);
			position++;
		}

		// Clear extra holders (e.g. after doing a refresh operation).
		while (position < mViews.size())
			mViews.remove(position);
	}

	private void removeView(ViewHolder holder)
	{
		if (holder.getView() != null)
		{
			// The view is not visible anymore, so we can reuse it in another view.
			// However, since it may come into the visible again without having been reused yet,
			// also mark it as INVISIBLE.
			mHelper.discardItemView(holder.getView().getChildAt(0));
			mRecycleBin.put(holder.getRecyclerKey(), holder.getView());
			holder.getView().setVisibility(INVISIBLE);
			holder.setView(null);
		}
	}

	/***
	 * Add View to the visible area, this can be a new View or just set a new
	 * position for a recycled view
	 *
	 * @param index
	 * @param holder
	 */
	private void addView(int index, ViewHolder holder)
	{
		// set the size for the new view
		Rect rect = holder.getRect();
		mAdapter.setBounds(rect.width(), rect.height());

		// try to get a reusable view
		View previousView = null;
		GxLinearLayout previousContainer = mRecycleBin.get(holder.getRecyclerKey());
		if (previousContainer != null)
		{
			previousView = previousContainer.getChildAt(0);
			previousContainer.setVisibility(VISIBLE);
		}

		Entity item = mAdapter.getEntity(index);

		// ask the adapter for a new view or existing view
		View itemView = mAdapter.getView(index, previousView, null);
		itemView.setOnClickListener(mOnItemClickListener);

		GxLinearLayout itemContainer;
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(rect.width(), rect.height(), rect.left, rect.top);

		if (previousView == null)
		{
			// A new item, without reusing anything (this also means previousContainer == null)
			itemContainer = new GxLinearLayout(getContext());
			itemContainer.addView(itemView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			// Add the new container to the grid.
			contentTable.addViewInLayout(itemContainer, params);
		}
		else
		{
			// Attempted to reuse. This also means we already had a previousContainer (that can
			// always be reused, even if the item itself cannot). Move it to its new position.
			itemContainer = previousContainer;
			itemContainer.setLayoutParams(params);

			if (previousView != itemView)
			{
				// The itemView itself was not reused. Discard the old one and put the new one inside the container.
				itemContainer.removeAllViews();
				itemContainer.addView(itemView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				previousView.setOnClickListener(null);
			}
		}

		itemContainer.applyClass(item.isSelected() ? mMatrixClass.getSelectedCellClass() : mMatrixClass.getCellClass());

		// ensure we know it's a visible view by setting to the holder the associated view
		holder.setView(itemContainer);
	}

	// Handle default action on each data item
	private final OnClickListener mOnItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			GridItemLayout item = (GridItemLayout) v;
			Entity entity = item.getEntity();

			mHelper.runDefaultAction(entity, new Anchor(v));
		}
	};
	private MatrixGridThemeClass mMatrixClass;

	// IGxThemeable Implementation
	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		mThemeClass = themeClass;
		mMatrixClass = new MatrixGridThemeClass(themeClass);
		applyClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemeClass;
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		mHelper.setThemeClass(themeClass);
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	private static class RowAndColumn
	{
		public final int row;
		public final int column;

		public RowAndColumn(int r, int c)
		{
			row = r;
			column = c;
		}
	}

	@Override
	public Expression.Value callMethod(String name, List<Expression.Value> parameters) {
		final String METHOD_SCROLL_TO = "ScrollToCoordinates";
		if (name.equalsIgnoreCase(METHOD_SCROLL_TO) && parameters.size() >= 2) {
			int row = Services.Strings.tryParseInt(parameters.get(1).coerceToString(), 0);
			int column = Services.Strings.tryParseInt(parameters.get(0).coerceToString(), 0);
			scrollToRowAndColumn(new RowAndColumn(row, column));
		}

		return null;
	}

	private void scrollToRowAndColumn(RowAndColumn rowCol)
	{
		if (!mDataArrived)
		{
			mPendingScrollToRowAndColumn = rowCol;
			return;
		}

		// Adjust gx 1-based indexes to our 0-based.
		rowCol = new RowAndColumn(rowCol.row - 1, rowCol.column - 1);

		int x = (rowCol.column * mDefinition.getCellWidth());
		int y = (rowCol.row * mDefinition.getCellHeight());

		if (mDefinition.hasSelectedRow() && rowCol.row > mDefinition.getSelectedRowIndex()) // Offset if the selected row is previous to this one.
			y += mDefinition.getSelectedRowExtraHeight();

		x = MathUtils.constrain(x, 0, mDefinition.getTotalContentWidth());
		y = MathUtils.constrain(y, 0, mDefinition.getTotalContentHeight());

		headerScrollView.scrollTo(x, 0);
		frozenColumnView.scrollTo(0, y);
	}
}
