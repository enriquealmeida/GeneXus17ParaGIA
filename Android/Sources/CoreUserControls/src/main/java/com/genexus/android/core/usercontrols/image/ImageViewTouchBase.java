package com.genexus.android.core.usercontrols.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import com.genexus.android.core.base.services.Services;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Base View to manage image zoom/scroll/pinch operations
 */
public class ImageViewTouchBase extends AppCompatImageView implements IDisposable {

	public interface OnBitmapChangedListener {

		void onBitmapChanged( Drawable drawable );
	}

	public static final String LOG_TAG = "image";

	protected Easing mEasing = new Cubic();
	protected Matrix mBaseMatrix = new Matrix();
	protected Matrix mSuppMatrix = new Matrix();
	protected Handler mHandler = new Handler(Looper.getMainLooper());
	protected Runnable mOnLayoutRunnable = null;
	protected boolean mZoomOutsideControl = false;
	protected float mMaxZoom;
	protected float mMinZoom = -1;
	protected final Matrix mDisplayMatrix = new Matrix();
	protected final float[] mMatrixValues = new float[9];
	protected int mThisWidth = -1;
	protected int mThisHeight = -1;
	protected boolean mFitToScreen = false;
	protected static final float MAX_ZOOM = 10.0f;
	protected static final float MIN_ZOOM = 0.9f;
	protected static final int DEFAULT_ANIMATION_DURATION = 200;

	protected RectF mBitmapRect = new RectF();
	protected RectF mCenterRect = new RectF();
	protected RectF mScrollRect = new RectF();

	private OnBitmapChangedListener mListener;

	ZoomInImageViewAttacher mZoomInImageViewAttacher;

	public ImageViewTouchBase( Context context ) {
		super( context );
		init();
	}

	public ImageViewTouchBase( Context context, AttributeSet attrs ) {
		super( context, attrs );
		init();
	}

	public void setOnBitmapChangedListener( OnBitmapChangedListener listener ) {
		mListener = listener;
	}

	protected void init() {
		setScaleType( AppCompatImageView.ScaleType.MATRIX );
	}

	public void clear() {
		setImageBitmap( null, true );
	}

	public void setFitToScreen( boolean value ) {
		if ( value != mFitToScreen ) {
			mFitToScreen = value;
			requestLayout();
		}
	}

	public void setMinZoom( float value ) {
		mMinZoom = value;
	}

	public void setMaxZoom( float value ) {
		mMaxZoom = value;
	}

	public void setZoomOutsideControl(boolean zoomOutsideControl) {
		mZoomOutsideControl = zoomOutsideControl;
		if (mZoomOutsideControl) {
			mZoomInImageViewAttacher = new ZoomInImageViewAttacher(this);
		} else {
			if (mZoomInImageViewAttacher != null) {
				mZoomInImageViewAttacher.detach();
			}
		}
	}

	public boolean getZoomOutsideControl() {
		return mZoomOutsideControl;
	}

	public RectF getScrollRect(){
		return mScrollRect;
	}

	@Override
	protected void onLayout( boolean changed, int left, int top, int right, int bottom ) {
		super.onLayout( changed, left, top, right, bottom );
		mThisWidth = right - left;
		mThisHeight = bottom - top;
		Runnable r = mOnLayoutRunnable;
		if ( r != null ) {
			mOnLayoutRunnable = null;
			r.run();
		}
		if ( getDrawable() != null ) {
			if ( mFitToScreen ) {
				getProperBaseMatrixForFitToScreen( getDrawable(), mBaseMatrix );
				setMinZoom( 1.0f );
			} else {
				getProperBaseMatrix( getDrawable(), mBaseMatrix );
				setMinZoom( getMinZoom() );
			}
			setImageMatrix( getImageViewMatrix() );
			zoomTo( getMinZoom() );
		}
	}

	@Override
	public void setImageBitmap( Bitmap bm ) {
		setImageBitmap( bm, true );
	}


	@SuppressWarnings("deprecation")
	@Override
	public void setImageResource( int resId ) {
		setImageDrawable( getContext().getResources().getDrawable( resId ) );
	}

	/**
	 * Set the new image to display and reset the internal matrix.
	 *
	 * @param bitmap
	 *           - the {@link Bitmap} to display
	 * @param reset
	 *           - if true the image bounds will be recreated, otherwise the old {@link Matrix} is used to display the new bitmap
	 * @see #setImageBitmap(Bitmap)
	 */
	public void setImageBitmap( final Bitmap bitmap, final boolean reset ) {
		setImageBitmap( bitmap, reset, null );
	}

