package com.genexus.android.core.controls.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TabControlDefinition;
import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;
import com.genexus.android.core.base.metadata.theme.TabControlClassExtensionKt;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.BitmapUtils;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.DrawableUtils;
import com.genexus.android.core.utils.ThemeUtils;

import static com.genexus.android.core.base.metadata.theme.TabControlClassExtensionKt.TAB_STRIP_POSITION_BOTTOM;

public class TabUtils
{
	private static final int TAB_DRAWABLE_PADDING_DIPS = 4;
	private static final int TAB_VIEW_MIN_PADDING_DIPS = 2;

	public static void applyTabControlClass(ViewGroup container, LayoutItemDefinition layoutItem, SlidingTabLayout slidingTabs, ThemeClassDefinition tabControlClass)
	{
		LayoutBoxMeasures margins = tabControlClass == null ? LayoutBoxMeasures.EMPTY : tabControlClass.getMargins();
		if (margins != null) {
			ViewGroup.LayoutParams lp = container.getLayoutParams();
			if (lp != null) {
				ViewGroup.MarginLayoutParams marginParams = Cast.as(ViewGroup.MarginLayoutParams.class, lp);
				if (marginParams != null) {
					marginParams.setMargins(margins.left, margins.top, margins.right, margins.bottom);

					if (layoutItem != null) {
						if (lp.width > 0)
							lp.width = layoutItem.getDesiredWidth(tabControlClass);
						if (!layoutItem.hasAutoGrow()) {
							if (lp.height > 0)
								lp.height = layoutItem.getDesiredHeight(tabControlClass);
						}
					}

					ViewGroup parent = Cast.as(ViewGroup.class, container.getParent());
					if (parent != null) {
						parent.updateViewLayout(container, lp);
						parent.requestLayout();
						parent.invalidate();
					}
				}
			}
		}

		if (tabControlClass == null)
			return;

		// Padding for the whole tab control.
		ThemeUtils.setPadding(container, tabControlClass);

		// Reorder children, if necessary, according to "tabs at bottom" preference.
		boolean isTabStripAtBottom = (container.getChildAt(1) == slidingTabs);
		boolean shouldPutTabStripAtBottom = TabControlClassExtensionKt.getTabStripPosition(tabControlClass) == TAB_STRIP_POSITION_BOTTOM;
		if (isTabStripAtBottom != shouldPutTabStripAtBottom)
		{
			View previousFirstChild = container.getChildAt(0);
			container.removeView(previousFirstChild);
			container.addView(previousFirstChild);
		}

		// Background for the whole tab control.
		ThemeUtils.setBackgroundBorderProperties(container, tabControlClass, BackgroundOptions.DEFAULT);

		// Background for the tab strip only.
		ThemeUtils.setBackgroundColor(slidingTabs, TabControlClassExtensionKt.getTabStripColorId(tabControlClass));

		Integer indicatorColor = TabControlClassExtensionKt.getIndicatorColorId(tabControlClass);
		if (indicatorColor == null)
			indicatorColor = ThemeUtils.getAndroidThemeColorId(container.getContext(), R.attr.colorAccent); // As per material design guidelines.
		if (indicatorColor != null)
			slidingTabs.setSelectedIndicatorColors(indicatorColor);

		ThemeUtils.setElevation(slidingTabs, TabControlClassExtensionKt.getTabStripElevation(tabControlClass));

		// As per Material Design guidelines
		slidingTabs.setDividerColors(Color.TRANSPARENT);
	}

	public static void applyTabItemClass(TextView tabTitleView, ThemeClassDefinition normalClass, ThemeClassDefinition selectedClass)
	{
		ThemeClassDefinition currentAppliedClass =
				applyTabItemClass(tabTitleView, normalClass, selectedClass, null);
		tabTitleView.setTag(LayoutTag.CONTROL_THEME_CLASS, currentAppliedClass);
	}

