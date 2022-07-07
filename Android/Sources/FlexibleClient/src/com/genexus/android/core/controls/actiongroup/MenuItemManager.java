package com.genexus.android.core.controls.actiongroup;

import java.util.List;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.genexus.android.core.base.metadata.layout.ActionGroupItem;
import com.genexus.android.core.base.metadata.layout.ActionGroupItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.UIActionHelper;
import com.genexus.android.core.utils.Cast;

class MenuItemManager extends ActionGroupItemManager<MenuItemControl>
{
	private final Context mContext;

	public MenuItemManager(Context context)
	{
		mContext = context;
	}

	@Override
	protected MenuItemControl newItemControl(ActionGroupItemDefinition definition, int id)
	{
		return new MenuItemControl(definition, id);
	}

	public void initializeMenu(Menu menu)
	{
		initializeMenu(menu, getMainItems(), true);
	}

	private void initializeMenu(Menu menu, List<MenuItemControl> controls, boolean isFirstLevel)
	{
		for (MenuItemControl control : controls)
		{
			if (control.getType() == ActionGroupItem.TYPE_ACTION)
			{
				MenuItem item = menu.add(Menu.NONE, control.getMenuId(), Menu.NONE, control.getCaption());
				configureMenuItem(item, control);
				control.setMenuItem(item);
			}
			else if (control.getType() == ActionGroupItem.TYPE_GROUP)
			{
				SubMenu subMenu = menu.addSubMenu(Menu.NONE, control.getMenuId(), Menu.NONE, control.getCaption());
				configureMenuItem(subMenu.getItem(), control);
				control.setMenuItem(subMenu.getItem());

				// Android only supports only ONE LEVEL of SubMenus.
				// Trying to create a SubMenu inside of another SubMenu throws an exception.
				if (isFirstLevel)
					initializeMenu(subMenu, control.getSubItems(), false);
				else
					Services.Log.warning(String.format("Android does not support nested SubMenus. Ignoring children of '%s'.", control.getName()));
			}
		}
	}

	private void configureMenuItem(MenuItem menuItem, ActionGroupBaseItemControl<?> control)
	{
		int showOption;
		if (control.getPriority() == ActionGroupItemDefinition.PRIORITY_HIGH)
			showOption = MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT;
		else if (control.getPriority() == ActionGroupItemDefinition.PRIORITY_LOW)
			showOption = MenuItem.SHOW_AS_ACTION_NEVER;
		else
			showOption = MenuItem.SHOW_AS_ACTION_IF_ROOM;

		menuItem.setShowAsAction(showOption);
		menuItem.setEnabled(control.isEnabled());
		menuItem.setVisible(control.isVisible());

		if (control.getAction() != null) {
			MenuItemControl itemControl = Cast.as(MenuItemControl.class, control);
			UIActionHelper.setMenuItemImage(mContext, menuItem, control.getAction(), itemControl);
		}
	}

	public void finalizeMenu()
	{
		for (MenuItemControl control : getAllItems())
			control.setMenuItem(null);
	}
}
