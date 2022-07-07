package com.genexus.android.core.usercontrols.matrixgrid;

import java.util.Date;

import android.content.Context;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.types.IStructuredDataType;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.grids.CustomGridDefinition;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.ui.CoordinatorBase;

public class ScheduleGridDefinition extends CustomGridDefinition
{
	// Data Bound
	private String mYFromFieldName;
	private String mYToFieldName;
	private String mXFromFieldName;
	private String mXToFieldName;
	private @NonNull String mRowSelectionFlag;

	// Cell (and axis row/columns) measurements (in dips or percentage).
	private String mCellWidthExpression;
	private String mCellHeightExpression;
	private String mSelectedRowHeightExpression;
	private String mXAxisHeightExpression;
	private String mYAxisWidthExpression;

	private String mXValuesFieldName;
	private String mXValuesFieldDescription;
	private String mXValuesFieldTitle;
	private String mYValuesFieldName;
	private String mYValuesFieldDescription;
	private String mYValuesFieldTitle;
	private DataItem mAxisXDataItem;
	private DataItem mAxisYDataItem;

	// Axis data.
	private EntityList mXAxis; // Data for the X-Axis.
	private EntityList mYAxis; // Data for the Y-Axis.
	private int mSelectedRowIndex; // Index of the selected row (-1 if no selection).
	private Float mMinX; // Minimum value of the X-Axis (in the domain of the X-Axis dimension).
	private Float mMaxX; // Maximum value of the X-Axis (in the domain of the X-Axis dimension).
	private Float mMinY; // Minimum value of the Y-Axis (in the domain of the Y-Axis dimension).
	private Float mMaxY; // Maximum value of the X-Axis (in the domain of the Y-Axis dimension).

	// Sizes evaluated from control dimensions and axis data.
	private int mCellWidth; // Width of a cell.
	private int mCellHeight; // Height of a cell.
	private int mSelectedRowHeight; // Height of the selected row (and its cells).
	private int mXAxisHeight; // Height of the X-Axis (fixed top row).
	private int mYAxisWidth; // Width of the Y-Axis (fixed left column).
	private int mTotalContentWidth; // Width of the data (normally exceeds visible area).
	private int mTotalContentHeight; // Height of the data (normally exceeds visible area).

	private ControlInfo mControlInfo;

	public ScheduleGridDefinition(Context context, GridDefinition grid)
	{
		super(context, grid);
		mSelectedRowIndex = -1;
	}

	@Override
	protected void init(GridDefinition grid, ControlInfo controlInfo)
	{
		mControlInfo = controlInfo;
		mCellWidthExpression = controlInfo.optStringProperty("@MatrixGridCellWidth");
		mCellHeightExpression = controlInfo.optStringProperty("@MatrixGridCellHeight");
		mSelectedRowHeightExpression = controlInfo.optStringProperty("@MatrixGridSelectedCellHeight");
		mXAxisHeightExpression = controlInfo.optStringProperty("@MatrixGridxAxisHeight");
		mYAxisWidthExpression = controlInfo.optStringProperty("@MatrixGridyAxisWidth");

		mXValuesFieldName = controlInfo.optStringProperty("@MatrixGridXAxisIdField").replace("item(0).", "");
		mXValuesFieldDescription = controlInfo.optStringProperty("@MatrixGridXAxisDescField").replace("item(0).", "");
		mXValuesFieldTitle = controlInfo.optStringProperty("@MatrixGridXAxisTitleField").replace("item(0).", "");

		mYValuesFieldName = controlInfo.optStringProperty("@MatrixGridYAxisIdField").replace("item(0).", "");
		mYValuesFieldDescription = controlInfo.optStringProperty("@MatrixGridYAxisDescField").replace("item(0).", "");
		mYValuesFieldTitle = controlInfo.optStringProperty("@MatrixGridYAxisTitleField").replace("item(0).", "");
		mRowSelectionFlag = controlInfo.optStringProperty("@MatrixGridYAxisSelectionFlagField").replace("item(0).", "");

		mXFromFieldName = readDataExpression("@MatrixGridXDataFromAttribute", "@MatrixGridXDataFromFieldSpecifier");
		mXToFieldName = readDataExpression("@MatrixGridXDataToAttribute", "@MatrixGridXDataToFieldSpecifier");
		mYFromFieldName = readDataExpression("@MatrixGridYDataFromAttribute", "@MatrixGridYDataFromFieldSpecifier");
		mYToFieldName = readDataExpression("@MatrixGridYDataToAttribute", "@MatrixGridYDataToFieldSpecifier");
	}

