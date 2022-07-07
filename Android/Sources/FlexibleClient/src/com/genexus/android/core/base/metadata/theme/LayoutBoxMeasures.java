package com.genexus.android.core.base.metadata.theme;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.enums.MeasureUnit;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;

public class LayoutBoxMeasures implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final int left;
	public final int top;
	public final int right;
	public final int bottom;

	public static final LayoutBoxMeasures EMPTY = new LayoutBoxMeasures(0, 0, 0, 0);

	public static LayoutBoxMeasures from(PropertiesObject data, String measure)
	{
		return from(data, measure, 0);
	}

	public static LayoutBoxMeasures from(PropertiesObject data, String measure, int extra)
	{
		int left = Services.Device.dipsToPixels(parseIntPixelValue(data, measure + "_left"));
		int top = Services.Device.dipsToPixels(parseIntPixelValue(data, measure + "_top"));
		int right = Services.Device.dipsToPixels(parseIntPixelValue(data, measure + "_right"));
		int bottom = Services.Device.dipsToPixels(parseIntPixelValue(data, measure + "_bottom"));

		return new LayoutBoxMeasures(left + extra, top + extra, right + extra, bottom + extra);
	}

	public LayoutBoxMeasures(int aLeft, int aTop, int aRight, int aBottom)
	{
		left = aLeft;
		top = aTop;
		right = aRight;
		bottom = aBottom;
	}

	private static int parseIntPixelValue(PropertiesObject data, String property)
	{
		String strValue = data.optStringProperty(property);
		Integer value = Services.Strings.parseMeasureValue(strValue, MeasureUnit.DIP);
		return value!=null? value: 0;
	}

	public int getTotalVertical()
	{
		return top + bottom;
	}

	public int getTotalHorizontal()
	{
		return left + right;
	}
	
	public boolean isEmpty()
	{
		return ((left==0) && (top==0) && (right==0) && (bottom==0));
	}

	public LayoutBoxMeasures add(LayoutBoxMeasures other) {
		return new LayoutBoxMeasures(left + other.left, top + other.top, right + other.right, bottom + other.bottom);
	}
}
