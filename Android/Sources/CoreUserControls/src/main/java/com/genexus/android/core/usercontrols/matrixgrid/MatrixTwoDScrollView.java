package com.genexus.android.core.usercontrols.matrixgrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;

import com.genexus.android.core.usercontrols.R;

public class MatrixTwoDScrollView extends TwoDScrollView
{
	private Rect mDrawingRect = new Rect();
	private Rect mForegroundRect = new Rect();
	private Drawable mShadowsDrawable;
	
	private BitmapShader mIndicatorShader;
	private Paint mIndicatorPaint;
	private Path mIndicatorPath;
	private int mIndicatorBitmapHeight;
	private int mSelectedRowY;
	private int mSelectedRowHeight;

	private static final int INDICATOR_HEIGHT = 40;
	private static final int INDICATOR_WIDTH = 20;
	
	public MatrixTwoDScrollView(Context context)
	{
		super(context);
		initShadowDrawable();
	}
	
	public MatrixTwoDScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initShadowDrawable();
	}

	public MatrixTwoDScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initShadowDrawable();
	}

	private void initShadowDrawable()
	{
		mShadowsDrawable = ContextCompat.getDrawable(getContext(), R.drawable.gx_matrix_grid_shadows);
	}

	public void setIndicatorBitmap(Bitmap bmp)
	{
		if (bmp != null)
		{
			mIndicatorShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
			mIndicatorBitmapHeight = bmp.getHeight() / 2;
			
			mIndicatorPaint = new Paint();
			mIndicatorPaint.setStyle(Style.FILL);
			
			// Draw path considering mIndicatorBitmapHeight so that the correct (i.e. center) part of the bitmap is shown.
			mIndicatorPath = new Path();
			mIndicatorPath.setFillType(Path.FillType.EVEN_ODD);
			mIndicatorPath.moveTo(0, mIndicatorBitmapHeight - INDICATOR_HEIGHT / 2);
			mIndicatorPath.lineTo(0, mIndicatorBitmapHeight + INDICATOR_HEIGHT / 2);
			mIndicatorPath.lineTo(INDICATOR_WIDTH, mIndicatorBitmapHeight);
			mIndicatorPath.close();
		}
		else
			mIndicatorShader = null;
	}
	
	public void setSelectedRow(int rowY, int rowHeight)
	{
		mSelectedRowY = rowY;
		mSelectedRowHeight = rowHeight;
	}
	
	@Override
	public void draw(@NonNull Canvas canvas)
	{
		super.draw(canvas);
		
		// Draw the border shadow.
		getDrawingRect(mDrawingRect);
	    Gravity.apply(Gravity.FILL, mShadowsDrawable.getIntrinsicWidth(), mShadowsDrawable.getIntrinsicHeight(), mDrawingRect, mForegroundRect);
	    mShadowsDrawable.setBounds(mForegroundRect);
		mShadowsDrawable.draw(canvas);		
		
		// Draw the indicator for the selected row (based on current scroll).
		if (mIndicatorShader != null)
		{
			canvas.save();
			canvas.translate(getScrollX(), mSelectedRowY + mSelectedRowHeight / 2 - mIndicatorBitmapHeight);

			mIndicatorPaint.setShader(mIndicatorShader);
			canvas.drawPath(mIndicatorPath, mIndicatorPaint);
			mIndicatorPaint.setShader(null);
			
			canvas.restore();
		}
	}
}
