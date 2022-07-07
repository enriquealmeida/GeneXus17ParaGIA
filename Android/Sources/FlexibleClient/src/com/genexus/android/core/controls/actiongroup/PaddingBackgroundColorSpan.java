package com.genexus.android.core.controls.actiongroup;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;

public class PaddingBackgroundColorSpan extends ReplacementSpan {

	private Integer mBackgroundColor;
	private Integer mForegroundColor;
	private LayoutBoxMeasures mPadding;
	private Rect mRect;

	public PaddingBackgroundColorSpan(Integer backgroundColor, Integer foregroundColor, LayoutBoxMeasures padding) {
		super();
		mBackgroundColor = backgroundColor;
		mForegroundColor = foregroundColor;
		mPadding = padding;
		mRect = new Rect();
	}

	@Override
	public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
		return Math.round(paint.measureText(text, start, end)) + mPadding.left + mPadding.right;
	}

	@Override
	public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
		int textWidth = Math.round(paint.measureText(text, start, end));
		int paintColor = paint.getColor();

		mRect.set(start - mPadding.left,
					top - mPadding.top / 2,
					end + textWidth + mPadding.right,
					bottom + mPadding.bottom / 2);

		paint.setColor(mBackgroundColor);
		canvas.drawRect(mRect, paint);

		if (mForegroundColor != null)
			paintColor = mForegroundColor;

		paint.setColor(paintColor);
		canvas.drawText(text.toString(), x + mPadding.left / 2, y, paint);
	}
}
