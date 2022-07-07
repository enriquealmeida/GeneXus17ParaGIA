package com.genexus.android.core.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.genexus.android.core.controls.common.IViewDisplayImage;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.Cast;

public class ImageViewDisplayImageWrapper implements IViewDisplayImage
{
	private final ImageView mView;
	private String mTag;

	public static ImageViewDisplayImageWrapper to(ImageView view)
	{
		return new ImageViewDisplayImageWrapper(view);
	}

	private ImageViewDisplayImageWrapper(ImageView view)
	{
		mView = view;
	}

	@Override
	public void setImageBitmap(Bitmap bm)
	{
		mView.setImageBitmap(bm);
	}

	@Override
	public void setImageDrawable(Drawable drawable)
	{
		mView.setImageDrawable(drawable);
	}

	@Override
	public void setImageResource(int resId)
	{
		mView.setImageResource(resId);
	}

	@Override
	public void setLayerType(int layerType, @Nullable Paint paint)
	{
		mView.setLayerType(layerType, paint);
	}

	@Override
	public Context getContext() {
		return mView.getContext();
	}

	@Override
	public String getImageTag()
	{
		return mTag;
	}

	@Override
	public void setImageTag(String tag)
	{
		mTag = tag;
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return Cast.as(ThemeClassDefinition.class, mView.getTag(LayoutTag.CONTROL_THEME_CLASS));
	}
}