	/**
	 * Similar to {@link #setImageBitmap(Bitmap, boolean)} but an optional view {@link Matrix} can be passed to determine the new
	 * bitmap view matrix.<br>
	 * This method is useful if you need to restore a Bitmap with the same zoom/pan values from a previous state
	 *
	 * @param bitmap
	 *           - the {@link Bitmap} to display
	 * @param reset
	 * @param matrix
	 *           - the {@link Matrix} to be used to display the new bitmap
	 *
	 * @see #setImageBitmap(Bitmap, boolean)
	 * @see #setImageBitmap(Bitmap)
	 * @see #getImageViewMatrix()
	 * @see #getDisplayMatrix()
	 */
	public void setImageBitmap( final Bitmap bitmap, final boolean reset, Matrix matrix ) {
		setImageBitmap( bitmap, reset, matrix, -1 );
	}

	/**
	 *
	 * @param bitmap
	 * @param reset
	 * @param matrix
	 * @param maxZoom
	 *           - maximum zoom allowed during zoom gestures
	 *
	 * @see #setImageBitmap(Bitmap, boolean, Matrix)
	 */
	public void setImageBitmap( final Bitmap bitmap, final boolean reset, Matrix matrix, float maxZoom ) {

		if ( bitmap != null )
			setImageDrawable( new FastBitmapDrawable( bitmap ), reset, matrix, maxZoom );
		else
			setImageDrawable( null, reset, matrix, maxZoom );
	}

	@Override
	public void setImageDrawable( Drawable drawable ) {
		setImageDrawable( drawable, true, null, -1 );
	}

	public void setImageDrawable( final Drawable drawable, final boolean reset, final Matrix initialMatrix, final float maxZoom ) {

		final int viewWidth = getWidth();

		if ( viewWidth <= 0 ) {
			mOnLayoutRunnable = new Runnable() {

				@Override
				public void run() {
					setImageDrawable( drawable, reset, initialMatrix, maxZoom );
				}
			};
			return;
		}

		setImageDrawable2( drawable, reset, initialMatrix, maxZoom );
	}

	protected void setImageDrawable2(final Drawable drawable, final boolean reset, final Matrix initialMatrix, final float maxZoom ) {

		if ( drawable != null ) {
			if ( mFitToScreen ) {
				getProperBaseMatrixForFitToScreen( drawable, mBaseMatrix );
				setMinZoomForFitToScreen(drawable, mBaseMatrix);
			} else {
				getProperBaseMatrix( drawable, mBaseMatrix );
				setMinZoom( getMinZoom() );
			}
			super.setImageDrawable( drawable );
		} else {
			mBaseMatrix.reset();
			super.setImageDrawable( null );
		}

		if ( reset ) {
			mSuppMatrix.reset();
			if ( initialMatrix != null ) {
				mSuppMatrix = new Matrix( initialMatrix );
			}
		}

		setImageMatrix( getImageViewMatrix() );

		/*if ( maxZoom < 1 )
			mMaxZoom = maxZoom();
		else
			mMaxZoom = maxZoom;*/

		onBitmapChanged( drawable );
	}

	protected void onBitmapChanged( final Drawable bitmap ) {
		if ( mListener != null ) {
			mListener.onBitmapChanged( bitmap );
		}
	}

	/**
	 * compute the max allowed zoom fator
	 *
	 * @return
	 */
	protected float maxZoom() {
		final Drawable drawable = getDrawable();

		if ( drawable == null ) {
			return 1F;
		}

		float fw = (float) drawable.getIntrinsicWidth() / (float) mThisWidth;
		float fh = (float) drawable.getIntrinsicHeight() / (float) mThisHeight;
		float max = Math.max( fw, fh ) * 4;
		return max;
	}

	protected float minZoom() {
		return 1F;
	}

	public float getMaxZoom() {
		if ( mMaxZoom < 1 ) {
			mMaxZoom = maxZoom();
		}
		return mMaxZoom;
	}

	public float getMinZoom() {
		if ( mMinZoom < 0 ) {
			mMinZoom = minZoom();
		}
		return mMinZoom;
	}

	public Matrix getImageViewMatrix() {
		return getImageViewMatrix( mSuppMatrix );
	}

	public Matrix getImageViewMatrix( Matrix supportMatrix ) {
		mDisplayMatrix.set( mBaseMatrix );
		mDisplayMatrix.postConcat( supportMatrix );
		return mDisplayMatrix;
	}

	/**
	 * Returns the current image display matrix. This matrix can be used in the next call to the
	 * {@link #setImageBitmap(Bitmap, boolean, Matrix)} to restore the same view state of the previous {@link Bitmap}
	 *
	 * @return
	 */
	public Matrix getDisplayMatrix() {
		return new Matrix( mSuppMatrix );
	}


	private float mScale;
	private float mOffsetX;
	private float mOffsetY;

	public float getScaleRatio(){
		return mScale;
	}

	public float getOffsetX(){
		return mOffsetX;
	}

	public float getOffsetY(){
		return mOffsetY;
	}

