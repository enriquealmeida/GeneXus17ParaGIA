package com.genexus.android.core.usercontrols.gauge;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

public class ColumnElement {

	private int mHeight;
	private int mWidth;
	private int mColor;
	private int mX;
	private int mY;
	
	public ColumnElement(int x, int y, int height, int width, int color) {
		mX = x;
		mY = y;
		mHeight = height;
		mWidth = width;
		mColor = color;
	}
	
	public void drawElement(Canvas canvas) {
		
		Paint p = new Paint() { { setStyle(Paint.Style.FILL);
									setAntiAlias(true);
									setStrokeWidth(1.5f);
									setColor(mColor);
		}};
		//float fontHeight = p.getTextSize();
		Rect rect = new Rect(mX, mY - mHeight, mX + mWidth, mY);
		p.setShader(getColumnShader(mColor, rect));
		canvas.drawRect(rect, p);
	}
	
	public static Shader getColumnShader(int color, Rect rect) {
		return new LinearGradient((rect.right - rect.left) / 2, rect.top, (rect.right - rect.left) / 2, rect.bottom, color, color, TileMode.MIRROR);
	}
	
	
	
	
}
