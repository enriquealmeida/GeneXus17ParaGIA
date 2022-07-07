package com.genexus.android.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.genexus.android.R;
import com.genexus.android.ViewHierarchyVisitor;
import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.layout.CellDefinition;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.RowDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MultiMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.GxHorizontalSeparator;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.controls.ThemedOverrideViewHelper;
import com.genexus.android.core.fragments.GridContainer;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;

public class GxLayout extends ViewGroup implements IGxLayout
{
	/***
	 *  The TableDefinition for this Layout, this is set on constructor or using setTags function
	 */
	private TableDefinition mTable;
	/***
	 * Coordinator know all associated controls and events for this layout.
	 */
	private Coordinator mCoordinator;
	/***
	 * Map with the views for each horizontal line. This map is used when show horizontal lines = true in a GeneXus theme
	 */
	private final HashMap<RowDefinition, View> mHorizontalLines = new LinkedHashMap<>();

	private boolean mAlignFieldLabelsRequested;
	private ThemedOverrideViewHelper mThemedHelper;

	private boolean mIsInRTLLayout;

	public GxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mThemedHelper = new ThemedOverrideViewHelper(this);
		initGxLayout();
	}

	public GxLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mThemedHelper = new ThemedOverrideViewHelper(this);
		initGxLayout();
	}

	public GxLayout(Context context, TableDefinition table, Coordinator coordinator)
	{
		super(context);
		mTable = table;
		mCoordinator = coordinator;
		mThemedHelper = new ThemedOverrideViewHelper(this, mTable);
		setTag(table.getName());
		initGxLayout();
	}

	private void initGxLayout()
	{
		mIsInRTLLayout = Services.Application.isRTLLanguage();
	}

	protected Coordinator getCoordinator()
	{
		return mCoordinator;
	}

	@Override
	public void setLayout(Coordinator coordinator, TableDefinition table)
	{
		mCoordinator = coordinator;
		mTable = table;
		mThemedHelper.setLayoutItem(table);
	}

	// Preallocated variables used by onMeasure().
	private final HashMap<Integer, Integer> mOnMeasureOffsets = new LinkedHashMap<>();
	private final SparseIntArray mOnMeasureHiddens = new SparseIntArray();
	private final Rect mOnMeasureHr = new Rect();
	private final Rect mOnMeasureFrame = new Rect();
	private final RowLayoutContext mRowContext = new RowLayoutContext();
	private final List<Integer> mOnMeasureKeys = new ArrayList<>();

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if(mTable != null && mTable.isCanvas() && Strings.starsWithIgnoreCase(mTable.getName(), "GxAutoMeasure"))
		{
			recalculateTableChildren(heightMeasureSpec);
		}

		if (mAlignFieldLabelsRequested)
			alignFieldLabels();

		// Find out how big every child wants to be
		measureChildrenBeforeLayout(widthMeasureSpec, heightMeasureSpec);

		if (mTable == null)
			Services.Log.debug("GxLayout with out definition");
		RowLayoutContext rowContext = mRowContext;
		mRowContext.clear();
		if (mTable != null)
		{
			// reset hidden space and autogrow space
			mOnMeasureOffsets.clear();
			mOnMeasureHiddens.clear();

			// traverse all rows calculating positions for each cell
			// taking into account visibility and autogrow properties
			for (RowDefinition row : mTable.Rows )
			{
				initializeRowContext(rowContext);
				layoutRow(row, rowContext);
				updateHiddenSpace(row, rowContext);
			}
			// Shift controls affected by autoHeight and consider hidden controls
			adjustSizes();
		}

		setDimensions(widthMeasureSpec, heightMeasureSpec);
	}

	private int mPreviousMeasureHeight;

	protected void recalculateTableChildren(int heightMeasureSpec)
	{
		//recalculate canvas childs.
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST)
		{
			int heightSize = MeasureSpec.getSize(heightMeasureSpec);
			// if heightSize > 0 recalculate childs, when resume from backgound previos is 0 and need to be triggered.
			if (heightSize > 0 /*&& mPreviousMeasureHeight != 0*/
					&& (heightSize != mPreviousMeasureHeight)
					)
			{
				//Services.Log.debug("onMeasure table recalculate : " + mTable.getName() + " HM: " + heightMode + " HS: " + heightSize
				//		+ " Previous: " + mPreviousMeasureHeight);

				if (mTable.getThemeClass() != null && !mTable.hasDipHeight())
				{
					// add margin only if size is not fixed (in percent) , because recalculateHeight remove it again.
					LayoutBoxMeasures margins = mTable.getThemeClass().getMargins();
					mTable.recalculateHeight(heightSize + margins.getTotalVertical());
				} else
				{
					mTable.recalculateHeight(heightSize);
				}

				// Force the newly calculated heights on all descendant controls.
				for (View descentant : ViewHierarchyVisitor.getViews(View.class, this))
				{
					if (descentant != this && descentant.getLayoutParams() instanceof LayoutParams)
						((LayoutParams) descentant.getLayoutParams()).applyCellDefinition();
				}

			}
			mPreviousMeasureHeight = heightSize;
		}
	}

	private void measureChildrenBeforeLayout(int parentWidthMeasureSpec, int parentHeightMeasureSpec)
	{
		final int size = getChildCount();
		for (int i = 0; i < size; i++)
		{
			View child = getChildAt(i);
			if (child.getVisibility() != View.GONE)
			{
				measureChildWithMargins(child, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, 0);
			}
		}
	}

	private void updateHiddenSpace(RowDefinition row,
			RowLayoutContext rowContext) {
		if (rowContext.MaxHeightHiddenInCurrentRow > 0 && rowContext.MaxHeightHiddenInCurrentRow > rowContext.MaxHeightVisibleInCurrentRow ) {
			rowContext.RowsHiddenHeightSum += Math.max(0, rowContext.MaxHeightHiddenInCurrentRow - rowContext.MaxHeightVisibleInCurrentRow);
		}
		mOnMeasureHiddens.put(row.getIndex() + 1, rowContext.RowsHiddenHeightSum);
	}

	private void initializeRowContext(RowLayoutContext rowContext) {
		rowContext.MaxHeightVisibleInCurrentRow = 0;
		rowContext.MaxHeightHiddenInCurrentRow = 0;
	}

	private int invisibleHeight() {
		int invisibleHeight = (mTable != null ? mOnMeasureHiddens.get(mTable.Rows.size()) : 0);

		// Special case: Do not shrink a main table that should occupy the whole screen.
		if (mTable != null && mTable.isMainTable() && mTable.getHeight() != null && mTable.getHeight().equals(DimensionValue.HUNDRED_PERCENT))
			invisibleHeight = 0;

		// Special case: Do not shrink a table with AutoGrow = false
		if (mTable != null && !mTable.hasAutoGrow())
			invisibleHeight = 0;

		return invisibleHeight;
	}

	private void setDimensions(int widthMeasureSpec, int heightMeasureSpec)
	{
		int maxHeight = 0;
		int maxWidth = 0;
		int childCount = getChildCount();

		// Find rightmost and bottom-most child
		for (int i = 0; i < childCount; i++)
		{
			View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();

			// Consider this child space if it is visible or if needs to keep space when invisible
			if (child.getVisibility() == VISIBLE || lp.cell.keepSpace)
			{
				int childRight;
				int childBottom;

				childRight = lp.x + Math.max(child.getMeasuredWidth(), lp.width) + lp.leftMargin + lp.rightMargin;
				childBottom = lp.y + Math.max(child.getMeasuredHeight(), lp.height) + lp.topMargin + lp.bottomMargin;

				maxWidth = Math.max(maxWidth, childRight);
				maxHeight = Math.max(maxHeight, childBottom);
			}
		}

		// Account for padding too
		maxWidth += getPaddingLeft() + getPaddingRight();
		maxHeight += getPaddingTop() + getPaddingBottom();

		// Check against minimum height and width (minimum height also accounts for collapsed views).
		maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight() - invisibleHeight());
		maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && mTable != null && mTable.getEnableHeaderRowPattern()) {
			// Check if it is inside a Tabbed Navigation
			ViewParent parent = getParent();
			while (parent != null &&
				!(parent instanceof ViewPager && ((ViewPager)parent).getId() == R.id.tab_navigation_viewpager))
			{
				parent = parent.getParent();
			}
			// Fill empty space under the navigation bar because we are drawing under system bars
			if (parent == null) {
				Insets insets = getRootWindowInsets().getInsets(WindowInsets.Type.navigationBars());
				maxHeight += insets.bottom + insets.top;
			}
		}

		setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
	}

	private void adjustSizes()
	{
		shiftOffsets();

		for (RowDefinition row : mTable.Rows)
		{
			CellMeasures lastCell = null;
			int maxCellHeight = 0;
			for (CellDefinition cell : row.Cells)
			{
				View control = findViewFromCell(cell);
				GxLayout.LayoutParams lp = getGxLayoutParams(control);
				if (lp != null) {
					lastCell = lp.cell;

					int previousHiddenSpace = mOnMeasureHiddens.get(lp.cell.row);

					lp.y = lp.cell.y + mOnMeasureOffsets.get(lp.cell.row) - previousHiddenSpace;

					//ignore controls with row span to calculate line position
					if (lp.cell.rowSpan == 1) {
						int height = Math.max(lp.cell.height, control.getMeasuredHeight());
						height = considerMargin(control, height);
						maxCellHeight = Math.max(maxCellHeight, height);
					}
				}
			}

			if (lastCell != null)
				setLinePosition(row, lastCell, maxCellHeight);
		}
	}

	private static GxLayout.LayoutParams getGxLayoutParams(View control) {
		if (control != null) {
			ViewGroup.LayoutParams params = control.getLayoutParams();
			if (!(params instanceof GxLayout.LayoutParams)) // It isn't GxLayout.LayoutParams when it is inside a scroll
				params = ((View) control.getParent()).getLayoutParams();
			if (params instanceof GxLayout.LayoutParams)
				return (GxLayout.LayoutParams) params;
		}
		return null;
	}

	private void setLinePosition(RowDefinition row, CellMeasures lastCell, int maxCellHeight)
	{
		if (lastCell == null)
			throw new IllegalArgumentException("lastCell must not be null");

		//set line position
		View control =  mHorizontalLines.get(row);
		int previousHiddenSpace = mOnMeasureHiddens.get(row.getIndex());

		Integer rowOffset = mOnMeasureOffsets.get(lastCell.row);
		if (control != null && rowOffset != null)
		{
			GxLayout.LayoutParams lp = (GxLayout.LayoutParams) control.getLayoutParams();

			lp.y = lastCell.y + rowOffset + maxCellHeight - previousHiddenSpace;
			if (lp.y > 0)
				lp.y = lp.y - 1;
		}
	}

	private int considerMargin(View control, int height) {
		// add margin for DataBoundControl
		if (control instanceof DataBoundControl)
		{
			DataBoundControl dbControl = (DataBoundControl)control;
			ThemeClassDefinition themeClass = dbControl.getDefinition().getThemeClass();
			if (themeClass!=null && themeClass.hasMarginSet())
			{
				LayoutBoxMeasures margins = themeClass.getMargins();
				height = height + margins.bottom;
			}
		}
		return height;
	}

	private void shiftOffsets()
	{
		// TODO: Optimize this method! Should be MUCH simpler if using a SparseIntArray for mOnMeasureOffsets.
		List<Integer> keys = mOnMeasureKeys;
		keys.clear();
		keys.addAll(mOnMeasureOffsets.keySet());
		Collections.sort(keys);

		int previous = 0;
		for (Integer key : keys) {
			Integer o1 = mOnMeasureOffsets.get(key);
			previous = previous + o1;
			mOnMeasureOffsets.put(key, previous);
		}

		// shift offset to next row
		previous = 0;
		for (Integer key : keys) {
			int current = mOnMeasureOffsets.get(key);
			if (current >= 0)
			{
				mOnMeasureOffsets.put(key, previous);
			}
			previous = current;
		}
	}

	private void layoutRow(RowDefinition row, RowLayoutContext rowContext) {
		Rect hr = mOnMeasureHr;
		hr.setEmpty();
		List<CellDefinition> vector = row.Cells;
		for (CellDefinition cell : vector) {
			View cellView = findViewFromCell(cell);
			GxLayout.LayoutParams lp = getGxLayoutParams(cellView);
			if (lp != null)
				layoutCell(cellView, lp.cell, hr, rowContext);
		}
	}

	private View findViewFromCell(CellDefinition cell)
	{
		if (cell != null && cell.getContent() != null)
		{
			View view = mCoordinator.getControl(cell.getContent().getName());
			if (view instanceof GxLayout && view.getParent() instanceof ScrollViewThemeable)
			{
				return (View)view.getParent();
			}
			else
			{
				return view;
			}
		}
		return null;
	}

	private void layoutCell(View control, CellMeasures cell, Rect hr, RowLayoutContext rowContext)
	{
		boolean isControlHiddenAndCollapseMode = (control.getVisibility() != VISIBLE && !cell.keepSpace);

		Rect frame = getDesignTimeFrame(cell);
		// ensure row in offsets dictionary
		if (!mOnMeasureOffsets.containsKey(cell.row))
			mOnMeasureOffsets.put(cell.row, 0);

		int frameHeight = frame.height();
		if (frame.top + frameHeight > hr.top)
			hr.top = frame.top + frameHeight;

		ViewGroup.LayoutParams parms = control.getLayoutParams();
		if (parms instanceof GxLayout.LayoutParams)
		{
			GxLayout.LayoutParams lp = (GxLayout.LayoutParams) control.getLayoutParams();

			// if in RTL layout mirror x with the value calculate in getDesignTimeFrame
			if (mIsInRTLLayout) {
				lp.x = frame.left;
			}

			int height = lp.cell.height;

			if (lp.cell.autoHeight && control instanceof DataBoundControl)
				lp.height = LayoutParams.WRAP_CONTENT;

			if (lp.height == LayoutParams.WRAP_CONTENT && !lp.cell.autoHeight)
				lp.height = height;

			// Calculate auto height event if the control is hidden because the adjustSizes method
			// will decrement the full size of hidden controls
			int controlMeasureHeight = addMarginToControlMeasure(control, control.getMeasuredHeight());
			if (lp.cell.autoHeight)
			{
				if (height < controlMeasureHeight)
				{
					Integer add = controlMeasureHeight - height;
					Integer value = mOnMeasureOffsets.get(cell.row);
					if (add > value)
					{
						mOnMeasureOffsets.put(cell.row, add );
						frame.bottom = frame.bottom + add;
					}
				}
				else
				{
					if (lp.height != LayoutParams.WRAP_CONTENT )
					{
						// If not wrap context , set current size.
						// we set setMinimumHeight to all controls so only set height ig now wrap_content
						lp.height = height;
					}
				}
			}

			if (isControlHiddenAndCollapseMode)
			{
				if (cell.rowSpan == 1)
					rowContext.MaxHeightHiddenInCurrentRow = Math.max(rowContext.MaxHeightHiddenInCurrentRow, Math.max(height, frame.height()));
				else
					rowContext.MaxHeightHiddenInCurrentRow = Math.max(rowContext.MaxHeightHiddenInCurrentRow, frame.height());
			}
			else if (control instanceof GxLayout && controlMeasureHeight < height)
			{
				// if the height set for the row is bigger than the measured height, calculate how much of that is hidden to move the following rows up
				rowContext.MaxHeightHiddenInCurrentRow = Math.min(height - controlMeasureHeight, ((GxLayout)control).invisibleHeight());
				if (rowContext.MaxHeightHiddenInCurrentRow > 0)
					return; // skip MaxHeightVisibleInCurrentRow assignment so this one counts
			}
		}

		if (!isControlHiddenAndCollapseMode)
			rowContext.MaxHeightVisibleInCurrentRow = Math.max(rowContext.MaxHeightVisibleInCurrentRow, (parms.height > 0)? parms.height : control.getMeasuredHeight());
	}

	private int addMarginToControlMeasure(View control, int controlHeight) {
		// add margin to control height
		LayoutParams params = (LayoutParams)control.getLayoutParams();
		controlHeight = controlHeight + params.topMargin + params.bottomMargin;
		return controlHeight;
	}

	private Rect getDesignTimeFrame(CellMeasures cell)
	{
		Rect frame = mOnMeasureFrame;
		frame.setEmpty();
		// offset x & y with padding?
		frame.left = cell.x;
		frame.top = cell.y;
		frame.bottom = frame.top + cell.height;

		// If application is RTL, mirror table
		// change the left value with = total table size minus position and size of cell.
		if (mIsInRTLLayout) {
			frame.left = mTable.getDesiredWidth(null) - cell.x - cell.width;
		}
		return frame;
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
	}

	@SuppressLint("RtlHardcoded") // GravityCompat.getAbsoluteGravity() is "fixing" RTL before the switch statement.
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			View child = getChildAt(i);
			if (child.getVisibility() != GONE)
			{
				// Find the exact position for the view according to the cell gravity,
				// the parent's padding, and the view's margins.
				GxLayout.LayoutParams lp = (GxLayout.LayoutParams) child.getLayoutParams();
				int cellGravity = GravityCompat.getAbsoluteGravity(lp.cellGravity, ViewCompat.getLayoutDirection(child));
				int horizontalSpace = lp.cell.width;
				int verticalSpace = lp.cell.height;

				int childWidth = child.getMeasuredWidth();
				int childHeight = child.getMeasuredHeight();

				int horizontalShift;
				switch (cellGravity & Gravity.HORIZONTAL_GRAVITY_MASK)
				{
					case Gravity.CENTER_HORIZONTAL:
						horizontalShift = (horizontalSpace + lp.leftMargin - lp.rightMargin - childWidth) / 2;
						break;

					case Gravity.RIGHT:
						horizontalShift = horizontalSpace - childWidth - lp.rightMargin;
						break;

					case Gravity.LEFT:
					default:
						horizontalShift = lp.leftMargin;
						break;
				}

				int verticalShift;
				boolean cellHasGrown = lp.cell.autoHeight && (childHeight + lp.topMargin + lp.bottomMargin > verticalSpace);
				if (!cellHasGrown)
				{
					switch (cellGravity & Gravity.VERTICAL_GRAVITY_MASK)
					{
						case Gravity.CENTER_VERTICAL:
							verticalShift = (verticalSpace + lp.topMargin - lp.bottomMargin - childHeight) / 2;
							break;

						case Gravity.BOTTOM:
							verticalShift = verticalSpace - childHeight - lp.bottomMargin;
							break;

						case Gravity.TOP:
						default:
							verticalShift = lp.topMargin;
							break;
					}
				}
				else
				{
					// The view had grown and its cell has also grown to accommodate it.
					// Therefore, just shifting vertically to account for margins is enough.
					verticalShift = lp.topMargin;
				}

				int childLeft = getPaddingLeft() + lp.x + horizontalShift;
				// application is RTL, mirror table. x position minus Right Padding (mirror of Left padding)
				if (mIsInRTLLayout)
					childLeft = lp.x - getPaddingRight() + horizontalShift;
				
				int childTop = getPaddingTop() + lp.y + verticalShift;

				child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
			}
		}
	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new GxLayout.LayoutParams(getContext(), attrs);
	}

	// Override to allow type-checking of LayoutParams.
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof GxLayout.LayoutParams;
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new GxLayout.LayoutParams(p);
	}

	public ThemeClassDefinition getRowThemeClass(ThemeClassDefinition rowClass) {
		if (mTable != null)
			return mTable.getThemeClass(rowClass == null ? null : rowClass.getName());
		else
			return null;
	}

	public static class LayoutParams extends ViewGroup.MarginLayoutParams
	{
		public int x;
		public int y;
		public CellMeasures cell;
		private CellDefinition mLayoutCell;

		/**
		 * Gravity for the view associated with these LayoutParams.
		 * Note that this is the gravity of the CELL that contains this view in the GX layout.
		 * In other words, this will only have an effect if the view is smaller than its containing cell.
		 *
		 * @see android.view.Gravity
		 */
		public int cellGravity = Gravity.NO_GRAVITY;

		public LayoutParams(int width, int height, int x, int y)
		{
			super(width, height);
			this.x = x;
			this.y = y;
			cell = new CellMeasures();
		}

		public LayoutParams(Context c, AttributeSet attrs)
		{
			super(c, attrs);
			x = 0;
			y = 0;
			cell = new CellMeasures();
		}

		public LayoutParams(ViewGroup.LayoutParams source)
		{
			super(source);
			cell = new CellMeasures();
		}

		public LayoutParams(CellDefinition layoutCell, LayoutItemDefinition item, View view)
		{
			super(layoutCell.getAbsoluteWidth(), layoutCell.getAbsoluteHeight());

			mLayoutCell = layoutCell;
			cellGravity = item.getCellGravity();
			applyCellDefinition();
		}

		public void applyCellDefinition()
		{
			if (mLayoutCell != null)
			{
				boolean wasAutoHeight = (cell != null && cell.autoHeight);

				cell = new CellMeasures(mLayoutCell);
				width = mLayoutCell.getAbsoluteWidth();

				if (wasAutoHeight)
				{
					height = LayoutParams.WRAP_CONTENT;
					cell.autoHeight = true;
				}
				else
					height = mLayoutCell.getAbsoluteHeight();

				x = cell.x;
				y = cell.y;
			}
		}

		public CellDefinition getCellDefinition() {
			return mLayoutCell;
		}
	}

	public static class CellMeasures
	{
		public final int width;
		public final int height;
		public final int x;
		public final int y;
		public final boolean keepSpace;
		public final int row;
		public final int rowSpan;
		public boolean autoHeight;

		public CellMeasures()
		{
			width = 0;
			height = 0;
			x = 0;
			y = 0;
			row = 0;
			rowSpan = 1;
			keepSpace = true;
		}

		public CellMeasures(CellDefinition cell)
		{
			width = cell.getAbsoluteWidth();
			height = cell.getAbsoluteHeight();
			x = cell.getAbsoluteX();
			y = cell.getAbsoluteY();
			row = cell.getRow();
			rowSpan = cell.getRowSpan();
			keepSpace = (cell.getContent() != null && cell.getContent().getKeepSpace());
		}
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		mThemedHelper.setThemeClass(themeClass);
		applyControlClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemedHelper.getThemeClass();
	}

	@Override
	public void setOverride(String propertyName, String propertyValue) {
		mThemedHelper.setOverride(propertyName, propertyValue);
	}

	@Override
	public ThemeOverrideProperties getThemeOverrideProperties() {
		return mThemedHelper.getThemeOverrideProperties();
	}

	@Override
	public void updateHorizontalSeparators(GxHorizontalSeparator separator)
	{
		// Add separator between rows (i.e. after all rows but the last one).
		int rowCountWithSeparators = mTable.Rows.size() - 1;

		for (int i = 0; i < rowCountWithSeparators; i++)
		{
			RowDefinition row = mTable.Rows.get(i);

			// Remove previous separator, if any.
			View oldSeparator = mHorizontalLines.get(row);
			if (oldSeparator != null) {
				removeView(oldSeparator);
				mHorizontalLines.remove(row);
			}

			// Add new separator, if applicable.
			if (separator.isVisible())
			{
				View separatorView;
				if (!separator.isDefault())
				{
					ImageView imgView = new ImageView(getContext());
					imgView.setImageDrawable(separator.getDrawable());
					imgView.setScaleType(ScaleType.FIT_XY);
					separatorView = imgView;
				}
				else
					separatorView = LayoutInflater.from(getContext()).inflate(R.layout.listdivider, null);

				separatorView.setLayoutParams(new GxLayout.LayoutParams(LayoutParams.MATCH_PARENT, separator.getHeight(), 0, row.getEndY()));
				separatorView.setTag(row);
				mHorizontalLines.put(row, separatorView);
				addView(separatorView);
			}
		}
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		mThemedHelper.applyClass(themeClass);
		applyControlClass(themeClass);
	}

	private void applyControlClass(ThemeClassDefinition themeClass) {
		updateHorizontalSeparators(new GxHorizontalSeparator(getContext(), mTable, themeClass));
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params)
	{
		super.setLayoutParams(params);
		mThemedHelper.setLayoutParams(params);
	}

	private void alignFieldLabels()
	{
		MultiMap<Integer, DataBoundControl> fieldsPerColumn = new MultiMap<>();
		for (int i = 0; i < getChildCount(); i++)
		{
			DataBoundControl field = Cast.as(DataBoundControl.class, getChildAt(i));
			if (field != null)
			{
				LayoutItemDefinition layoutItem = field.getDefinition();
				CellDefinition cell = Cast.as(CellDefinition.class, layoutItem.getParent());

				// Group the fields to be aligned together based on their design-time y-coordinate.
				if (cell != null && layoutItem.getLabelPosition().equals(Properties.LabelPositionType.LEFT))
					fieldsPerColumn.put(cell.getAbsoluteX(), field);
			}
		}

		// Groups of fields have been collected. Align every group with at least 2 elements.
		for (Collection<DataBoundControl> fields : fieldsPerColumn.valueGroups())
		{
			if (fields.size() <= 1)
				continue;

			// Measure every label's width to get maximum.
			int maxLabelWidth = 0;
			ArrayList<GxTextView> columnLabels = new ArrayList<>();
			for (DataBoundControl field : fields)
			{
				GxTextView label = field.getLabel();
				if (label != null && label.getText() != null)
				{
					int labelWidth = (int)label.getPaint().measureText(label.getText().toString());
					labelWidth += label.getTotalPaddingLeft() + label.getTotalPaddingRight();

					columnLabels.add(label);
					maxLabelWidth = Math.max(maxLabelWidth, labelWidth);
				}
			}

			// Apply calculated size to labels. Edits fill remaining space, so this should align them.
			if (maxLabelWidth != 0)
			{
				for (GxTextView label : columnLabels)
					label.setWidth(maxLabelWidth);
			}
		}

		mAlignFieldLabelsRequested = false;
	}

	@Override
	public void requestAlignFieldLabels()
	{
		// Just flag to do it on the next measure/layout pass. That way we can perform the
		// calculations only once if we set the caption for multiple fields in the same event
		// (remember that SetControlPropertyAction has thread preference = main).
		mAlignFieldLabelsRequested = true;
		requestLayout();
	}

	@Override
	public void addChild(View view) {
		addView(view);
	}

	@Override
	public void setChildLayoutParams(CellDefinition cell, LayoutItemDefinition item, View view) {
		GxLayout.LayoutParams lp = new GxLayout.LayoutParams(cell, item, view);
		view.setLayoutParams(lp);
	}

	@Override
	public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
		super.updateViewLayout(view, params);
		IGxThemeable themeable = Cast.as(IGxThemeable.class, view);
		GxLayout.LayoutParams lp = Cast.as(GxLayout.LayoutParams.class, view.getLayoutParams());
		if (themeable != null && lp != null && lp.getCellDefinition() != null && lp.getCellDefinition().getContent() != null)
			updateChildLayoutParams(view, lp, lp.getCellDefinition().getContent(), themeable.getThemeClass());
	}

	@Override
	public void updateChildLayoutParams(CellDefinition cell, LayoutItemDefinition item, View view) {
		GxLayout.LayoutParams lp = Cast.as(GxLayout.LayoutParams.class, view.getLayoutParams());
		if (lp != null)
			updateChildLayoutParams(view, lp, item, item.getThemeClass());
		else
			Services.Log.warning("updateChildLayoutParams view lp are not GxLayout.LayoutParams " + view.getLayoutParams().toString());
	}

	private void updateChildLayoutParams(View view, GxLayout.LayoutParams lp, LayoutItemDefinition item, ThemeClassDefinition themeClass) {
		// Adjust layoutparams according to autogrow and other things.
		lp.width = item.getDesiredWidth(themeClass); // Margin already added when theme was applied

		// TODO: Some of this stuff should be moved to GxLayout.onMeasure()
		if (item.hasAutoGrow()) {
			lp.cell.autoHeight = true;

			if (!(view instanceof GridContainer)) {
				lp.height = GxLayout.LayoutParams.WRAP_CONTENT;
				view.setMinimumHeight(item.getDesiredHeight(themeClass)); // Margin already added when theme was applied
			}

			// if grid container and grid support autogrow (ie horizontal grid).
			if ((view instanceof GridContainer) &&
					(item instanceof GridDefinition))
			{
				GridDefinition myGrid = (GridDefinition) item;
				if (myGrid.gridUserControlSupportAutoGrow()) {
					if (myGrid.hasPullToRefresh() && !myGrid.hasInverseLoad()) {
						// SwipeRefreshLayout doesn't work with WRAP_CONTENT,
						// onMeasure() is called with heightMeasureSpec = 0, and it calls measure with 0 and MeasureSpec.EXACTLY
						lp.height = item.getDesiredHeight(themeClass); // This prevents the grid ending with height = 0 but AutoGrow will still not work
					} else {
						lp.height = GxLayout.LayoutParams.WRAP_CONTENT;
						view.setMinimumHeight(item.getDesiredHeight(themeClass));  // Margin already added when theme was applied
					}
				}
			}
		} else {
			lp.height = item.getDesiredHeight(themeClass); // Margin already added when theme was applied
		}
	}

	@Override
	public void updateSelfLayoutParams() { }
}
