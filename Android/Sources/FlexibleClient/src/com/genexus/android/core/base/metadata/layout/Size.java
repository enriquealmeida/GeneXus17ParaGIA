package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class Size
{
	private final int mWidth;
	private final int mHeight;

	public Size(int width, int height)
	{
		mWidth = width;
		mHeight = height;
	}

	public static int getPixels(float parentDimension, String gxSize)
	{
		if (gxSize.contains("%"))
		{
			float percentage = Float.parseFloat(gxSize.substring(0, gxSize.length() - 1).trim());
			return (int)((parentDimension * percentage) / 100);
		}
		else
		{
			String sIntValue = gxSize.replace("dip", Strings.EMPTY);
			float dipValue = Float.parseFloat(sIntValue);
			return Services.Device.dipsToPixels((int) dipValue);
		}
	}

	public Size minusWidth(int width)
	{
		return new Size(mWidth - width, mHeight);
	}

	public Size plusWidth(int width)
	{
		return new Size(mWidth + width, mHeight);
	}

	public Size minusHeight(int height)
	{
		return new Size(mWidth, mHeight - height);
	}

	public Size plusHeight(int height)
	{
		return new Size(mWidth, mHeight + height);
	}

	public int getWidth() { return mWidth; }
	public int getHeight() { return mHeight; }
}
