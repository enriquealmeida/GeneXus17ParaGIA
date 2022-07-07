package com.genexus.android.core.usercontrols.sparkline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.utils.ThemeUtils;

@SuppressLint("ViewConstructor")
public class GxSparkLine extends SparkLineView implements IGridView, IGxThemeable
{
	public static final String NAME = "SDSparkLine";

	private LayoutItemDefinition mItemDef;
	private ThemeClassDefinition mThemeClass;

	public GxSparkLine(Context context, LayoutItemDefinition def) {
		super(context);
		setLayoutDefinition(def);

		mItemDef = getPropertyItemDef(def);
	}

	private LayoutItemDefinition getPropertyItemDef(LayoutItemDefinition definition) {
		if (definition.getType().equalsIgnoreCase(LayoutItemsTypes.DATA)) {
			return definition;
		} else {
			LayoutItemDefinition itemDef = null;
			for (LayoutItemDefinition child : definition.getChildItems()) {
				itemDef = getPropertyItemDef(child);
				if (itemDef != null) {
					break;
				}
			}
			return itemDef;
		}
	}

	public void setControlInfo(ControlInfo info) {
		SparkLineOptions options = new SparkLineOptions();
		options.setLabelText(info.optStringProperty("@SDSparkLineLabelText"));
		options.setShowCurrentValue(info.optBooleanProperty("@SDSparkLineShowCurrentValue"));
		options.setCurrentValueColor(ThemeUtils.getColorId("@SDSparkLineCurrentValueColor", options.getCurrentValueColor()));
		setOptions(options);
	}


	private void setLayoutDefinition(LayoutItemDefinition layoutItemDefinition) {
		if (layoutItemDefinition != null)
			setControlInfo(layoutItemDefinition.getControlInfo());
	}

	@Override
	public void addListener(GridEventsListener listener) {
	}

	@Override
	public void update(ViewData data) {
		SparkLineData values = new SparkLineData();

		if (mItemDef != null) {
			EntityList entities = data.getEntities();

			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);

				String propertyName = mItemDef.getDataId(i);

				// if it's a control
				int startPos = propertyName.lastIndexOf('.');
				if (startPos != -1) {
					propertyName = propertyName.substring(startPos + 1);
				}

				try {
					Float value = Float.valueOf((String) entity.getProperty(propertyName));
					values.add(value);
				} catch (NumberFormatException ex) {
					Services.Log.error("Invalid Values for SparkLine");
					values = null;
					break;
				}
			}
		}

		if (values != null) {
			setDataValues(values);
		}
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		mThemeClass = themeClass;
		applyClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemeClass;
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass)
	{
		if (themeClass == null)
			return;

		SparkLineOptions options = getOptions();

		options.setPenColor(ThemeUtils.getColorId(themeClass.getBorderColor(), options.getPenColor()));
		if (themeClass.getBorderWidth() != 0)
			options.setPenWidth(themeClass.getBorderWidth());

		setBackgroundColor(ThemeUtils.getColorId(themeClass.getBackgroundColor(), Color.WHITE));
	}
}
