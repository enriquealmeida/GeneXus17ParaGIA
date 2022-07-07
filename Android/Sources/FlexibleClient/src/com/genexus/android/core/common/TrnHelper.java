package com.genexus.android.core.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.DomainDefinition;
import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.RelationDefinition;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.FKPickerControl;
import com.genexus.android.core.controls.GxDateTimeEdit;
import com.genexus.android.core.controls.GxEditText;
import com.genexus.android.core.controls.GxEditTextMail;
import com.genexus.android.core.controls.GxEditTextNumeric;
import com.genexus.android.core.controls.GxEditTextPhone;
import com.genexus.android.core.controls.GxEnumComboSpinner;
import com.genexus.android.core.controls.GxImageViewData;
import com.genexus.android.core.controls.IGxComboEdit;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.controls.common.EditInputDescriptions;
import com.genexus.android.core.controls.media.GxMediaEditControl;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.usercontrols.IGxUserControl;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.utils.Cast;

public class TrnHelper
{
	//create edit controls

	public static void createEditRow(Context context, Coordinator coordinator, LinearLayout parent, Entity entity, LayoutItemDefinition att, ArrayList<IGxEdit> editables, OnClickListener listener)
	{
		if (!hideLabel(att))
		{
			TextView textView = new TextView(context);
			textView.setText(att.getCaption());
			parent.addView(textView);
		}

		//Check if is FK
		RelationDefinition relDef = att.getFK();

		//Check if user control is Dynamic Combo Box
		ControlInfo info = att.getControlInfo();
		String controlInfo = Strings.EMPTY;
		if (info!=null)
			controlInfo = info.getControl();

		if (relDef!=null && !controlInfo.equalsIgnoreCase(ControlTypes.DYNAMIC_COMBO_BOX))
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fkeditor, parent, false);
			layout.setTag(att.getDataId());
			//Add listener if not read only
			layout.setOnClickListener(listener);

			LinearLayout editor = layout.findViewById(R.id.itemEditor);
			View control = createEditField(context, coordinator, att, editables);
			editor.addView(control);

			LinearLayout fkButton = layout.findViewById(R.id.fkButton);
			//	Add Button For FK
			FKPickerControl btn = new FKPickerControl(context);
			btn.setTag(att.getDataId());
			//Add listener if not read only
			btn.setOnClickListener(listener);
			fkButton.addView(btn);

