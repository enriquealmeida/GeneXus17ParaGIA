package com.genexus.android.controls.grids.magazineviewer;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.controls.grids.magazineviewer.FlipperOptions.FlipperLayoutType;

public class GxHorizontalGrid extends GxMagazineViewer {
	public static final String NAME = "SDHorizontalGrid";

	private static final String PROPERTY_COLUMNS_PER_PAGE_PORTRAIT = "ColumnsPerPagePortrait";
	private static final String PROPERTY_COLUMNS_PER_PAGE_LANDSCAPE = "ColumnsPerPageLandscape";
	private static final String PROPERTY_ROWS_PER_PAGE_PORTRAIT = "RowsPerPagePortrait";
	private static final String PROPERTY_ROWS_PER_PAGE_LANDSCAPE = "RowsPerPageLandscape";

	//Properties
	private static int mColumnsPerPagePortrait = 0;
	private static int mColumnsPerPageLandscape = 0;
	private static int mRowsPerPagePortrait = 0;
	private static int mRowsPerPageLandscape = 0;

	private boolean mShowPageController;
	private ThemeClassDefinition mIndicatorClass;
	private Orientation mCurrentOrientation;

	public GxHorizontalGrid(Context context, Coordinator coordinator, GridDefinition def) {
		super(context, coordinator, def);
	}

	public GxHorizontalGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void setControlInfo(ControlInfo info) {
		mColumnsPerPagePortrait = info.optIntProperty("@SDHorizontalGridColumnsPerPagePortrait");
		mRowsPerPagePortrait = info.optIntProperty("@SDHorizontalGridRowsPerPagePortrait");
		mColumnsPerPageLandscape = info.optIntProperty("@SDHorizontalGridColumnsPerPageLandscape");
		mRowsPerPageLandscape = info.optIntProperty("@SDHorizontalGridRowsPerPageLandscape");
		mShowPageController = info.optBooleanProperty("@SDHorizontalGridShowPageController");
		mIndicatorClass = Services.Themes.getThemeClass(info.optStringProperty("@SDHorizontalGridPageControllerClass"));

		updateValues(false);
	}

	@Override
	public Expression.Value getPropertyValue(String name) {

		switch (name) {
			case PROPERTY_COLUMNS_PER_PAGE_PORTRAIT:
				return Expression.Value.newInteger(getColumnsPerPagePortrait());
			case PROPERTY_COLUMNS_PER_PAGE_LANDSCAPE:
				return Expression.Value.newInteger(getColumnsPerPageLandscape());
			case PROPERTY_ROWS_PER_PAGE_PORTRAIT:
				return Expression.Value.newInteger(getRowsPerPagePortrait());
			case PROPERTY_ROWS_PER_PAGE_LANDSCAPE:
				return Expression.Value.newInteger(getRowsPerPageLandscape());
		}

		return super.getPropertyValue(name);
	}

	@Override
	public void setPropertyValue(String name, Expression.Value value) {

		switch (name) {
			case PROPERTY_COLUMNS_PER_PAGE_PORTRAIT:
				setColumnsPerPagePortrait(value.coerceToInt());
				return;
			case PROPERTY_COLUMNS_PER_PAGE_LANDSCAPE:
				setColumnsPerPageLandscape(value.coerceToInt());
				return;
			case PROPERTY_ROWS_PER_PAGE_PORTRAIT:
				setRowsPerPagePortrait(value.coerceToInt());
				return;
			case PROPERTY_ROWS_PER_PAGE_LANDSCAPE:
				setRowsPerPageLandscape(value.coerceToInt());
				return;
		}

		super.setPropertyValue(name, value);
	}

	private int getColumnsPerPagePortrait() {
		return mColumnsPerPagePortrait;
	}

	private int getColumnsPerPageLandscape() {
		return mColumnsPerPageLandscape;
	}

	private int getRowsPerPagePortrait() {
		return mRowsPerPagePortrait;
	}

	private int getRowsPerPageLandscape() {
		return mRowsPerPageLandscape;
	}

	private void setColumnsPerPagePortrait(int value) {
		if (value > 0 && value != mColumnsPerPagePortrait) {
			mColumnsPerPagePortrait = value;
			updateValues(true);
		}
	}

	private void setColumnsPerPageLandscape(int value) {
		if (value > 0 && value != mColumnsPerPageLandscape) {
			mColumnsPerPageLandscape = value;
			updateValues(true);
		}
	}

	private void setRowsPerPagePortrait(int value) {
		if (value > 0 && value != mRowsPerPagePortrait) {
			mRowsPerPagePortrait = value;
			updateValues(true);
		}
	}

	private void setRowsPerPageLandscape(int value) {
		if (value > 0 && value != mRowsPerPageLandscape) {
			mRowsPerPageLandscape = value;
			updateValues(true);
		}
	}

	private void updateValues(boolean restartView) {
		int rows;
		int columns;

		mFlipperOptions = new FlipperOptions();
		mCurrentOrientation = Services.Device.getScreenOrientation();
		if (mCurrentOrientation == Orientation.PORTRAIT) {
			rows = mRowsPerPagePortrait;
			columns = mColumnsPerPagePortrait;
		} else {
			rows = mRowsPerPageLandscape;
			columns = mColumnsPerPageLandscape;
		}

		ArrayList<Integer> layout = new ArrayList<>();
		for (int i = 0; i < columns; i++)
			layout.add(rows);

		mFlipperOptions.setRowsPerColumn(rows);
		mFlipperOptions.setLayout(layout);

		mFlipperOptions.setItemsPerPage(columns * rows);
		mFlipperOptions.setShowFooter(mShowPageController);
		mFlipperOptions.setLayoutType(FlipperLayoutType.Specific);

		if (mIndicatorClass != null)
			mFlipperOptions.setFooterThemeClass(mIndicatorClass);

		if (restartView) finishViewInitialization(false);
	}
}
