package com.genexus.android.maps;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;

import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IGxMapView;
import com.genexus.android.location.R;

public class MapViewHelper
{
	private static final int GROUP_MAP_TYPE = 20001;
	private static final int MENU_TYPE_STANDARD = 1;
	private static final int MENU_TYPE_HYBRID = 2;
	private static final int MENU_TYPE_SATELLITE = 3;

	public static void addMapTypeMenu(IGxMapView mapView, Menu menu)
	{
		SubMenu subMenu = menu.addSubMenu(Menu.NONE, R.id.map_mode, 0, R.string.GXM_SelectMapType);

        MenuItem itemStandard = addMapTypeMenuOption(mapView, subMenu, GxMapViewDefinition.MAP_TYPE_STANDARD, MENU_TYPE_STANDARD, R.string.GXM_Standard);
        MenuItem itemSatellite = addMapTypeMenuOption(mapView, subMenu, GxMapViewDefinition.MAP_TYPE_SATELLITE, MENU_TYPE_SATELLITE, R.string.GXM_Satellite);
        MenuItem itemHybrid = addMapTypeMenuOption(mapView, subMenu, GxMapViewDefinition.MAP_TYPE_HYBRID, MENU_TYPE_HYBRID, R.string.GXM_Hybrid);

        subMenu.setGroupCheckable(GROUP_MAP_TYPE, true, true);

        String currentMapType = mapView.getMapType();
        if (currentMapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_SATELLITE))
        	itemSatellite.setChecked(true);
        else if (currentMapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_HYBRID))
        	itemHybrid.setChecked(true);
        else
        	itemStandard.setChecked(true);
	}

	private static MenuItem addMapTypeMenuOption(IGxMapView mapView, SubMenu group, String mapType, int itemId, int itemTitleRes)
	{
        MenuItem item = group.add(GROUP_MAP_TYPE, itemId, 0, itemTitleRes);
        item.setOnMenuItemClickListener(new MenuItemClickListener(item, mapView, mapType));
        return item;
	}

	private static class MenuItemClickListener implements OnMenuItemClickListener
	{
		private final MenuItem mItem;
		private final IGxMapView mMapView;
		private final String mType;

		public MenuItemClickListener(MenuItem item, IGxMapView mapView, String type)
		{
			mItem = item;
			mMapView = mapView;
			mType = type;
		}

		@Override
		public boolean onMenuItemClick(MenuItem item)
		{
			mMapView.setMapType(mType);
			mItem.setChecked(true);
			return true;
		}
	}
}
