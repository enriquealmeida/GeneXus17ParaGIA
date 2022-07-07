package com.genexus.android.layout;

import java.util.Map;

import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.grids.GridHelper;

public class ControlPropertiesDefaults extends ControlProperties
{
	private final TableDefinition mTable;

	public ControlPropertiesDefaults(TableDefinition table)
	{
		mTable = table;
	}

	/**
	 * Adds to this object the default values of the supplied properties.
	 *
	 * @return True if all default values were successfully calculated; false otherwise.
	 */
	public boolean putDefaultsFor(ControlProperties props)
	{
		boolean success = true;
		for (Map.Entry<String, Map<String, Value>> controlProperties : props.getProperties())
		{
			// Ignore properties for controls not on the screen (probably for another layout).
			LayoutItemDefinition control = mTable.getControl(controlProperties.getKey());
			if (control != null)
			{
				if (!putDefaultsFor(control, controlProperties.getValue())
						&& !controlIsUCIgnorable(control))
					success = false; // Not all property defaults were obtained. Continue anyway to get as much as possible.
			}
			else
			{
				Services.Log.debug("Control " + controlProperties.getKey() + " not found in table " + mTable.getName());

			}
		}

		return success;
	}

	private boolean controlIsUCIgnorable(LayoutItemDefinition control)
	{
		if (control.getControlInfo()!=null)
		{
			// if uc has a SDGxReuseView property, ignore it from the controls that can invalidate reuse
			return control.getControlInfo().optBooleanProperty("@" + control.getControlInfo().getControl() + "SDGxReuseView") ;
		}
		return false;
	}

	private boolean putDefaultsFor(LayoutItemDefinition control, Map<String, Value> properties)
	{
		boolean success = true;
		for (Map.Entry<String, Value> property : properties.entrySet())
		{
			if (ControlHelper.PROPERTY_CLASS.equalsIgnoreCase(property.getKey()))
			{
				ThemeClassDefinition themeClass = control.getThemeClass();
				putProperty(control.getName(), ControlHelper.PROPERTY_CLASS, Value.newString((themeClass != null ? themeClass.getName() : Strings.EMPTY)));
			}
			else if (ControlHelper.PROPERTY_ENABLED.equalsIgnoreCase(property.getKey()))
			{
				boolean enabled = control.isEnabled();
				putProperty(control.getName(), ControlHelper.PROPERTY_ENABLED, Value.newBoolean(enabled));
			}
			else if (ControlHelper.PROPERTY_CAPTION.equalsIgnoreCase(property.getKey()))
			{
				String caption = control.getCaption();
				putProperty(control.getName(), ControlHelper.PROPERTY_CAPTION, Value.newString(caption));
			}
			else if (ControlHelper.PROPERTY_VISIBLE.equalsIgnoreCase(property.getKey()))
			{
				boolean visible = control.isVisible();
				putProperty(control.getName(), ControlHelper.PROPERTY_VISIBLE, Value.newBoolean(visible));
			}
			else if (GridHelper.PROPERTY_ITEM_LAYOUT.equalsIgnoreCase(property.getKey()) || GridHelper.PROPERTY_ITEM_SELECTED_LAYOUT.equalsIgnoreCase(property.getKey()))
			{
				// Ignore these special properties. They are not actually used via setProperty(), but processed later.
				// Therefore, custom values here do not need to be converted to defaults.
			}
			// here reuse view is invalidated for inner grid that change page? should do this or ignore this property also? At least do it for horizontal grid.
			else if (GridHelper.PROPERTY_GRID_CURRENTPAGE.equalsIgnoreCase(property.getKey()) && isHorizontalGrid(control))
			{
				// Ignore this property, CurrentPage in horizontal grid inside a grid
				// Default value shoult be 0?
			}
			else
				success = false; // This property is not known, or doesn't have a logical default (e.g. Focus).
		}

		return success;
	}

	private boolean isHorizontalGrid(LayoutItemDefinition control)
	{
		if (control instanceof GridDefinition)
		{
			GridDefinition myGrid = (GridDefinition)control;
			return myGrid.gridIsHorizontalGridUserControl();
		}
		return false;
	}
}
