package com.genexus.android.core.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.common.IViewDisplayGifSupport;
import com.genexus.android.core.controls.common.IViewDisplayImage;
import com.genexus.android.core.ui.Coordinator;

import java.io.File;
import java.io.IOException;

public abstract class GxImageViewBase extends FrameLayout implements IViewDisplayImage, IViewDisplayGifSupport {
	private IGxImageView mImageView;
	private String mImageTag;
	private GxImageViewFactory mFactory;
	private ThemeClassDefinition mThemeClass;
	private ImageScaleType mScaleType;
	private Size mImageSize;

	public GxImageViewBase(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
		super(context);
		mFactory = new GxImageViewFactory(context, coordinator, definition);
		setImageView(mFactory.createEmptyImage()); // An image must be assigned in the constructor because GxSDGeoLocation loads the image in onLayout
	}

	public GxImageViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		mFactory = new GxImageViewFactory(context, null, null);
	}

	protected void setImageViewVisibility(int visibility) {
		if (mImageView != null)
			((View)mImageView).setVisibility(visibility);
	}

	private void setImageView(IGxImageView imageView) {
		if (mImageView == imageView)
			return;

		if (mImageView != null)
			removeView((View)mImageView);

		mImageView = imageView;
		ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		addView((View)mImageView, params);

		if (mThemeClass != null)
			mImageView.setImagePropertiesFromThemeClass(mThemeClass);
		if (mScaleType != null)
			mImageView.setImageScaleType(mScaleType);
		if (mImageSize != null)
			mImageView.setImageSize(mImageSize.getWidth(), mImageSize.getHeight());
	}

	private void clearImageView() {
		if (mImageView == null || mFactory.clearImage(mImageView)) // don't remove the view because of GxSDGeoLocation
			return;

		removeImageView();
	}

	protected void removeImageView() {
		if (mImageView != null) {
			removeView((View) mImageView);
			mImageView = null;
		}
	}

	protected void setImagePropertiesFromThemeClass(ThemeClassDefinition themeClass) {
		mThemeClass = themeClass;
		if (mImageView != null)
			mImageView.setImagePropertiesFromThemeClass(themeClass);
	}

	public void setImageScaleType(ImageScaleType scaleType) {
		mScaleType = scaleType;
		if (mImageView != null)
			mImageView.setImageScaleType(scaleType);
	}

	public void setImageSize(Integer width, Integer height) {
		mImageSize = new Size(width, height);
		if (mImageView != null)
			mImageView.setImageSize(width, height);
	}

	protected boolean hasImageDrawable() {
		if (mImageView != null)
			return mImageView.hasImageDrawable();
		return false;
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		if (drawable != null)
			setImageView(mFactory.getImage(mImageView, drawable)); // reuse the image because of GxSDGeoLocation
		else
			clearImageView();
	}

	@Override
	public void setImageResource(int resId) {
		Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
		setImageDrawable(drawable);
	}

	@Override
	public void setImageBitmap(Bitmap bmp) {
		if (bmp != null)
			setImageDrawable(new BitmapDrawable(getContext().getResources(), bmp));
		else
			clearImageView();
	}

	private void makeGifImageView(int id, final File file) {
		try {
			IGxImageView view;
			if (file == null)
				view = mFactory.createGif(id);
			else
				view = mFactory.createGif(file);
			setImageView(view);
		} catch (IOException e) {
			// Gif can't be loaded, just log the exception and show nothing
			Services.Log.error("Can't load gif file", e);
			clearImageView();
		}
	}

	@Override
	public void setGifImageResource(int id) {
		makeGifImageView(id, null);
	}

	@Override
	public void setGifImageFile(File file) {
		makeGifImageView(0, file);
	}

	@Override
	public String getImageTag() {
		return mImageTag;
	}

	@Override
	public void setImageTag(String tag) {
		mImageTag = tag;
	}
}