	public static ThemeClassDefinition applyTabItemClass(TextView tabTitleView, ThemeClassDefinition normalClass, ThemeClassDefinition selectedClass, ThemeClassDefinition currentAppliedClass)
	{
		// applyTabItemClass for tab menu
		ThemeClassDefinition themeClass = getThemeClassToApply(tabTitleView.isSelected(), normalClass, selectedClass);

		if (themeClass != null && themeClass != currentAppliedClass)
		{
			ThemeUtils.setBackgroundBorderProperties(tabTitleView, themeClass, BackgroundOptions.DEFAULT);
			ThemeUtils.setFontProperties(tabTitleView, themeClass);

			// change default allCaps if necessary
			if (!themeClass.getFontAllCaps())
				tabTitleView.setAllCaps(false);

			return themeClass;
		}
		else
			return currentAppliedClass;
	}

	public static ThemeClassDefinition applyTabItemClass(GxTabPageTextView tabTitleView, ThemeClassDefinition normalClass, ThemeClassDefinition selectedClass, ThemeClassDefinition currentAppliedClass)
	{
		// applyTabItemClass for tab control
		ThemeClassDefinition themeClass = getThemeClassToApply(tabTitleView.isSelected(), normalClass, selectedClass);

		if (themeClass != null && themeClass != currentAppliedClass)
		{
			ThemeUtils.setBackgroundBorderProperties(tabTitleView, themeClass, BackgroundOptions.DEFAULT);
			ThemeUtils.setFontProperties(tabTitleView.getInternalTextView(), themeClass);
			// change default allCaps if necessary
			if (!themeClass.getFontAllCaps())
				tabTitleView.getInternalTextView().setAllCaps(false);

			return themeClass;
		}
		else
			return currentAppliedClass;
	}

	private static @Nullable ThemeClassDefinition getThemeClassToApply(boolean isSelected, ThemeClassDefinition normalClass, ThemeClassDefinition selectedClass)
	{
		ThemeClassDefinition themeClass;
		if (normalClass != null && selectedClass != null)
			themeClass = (isSelected ? selectedClass : normalClass);
		else if (normalClass != null)
			themeClass = normalClass;
		else if (selectedClass != null)
			themeClass = selectedClass;
		else
			themeClass = null;
		return themeClass;
	}

	public static void setTabImage(TextView tabTitleView, String image, String selectedImage, int imageAlignment)
	{
		Context context = tabTitleView.getContext();

		Drawable normalDrawable = fixTabIconDrawable(context, Services.Images.getStaticImage(context, image));
		Drawable selectedDrawable = fixTabIconDrawable(context, Services.Images.getStaticImage(context, selectedImage));

		Drawable tabImage = DrawableUtils.newStateListDrawable(normalDrawable, selectedDrawable);
		if (tabImage != null)
		{
			tabTitleView.setCompoundDrawablePadding(Services.Device.dipsToPixels(TAB_DRAWABLE_PADDING_DIPS));
			DrawableUtils.setCompoundDrawableWithIntrinsicBounds(tabTitleView, tabImage, imageAlignment);
		}
	}

	private static Drawable fixTabIconDrawable(Context context, Drawable drawable)
	{
		if (drawable != null)
		{
			Bitmap tmpBitmap = BitmapUtils.createFromDrawable(drawable);

			final int iconSizePx = Services.Device.dipsToPixels(TabControlDefinition.TAB_ICON_SIZE);
			tmpBitmap = BitmapUtils.createScaledBitmap(context.getResources(), tmpBitmap, iconSizePx, iconSizePx, ImageScaleType.FIT);

			BitmapDrawable tmpDrawable = new BitmapDrawable(context.getResources(), tmpBitmap);
			tmpDrawable.setGravity(Gravity.CENTER);
			return tmpDrawable;
		}
		else
			return null;
	}

	public static void fixTabHeightAndPadding(LinearLayout tabView, int tabHeight, boolean hasImageAndText)
	{
		// HACK: We need a FIXED size for tab title views, because we have measured all controls before!
		// Remove this when we measure in onMeasure() instead of before adding the controls.
		tabView.getLayoutParams().height = tabHeight;
		int minPadding = Services.Device.dipsToPixels(TAB_VIEW_MIN_PADDING_DIPS);
		if (hasImageAndText)
			tabView.setPadding(minPadding, 0, minPadding, 0);
		else
			tabView.setPadding(tabView.getPaddingLeft(), 0, tabView.getPaddingRight(), 0);
		// End HACK
	}

}
