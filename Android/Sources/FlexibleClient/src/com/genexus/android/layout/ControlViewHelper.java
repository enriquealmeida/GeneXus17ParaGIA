package com.genexus.android.layout;

import android.view.View;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.utils.Cast;

public class ControlViewHelper
{
	public static LayoutItemDefinition getDefinition(View view)
	{
		return Cast.as(LayoutItemDefinition.class, view.getTag(LayoutTag.CONTROL_DEFINITION));
	}

	public static View getParentView(View view)
	{
		// Get the first parent class that is a control (some views do not correspond to GX controls).
		View parent = Cast.as(View.class, view.getParent());

		while (parent != null && ControlViewHelper.getDefinition(parent) == null)
			parent = Cast.as(View.class, parent.getParent());

		return parent;
	}

	public static View getRootView(View from)
	{
		LayoutItemDefinition fromDefinition = getDefinition(from);
		if (fromDefinition != null)
		{
			LayoutItemDefinition rootDefinition = fromDefinition.getLayout().getTable();

			View parent = from;
			while (parent != null && getDefinition(parent) != rootDefinition)
				parent = Cast.as(View.class, parent.getParent());

			if (parent != null)
				return parent;
		}

		return from.getRootView(); // Last resort.
	}
}
