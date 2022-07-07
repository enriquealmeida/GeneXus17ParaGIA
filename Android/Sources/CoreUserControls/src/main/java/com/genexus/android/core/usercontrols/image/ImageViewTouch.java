package com.genexus.android.core.usercontrols.image;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ViewConfiguration;

import com.genexus.android.core.controls.GxTouchEvents;

public class ImageViewTouch extends ImageViewTouchBase {
	static final float SCROLL_DELTA_THRESHOLD = 1.0f;

	protected ScaleGestureDetector mScaleDetector;
	protected GestureDetector mGestureDetector;
	protected int mTouchSlop;
	protected float mCurrentScaleFactor;
	protected float mScaleFactor;
	protected int mDoubleTapDirection;
	protected OnGestureListener mGestureListener;
	protected OnScaleGestureListener mScaleListener;
	protected boolean mDoubleTapEnabled = true;
	protected boolean mScaleEnabled = true;
	protected boolean mScrollEnabled = true;
	private OnImageViewTouchTapListener mTapListener;

	public ImageViewTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void init() {
		super.init();
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		mGestureListener = getGestureListener();
		mScaleListener = getScaleListener();

		mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
		mGestureDetector = new GestureDetector(getContext(), mGestureListener, null, true);

		mCurrentScaleFactor = 1f;
		mDoubleTapDirection = 1;
	}

	public void setTapListener(OnImageViewTouchTapListener listener) {
		mTapListener = listener;
	}

	public void setDoubleTapEnabled(boolean value) {
		mDoubleTapEnabled = value;
	}

	public void setScaleEnabled(boolean value) {
		mScaleEnabled = value;
	}

	public void setScrollEnabled(boolean value) {
		mScrollEnabled = value;
	}

	public boolean getDoubleTapEnabled() {
		return mDoubleTapEnabled;
	}

	protected OnGestureListener getGestureListener() {
		return new GestureListener();
	}

	protected OnScaleGestureListener getScaleListener() {
		return new ScaleListener();
	}

	public float getCurrentScaleFactor() {
		return mCurrentScaleFactor;
	}

	@Override
	protected void onBitmapChanged(Drawable drawable) {
		super.onBitmapChanged(drawable);

		float[] v = new float[9];
		mSuppMatrix.getValues(v);
		mCurrentScaleFactor = v[Matrix.MSCALE_X];
	}

