package com.genexus.android.core.usercontrols.imagemap;

import android.content.Context;
import android.util.AttributeSet;

import com.genexus.android.core.usercontrols.image.ImageViewTouch;

public class ImageMapTouch extends ImageViewTouch
{

	private OnImageZoomedListener mZoomListener;

	public ImageMapTouch(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void addZoomListener(OnImageZoomedListener listener){
		mZoomListener = listener;
	}


	@Override
	protected void panBy(double dx, double dy)
	{
		if (isEnabled())
		{
			super.panBy(dx, dy);

			if (mZoomListener != null)
			{
				//Services.Log.debug("scroll rect " + getScrollRect().left + " , " + getScrollRect().top );
				//Services.Log.debug("dx , dy " + dx + " , " + dy );
				//Services.Log.debug("scale " + this.getScale());

				// do not scroll if scale is 1, could miss position of item in image map.
				if (this.getScale() <= 1)
					return;

				mZoomListener.panBy(getScrollRect().left, getScrollRect().top);
			}
		}
	}


	@Override
	protected void onLayout( boolean changed, int left, int top, int right, int bottom ) {
		super.onLayout(changed, left, top, right, bottom);

		if (mZoomListener != null )
			mZoomListener.bitmapLoaded();
	}

	@Override
	protected void zoomTo( float scale, float centerX, float centerY )
	{
		if (isEnabled())
		{
			super.zoomTo(scale, centerX, centerY);

			if (mZoomListener != null)
				mZoomListener.zoom(scale);
		}
	}

	public interface OnImageZoomedListener
	{
		void panBy(double dx, double dy);
		void zoom(float scale);
		void bitmapLoaded();
	}
}