			parent.addView(layout);
		}
		else
			parent.addView(createEditField(context, coordinator, att, editables));
	}


	public static void createEditRowRange(Context context, Coordinator coordinator, LinearLayout parent, short trnMode, Entity entity, LayoutItemDefinition att, ArrayList<IGxEdit> editables, OnClickListener listener, String labelCaption)
	{
		TextView textView = new TextView(context);
		textView.setText(labelCaption);
		if (!hideLabel(att))
			parent.addView(textView);
		//Check if is FK
		RelationDefinition relDef = att.getFK();
		if (relDef != null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fkeditor, parent, false);
			layout.setTag(att.getDataId());
			//Add listener if not read only
			layout.setOnClickListener(listener);
			LinearLayout editor = layout.findViewById(R.id.itemEditor);
			View control = createEditField(context, coordinator, att, editables);
			editor.addView(control);
			LinearLayout fkButton = layout.findViewById(R.id.fkButton);
			//	Add Button For FK
			FKPickerControl btn = new FKPickerControl(context);
			btn.setTag(att.getDataId());
			//Add listener if not read only
			btn.setOnClickListener(listener);
			fkButton.addView(btn);
			parent.addView(layout);
		}
		else
			parent.addView(createEditField(context, coordinator, att, editables));
	}

	private static boolean hideLabel(LayoutItemDefinition att)
	{
		return att.getLabelPosition().equals(Properties.LabelPositionType.NONE);

	}

	private static final HashMap<String, Class<? extends IGxEdit>> EDIT_CONTROLS;

	static
	{
		EDIT_CONTROLS = new LinkedHashMap<>();
		EDIT_CONTROLS.put(ControlTypes.DATE_BOX, GxDateTimeEdit.class);
		EDIT_CONTROLS.put(ControlTypes.EMAIL_TEXT_BOX, GxEditTextMail.class);
		EDIT_CONTROLS.put(ControlTypes.PHONE_NUMERIC_TEXT_BOX, GxEditTextPhone.class);
		EDIT_CONTROLS.put(ControlTypes.ENUM_COMBO, GxEnumComboSpinner.class);
		EDIT_CONTROLS.put(ControlTypes.NUMERIC_TEXT_BOX, GxEditTextNumeric.class);
		EDIT_CONTROLS.put(ControlTypes.PHOTO_EDITOR, GxMediaEditControl.class);
		EDIT_CONTROLS.put(ControlTypes.VIDEO_VIEW, GxMediaEditControl.class);
		EDIT_CONTROLS.put(ControlTypes.AUDIO_VIEW, GxMediaEditControl.class);
	}

	public static void addControl(String controlType, Class<? extends IGxEdit> gxEditClass) {
		EDIT_CONTROLS.put(controlType, gxEditClass);
	}

	public static View createEditField(Context context, Coordinator coordinator, LayoutItemDefinition layoutItem, ArrayList<IGxEdit> editables)
	{
		View item =	getEditUserControl(context, coordinator, layoutItem, editables);
		if (item != null)
			return item;

		String dataId = layoutItem.getDataId();
		IGxEdit fieldControl = null;

		// For input type descriptions, use GxEditText regardless of custom editor.
		if (!EditInputDescriptions.isInputTypeDescriptions(layoutItem))
		{
			String controlType = layoutItem.getControlType();
			if (EDIT_CONTROLS.containsKey(controlType))
				fieldControl = createControlEdit(context, coordinator, layoutItem, controlType);
		}

		if (fieldControl == null)
			fieldControl = new GxEditText(context, coordinator, layoutItem);

		item = (View)fieldControl;
		item.setTag(dataId);

		ThemeClassDefinition theme = Services.Themes.getThemeClass(FiltersHelper.THEME_ATTR);
		if (theme != null) {
			IGxThemeable themeableControl = Cast.as(IGxThemeable.class, fieldControl);
			if (themeableControl != null)
				themeableControl.setThemeClass(theme);
			else
			{
				IGxEditThemeable editThemeableControl = Cast.as(IGxEditThemeable.class, fieldControl);
				if (editThemeableControl != null)
					editThemeableControl.applyEditClass(theme);
			}
		}

		editables.add(fieldControl);
		return item;
	}

	private static IGxEdit createControlEdit(Context context, Coordinator coordinator, LayoutItemDefinition layoutItemDefinition, String controlType)
	{
		Class<?> controlClass = EDIT_CONTROLS.get(controlType);
		if (controlType.equals(ControlTypes.PHOTO_EDITOR) &&
			layoutItemDefinition.getControlInfo().optBooleanProperty("@EnableZoom")) {
			try {
				controlClass = Class.forName("com.genexus.coreusercontrols.image.GxAdvancedImage");
			} catch (ClassNotFoundException ignore) { }
		}
		return (IGxEdit) UcFactory.createUserControl(controlClass, context, coordinator, layoutItemDefinition);
	}

	private static View getEditUserControl(Context context, Coordinator coordinator, LayoutItemDefinition att, ArrayList<IGxEdit> editables)
	{
		IGxEdit item =	getEditUserControl(context, coordinator, att);
		if (item != null)
		{
			editables.add(item);
			return (View) item;
		}
		return null;
	}

	private static IGxEdit getEditUserControl(Context context, Coordinator coordinator, LayoutItemDefinition layoutItem)
	{
		if (layoutItem != null)
		{
			IGxEdit edit = createDataUserControl(context, coordinator, layoutItem);

			if (edit != null)
			{
				edit.setGxTag(layoutItem.getDataId());
				return edit;
			}
		}

		return null;
	}

	private static final Set<String> BUILT_IN_EDIT_CONTROLS = Strings.newSet("Edit", "Image");

	public static IGxEdit createDataUserControl(Context context, Coordinator coordinator, LayoutItemDefinition layoutItem)
	{
		if (layoutItem.getControlInfo() != null)
		{
			String controlType = layoutItem.getControlInfo().getControl();
			if (BUILT_IN_EDIT_CONTROLS.contains(controlType))
				return null; // Standard control creation is not handled by this method.

			IGxUserControl edit = UcFactory.createUserControl(context, coordinator, layoutItem);
			return Cast.as(IGxEdit.class, edit);
		}

		return null;
	}

	//Enums Handler

	public static void setEnumCombosData(List<IGxEdit> items)
	{
		for (IGxEdit item : items)
		{
			if (item instanceof IGxComboEdit)
				setEnumComboData((IGxComboEdit)item);
		}
	}

	private static void setEnumComboData(IGxComboEdit combo)
	{
		LayoutItemDefinition def = combo.getDefinition();
		if (def != null)
		{
			if (def.getDataItem().getBaseType().getIsEnumeration())
			{
				DomainDefinition defEnum = Services.Application.getDefinition().getDomain(def.getDataTypeName().getDataType());
				if (defEnum != null)
					combo.setComboValues(defEnum.getEnumValues());
			}
		}
	}
	public static GxImageViewData getGxImage(View v)
	{
		if(v instanceof GxImageViewData)
			return (GxImageViewData)v;
		if (v instanceof DataBoundControl)
		{
			DataBoundControl boundControl = (DataBoundControl)v;
			if (boundControl.getAttribute()!=null && boundControl.getAttribute() instanceof GxImageViewData)
				return (GxImageViewData) boundControl.getAttribute();
		}
		return null;
	}
}