	/**
	 * Setup the base matrix so that the image is centered and scaled properly.
	 */
	protected void getProperBaseMatrix( Drawable drawable, Matrix matrix ) {
		Services.Log.info( LOG_TAG, "getProperBaseMatrix" );
		float viewWidth = getWidth();
		float viewHeight = getHeight();
		float w = drawable.getIntrinsicWidth();
		float h = drawable.getIntrinsicHeight();
		matrix.reset();

		float tw;
		float th;

		//Log.d( LOG_TAG, "image size : " +  viewWidth +" x " + viewHeight);
		//Log.d( LOG_TAG, "image Intrinsic size : " +  w +" x " + h);

		if ( w > viewWidth || h > viewHeight ) {
			float widthScale = Math.min( viewWidth / w, 2.0f );
			float heightScale = Math.min( viewHeight / h, 2.0f );
			float scale = Math.min( widthScale, heightScale );
			//Log.d( LOG_TAG, "scale: " + scale );
			matrix.postScale( scale, scale );
			tw = ( viewWidth - w * scale ) / 2.0f;
			th = ( viewHeight - h * scale ) / 2.0f;
			matrix.postTranslate( tw, th );

			mScale = scale;

		} else {
			tw = ( viewWidth - w ) / 2.0f;
			th = ( viewHeight - h ) / 2.0f;
			matrix.postTranslate( tw, th );
			//Log.d( LOG_TAG, "scale: null" );

			mScale = 1;

		}

		mOffsetX = tw;
		mOffsetY = th;
	}

	/**
	 * Setup the base matrix so that the image is centered and scaled properly.
	 *
	 * @param bitmap
	 * @param matrix
	 */
	protected void getProperBaseMatrixForFitToScreen(Drawable bitmap, Matrix matrix ) {
		float viewWidth = getWidth();
		float viewHeight = getHeight();
		float w = bitmap.getIntrinsicWidth();
		float h = bitmap.getIntrinsicHeight();
		matrix.reset();
		float widthScale = Math.min( viewWidth / w, MAX_ZOOM );
		float heightScale = Math.min( viewHeight / h, MAX_ZOOM );
		float scale = Math.min( widthScale, heightScale );
		matrix.postScale( scale, scale );
		matrix.postTranslate( ( viewWidth - w * scale ) / MAX_ZOOM, ( viewHeight - h * scale ) / MAX_ZOOM );
	}

	protected void setMinZoomForFitToScreen(Drawable bitmap, Matrix baseMatrix)
	{
		setMinZoom(getScale(baseMatrix));
	}

	protected float getValue( Matrix matrix, int whichValue ) {
		matrix.getValues( mMatrixValues );
		return mMatrixValues[whichValue];
	}

	public RectF getBitmapRect() {
		return getBitmapRect( mSuppMatrix );
	}

	protected RectF getBitmapRect( Matrix supportMatrix ) {
		final Drawable drawable = getDrawable();

		if ( drawable == null ) return null;
		Matrix m = getImageViewMatrix( supportMatrix );
		mBitmapRect.set( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );
		m.mapRect( mBitmapRect );
		return mBitmapRect;
	}

	protected float getScale( Matrix matrix ) {
		return getValue( matrix, Matrix.MSCALE_X );
	}

	@Override
	public float getRotation() {
		return 0;
	}

	public float getScale() {
		return getScale( mSuppMatrix );
	}

	protected void center( boolean horizontal, boolean vertical ) {
		final Drawable drawable = getDrawable();

		if ( drawable == null ) return;
		RectF rect = getCenter( mSuppMatrix, horizontal, vertical );
		if ( rect.left != 0 || rect.top != 0 ) {
			postTranslate( rect.left, rect.top );
		}
	}

	public RectF getCenter( boolean horizontal, boolean vertical ) {
		return getCenter(mSuppMatrix, horizontal, vertical);
	}

	protected RectF getCenter( Matrix supportMatrix, boolean horizontal, boolean vertical ) {
		final Drawable drawable = getDrawable();

		if ( drawable == null ) return new RectF( 0, 0, 0, 0 );

		mCenterRect.set( 0, 0, 0, 0 );
		RectF rect = getBitmapRect( supportMatrix );
		float height = rect.height();
		float width = rect.width();
		float deltaX = 0;
		float deltaY = 0;
		if ( vertical ) {
			int viewHeight = getHeight();
			if ( height < viewHeight ) {
				deltaY = ( viewHeight - height ) / 2 - rect.top;
			} else if ( rect.top > 0 ) {
				deltaY = -rect.top;
			} else if ( rect.bottom < viewHeight ) {
				deltaY = getHeight() - rect.bottom;
			}
		}
		if ( horizontal ) {
			int viewWidth = getWidth();
			if ( width < viewWidth ) {
				deltaX = ( viewWidth - width ) / 2 - rect.left;
			} else if ( rect.left > 0 ) {
				deltaX = -rect.left;
			} else if ( rect.right < viewWidth ) {
				deltaX = viewWidth - rect.right;
			}
		}
		mCenterRect.set( deltaX, deltaY, 0, 0 );
		return mCenterRect;
	}

