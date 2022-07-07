package com.genexus.android.core.controls;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.widget.ProgressBar;

import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.StorageHelper;

public class GxImageViewData extends GxImageViewBase implements IGxEdit, IGxEditThemeable
{
	private final LayoutItemDefinition mDefinition;
	private String mData;
	private boolean mEditable;

	private ThemeClassDefinition mThemeClass;
	private ProgressBar mLoadingIndicator;
	private String mUri;

	public GxImageViewData(Context context, LayoutItemDefinition layoutItem)
	{
		super(context, null, layoutItem);
		mDefinition = layoutItem;
		mData = layoutItem.getDataId();
	}

	public void setLoading(boolean loading)
	{
		if (mLoadingIndicator == null && loading)
		{
			mLoadingIndicator = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
			mLoadingIndicator.setIndeterminate(true);

			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
			addView(mLoadingIndicator, lp);
		}

		if (mLoadingIndicator != null)
			mLoadingIndicator.setVisibility(loading ? VISIBLE : INVISIBLE);

		setImageViewVisibility(loading ? INVISIBLE : VISIBLE);
	}

	@Override
	public String getGxValue()
	{
		return mUri;
	}

	@Override
	public void setGxValue(String value)
	{
		// if same image and already loaded ignore it
		if (Strings.hasValue(value) && Strings.hasValue(mUri) && value.equals(mUri)
				 && hasImageDrawable() && StorageHelper.isLocalFile(mUri))
		{
			Services.Log.debug("not loading image, already loaded.");
			return;
		}
		mUri = value;
		if (mUri != null)
		{
			// If the image is editable, we need a "visual tappable thingie". Otherwise don't display it.
			boolean needsPlaceholder = isEditable();
			Services.Images.showDataImage(getContext(), this, value, needsPlaceholder, false);
		}
	}

	@Override
	public String getGxTag()
	{
		return mData;
	}

	@Override
	public void setGxTag(String data)
	{
		mData = data;
		setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	@Override
	public IGxEdit getViewControl()
	{
		setEditable(false);
		return this;
	}

	@Override
	public IGxEdit getEditControl()
	{
		setEditable(true);
		return this;
	}

	public String getLabel()
	{
		return mDefinition.getCaption();
	}

	public String getControlType()
	{
		return mDefinition.getControlType();
	}

	@Override
	public boolean isEditable()
	{
		return mEditable;
	}

	public void setEditable(boolean value)
	{
		mEditable = value;
	}

	public int getMaximumUploadSizeMode()
	{
		return mDefinition.getMaximumUploadSizeMode();
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		// For data images, we only consider scale type and custom size.
		// Background, etc is handled by the DataBoundControl parent.
		mThemeClass = themeClass;
		setImagePropertiesFromThemeClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mThemeClass;
	}
}
