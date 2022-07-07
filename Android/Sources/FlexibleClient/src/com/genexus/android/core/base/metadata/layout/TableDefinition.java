package com.genexus.android.core.base.metadata.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;
import com.genexus.android.core.base.utils.Strings;

public class TableDefinition extends LayoutItemDefinition implements ILayoutContainer
{
	private static final long serialVersionUID = 1L;

	// Metadata Values
	private DimensionValue mWidth;
	private DimensionValue mHeight;
	private int mFixedWidthCellSum;
	private int mFixedHeightCellSum;

	// Real Size
	private float mAbsoluteHeightForTable;
	private float mAbsoluteWidthForTable;
	private float mAbsoluteHeight;
	private float mAbsoluteWidth;

	@SuppressWarnings("checkstyle:MemberName")
	public List<RowDefinition> Rows = new ArrayList<>();
	private List<CellDefinition> mCells;
	private String mBackground;
	private String mRowThemeClass;
	private String mOverflowBehavior;

	// Flex
	private String mFlexDirection;
	private String mFlexWrap;
	private String mJustifyContent;
	private String mAlignItems;
	private String mAlignContent;
	private boolean mAdjustContentSize;

	private LayoutItemLookup mItemLookup;

	public TableDefinition(LayoutDefinition parent, LayoutItemDefinition itemParent)
	{
		super(parent, itemParent);
	}

	@Override
	public TableDefinition getContent() {
		return this;
	}

	public boolean isMainTable()
	{
		return (getLayout() != null && getLayout().getTable() == this);
	}

	private List<CellDefinition> getCells()
	{
		if (mCells == null)
		{
			mCells = new ArrayList<>();
			for (RowDefinition row : Rows)
				mCells.addAll(row.Cells);
		}

		return mCells;
	}

	@Override
	public void readData(INodeObject tableNode)
	{
		super.readData(tableNode);

		String width = tableNode.optString("@width");
		String height = tableNode.optString("@height");
		String fixedHeight = tableNode.optString("@FixedHeightSum");
		String fixedWidth = tableNode.optString("@FixedWidthSum");

		mWidth = DimensionValue.parse(width);
		mHeight = DimensionValue.parse(height);

		// Just to be safe, in case parsing fails.
		if (mWidth == null)
			mWidth = DimensionValue.HUNDRED_PERCENT;
		if (mHeight == null)
			mHeight = DimensionValue.HUNDRED_PERCENT;

		mFixedHeightCellSum = 0;
		if (fixedHeight.length() > 0)
			mFixedHeightCellSum = Services.Device.dipsToPixels((int) Float.parseFloat(fixedHeight));

		mFixedWidthCellSum = 0;
		if (fixedWidth.length() > 0)
			mFixedWidthCellSum = Services.Device.dipsToPixels((int) Float.parseFloat(fixedWidth));

		mBackground = tableNode.optString("@background");
		mFlexDirection = tableNode.optString("@flexDirection");
		mFlexWrap = tableNode.optString("@flexWrap");
		mJustifyContent = tableNode.optString("@justifyContent");
		mAlignItems = tableNode.optString("@alignItems");
		mAlignContent = tableNode.optString("@alignContent");
		mAdjustContentSize = tableNode.optBoolean("@adjustContainerSize");

		String tableType = tableNode.optString("@tableType");
		boolean isCanvas = tableType.equalsIgnoreCase("Absolute");
		if(isCanvas && Strings.starsWithIgnoreCase(this.getName(), "GxAutoMeasure"))
		{
			// force autogrow for GxAutoMeasure canvas
			mIsAutogrow = true;
		}
		mOverflowBehavior = tableNode.optString("@overflowBehavior");
	}

	@Override
	public boolean addScrollView() {
		if (this.hasAutoGrow())
			return false; // It will grow with it's size, no scroll needed

		if (mOverflowBehavior.equals("Add Scroll") ||
			(mOverflowBehavior.equals("Platform Default") && Strings.starsWithIgnoreCase(this.getName(), "GxAddScroll"))) {
			return true; // To use scroll where the developer knows it will work
		}

		return false;
	}

