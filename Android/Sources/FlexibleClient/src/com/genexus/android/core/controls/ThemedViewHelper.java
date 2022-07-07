package com.genexus.android.core.controls;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;

import com.genexus.android.layout.ScrollViewThemeable;
import com.genexus.android.core.base.metadata.layout.CellDefinition;
import com.genexus.android.core.base.metadata.layout.GroupDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;

/**
 * Class used to centralize margin/padding/background code among all IGxThemeable controls.
 * Should be called on all IGxThemeable methods and on setLayoutParams().
 */
public class ThemedViewHelper implements IGxThemeable {
	private final View mView;
	private LayoutItemDefinition mDefinition;
	protected ThemeClassDefinition mThemeClass;

	// Partial apply
	private boolean mApplyMargin = true;
	private boolean mApplyPadding = true;
	private boolean mApplyBackgroudAndBorder = true;

	// Deferred info.
	private LayoutBoxMeasures mDeferredMargins;

	public ThemedViewHelper(View view, LayoutItemDefinition layoutItem) {
		mView = view;
		mDefinition = layoutItem;
	}

	public void setScope(boolean applyMargin, boolean applyPadding, boolean applyBackgroudAndBorder) {
		mApplyMargin = applyMargin;
		mApplyPadding = applyPadding;
		mApplyBackgroudAndBorder = applyBackgroudAndBorder;
	}

	public void setLayoutItem(LayoutItemDefinition layoutItem) {
		mDefinition = layoutItem;
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

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		if (themeClass == null) {
			return;
		}

		if (mApplyMargin) {
			// Margins.
			LayoutBoxMeasures margins = themeClass.getMargins();
			ViewGroup.LayoutParams lp = mView.getLayoutParams();
			if (lp != null) {
				// Does its site support margins?
				if (lp instanceof MarginLayoutParams) {
					MarginLayoutParams marginParams = (MarginLayoutParams) lp;
					marginParams.setMargins(margins.left, margins.top, margins.right, margins.bottom);

					if (mDefinition != null && (mDefinition.getParent() instanceof CellDefinition || mDefinition.getParent() instanceof GroupDefinition)) {
						LayoutItemDefinition layoutItem = (mDefinition.getParent() instanceof CellDefinition)
								? mDefinition : mDefinition.getParent();
						// only change width / height if already set
						// respect WRAP_CONTENT and MATCH_PARENT
						if (lp.width > 0) {
							lp.width = layoutItem.getDesiredWidth(themeClass);
						}
						if (!layoutItem.hasAutoGrow() && lp.height > 0) {
							lp.height = layoutItem.getDesiredHeight(themeClass);
						}
					}

					ViewParent viewParent = mView.getParent();
					if (viewParent instanceof ViewGroup) {
						ViewGroup parent = (ViewGroup) viewParent;
						parent.updateViewLayout(mView, lp);
						parent.requestLayout();
						parent.invalidate();
					}
				}
			} else {
				// The layout could not be on site yet; in that case differ the setting to setLayoutParams().
				mDeferredMargins = margins;
			}
		}

		if (mApplyPadding) {
			// Padding.
			LayoutBoxMeasures padding = themeClass.getPadding();
			mView.setPadding(padding.left, padding.top, padding.right, padding.bottom);
		}

		if (mApplyBackgroudAndBorder) {
			// Background and border.
			if (mView.getParent() == null || !(mView.getParent() instanceof ScrollViewThemeable)) {    // ScrollViewThemeable draw border and background
				ThemeUtils.setBackgroundBorderProperties(mView, themeClass, mDefinition == null ? BackgroundOptions.DEFAULT : BackgroundOptions.defaultFor(mDefinition));
			}
		}
	}

	public void setLayoutParams(ViewGroup.LayoutParams params) {
		// Does its site support margins and we deferred the assignment? If so, do it now.
		if (mDeferredMargins != null && params instanceof MarginLayoutParams) {
			MarginLayoutParams marginParams = (MarginLayoutParams) params;
			marginParams.setMargins(mDeferredMargins.left, mDeferredMargins.top, mDeferredMargins.right, mDeferredMargins.bottom);
			mDeferredMargins = null;
		}
	}
}
