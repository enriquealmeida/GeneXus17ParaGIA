package com.genexus.android.core.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.LinearLayout;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.metadata.DomainDefinition;
import com.genexus.android.core.base.metadata.EnumValuesDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.RelationDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.GxButton;
import com.genexus.android.core.controls.GxCheckBox;
import com.genexus.android.core.controls.GxEnumComboSpinner;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.GxRadioGroupThemeable;
import com.genexus.android.core.controls.GxTextBlockTextView;

public class FiltersHelper
{
	private static final String THEME_TABLE = "Table";
	public static final String THEME_ATTR = "Attribute";
	public static final String THEME_LABEL = "TextBlock";
	private static final String THEME_BUTTON = "Button";

	public static final String PREFIX_FROM = "From";
	public static final String PREFIX_TO = "To";

	public static final int SELECT_FK = 3;

	public static DataItem getFilterDataItem(IDataSourceDefinition dataSource, String filterAttName)
	{
		return DataItemHelper.find(dataSource, filterAttName, true);
	}

    public static List<CharSequence> obtainAttributeDefinitionEnumCombo(LayoutItemDefinition formAttDef, Context context)
    {
    	List<CharSequence> enumStrings = new ArrayList<>();
		GxEnumComboSpinner enumCombo1 = new GxEnumComboSpinner(context, null, formAttDef);
        enumCombo1.setTag(formAttDef.getDataId());

        enumStrings.add(context.getResources().getText(R.string.GX_AllItems));

		if (formAttDef.getDataItem().getBaseType().getIsEnumeration())
		{
			DomainDefinition defEnum = Services.Application.getDefinition().getDomain(formAttDef.getDataTypeName().getDataType());
			if (defEnum!=null)
			{
				List<EnumValuesDefinition> items = defEnum.getEnumValues();
				for (EnumValuesDefinition itemDef : items) {
					enumStrings.add(itemDef.getDescription());
				}
			}
		}
    	return enumStrings;
    }

    public static List<CharSequence> obtainAttributeDefinitionCheckBox(LayoutItemDefinition formAttDef, Context context)
    {
    	List<CharSequence> checkBoxStrings = new ArrayList<>();
    	GxCheckBox checkBox = new GxCheckBox(context, null, formAttDef);
        checkBox.setTag(formAttDef.getDataId());
        checkBox.setHint(formAttDef.getInviteMessage());

        checkBoxStrings.add(context.getResources().getText(R.string.GX_AllItems));
        checkBoxStrings.add(context.getResources().getText(R.string.GXM_True));
        checkBoxStrings.add(context.getResources().getText(R.string.GXM_False));
    	return checkBoxStrings;
    }

    public static LayoutItemDefinition getFormAttDef(DataItem attDef, StructureDefinition structure)
	{
		LayoutItemDefinition formitem = null;
		if (attDef != null)
		{
			formitem = new LayoutItemDefinition(attDef);

			//set the caption for Hint
			formitem.setCaption(attDef.getCaption());

			if (structure == null) return formitem;

			//set if is FK
			for(int i = 0; i < structure.ManyToOneRelations.size(); i++)
			{
				RelationDefinition relation = structure.ManyToOneRelations.get(i);
				StructureDefinition def = Services.Application.getDefinition().getBusinessComponent(relation.getBCRelated());
				//BC could not be defined in SD WW
				if (def!=null)
				{
					int keySize = relation.getKeys().size();
					// Add the FK relation in the last attribute of the key
					for(int j = 0; j < keySize; j++)
					{
						String attRef = relation.getKeys().get(j);
						if (formitem.getDataId().equalsIgnoreCase(attRef))
						{
							formitem.setFK(relation);
						}
					}
				}
			}
		}
		return formitem;
	}

    public static String calculateAttName(String att, RelationDefinition relation)
    {
    	return PromptHelper.calculateAttName(att, relation);
	}

    public static String makeGetFilterWithValue(List<String> attsAllToSend, List<String> values)
	{
		StringBuilder sb = new StringBuilder();
		if (attsAllToSend != null && values != null)
    	{
			for (int i = 0; i < attsAllToSend.size(); i++)
			{
				StringBuilder sbAtt = makeGetFilterWithValue(attsAllToSend.get(i), values.get(i));
				sb = sb.append(sbAtt);
			}
			if (sb.length()>0)
				sb = sb.deleteCharAt(sb.length() - 1);
    	}
		return sb.toString();
	}

    private static StringBuilder makeGetFilterWithValue(String attAllToSend, String value)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(attAllToSend);
		sb.append(Strings.EQUAL);
		sb.append(value);
		sb.append(Strings.AND);
		return sb;
	}

    public static void setButtonAttributes(GxButton viewButtonOne, GxButton viewButtonSecond, int idButtonOne, int idButtonSecond) {
		viewButtonOne.setAttributes(idButtonOne, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		viewButtonSecond.setAttributes(idButtonSecond, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	}

    public static void setThemeFilters(GxLinearLayout viewTable, GxLinearLayout viewAttr, GxTextBlockTextView viewAttValue, GxTextBlockTextView viewAttLabel, GxRadioGroupThemeable radioGroupOrder, GxButton viewButtonFirst, GxButton viewButtonSecond) {

    	//Set the table Theme
		ThemeClassDefinition theme = Services.Themes.getThemeClass(FiltersHelper.THEME_TABLE);
		if ((theme != null) && (viewTable != null))
			viewTable.setThemeClass(theme);

		//Set the attribute Theme
		theme = Services.Themes.getThemeClass(FiltersHelper.THEME_ATTR);
		if ((theme != null) && (viewAttr != null))
			viewAttr.setThemeClass(theme);

		//Set the attribute Theme
		theme = Services.Themes.getThemeClass(FiltersHelper.THEME_ATTR);
		if ((theme != null) && (viewAttValue != null))
			viewAttValue.setThemeClass(theme);

		//Set the attribute Label Theme
		theme = Services.Themes.getThemeClass(FiltersHelper.THEME_LABEL);
		if ((theme != null) && (viewAttLabel != null))
			viewAttLabel.setThemeClass(theme);

		//Set the attribute Label Theme
		theme = Services.Themes.getThemeClass(FiltersHelper.THEME_LABEL);
		if ((theme != null) && (radioGroupOrder != null))
			radioGroupOrder.setThemeClass(theme);

		//Set the button Theme
		theme = Services.Themes.getThemeClass(FiltersHelper.THEME_BUTTON);
		if ((theme != null) && (viewButtonFirst != null) && (viewButtonSecond != null)) {
			viewButtonFirst.setThemeClass(theme);
			viewButtonSecond.setThemeClass(theme);
		}
	}
}
