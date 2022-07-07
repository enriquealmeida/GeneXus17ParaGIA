package com.genexus.android.core.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.Cast;

/*
 * Table wrapper. Implements only margins, but not border, padding, or background.
 * Table itself is a GXLayout with border, background, &c.
 */
public class GxTableLayout extends android.widget.LinearLayout implements IGxThemeable
{
	private LayoutBoxMeasures mMargins;
	private ThemeClassDefinition mThemeClass;

	public GxTableLayout(Context context)
	{
		super(context);
	}

	public GxTableLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params)
	{
		if (mMargins != null && Cast.as(MarginLayoutParams.class, params) != null)
			((MarginLayoutParams)params).setMargins(mMargins.left, mMargins.top, mMargins.right, mMargins.bottom);

		super.setLayoutParams(params);
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

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		// Margins
		LayoutBoxMeasures margins = themeClass == null ? LayoutBoxMeasures.EMPTY : themeClass.getMargins();
		if (margins != null)
		{
			ViewGroup.LayoutParams lp = getLayoutParams(); // The layout could not be on site yet, differ the setting to the setLayoutParams
			if (lp != null)
			{
				MarginLayoutParams marginParms = Cast.as(MarginLayoutParams.class, lp); // does its site support margins?
				if (marginParms != null)
				{
					marginParms.setMargins( margins.left, margins.top,margins.right, margins.bottom);
					setLayoutParams(lp);
				}
			}
			else
				mMargins = margins;
		}

		// Everything else, handled by contained GxLayout.
		if (getChildCount() == 1 && getChildAt(0) instanceof IGxThemeable)
			((IGxThemeable)getChildAt(0)).setThemeClass(themeClass);
	}
}