	public void recalculateBounds(int reservedWidth)
	{
		if (mSuppliedSize != null)
		{
			Size newSize = mSuppliedSize.minusWidth(reservedWidth);
			calculateBoundsInternal(newSize.getWidth(), newSize.getHeight());
		}
		else
			Services.Log.warning("Cannot recalculate size of table because calculateBounds() hasn't been called yet.");
	}

	public void recalculateHeight(int availableHeight)
	{
		if (mSuppliedSize != null)
			calculateBoundsInternal(mSuppliedSize.getWidth(), availableHeight);
		else
			Services.Log.warning("Invalid call to recalculateHeight -- calculateBounds() hasn't been called yet.");

	}

	// Size last passed to calculateBounds().
	private transient Size mSuppliedSize;

	/***
	 * Calculate absolute bounds of this table
	 * @param widthReal Available width, in pixels.
	 * @param heightReal Available height, in pixels.
	 */
	@Override
	public void calculateBounds(float widthReal, float heightReal)
	{
		mSuppliedSize = new Size((int)widthReal, (int)heightReal);
		calculateBoundsInternal(widthReal, heightReal);
	}

	public void calculateBounds(float widthReal, float heightReal, ThemeClassDefinition themeClass)
	{
		mSuppliedSize = new Size((int)widthReal, (int)heightReal);
		calculateBoundsInternal(widthReal, heightReal, themeClass);
	}

	private void calculateBoundsInternal(float widthReal, float heightReal)
	{
		calculateBoundsInternal(widthReal, heightReal, getThemeClass());
	}

	private void calculateBoundsInternal(float widthReal, float heightReal, ThemeClassDefinition themeClass)
	{
		mAbsoluteWidth = DimensionValue.toPixels(mWidth, widthReal);
		float absoluteWidthAvailable = mAbsoluteWidth - mFixedWidthCellSum;

		mAbsoluteHeight = DimensionValue.toPixels(mHeight, heightReal);
		float absoluteHeightAvailable = mAbsoluteHeight - mFixedHeightCellSum;

		mAbsoluteHeightForTable = mAbsoluteHeight;
		mAbsoluteWidthForTable = mAbsoluteWidth;

		if (themeClass != null)
		{
			// subtract margings, border and padding.
			// subtract margin only if size is not fixed.
			LayoutBoxMeasures margins = themeClass.getMargins();
			if (mHeight.Type == DimensionValue.ValueType.PERCENT)
			{
				absoluteHeightAvailable -= margins.getTotalVertical();
				mAbsoluteHeightForTable -= margins.getTotalVertical();
			}

			if (mWidth.Type == DimensionValue.ValueType.PERCENT)
			{
				absoluteWidthAvailable -= margins.getTotalHorizontal();
				mAbsoluteWidthForTable -= margins.getTotalHorizontal();
			}

			// subtract padding and border, padding now include border.
			LayoutBoxMeasures padding = themeClass.getPadding();
			absoluteWidthAvailable = absoluteWidthAvailable - padding.getTotalHorizontal();
			absoluteHeightAvailable = absoluteHeightAvailable - padding.getTotalVertical();
		}

		if (absoluteWidthAvailable < 0)
			absoluteWidthAvailable = 0;

		if (absoluteHeightAvailable < 0)
			absoluteHeightAvailable = 0;

		for (CellDefinition cell : getCells())
			cell.calculateBounds(absoluteWidthAvailable, absoluteHeightAvailable);
	}

	public boolean hasDipHeight()
	{
		return (mHeight.Type == DimensionValue.ValueType.PIXELS);
	}

	public DimensionValue getWidth()
	{
		return mWidth;
	}

	public DimensionValue getHeight()
	{
		return mHeight;
	}

