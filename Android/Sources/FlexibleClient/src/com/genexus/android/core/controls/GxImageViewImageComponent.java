package com.genexus.android.core.controls;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.utils.PrimitiveUtils;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.BitmapUtils;
import com.makeramen.roundedimageview.RoundedImageView;

public class GxImageViewImageComponent extends FrameLayout implements IGxImageView {
	protected Drawable mDrawable;
	private ImageView mImageView;
	private int mAlignment;
	private ImageScaleType mScaleType;
	private Integer mImageWidth;
	private Integer mImageHeight;
	private boolean mAutogrow;

	private Matrix mMatrix;

	public GxImageViewImageComponent(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
		super(context);
		initialize();

		if (definition != null) {
			setAlignment(definition.getCellGravity());
			setAutogrow(definition.hasAutoGrow());
		}
	}

	public GxImageViewImageComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize() {
		mScaleType = ImageScaleType.FIT;
		mAlignment = Alignment.CENTER;
		mMatrix = new Matrix();
		setWillNotDraw(true);

		mImageView = new ImageView(getContext());

		addView(mImageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));

		onImageParametersChanged();
	}

	public void setImageDrawable(Drawable drawable) {
		if (mDrawable != drawable) {
			mDrawable = drawable;
			onImageParametersChanged();
		}
	}

	@Override
	public boolean hasImageDrawable() {
		return mDrawable != null;
	}

	public static int fixDefaultValueOnAlignment(int alignment) {
		// Add default values if not specified, to make sure comparison works.
		if ((alignment & Alignment.HORIZONTAL_MASK) == 0)
			alignment |= Alignment.CENTER_HORIZONTAL;

		if ((alignment & Alignment.VERTICAL_MASK) == 0)
			alignment |= Alignment.CENTER_VERTICAL;

		return alignment;
	}

	public void setAlignment(int alignment) {
		alignment = fixDefaultValueOnAlignment(alignment);
		if (mAlignment != alignment) {
			mAlignment = alignment;

			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
			lp.gravity = alignment;
			mImageView.setLayoutParams(lp);

			// Gravity affects both the parent layout and the imageview itself.
			onImageParametersChanged();
		}
	}

	@Override
	public void setImageScaleType(ImageScaleType scaleType) {
		if (mScaleType != scaleType) {
			mScaleType = scaleType;
			onImageParametersChanged();
		}
	}

	@Override
	public void setImageSize(int width, int height) {
		if (!PrimitiveUtils.areEquals(mImageWidth, width) || !PrimitiveUtils.areEquals(mImageHeight, height)) {
			mImageWidth = width;
			mImageHeight = height;

			// Image size affects the parent layout, since we need to resize the ImageView.
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
			lp.width = (mImageWidth != null ? mImageWidth : FrameLayout.LayoutParams.MATCH_PARENT);
			lp.height = (mImageHeight != null ? mImageHeight : FrameLayout.LayoutParams.MATCH_PARENT);
			mImageView.setLayoutParams(lp);

			// It may also affect the ImageView, if we need to calculate a new matrix.
			getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					getViewTreeObserver().removeOnGlobalLayoutListener(this);
					onImageParametersChanged();
				}
			});
		}
	}

	@Override
	public void setImagePropertiesFromThemeClass(ThemeClassDefinition themeClass) {
		boolean controlChanged = false;
		if (themeClass.getMaxCornersRadius() > 0) {
			if (!(mImageView instanceof RoundedImageView)) {
				ViewGroup.LayoutParams params = mImageView.getLayoutParams();
				removeView(mImageView);
				mImageView = new RoundedImageView(getContext());
				addView(mImageView, params);
				controlChanged = true;
			}
			RoundedImageView roundedImageView = (RoundedImageView) mImageView;
			Integer[] corners = themeClass.getCornersRadius();
			// rounded image view order different the corner.
			// temporary use only setcornerRadius because multiple corner do not work ok.
			//roundedImageView.setCornerRadius(corners[0], corners[1], corners[3], corners[2]);
			roundedImageView.setCornerRadius(themeClass.getMaxCornersRadius());
		} else {
			if (mImageView instanceof RoundedImageView) {
				ViewGroup.LayoutParams params = mImageView.getLayoutParams();
				removeView(mImageView);
				mImageView = new ImageView(getContext());
				addView(mImageView, params);
				controlChanged = true;
			}
		}
		// call to onImageParametersChanged
		setImageScaleType(themeClass.getImageScaleType());

		Integer width = themeClass.getImageWidth();
		Integer height = themeClass.getImageHeight();
		if (width != null && height != null)
			setImageSize(width, height);

		//force call to onImageParameterChanged if control was changed
		if (controlChanged)
			onImageParametersChanged();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (changed)
			onImageParametersChanged();
	}

	private void onImageParametersChanged() {
		mImageView.setImageDrawable(mDrawable);

		// Cleanup: remove any previous tiling mode possibly applied to the drawable.
		if (mScaleType != ImageScaleType.TILE && mDrawable instanceof BitmapDrawable)
			((BitmapDrawable) mDrawable).setTileModeXY(null, null);

		if (isAutogrow()) {
			// Autogrow is a special case: just let the ImageView grow as needed, vertically.
			mImageView.setAdjustViewBounds(true);
		} else if (mScaleType == ImageScaleType.FILL) {
			mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
		} else if (mScaleType == ImageScaleType.FIT && mAlignment == Alignment.CENTER) {
			mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		} else if (mScaleType == ImageScaleType.FIT && mAlignment == (Alignment.START | Alignment.TOP)) {
			mImageView.setScaleType(ImageView.ScaleType.FIT_START);
		} else if (mScaleType == ImageScaleType.FIT && mAlignment == (Alignment.END | Alignment.BOTTOM)) {
			mImageView.setScaleType(ImageView.ScaleType.FIT_END);
		} else if (mScaleType == ImageScaleType.FILL_KEEPING_ASPECT && mAlignment == Alignment.CENTER) {
			mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else if (mScaleType == ImageScaleType.NO_SCALE && mAlignment == Alignment.CENTER) {
			mImageView.setScaleType(ImageView.ScaleType.CENTER);
		} else if (mScaleType == ImageScaleType.TILE) {
			// It doesn't make sense to tile a drawable that isn't a bitmap
			// (since it will have no intrinsic dimensions).
			if (mDrawable instanceof BitmapDrawable)
				((BitmapDrawable) mDrawable).setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

			mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
		} else {
			// Needs a matrix.
			calculateMatrix();
		}
	}

	private boolean isAutogrow() {
		return (mAutogrow &&
				mImageWidth == null && mImageHeight == null &&
				(mScaleType == ImageScaleType.FILL_KEEPING_ASPECT || mScaleType == ImageScaleType.FIT));
	}

	protected void setAutogrow(boolean autogrow) {
		mAutogrow = autogrow;
	}

	private void calculateMatrix() {
		if (mDrawable == null)
			return; // We don't have a drawable to calculate measurements.

		// Note: No need to consider padding, the inner view never has padding.
		float viewWidth = mImageView.getWidth();
		float viewHeight = mImageView.getHeight();

		// Use custom dimensions if applied.
		if (mImageWidth != null)
			viewWidth = mImageWidth;
		if (mImageHeight != null)
			viewHeight = mImageHeight;

		if (viewWidth == 0 && viewHeight == 0)
			return; // If the view's bounds aren't known yet, hold off on until they are.

		float drawWidth = mDrawable.getIntrinsicWidth();
		float drawHeight = mDrawable.getIntrinsicHeight();

		if (drawWidth <= 0 || drawHeight <= 0) {
			// For a drawable with no intrinsic size (e.g. a solid color), the only scale
			// type that makes sense is FILL.
			mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
			return;
		}

		// Some cases (such as FILL or TILE) have already been resolved without the need for a matrix.
		// We handle the remaining cases here (basically finding the scale and computing the translation
		// from the desired alignment).
		mMatrix.reset();
		BitmapUtils.computeScalingMatrix(drawWidth, drawHeight, viewWidth, viewHeight, mScaleType, mAlignment, mMatrix);

		mImageView.setScaleType(ImageView.ScaleType.MATRIX);
		mImageView.setImageMatrix(mMatrix);
	}

}
