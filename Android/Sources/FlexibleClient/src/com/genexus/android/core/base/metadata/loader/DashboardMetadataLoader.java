package com.genexus.android.core.base.metadata.loader;

import android.content.Context;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DashboardItem;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IPatternMetadata;
import com.genexus.android.core.base.metadata.enums.GxObjectTypes;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class DashboardMetadataLoader extends MetadataLoader
{
	public static final short COMPONENT_KIND = 100;

	@Override
	public IPatternMetadata load(Context context, String name)
	{
		INodeObject jsonData = getDefinition(context, Strings.toLowerCase(name));
		if (jsonData == null)
		{
			Services.Log.error("Could not connect to metadata for " + name);
			return null;
		}

		return loadJSON(jsonData);
	}

	private DashboardMetadata loadJSON(INodeObject jsonData)
	{
		if (jsonData == null)
			return null;

		DashboardMetadata dashboardMetadata = new DashboardMetadata();
		INodeObject dashboardNode = jsonData.getNode("Dashboard");

		//Read Dashboard Data
		String background = getAttributeName(dashboardNode.getString("@Background"));
		String header = getAttributeName(dashboardNode.getString("@Header"));
		String title = getAttributeName(dashboardNode.getString("@Title"));
		String themeClass = dashboardNode.optString("@Class");
		dashboardMetadata.setCaption(title);
		dashboardMetadata.setThemeClass(themeClass);

		String control = dashboardNode.optString("@Control");
		dashboardMetadata.setControl(control);

		String tabsBehavior = dashboardNode.optString("@TabsBehavior");
		dashboardMetadata.setTabsBehavior(tabsBehavior);

		String tabsImagePosition = dashboardNode.optString("@ImagePosition");
		dashboardMetadata.setTabsImagePosition(tabsImagePosition);

		String userControl = dashboardNode.optString("@UserControl");
		dashboardMetadata.setUserControl(userControl);

		boolean showAds = dashboardNode.optBoolean("@showAds");
		dashboardMetadata.setShowAds(showAds);
		String adsPosition = dashboardNode.optString("@adsPosition");
		dashboardMetadata.setAdsPosition(adsPosition);

		dashboardMetadata.setBackgroundImage(background);
		dashboardMetadata.setHeaderImage(header);

		dashboardMetadata.setShowApplicationBar(dashboardNode.optBoolean("@showApplicationBars"));
		dashboardMetadata.setApplicationBarClass(dashboardNode.optString("@applicationBarsClass"));

		//Read Instance Properties
		INodeObject instancePropNode = dashboardNode.getNode("instanceProperties");
		dashboardMetadata.getInstanceProperties().deserialize(instancePropNode);

		//read variables (before items and events)
		WorkWithMetadataLoader.readVariables(dashboardMetadata, dashboardNode);
		
		INodeCollection itemsNode = dashboardNode.optCollection("Item");
		for (int i = 0; i < itemsNode.length() ; i++)
		{
			INodeObject itemNode = itemsNode.getNode(i);
			DashboardItem item = loadDashboardItem(dashboardMetadata, itemNode);
			if (item != null)
				dashboardMetadata.getItems().add(item);
		}

		// Read events
		INodeObject eventsNode = dashboardNode.getNode("events");
		if (eventsNode != null)
		{
			for (INodeObject eventNode : eventsNode.optCollection("Item"))
			{
				DashboardItem item = loadDashboardItem(dashboardMetadata, eventNode);
				if (item != null)
				{
					// Unlike normal items, events can have parameters too.
					ActionDefinition event = item.getActionDefinition();
					WorkWithMetadataLoader.readEventParameters(dashboardMetadata, event.getEventParameters(), eventNode);
					dashboardMetadata.getEvents().add(event);
				}
			}
		}

		// Read Subroutines
		INodeObject subsNode = dashboardNode.getNode("subroutines");
		if (subsNode != null)
		{
			for (INodeObject subNode : subsNode.optCollection("Item"))
			{
				DashboardItem item = loadDashboardItem(dashboardMetadata, subNode);
				if (item != null)
				{
					// Unlike normal items, subroutines can have parameters too.
					ActionDefinition subroutine = item.getActionDefinition();
					WorkWithMetadataLoader.readEventParameters(dashboardMetadata, subroutine.getEventParameters(), subNode);
					dashboardMetadata.getSubroutines().add(subroutine);
				}
			}
		}

		// Read notifications
		INodeObject jsonNotifications = dashboardNode.getNode("notifications");
		if (jsonNotifications != null)
		{
			INodeCollection actionsNode = jsonNotifications.optCollection("Action");
			for (int i = 0; i < actionsNode.length() ; i++)
			{
				INodeObject actionNode = actionsNode.getNode(i);
				DashboardItem item = loadDashboardItem(dashboardMetadata, actionNode);
				if (item != null)
					dashboardMetadata.getNotificationActions().put(item.getName(), item);
			}
		}

		return dashboardMetadata;
	}

	/*
	 * Load a dashboaritem from a JSON representation, if it is not possible it returns null
	 */
	private DashboardItem loadDashboardItem(DashboardMetadata dashboard, INodeObject itemNode)
	{
		DashboardItem item = new DashboardItem(dashboard);

		String image = getAttributeName(itemNode.optString("@Image"));

		String data = itemNode.optString("@Data", Strings.EMPTY);
		String name = itemNode.optString("@Name", Strings.EMPTY);
		String cls = itemNode.optString("@Class", Strings.EMPTY);
		String description = itemNode.optString("@Description", Strings.EMPTY);
		String link = itemNode.optString("@Link", Strings.EMPTY);

	    item.deserialize(itemNode);

	    item.setThemeClass(cls);

		// Read Sub Items
		INodeCollection parArray = itemNode.optCollection("Item");
		for (int k = 0; k < parArray.length() ; k++) {
			DashboardItem childItem = loadDashboardItem(dashboard, parArray.getNode(k));
			item.getItems().add(childItem);
		}

		WorkWithMetadataLoader.readActionParameters(dashboard, item.getParameters(), itemNode);

		item.setName(name);
		item.setTitle(description);

		if (link.length() > 0)
		{
			item.setKind(COMPONENT_KIND);
			item.setObjectName(link);
		}
		else
		{
			item.setKind( GxObjectTypes.getGxObjectTypeFromName(data));
			item.setObjectName(getAttributeName(data));
		}

		item.setImage(image);

		return item;
	}
}