	protected void postTranslate( float deltaX, float deltaY ) {
		mSuppMatrix.postTranslate( deltaX, deltaY );
		setImageMatrix( getImageViewMatrix() );
	}

	protected void postScale( float scale, float centerX, float centerY ) {
		mSuppMatrix.postScale( scale, scale, centerX, centerY );
		setImageMatrix( getImageViewMatrix() );
	}

	protected void zoomTo( float scale ) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;
		zoomTo( scale, cx, cy );
	}

	public void zoomTo( float scale, float durationMs ) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;
		zoomTo( scale, cx, cy, durationMs );
	}

	protected void zoomTo( float scale, float centerX, float centerY ) {
		if (!getZoomOutsideControl()) {
			if (scale > mMaxZoom) scale = mMaxZoom;
			float oldScale = getScale();
			float deltaScale = scale / oldScale;

			postScale(deltaScale, centerX, centerY);
			onZoom(getScale());
			center(true, true);
		}
	}

	protected void onZoom( float scale ) {}

	protected void onZoomAnimationCompleted( float scale ) {}

	public void scrollBy( float x, float y ) {
		panBy( x, y );
	}

	protected void panBy( double dx, double dy ) {
		RectF rect = getBitmapRect();
		mScrollRect.set( (float) dx, (float) dy, 0, 0 );
		updateRect( rect, mScrollRect );
		postTranslate( mScrollRect.left, mScrollRect.top );
		center( true, true );
	}

	protected void updateRect( RectF bitmapRect, RectF scrollRect ) {
        if( bitmapRect==null )
            return;

		float width = getWidth();
		float height = getHeight();

		if ( bitmapRect.top >= 0 && bitmapRect.bottom <= height ) scrollRect.top = 0;
		if ( bitmapRect.left >= 0 && bitmapRect.right <= width ) scrollRect.left = 0;
		if ( bitmapRect.top + scrollRect.top >= 0 && bitmapRect.bottom > height ) scrollRect.top = (int) ( 0 - bitmapRect.top );
		if ( bitmapRect.bottom + scrollRect.top <= ( height - 0 ) && bitmapRect.top < 0 )
			scrollRect.top = (int) ( ( height - 0 ) - bitmapRect.bottom );
		if ( bitmapRect.left + scrollRect.left >= 0 ) scrollRect.left = (int) ( 0 - bitmapRect.left );
		if ( bitmapRect.right + scrollRect.left <= ( width - 0 ) ) scrollRect.left = (int) ( ( width - 0 ) - bitmapRect.right );
	}

	protected void scrollBy( float distanceX, float distanceY, final double durationMs ) {
		final double dx = distanceX;
		final double dy = distanceY;
		final long startTime = System.currentTimeMillis();
		mHandler.post( new Runnable() {

			double mOldX = 0;
			double mOldY = 0;

			@Override
			public void run() {
				long now = System.currentTimeMillis();
				double currentMs = Math.min( durationMs, now - startTime );
				double x = mEasing.easeOut( currentMs, 0, dx, durationMs );
				double y = mEasing.easeOut( currentMs, 0, dy, durationMs );
				panBy( ( x - mOldX), ( y - mOldY) );
				mOldX = x;
				mOldY = y;
				if ( currentMs < durationMs ) {
					mHandler.post( this );
				} else {
					RectF centerRect = getCenter( mSuppMatrix, true, true );
					if ( centerRect.left != 0 || centerRect.top != 0 ) scrollBy( centerRect.left, centerRect.top );
				}
			}
		} );
	}

	protected void zoomTo( float scale, float centerX, float centerY, final float durationMs ) {
		if ( scale > getMaxZoom() ) scale = getMaxZoom();
		final long startTime = System.currentTimeMillis();
		final float oldScale = getScale();

		final float deltaScale = scale - oldScale;

		Matrix m = new Matrix( mSuppMatrix );
		m.postScale( scale, scale, centerX, centerY );
		RectF rect = getCenter( m, true, true );

		final float destX = centerX + rect.left * scale;
		final float destY = centerY + rect.top * scale;

		mHandler.post( new Runnable() {

			@Override
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min( durationMs, now - startTime );
				float newScale = (float) mEasing.easeInOut( currentMs, 0, deltaScale, durationMs );
				zoomTo( oldScale + newScale, destX, destY );
				if ( currentMs < durationMs ) {
					mHandler.post( this );
				} else {
					onZoomAnimationCompleted( getScale() );
					center( true, true );
				}
			}
		} );
	}

	@Override
	public void dispose() {
		clear();
	}
}
