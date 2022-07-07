package com.genexus.android.core.controls;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;

import com.genexus.android.core.resources.StandardImages;

public class FKPickerControl extends AppCompatImageView
{
	public FKPickerControl(Context context)
	{
		super(context);
		StandardImages.setPromptImage(this);
	}
}