	public String getBackground()
	{
		return mBackground;
	}

	void addToFixedHeightSum(int add)
	{
		mFixedHeightCellSum += add;
	}

	int getFixedHeightSum()
	{
		return mFixedHeightCellSum;
	}

	public int getAbsoluteHeight()
	{
		return MathUtils.round(mAbsoluteHeight);
	}

	public int getAbsoluteWidth()
	{
		return MathUtils.round(mAbsoluteWidth);
	}

	@Override
	public int getDesiredWidth(ThemeClassDefinition themeClass)
	{
		return MathUtils.round(mAbsoluteWidthForTable);
	}

	@Override
	public int getDesiredHeight(ThemeClassDefinition themeClass)
	{
		return MathUtils.round(mAbsoluteHeightForTable);
	}

	public String getFlexDirection() { return mFlexDirection; }
	public String getFlexWrap() { return mFlexWrap; }
	public String getJustifyContent() { return mJustifyContent; }
	public String getAlignItems() { return mAlignItems; }
	public String getAlignContent() { return mAlignContent; }
	public boolean hasAdjustContentSize() { return mAdjustContentSize; }

	public boolean isCanvas()
	{
		String tableType = optStringProperty("@tableType");
		return tableType.equalsIgnoreCase("Absolute");
	}

	public boolean isFlex()
	{
		String tableType = optStringProperty("@tableType");
		return tableType.equalsIgnoreCase("Flex");
	}

	public boolean getEnableHeaderRowPattern() { return optBooleanProperty("@enableHeaderRowPattern"); }

	public String getHeaderRowApplicationBarClass() { return optStringProperty("@headerRowApplicationBarsClass"); }

	@Override
	public ThemeClassDefinition getThemeClass() {
		return getThemeClass(mRowThemeClass);
	}

	private Map<String, ThemeClassDefinition> mClassDefinitionCache;

	/**
	 * Gets the table class merged with the row class
	 * @param rowClassName name of the row class, use the name instead of the definition so it gets the class from the current theme
	 * @return a class with the merged properties
	 */
	public ThemeClassDefinition getThemeClass(String rowClassName) {
		if (rowClassName == null)
			return super.getThemeClass();

		// Recover the class from cache
		ThemeClassDefinition classDefinition;
		if (mClassDefinitionCache == null
				|| Services.Application.isLiveEditingEnabled()) { // don't cache the ThemeDefinition if LiveEditing is on.
			classDefinition = null;
		} else {
			classDefinition = mClassDefinitionCache.get(rowClassName);
		}

		if (classDefinition == null ||
				classDefinition.getTheme() != Services.Themes.getCurrentTheme()) { // if class in cache is not from current theme don't use it (happends when setTheme() was called from GX to change it).
			ThemeClassDefinition themeClass = super.getThemeClass();
			ThemeClassDefinition rowThemeClass = Services.Themes.getThemeClass(rowClassName);
			if (rowThemeClass == null)
				classDefinition = themeClass;
			else
				classDefinition = rowThemeClass.cloneAndMergeClass(themeClass);

			// Save class in cache
			if (!Services.Application.isLiveEditingEnabled()) {
				if (mClassDefinitionCache == null)
					mClassDefinitionCache = new HashMap<>();
				mClassDefinitionCache.put(rowClassName, classDefinition);
			}
		}

		return classDefinition;
	}

	public TableDefinition cloneAndMergeClass(ThemeClassDefinition rowClass) {
		if (rowClass == null)
			return this;
		try {
			TableDefinition definition = (TableDefinition) clone();
			definition.mRowThemeClass = rowClass.getName();
			return definition;
		}
		catch (CloneNotSupportedException e) {
			return this;
		}
	}
	
	public LayoutItemDefinition getControl(String name) {
		if (mItemLookup==null)
			mItemLookup = new LayoutItemLookup(this);
		return mItemLookup.getControl(name);
	}
}
