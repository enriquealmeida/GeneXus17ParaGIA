package com.genexus.android.core.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MenuItem;
import android.widget.TextView;

import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.layout.ILayoutActionDefinition;
import com.genexus.android.core.base.metadata.theme.ApplicationClassExtensionKt;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.actiongroup.MenuItemControl;
import com.genexus.android.core.utils.DrawableUtils;

public class UIActionHelper
{
	public static void setActionButtonImage(Context context, ILayoutActionDefinition action, int position, TextView control)
	{
		if (position == Alignment.NONE)
			return;

		Drawable drawable = getActionImage(context, action);
		if (drawable != null)
		{
			if (position == Alignment.CENTER)
				control.setBackground(drawable);
			else
			{
				// Position should be read from theme class afterwards.
				control.setCompoundDrawables(
					position == Alignment.START ? drawable : null,
					position == Alignment.TOP ? drawable : null,
					position == Alignment.END ? drawable : null,
					position == Alignment.BOTTOM ? drawable : null);
			}
		}
	}

	public static void setMenuItemImage(Context context, MenuItem menuItem, ILayoutActionDefinition action, MenuItemControl itemControl)
	{
		// Either use specified image or default one.
		Drawable customDrawable = readActionImage(context, action);
		if (customDrawable != null)
			setMenuItemIcon(context, menuItem, customDrawable, itemControl);
		else
			setStandardMenuItemImage(context, menuItem, action.getEventName());
	}

	public static void setStandardMenuItemImage(Context context, MenuItem menuItem, String action) {
		if (!Strings.hasValue(action)) {
			return;
		}

		Drawable drawable = Services.Resources.getActionBarDrawableFor(context, action);
		if (drawable != null) {
			setMenuItemIcon(context, menuItem, drawable, null);
		}
	}

	private static void setMenuItemIcon(Context context, MenuItem menuItem, Drawable icon, MenuItemControl itemControl)
	{
		if (icon != null)
		{
			// Apply the action tint color from theme.
			// Disabled for now, as I couldn't make it work for the overflow icon.
			if (Services.Themes.getApplicationClass()!=null)
			{
				Integer tintColor = ApplicationClassExtensionKt.getActionTintColorId(Services.Themes.getApplicationClass());
				if (tintColor != null)
					icon = DrawableUtils.applyTint(icon, tintColor);
			}

			if (itemControl != null)
				itemControl.setIcon(context, menuItem, icon);
			else
				menuItem.setIcon(icon);
		}
	}

	public static Drawable getActionImage(Context context, ILayoutActionDefinition action)
	{
		Drawable drawable = readActionImage(context, action);
		if (drawable != null)
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		return drawable;
	}

	private static Drawable readActionImage(Context context, ILayoutActionDefinition action)
	{
		Drawable normalImage = Services.Images.getStaticImage(context, action.getImage());

		if (normalImage == null)
			return null; // We don't support highlighted/disabled unless normal is set.

		StateListDrawable stateImage = new StateListDrawable();
		Drawable disabledImage = Services.Images.getStaticImage(context, action.getDisabledImage());
		Drawable highlightedImage = Services.Images.getStaticImage(context, action.getHighlightedImage());

		if (highlightedImage != null)
			stateImage.addState(new int[] { android.R.attr.state_pressed }, highlightedImage);

		if (disabledImage != null)
			stateImage.addState(new int[] { -android.R.attr.state_enabled }, disabledImage);

		stateImage.addState(new int[] { }, normalImage);

		return stateImage;
	}
}
