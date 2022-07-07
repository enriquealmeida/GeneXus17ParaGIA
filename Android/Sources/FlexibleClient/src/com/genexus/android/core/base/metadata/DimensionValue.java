package com.genexus.android.core.base.metadata;

import androidx.annotation.Nullable;

import java.io.Serializable;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;
import com.genexus.android.core.base.utils.Strings;

@SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
public class DimensionValue implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum ValueType { PERCENT, PIXELS }

	public final ValueType Type;
	public final float Value;

	/*
	private static final FactoryFlyweight sPercentFactoryFlyweight = new FactoryFlyweight(ValueType.PERCENT);
	private static final FactoryFlyweight sPixelFactoryFlyweight = new FactoryFlyweight(ValueType.PIXELS);

	private static class FactoryFlyweight extends LruCache<Float, DimensionValue>
	{
		private static final int SIZE = 100;
		private final ValueType mType;

		public FactoryFlyweight(ValueType type)
		{
			super(SIZE);
			mType = type;
		}

		@Override
		protected DimensionValue create(Float key)
		{
			return new DimensionValue(mType, key);
		}
	}
	*/

	public static final DimensionValue ZERO = pixels(0);
	public static final DimensionValue HUNDRED_PERCENT = percent(100);

	private DimensionValue(ValueType type, float value)
	{
		this.Type = type;
		this.Value = value;
	}

	public static DimensionValue pixels(int pixels)
	{
		// return sPixelFactoryFlyweight.get((float)pixels);
		return new DimensionValue(ValueType.PIXELS, pixels);
	}

	public static DimensionValue percent(float percent)
	{
		// return sPercentFactoryFlyweight.get(percent);
		return new DimensionValue(ValueType.PERCENT, percent);
	}

	@Override
	public String toString()
	{
		if (Type == ValueType.PERCENT)
			return String.format("%s%%", (int)Value);
		else
			return String.format("%spx", (int)Value);
	}

	@Override
	public boolean equals(Object o)
	{
		if (super.equals(o))
            return true;

        if (!(o instanceof DimensionValue))
            return false;

        DimensionValue other = (DimensionValue)o;
        return (Type == other.Type &&
        		Value == other.Value);
	}

	@Override
	public int hashCode()
	{
		return Type.hashCode() ^ Float.valueOf(Value).hashCode();
	}

	/**
	 * Parses a string with a measure. Can end with '%' or 'dip' to signify percentage
	 * or dips (dips are converted to pixels).
	 */
	public static @Nullable DimensionValue parse(String str)
	{
		if (!Strings.hasValue(str))
			return null;

		if (str.endsWith("%"))
		{
			str = str.replace("%", Strings.EMPTY).trim();
			Float percentage = Services.Strings.tryParseFloat(str);
			if (percentage != null)
				return DimensionValue.percent(percentage);
		}
		else
		{
			// Assume dip even if the word "dip" is missing.
			str = str.replace("dip", Strings.EMPTY).trim();
			Integer dips = Services.Strings.tryParseInt(str);
			if (dips != null)
				return DimensionValue.pixels(Services.Device.dipsToPixels(dips));
		}

		Services.Log.warning(String.format("Could not parse dimension value '%s'.", str));
		return null;
	}

	/**
	 * Converts the DimensionValue to an absolute value.
	 * If Type = PIXELS then returns Value. Otherwise returns Value as a percentage of the reference.
	 */
	public static float toPixels(DimensionValue value, float reference)
	{
		if (value.Type == ValueType.PIXELS)
			return value.Value;
		else
			return MathUtils.getPercent(reference, value.Value);
	}
}
