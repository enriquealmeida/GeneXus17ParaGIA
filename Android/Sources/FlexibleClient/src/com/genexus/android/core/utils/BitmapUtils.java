package com.genexus.android.core.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.enums.ImageScaleType;

public class BitmapUtils
{
	public static Bitmap createFromDrawable(Drawable drawable)
	{
		if (drawable == null)
			return null;

		if (drawable instanceof BitmapDrawable)
			return ((BitmapDrawable)drawable).getBitmap();

		int width = drawable.getIntrinsicWidth();
		if (width == -1) // e.g. for ColorDrawable.
			width = 1;

		int height = drawable.getIntrinsicHeight();
		if (height == -1)
			height = 1;

		// NOTE: Although the following code is a bit expensive, it will not be necessary
		// for most drawables, since they are normally bitmap resources.
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * Creates a (possibly new) Bitmap with the specified dimensions and applying the desired scale type
	 * on the source bitmap. May return the source bitmap itself if the dimensions are unchanged.
	 */
	public static Bitmap createScaledBitmap(Resources res, Bitmap src, int width, int height, ImageScaleType scaleType)
	{
		if (width == src.getWidth() && height == src.getHeight())
			return src;

		if (scaleType == ImageScaleType.FILL)
		{
			// Easiest, just scale the bitmap to fill the desired area.
			return Bitmap.createScaledBitmap(src, width, height, true);
		}
		else if (scaleType == ImageScaleType.FIT || scaleType == ImageScaleType.FILL_KEEPING_ASPECT || scaleType == ImageScaleType.NO_SCALE)
		{
			// Calculate a matrix to resize the bitmap.
			Matrix matrix = new Matrix();
			computeScalingMatrix(src.getWidth(), src.getHeight(), width, height, scaleType, Alignment.CENTER, matrix);
			return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		}
		else if (scaleType == ImageScaleType.TILE)
		{
			// TILE is a very special case, since we need a canvas to draw the bitmap multiple times.
			Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(dest);

			BitmapDrawable tmpTile = new BitmapDrawable(res, src);
			tmpTile.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			tmpTile.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

			tmpTile.draw(canvas);
			return dest;
		}
		else
			throw new IllegalArgumentException("Unknown ImageScaleType value: " + String.valueOf(scaleType));
	}

	/**
	 * Computes the matrix necessary to scale a source bitmap into a specified area,
	 * using a particular scale type and alignment.
	 */
	public static void computeScalingMatrix(float srcWidth, float srcHeight, float dstWidth, float dstHeight, ImageScaleType scaleType, int alignment, Matrix matrix)
	{
		if (srcWidth <= 0 || srcHeight <= 0)
			throw new IllegalArgumentException("Invalid srcWidth or srcHeight");

		matrix.reset();

		// Some cases (such as TILE) do not need a matrix, so they are not supported here.
		// We handle the remaining cases here (basically finding the scale and computing the translation
		// from the desired alignment).
		if (scaleType == ImageScaleType.FIT)
		{
            float scale;
            float dx = 0;
            float dy = 0;

			// Find the smallest scale to reach edges.
            if (srcWidth * dstHeight < dstWidth * srcHeight)
            {
            	scale = dstHeight / srcHeight;
                dx = computeTranslationFromAlignment(srcWidth, dstWidth, scale, alignment & Alignment.HORIZONTAL_MASK);
            }
            else
            {
            	scale = dstWidth / srcWidth;
                dy = computeTranslationFromAlignment(srcHeight, dstHeight, scale, alignment & Alignment.VERTICAL_MASK);
            }

			matrix.setScale(scale, scale);
			matrix.postTranslate((int)(dx + 0.5f), (int)(dy + 0.5f));
		}
		else if (scaleType == ImageScaleType.FILL)
		{
			float scaleX = dstWidth / srcWidth;
			float scaleY = dstHeight / srcHeight;
			matrix.setScale(scaleX, scaleY);
		}
		else if (scaleType == ImageScaleType.FILL_KEEPING_ASPECT)
		{
            float scale;
            float dx = 0;
            float dy = 0;

            // Find the largest scale to reach edges.
            if (srcWidth * dstHeight > dstWidth * srcHeight)
            {
                scale = dstHeight / srcHeight;
                dx = computeTranslationFromAlignment(srcWidth, dstWidth, scale, alignment & Alignment.HORIZONTAL_MASK);
            }
            else
            {
                scale = dstWidth / srcWidth;
                dy = computeTranslationFromAlignment(srcHeight, dstHeight, scale, alignment & Alignment.VERTICAL_MASK);
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate((int)(dx + 0.5f), (int)(dy + 0.5f));
		}
		else if (scaleType == ImageScaleType.NO_SCALE)
		{
			// No scale, just translate according to alignment.
			float dx = computeTranslationFromAlignment(srcWidth, dstWidth, 1.0f, alignment & Alignment.HORIZONTAL_MASK);
			float dy = computeTranslationFromAlignment(srcHeight, dstHeight, 1.0f, alignment & Alignment.VERTICAL_MASK);

			matrix.postTranslate((int)(dx + 0.5f), (int)(dy + 0.5f));
		}
		else
			throw new IllegalStateException("Invalid value for scaleType at this point: " + String.valueOf(scaleType));
	}

	private static float computeTranslationFromAlignment(float srcMeasure, float dstMeasure, float scale, int alignment)
	{
        // This is the same calculation for width and height.
        switch (alignment)
        {
        	case Alignment.LEFT :
        	case Alignment.TOP :
        		return 0f;

        	case Alignment.CENTER_HORIZONTAL :
        	case Alignment.CENTER_VERTICAL :
        		return (dstMeasure - srcMeasure * scale) * 0.5f;

        	case Alignment.RIGHT :
        	case Alignment.BOTTOM :
        		return dstMeasure - srcMeasure * scale;

    		default :
    			throw new IllegalArgumentException("Invalid alignment value: " + String.valueOf(alignment));
        }
	}
}