	public String getYValuesFieldTitle() {
		return mYValuesFieldTitle;
	}

	public String getXValuesFieldTitle() {
		return mXValuesFieldTitle;
	}

	/*
	 *  This inner class know how to transform from control coordinates (X, Y) to pixels
	 */
	private class NumericValueResolver
	{
		private float mDataMin;
		private float mDataMax;
		private float mCellSize;
		private int mCellCount;
		private float mSelectedCellSize;
		private float mSelectedValue = -1;

		public GxRange rangeFromValues(Object from, Object to)
		{
			float fFrom = (Float) from;
			float fTo = (Float) to;
			GxRange range = new GxRange();

			float realIntervalSize = (mDataMax - mDataMin) / (mCellCount - 1);
			float realStart = fFrom - mDataMin;
			float realLength = fTo - fFrom;
			if (realLength == 0)
				realLength = realIntervalSize;

			float realSelectedValue = -1;
			if (hasSelectedRow())
				realSelectedValue = (mSelectedValue / mCellSize) * realIntervalSize;

			float cellSize = mCellSize;
			if (hasSelectedRow() && Math.abs(realSelectedValue - realStart) < .0000001)
				cellSize = mSelectedCellSize;

			// Calculate if we need to add or subtract something for this cell, see that only cells below the selected are affected their position
			float additionalCellSize = 0;
			if (hasSelectedRow())
				additionalCellSize = (realSelectedValue < realStart) ? (mSelectedCellSize - mCellSize) : 0;

			// mCellSize <----> realIntervalSize
			// mStart <---->  realStart * mCellSize / realIntervalSize
			range.setStart(realStart * mCellSize / realIntervalSize + additionalCellSize);
			range.setLength(realLength * cellSize / realIntervalSize);
			return range;
		}

	 	public void setup(float min, float max, float cellSize, float selectedCellSize, float selectedValue, int cellCount, float offset) {
			mDataMin = min;
			mDataMax = max;
			mCellSize = cellSize;
			mCellCount = cellCount;
			mSelectedCellSize = selectedCellSize;
			mSelectedValue = selectedValue;
		}
	}

	private NumericValueResolver createXValueResolver()
	{
		return new NumericValueResolver();
	}

	private NumericValueResolver createYValueResolver()
	{
		return new NumericValueResolver();
	}

	public void updateSize(Coordinator mCoordinator, Size size)
	{
		CoordinatorBase baseCoordinator = (CoordinatorBase) mCoordinator;
		Entity formData = baseCoordinator.getData();
		if (formData != null && mControlInfo != null)
		{
			String dataXAxis = mControlInfo.optStringProperty("@MatrixGridXAxisSDT");
			mXAxis = (EntityList) formData.getProperty(dataXAxis);

			DataItem timeAxisDefinition = formData.getDefinition().getAttribute(dataXAxis.replace("&", ""));
			if (timeAxisDefinition != null)
			{
				IStructuredDataType dt = timeAxisDefinition.getTypeInfo(IStructuredDataType.class);
				mAxisXDataItem = dt.getItem(mXValuesFieldName);
			}

			String dataYAxis = mControlInfo.optStringProperty("@MatrixGridYAxisSDT");
			mYAxis = (EntityList) formData.getProperty(dataYAxis);

			DataItem timeAxisYDefinition = formData.getDefinition().getAttribute(dataYAxis.replace("&", ""));
			if (timeAxisYDefinition != null)
			{
				IStructuredDataType dt = timeAxisYDefinition.getTypeInfo(IStructuredDataType.class);
				mAxisYDataItem = dt.getItem(mYValuesFieldName);
			}

			mSelectedRowIndex = -1;
			for (int i = 0; i < mYAxis.size(); i++)
			{
				if (mYAxis.get(i).optBooleanProperty(mRowSelectionFlag))
					mSelectedRowIndex = i;
			}

			mCellWidth = Size.getPixels(size.getWidth(), mCellWidthExpression);
			mCellHeight = Size.getPixels(size.getHeight(), mCellHeightExpression);
			mSelectedRowHeight = Size.getPixels(size.getHeight(), mSelectedRowHeightExpression);
			mXAxisHeight = Size.getPixels(size.getHeight(), mXAxisHeightExpression);
			mYAxisWidth = Size.getPixels(size.getWidth(), mYAxisWidthExpression);

			mTotalContentWidth = mCellWidth * getXAxis().size();
			mTotalContentHeight = mCellHeight * getYAxis().size() + (hasSelectedRow() ? getSelectedRowExtraHeight() : 0);

			// Cleared, recalculated on request.
			mMinX = null;
			mMaxX = null;
			mMinY = null;
			mMaxY = null;
		}
	}