	@Override
	protected void setImageDrawable2(final Drawable drawable, final boolean reset, final Matrix initialMatrix, final float maxZoom) {
		super.setImageDrawable2(drawable, reset, initialMatrix, maxZoom);
		mScaleFactor = getMaxZoom() / 3;
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		if (!getZoomOutsideControl()) {
			mScaleDetector.onTouchEvent(event);
			int action = event.getAction();
			switch (action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_UP:
					if (getScale() < getMinZoom()) {
						zoomTo(getMinZoom(), 50);
					}
					break;
			}
		}

		if (!mScaleDetector.isInProgress()) mGestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	protected void onZoom(float scale) {
		super.onZoom(scale);
		if (!mScaleDetector.isInProgress()) mCurrentScaleFactor = scale;
	}

	@Override
	protected void onZoomAnimationCompleted(float scale) {
		super.onZoomAnimationCompleted(scale);
		if (!mScaleDetector.isInProgress()) mCurrentScaleFactor = scale;

		if (scale < getMinZoom()) {
			zoomTo(getMinZoom(), 50);
		}
	}

	protected float onDoubleTapPost(float scale, float maxZoom) {
		if (mDoubleTapDirection == 1) {
			if ((scale + (mScaleFactor * 2)) <= maxZoom) {
				return scale + mScaleFactor;
			} else {
				mDoubleTapDirection = -1;
				return maxZoom;
			}
		} else {
			mDoubleTapDirection = 1;
			return 1f;
		}
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (!mScrollEnabled) return false;

		if (e1 == null || e2 == null) return false;
		if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
		if (mScaleDetector.isInProgress()) return false;
		if (getScale() == 1f) return false;

		// Log.d( LOG_TAG, "onScroll: " + distanceX + ", " + distanceY );
		scrollBy(-distanceX, -distanceY);
		invalidate();
		return true;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (!mScrollEnabled) return false;

		if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
		if (mScaleDetector.isInProgress()) return false;

		float diffX = e2.getX() - e1.getX();
		float diffY = e2.getY() - e1.getY();

		if (Math.abs(velocityX) > 800 || Math.abs(velocityY) > 800) {
			scrollBy(diffX / 2, diffY / 2, 300);
			invalidate();
			return true;
		}
		return false;
	}

	/**
	 * Determines whether this ImageViewTouch can be scrolled.
	 *
	 * @param direction - positive direction value means scroll from right to left,
	 *                  negative value means scroll from left to right
	 * @return true if there is some more place to scroll, false - otherwise.
	 */
	public boolean canScroll(int direction) {
		RectF bitmapRect = getBitmapRect();
		updateRect(bitmapRect, mScrollRect);
		Rect imageViewRect = new Rect();
		getGlobalVisibleRect(imageViewRect);

		if (bitmapRect.right >= imageViewRect.right) {
			if (direction < 0) {
				return Math.abs(bitmapRect.right - imageViewRect.right) > SCROLL_DELTA_THRESHOLD;
			}
		}

		double bitmapScrollRectDelta = Math.abs(bitmapRect.left - mScrollRect.left);
		return bitmapScrollRectDelta > SCROLL_DELTA_THRESHOLD;
	}

	public class GestureListener extends GestureDetector.SimpleOnGestureListener {


		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {

			if (mTapListener != null)
				mTapListener.onTap(e);

			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// Log.i( LOG_TAG, "onDoubleTap. double tap enabled? " + mDoubleTapEnabled );
			if (mDoubleTapEnabled) {
				float scale = getScale();
				float targetScale = onDoubleTapPost(scale, getMaxZoom());
				targetScale = Math.min(getMaxZoom(), Math.max(targetScale, getMinZoom()));
				mCurrentScaleFactor = targetScale;
				zoomTo(targetScale, e.getX(), e.getY(), DEFAULT_ANIMATION_DURATION);
				invalidate();
			}

			if (mTapListener != null) {
				mTapListener.onDoubleTap();
			}

			return super.onDoubleTap(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (!mScaleDetector.isInProgress()) {
				setPressed(true);
				performLongClick();
				if (mTapListener != null)
					mTapListener.onLongTap(e);
			}
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return ImageViewTouch.this.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (mTapListener != null) {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (diffX > 0) {
						mTapListener.onSwipe(GxTouchEvents.SWIPE_RIGHT);
					} else {
						mTapListener.onSwipe(GxTouchEvents.SWIPE_LEFT);
					}
				} else {
					if (diffY > 0) {
						mTapListener.onSwipe(GxTouchEvents.SWIPE_DOWN);
					} else {
						mTapListener.onSwipe(GxTouchEvents.SWIPE_UP);
					}
				}
			}
			return ImageViewTouch.this.onFling(e1, e2, velocityX, velocityY);
		}
	}

	public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// Log.d( LOG_TAG, "onScale" );
			// float span = detector.getCurrentSpan() - detector.getPreviousSpan();
			float targetScale = mCurrentScaleFactor * detector.getScaleFactor();
			if (mScaleEnabled) {
				targetScale = Math.min(getMaxZoom(), Math.max(targetScale, getMinZoom() - 0.1f));
				zoomTo(targetScale, detector.getFocusX(), detector.getFocusY());
				mCurrentScaleFactor = Math.min(getMaxZoom(), Math.max(targetScale, getMinZoom() - 1.0f));
				mDoubleTapDirection = 1;
				invalidate();
				return true;
			}
			return false;
		}
	}

	public interface OnImageViewTouchTapListener {
		void onTap(MotionEvent e);

		void onDoubleTap();

		void onLongTap(MotionEvent e);

		void onSwipe(String swipeDirectionEvent);
	}
}
