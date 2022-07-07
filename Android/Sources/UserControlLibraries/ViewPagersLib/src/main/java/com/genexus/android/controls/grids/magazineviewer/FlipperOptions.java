package com.genexus.android.controls.grids.magazineviewer;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.ThemeUtils;

public class FlipperOptions
{
	private ArrayList<Integer> mLayout;
	private int mItemsPerPage;
	private FlipperLayoutType mLayoutType;
	private boolean mShowFooter;
	private boolean mTransparentFooter;
	private String mHeaderText;
	private int mDefaultItemsPerPage;
	private int mRowsPerColumn = -1;

	private Integer mFooterBackgroundColor = null;
	private Integer mFooterUnselectedColor = null;
	private Integer mFooterSelectedColor = null;

	enum FlipperLayoutType
	{
		Specific,
		Random
	}

	public int getItemsPerPage() {
		if (mItemsPerPage > 0)
			return mItemsPerPage;
		// if a specific layout is specified so count how many items are in each page
		if (mLayout != null) {
			mItemsPerPage = 0;
			for (Integer rows : mLayout)
				mItemsPerPage += rows;
			if (mItemsPerPage == 0)
				mItemsPerPage = 1;
		} else
			mItemsPerPage = mDefaultItemsPerPage;

		return mItemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		mDefaultItemsPerPage = itemsPerPage;
	}

	public FlipperLayoutType getLayoutType() {
		return mLayoutType;
	}

	public void setLayoutType(FlipperLayoutType layoutType) {
		mLayoutType = layoutType;
	}

	public ArrayList<Integer> getLayout() {
		if (mLayoutType == FlipperLayoutType.Specific)
			return mLayout;
		else {
			int totalItemsPerPage = getItemsPerPage();
			int cantItems = 0;
			mLayout = new ArrayList<Integer>();
			while (cantItems < totalItemsPerPage) {
				int inPageItems = getRandomNumber(1, totalItemsPerPage - cantItems);
				mLayout.add(inPageItems);
				cantItems += inPageItems;
			}
			return mLayout;
		}
	}

	/**
	 * Returns a random integer from the interval [min, max].
	 * 
	 * @return an integer betweetn min and max inclusive.
	 */
	private int getRandomNumber(int min, int max) {
		Random rnd = new Random();
		return rnd.nextInt(max - min + 1) + min;
	}

	public void setLayout(ArrayList<Integer> layout) {
		mLayout = layout;
	}

	public boolean isShowFooter() {
		return mShowFooter;
	}

	public boolean isTransparentFooter() {
		return mTransparentFooter;
	}

	public void setShowFooter(boolean showFooter) {
		mShowFooter = showFooter;
	}

	public String getHeaderText() {
		return mHeaderText;
	}

	public void setHeaderText(String headerText) {
		mHeaderText = headerText;
	}

	public void setFooterThemeClass(ThemeClassDefinition themeClass)
	{
		setFooterBackgroundColor(ThemeUtils.getColorId(themeClass.optStringProperty("SDPageControllerBackgroundColor")));
		setFooterSelectedColor(ThemeUtils.getColorId(themeClass.optStringProperty("SDPageIndicatorSelectedColor")));
		setFooterUnselectedColor(ThemeUtils.getColorId(themeClass.optStringProperty("SDPageIndicatorUnselectedColor")));
	}

	public Integer getFooterBackgroundColor()
	{
		return mFooterBackgroundColor;
	}

	public void setFooterBackgroundColor(Integer color)
	{
		mFooterBackgroundColor = color;
		if (color != null && Color.alpha(color) < 255)
		{
			mTransparentFooter = true;
		}
		else
		{
			mTransparentFooter = false;
		}
	}

	public Integer getFooterSelectedColor()
	{
		return mFooterSelectedColor;
	}

	public void setFooterSelectedColor(Integer color)
	{
		mFooterSelectedColor = color;
	}

	public Integer getFooterUnselectedColor()
	{
		return mFooterUnselectedColor;
	}

	public void setFooterUnselectedColor(Integer color)
	{
		mFooterUnselectedColor = color;
	}

	public int getRowsPerColumn() {
		return mRowsPerColumn;
	}

	public void setRowsPerColumn(int rows) {
		mRowsPerColumn = rows;
	}

	public int getFooterHeight()
	{
		if (mShowFooter  && !mTransparentFooter )
			return Services.Device.dipsToPixels(26); // 10*2 (padding), plus 3*2 (circle with 3dp radius).
		else
			return 0;
	}
}