	public int getCellWidth() { return mCellWidth; }
	public int getCellHeight() { return mCellHeight; }
	public int getSelectedRowHeight() { return mSelectedRowHeight; }
	public int getXAxisHeight() { return mXAxisHeight; }
	public int getYAxisWidth() { return mYAxisWidth; }

	public EntityList getXAxis() { return mXAxis; }
	public EntityList getYAxis() { return mYAxis; }

	public int getSelectedRowIndex() { return mSelectedRowIndex; }
	public boolean hasSelectedRow() { return mSelectedRowIndex != -1; }
	public boolean isSelectedRow(int rowNumber) { return hasSelectedRow() && mSelectedRowIndex == rowNumber; }

	private float getMinX()
	{
		if (mMinX == null)
			mMinX = getXDataValue(mXAxis.get(0).optStringProperty(mXValuesFieldName));

		return mMinX;
	}

	private float getMaxX()
	{
		if (mMaxX == null)
			mMaxX = getXDataValue(mXAxis.get(mXAxis.size() - 1).optStringProperty(mXValuesFieldName));

		return mMaxX;
	}

	private float getMinY()
	{
		if (mMinY == null)
			mMinY = getYDataValue(mYAxis.get(0).optStringProperty(mYValuesFieldName));

		return mMinY;
	}

	private float getMaxY()
	{
		if (mMaxY == null)
			mMaxY = getYDataValue(mYAxis.get(mYAxis.size() - 1).optStringProperty(mYValuesFieldName));

		return mMaxY;
	}

	private int getColumnCount() {
		return mXAxis.size();
	}

	private int getRowCount() {
		return mYAxis.size();
	}

	String geYFromFieldName() {
		return mYFromFieldName;
	}

	String getYToFieldName() {
		return mYToFieldName;
	}

	String getXFromFieldName() {
		return mXFromFieldName;
	}

	String getXToFieldName() {
		return mXToFieldName;
	}

	String getXValuesFieldDescription() {
		return mXValuesFieldDescription;
	}

	String getYValuesFieldDescription() {
		return mYValuesFieldDescription;
	}

	public int getSelectedRowExtraHeight()
	{
		return (mSelectedRowHeight - mCellHeight);
	}

	public int getTotalContentHeight()
	{
		return mTotalContentHeight;
	}

	public int getTotalContentWidth()
	{
		return mTotalContentWidth;
	}

	public static class GxRange
	{
		private float mStart;
		private float mLength;

		public GxRange() {
		}
		public float lenght() {
			return mLength;
		}
		private float getStart() {
			return mStart;
		}
		public void setStart(float f) {
			mStart = f;
		}
		public void setLength(float f) {
			mLength = f;
		}
	}

	public Rect createCellRect(float xFrom, float xTo, float yFrom, float yTo, float selectedValue)
	{
		NumericValueResolver resolver = createXValueResolver();
		resolver.setup(getMinX(), getMaxX(), mCellWidth, mCellWidth, -1, getColumnCount(), 0);
		GxRange xRange = resolver.rangeFromValues(xFrom, xTo);

		resolver = createYValueResolver();
		resolver.setup(getMinY(), getMaxY(), mCellHeight, mSelectedRowHeight, selectedValue, getRowCount(), 0);
		GxRange yRange = resolver.rangeFromValues(yFrom, yTo);

		// create the final rect for the entity
		Rect rect = new Rect((int)xRange.getStart(), (int)yRange.getStart(), (int)(xRange.getStart() + xRange.lenght()), (int)(yRange.getStart() + yRange.lenght()));
		return rect;
	}

	public float getXDataValue(String str)
	{
		if (mAxisXDataItem.getType().equals(DataTypes.DATE) || mAxisXDataItem.getType().equals(DataTypes.DATETIME))
			return dateToFloat(str);

		return Float.parseFloat(str);
	}

	public float getYDataValue(String str)
	{
		if (mAxisYDataItem.getType().equals(DataTypes.DATE) || mAxisYDataItem.getType().equals(DataTypes.DATETIME))
			return dateToFloat(str);

		return Float.parseFloat(str);
	}

	private static float dateToFloat(String str)
	{
		Date date = Services.Strings.getDate(str);
		long seconds = date.getTime() / 1000; // Lose some digits.
		return seconds;
	}
}
