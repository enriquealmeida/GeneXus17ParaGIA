package com.genexus.android.core.utils;

import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.controls.GxTouchEvents;

public class BackgroundOptions
{
	private boolean mIsHighlighted = false;
	private boolean mUseBitmapSize = false;
	private LayoutItemDefinition mLayoutItem;

	public static final BackgroundOptions DEFAULT = new BackgroundOptions();

	public static BackgroundOptions defaultFor(LayoutItemDefinition layoutItem)
	{
		BackgroundOptions options = new BackgroundOptions();
		options.mLayoutItem = layoutItem;
		return options;
	}

	public static BackgroundOptions copy(BackgroundOptions options)
	{
		BackgroundOptions copy = new BackgroundOptions();
		copy.mIsHighlighted = options.mIsHighlighted;
		copy.mUseBitmapSize = options.mUseBitmapSize;
		copy.mLayoutItem = options.mLayoutItem;

		return copy;
	}

	public boolean getIsHighlighted() {
		return mIsHighlighted;
	}

	public BackgroundOptions setIsHighlighted(boolean value) {
		mIsHighlighted = value;
		return this;
	}

	public boolean getUseBitmapSize() {
		return mUseBitmapSize;
	}

	public BackgroundOptions setUseBitmapSize(boolean value) {
		mUseBitmapSize = value;
		return this;
	}

	public LayoutItemDefinition getLayoutItem() { return mLayoutItem; }

	/**
	 * Returns whether the associated control is "actionable" (i.e. tappable, either by its nature
	 * or by having TAP, LONG_TAP or DOUBLE_TAP events).
	 */
	public boolean isActionableControl()
	{
		if (mLayoutItem != null)
		{
			// Buttons or grid items with a default action.
			if (hasDefaultActionFeedback())
				return true;

			// Normal controls that have a Control.Tap event, or similar.
			for (String tapEvent : GxTouchEvents.getTapEvents())
			{
				if (mLayoutItem.getEventHandler(tapEvent) != null)
					return true;
			}
		}

		return false;
	}

	public boolean hasDefaultActionFeedback()
	{
		if (mLayoutItem != null)
		{
			// Buttons
			if (mLayoutItem.getType().equalsIgnoreCase(LayoutItemsTypes.ACTION))
				return true;

			// Grid Items, when the grid has a default action.
			if (mLayoutItem instanceof TableDefinition && mLayoutItem.getParent() instanceof GridDefinition)
			{
				if (((GridDefinition) mLayoutItem.getParent()).getDefaultAction() != null)
					return true;
			}
		}

		return false;
	}



}
