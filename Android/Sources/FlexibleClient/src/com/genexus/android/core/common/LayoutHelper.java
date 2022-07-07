package com.genexus.android.core.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.json.NodeObject;
import com.genexus.android.layout.SectionsLayoutVisitor;
import com.genexus.android.layout.SectionsLayoutVisitor.LayoutSection;
import com.genexus.android.core.base.metadata.DetailDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.layout.ContentDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinitionFactory;
import com.genexus.android.core.base.metadata.layout.TabControlDefinition;
import com.genexus.android.core.base.metadata.layout.TabItemDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class LayoutHelper
{
	public static final int DEFAULT_AD_CONTROL_HEIGHT_DP = 56;

	// create row with one cell
	public static LayoutItemDefinition getRowWithCell(LayoutDefinition layout, LayoutItemDefinition parent, String cellContent, String rowHeight,
			String cellX, String cellY, String cellXRelative, String cellYRelative,
			String cellWidth, String cellHeight, String cellWidthRelative, String cellHeightRelative)
	{
		String attName = "row";

		// \"@hAlign\": \"Center\", \"@vAlign\": \"Middle\", "+

		String jsonRow = "{ \"@rowHeight\": \"" + rowHeight + "\", "+
	    "\"cell\": { \"@rowSpan\": \"1\", \"@colSpan\": \"1\", \"@hAlign\": \"Default\", \"@vAlign\": \"Default\", "+
	    cellContent + ", "+
        " \"CellBounds\": { \"@x\": \"" + cellX + "\", \"@y\": \"" + cellY +"\", " +
        "\"@xRelative\": \"" + cellXRelative + "\", \"@yRelative\": \"" + cellYRelative + "\", " +
        "\"@width\": \""+ cellWidth + "\", " + " \"@height\": \"" + cellHeight +
        "\", \"@widthRelative\": \"" + cellWidthRelative + "\", \"@heightRelative\": \"" + cellHeightRelative + "\"  } } } ";

		NodeObject rowNode = LayoutHelper.getNodeObjectFromJsonObjectString(jsonRow);
		if (rowNode!=null)
		{
			LayoutItemDefinition layoutItemDef = LayoutItemDefinitionFactory.createDefinition(layout, parent, attName);
			if (layoutItemDef != null) 	{
				layoutItemDef.setType(attName);
				layoutItemDef.readData(rowNode);
				LayoutDefinition.readLayoutItems(rowNode, layout, layoutItemDef);
				return layoutItemDef;
			}
		}
		return null;
	}

	//create tab with sections in it.
	public static TabControlDefinition getTabControlDefinition(LayoutDefinition layout, LayoutItemDefinition parent,
			List<LayoutSection> tabsSections)
	{
		TabControlDefinition tabControl = new TabControlDefinition(layout, parent);
		tabControl.setType(LayoutItemsTypes.TAB);
		String dataJson = "{ \"@class\": \"Tab\", \"@visible\": \"True\" } ";
		NodeObject dataNode = LayoutHelper.getNodeObjectFromJsonObjectString(dataJson);
		if (dataNode!=null)
			tabControl.readData(dataNode);

		//Add tab Items childs.
		int position = 1;
		for (LayoutSection section : tabsSections)
		{
			JSONObject dataItemJson = new JSONObject();
			try
			{
				dataItemJson.put("@itemControlName", "Tab" + position);
				dataItemJson.put("@visible", true);
				dataItemJson.put("@caption", section.getCaption());
				dataItemJson.put("@image", section.getImage());
				dataItemJson.put("@unselectedImage", section.getImageUnSelected());
				dataItemJson.put("@class", "TabPage.UnselectedTabPage");
				dataItemJson.put("@selClass", "TabPage.SelectedTabPage");
			}
			catch (JSONException e)
			{
				Services.Log.warning("Error creating tab item JSON", e);
			}
			
			// Create tabItem
			TabItemDefinition tabItem = new TabItemDefinition(layout, tabControl);
			tabItem.setType(LayoutItemsTypes.TAB_PAGE);
			tabItem.readData(new NodeObject(dataItemJson));

			// create child table
			TableDefinition tableItemDef = getTableForSection(layout, tabItem ,section, position);
			tabItem.getChildItems().add(tableItemDef);

			//Add tabItem to tab childs
			tabControl.getChildItems().add(tabItem);
			position++;
		}

		return tabControl;
	}

	private static TableDefinition getTableForSection(LayoutDefinition layout, LayoutItemDefinition parent,
			LayoutSection section, int position) {
		TableDefinition tableDef = new TableDefinition(layout, parent);
		tableDef.setType(LayoutItemsTypes.TABLE);
		String tableItemJson = " { \"@controlName\": \"Tab" + position + "Table1\", " +
                " \"@width\": \"100%\", \"@height\": \"100%\", \"@AutoGrow\": \"True\", " +
                " \"@class\": \"Table\", \"@visible\": \"True\", \"@FixedHeightSum\": \"0\", \"@FixedWidthSum\": \"0\" } ";
	    NodeObject tableItemNode = LayoutHelper.getNodeObjectFromJsonObjectString(tableItemJson);
		if (tableItemNode!=null)
			tableDef.readData(tableItemNode);

		//Add child row with a section
		String cellContent = "\"oneContent\": { \"@controlName\": \"Section" + position + "\", \"@content\": \"Section:" + section.getSection().getCode() +"\", \"@display\": \"Inline\", "+
        "  \"@visible\": \"True\", \"@showSectionTitle\": \"False\", \"@AutoGrow\":\"True\" }" ;
		LayoutItemDefinition layoutRowItemDef = LayoutHelper.getRowWithCell(layout, tableDef, cellContent, "100%",
				Strings.ZERO, Strings.ZERO, Strings.ZERO, Strings.ZERO,
				Strings.ZERO, Strings.ZERO, "100", "100");

		if (layoutRowItemDef!=null)
		{
			tableDef.getChildItems().add(layoutRowItemDef);
		}

		return tableDef;
	}

	//get section tab contentdefinition
	public static ContentDefinition getContentDefinition(LayoutDefinition layout, LayoutItemDefinition parent,
			List<LayoutSection> tabsSections)
	{
		ContentDefinition itemDef = new ContentDefinition(layout, parent);
		itemDef.setType(LayoutItemsTypes.ONE_CONTENT);
		String dataJson = "{ \"@content\": \"Section:"+ tabsSections.get(0).getSection().getCode() + "\"," +
		        "\"@display\": \"Platform Default\", \"@showSectionTitle\": \"False\", " +
		        "\"@visible\": \"True\" } ";

		NodeObject dataNode = LayoutHelper.getNodeObjectFromJsonObjectString(dataJson);
		if (dataNode!=null)
			itemDef.readData(dataNode);

		return itemDef;
	}

	private static NodeObject getNodeObjectFromJsonObjectString(String jsonObject)
	{
		JSONObject rowJsonObject = null;
		try {
			rowJsonObject = new JSONObject(jsonObject);
		} catch (JSONException e) {
		}
		if (rowJsonObject!=null)
		{
			return new NodeObject(rowJsonObject);
		}
		return null;
	}

	//get sections of a Detail metadata
	public static List<LayoutSection> getDetailSections(DetailDefinition formMetadata, short displayMode) {
		//For "All Sections" show a tab view with the section that correspond to this mode
		//For "Section" return the sections that are in the layout inline.
        List<LayoutSection> sections = LayoutSection.all(formMetadata.getSections());

        LayoutDefinition layout = formMetadata.getLayoutForMode(displayMode);
        if (layout != null)
        {
        	SectionsLayoutVisitor visitor = new SectionsLayoutVisitor();
        	layout.getTable().accept(visitor);
    		if (visitor.hasSections())
    			sections = visitor.getInlineSections();
        }

        List<LayoutSection> tabsSections = new ArrayList<>();

    	for (LayoutSection layoutSection : sections )
        {
        	if (layoutSection.getSection().getLayout(LayoutDefinition.TYPE_ANY) == null)
        		continue; // Don't show sections that have layouts defined for other platforms only (not Android and not Any).

        	if (displayMode != DisplayModes.VIEW && layoutSection.getSection().getLayout(LayoutDefinition.TYPE_EDIT) == null)
        		continue; // Don't show sections that don't have edit layouts defined when called in edit mode.

        	tabsSections.add(layoutSection);
        }

		return tabsSections;
	}
}
