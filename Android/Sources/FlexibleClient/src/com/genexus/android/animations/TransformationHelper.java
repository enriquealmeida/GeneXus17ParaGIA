package com.genexus.android.animations;

import android.view.View;

import com.genexus.android.layout.ControlViewHelper;
import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.DimensionValue.ValueType;
import com.genexus.android.core.base.metadata.layout.CellDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.metadata.theme.TransformationDefinition;
import com.genexus.android.core.base.metadata.theme.TransformationDefinition.RelativeTo;
import com.genexus.android.core.base.metadata.theme.TransformationDefinition.TranslationType;
import com.genexus.android.core.base.utils.MathUtils;

/**
 * Class that perform calculations to convert TransformationDefinition values (including
 * RelativeTo, TranslationType, percent values, &c).into Android animation values.
 */
class TransformationHelper
{
	private enum Axis { X, Y }

	public static Float getAnchorPointX(View view, TransformationDefinition transformation)
	{
		return getAnchorPoint(view, Axis.X, transformation.getAnchorPointX());
	}

	public static Float getAnchorPointY(View view, TransformationDefinition transformation)
	{
		return getAnchorPoint(view, Axis.Y, transformation.getAnchorPointY());
	}

	public static Float getTranslationX(View view, TransformationDefinition transformation)
	{
		return getTranslation(view, transformation.getTranslateType(), Axis.X, transformation.getTranslateRelativeTo(), transformation.getTranslateX());
	}

	public static Float getTranslationY(View view, TransformationDefinition transformation)
	{
		return getTranslation(view, transformation.getTranslateType(), Axis.Y, transformation.getTranslateRelativeTo(), transformation.getTranslateY());
	}

	public static Float getScaleX(View view, TransformationDefinition transformation)
	{
		return getScale(view, Axis.X, transformation.getScaleRelativeTo(), transformation.getScaleX());
	}

	public static Float getScaleY(View view, TransformationDefinition transformation)
	{
		return getScale(view, Axis.Y, transformation.getScaleRelativeTo(), transformation.getScaleY());
	}

	public static Integer getResizeX(View view, TransformationDefinition transformation)
	{
		return getResize(view, Axis.X, transformation.getResizeRelativeTo(), transformation.getResizeX());
	}

	public static Integer getResizeY(View view, TransformationDefinition transformation)
	{
		return getResize(view, Axis.Y, transformation.getResizeRelativeTo(), transformation.getResizeY());
	}

	private static Float getAnchorPoint(View view, Axis axis, DimensionValue value)
	{
		if (value == null)
			return null;

		if (value.Type == ValueType.PIXELS)
			return value.Value;

		return MathUtils.getPercent(getSize(view, axis), value.Value);
	}

	private static Float getTranslation(View view, TranslationType type, Axis axis, RelativeTo relativeTo, DimensionValue value)
	{
		if (value == null)
			return 0f; // A transformation with no value for translation means return to original place.

		if (type == TranslationType.BY)
		{
			 // By a fixed amount in pixels.
			if (value.Type == ValueType.PIXELS)
				return value.Value;

			// By the percentage of a control.
			View relativeView = getRelativeView(view, relativeTo);
			if (relativeView == null)
				return null;

			return MathUtils.getPercent(getSize(relativeView, axis), value.Value);
		}
		else if (type == TranslationType.TO)
		{
			// To an absolute point in the screen.
			if (relativeTo == RelativeTo.CONTROL)
			{
				// Move n dips or n% of the control's size.
				if (value.Type == ValueType.PIXELS)
					return value.Value;
				else
					return MathUtils.getPercent(getSize(view, axis), value.Value);
			}

			// Calculate translation vector from absolute position of control and relative control.
			View relativeView = getRelativeView(view, relativeTo);
			if (relativeView == null)
				return null;

			int absoluteDestination = getAbsolutePosition(relativeView, axis);
			int absoluteOrigin = getAbsolutePosition(view, axis);

			return Float.valueOf(absoluteDestination - absoluteOrigin);
		}
		else
			throw new IllegalArgumentException(String.format("Invalid TranslationType value (%s).", type));
	}

	private static Float getScale(View view, Axis axis, RelativeTo relativeTo, DimensionValue value)
	{
		if (value == null)
			return 1f; // A transformation with no value for scale means return to original size.

		// Calculate the ratio between the desired width or height and the control's width or height.
		float currentSize = getSize(view, axis);
		float desiredSize;

		if (value.Type == ValueType.PERCENT)
		{
			// If specified a percent of self, then it's not necessary to read size, just convert to 0..1.
			if (relativeTo == RelativeTo.CONTROL)
				return value.Value / 100f;

			View relativeView = getRelativeView(view, relativeTo);
			if (relativeView == null)
				return null;

			desiredSize = MathUtils.getPercent(getSize(relativeView, axis), value.Value);
		}
		else
			desiredSize = value.Value;

		return (desiredSize / currentSize);
	}

	private static Integer getResize(View view, Axis axis, RelativeTo relativeTo, DimensionValue value)
	{
		if (value == null)
			value = DimensionValue.percent(100); // A transformation with no value for resize means return to original size.

		if (value.Type == ValueType.PIXELS)
			return (int)value.Value;

		// Get the desired width or height from the relative control.
		View relativeView = getRelativeView(view, relativeTo);
		if (relativeView == null)
			return null;

		return (int)MathUtils.getPercent(getSize(relativeView, axis), value.Value);
	}

	private static View getRelativeView(View view, RelativeTo relativeTo)
	{
		switch (relativeTo)
		{
			case CONTROL : return view;
			case PARENT : return ControlViewHelper.getParentView(view);
			case FORM : return ControlViewHelper.getRootView(view);
			default : throw new IllegalArgumentException(String.format("Invalid RelativeTo value (%s).", relativeTo));
		}
	}

	private static int getAbsolutePosition(View view, Axis axis)
	{
		int[] position = new int[2];
		view.getLocationInWindow(position);

		switch (axis)
		{
			case X : return position[0];
			case Y : return position[1];
			default : throw new IllegalArgumentException(String.format("Invalid axis value (%s).", axis));
		}
	}

	private static int getSize(View view, Axis axis)
	{
		int size = getViewSize(view, axis);

		// If not yet measured, use control size as designed.
		if (size == 0)
			size = getDesignSize(view, axis);

		return size;
	}

	private static int getViewSize(View view, Axis axis)
	{
		switch (axis)
		{
			case X : return view.getWidth();
			case Y : return view.getHeight();
			default : throw new IllegalArgumentException(String.format("Invalid axis value (%s).", axis));
		}
	}

	private static int getDesignSize(View view, Axis axis)
	{
		LayoutItemDefinition definition = ControlViewHelper.getDefinition(view);
		if (definition != null)
		{
			if (definition instanceof TableDefinition)
			{
				switch (axis)
				{
					case X : return ((TableDefinition)definition).getAbsoluteWidth();
					case Y : return ((TableDefinition)definition).getAbsoluteHeight();
					default : throw new IllegalArgumentException(String.format("Invalid axis value (%s).", axis));
				}
			}
			else
			{
				// If not a table, then must be inside a cell of another table.
				CellDefinition parentCell = definition.findParentOfType(CellDefinition.class);
				if (parentCell != null)
				{
					switch (axis)
					{
						case X : return parentCell.getAbsoluteWidth();
						case Y : return parentCell.getAbsoluteHeight();
						default : throw new IllegalArgumentException(String.format("Invalid axis value (%s).", axis));
					}
				}
			}
		}

		return 0;
	}
}
