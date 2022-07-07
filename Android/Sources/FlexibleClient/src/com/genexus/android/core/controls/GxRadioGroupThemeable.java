package com.genexus.android.core.controls;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.ThemeUtils;

public class GxRadioGroupThemeable extends android.widget.RadioGroup implements IGxThemeable
{
	private ThemeClassDefinition mThemeClass;

	public GxRadioGroupThemeable(Context context)
	{
		super(context);
	}
	
	public GxRadioGroupThemeable(Context context, AttributeSet attrs)
	{
		super(context, attrs);
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
		int size = this.getChildCount();
		for(int i=0; i<size; i++){
			RadioButton radioButton = (RadioButton) this.getChildAt(i);
			if (radioButton!=null)
			{
				//default to textview colors.
				TextView dummyText = new TextView(this.getContext());
				radioButton.setTextColor(dummyText.getTextColors());
				ThemeUtils.setFontProperties(radioButton, themeClass);
			}
		}		
	}

}
