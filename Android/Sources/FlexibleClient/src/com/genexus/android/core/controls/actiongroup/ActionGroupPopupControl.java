package com.genexus.android.core.controls.actiongroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import android.view.MenuItem;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.layout.ActionGroupDefinition;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.ui.Anchor;

public class ActionGroupPopupControl extends ActionGroupBaseControl
{
	private final MenuItemManager mItemManager;
	private MenuPopupHelper mCurrentPopup;

	public ActionGroupPopupControl(IDataView dataView, ActionGroupDefinition definition)
	{
		super(dataView, definition);
		mItemManager = new MenuItemManager(dataView.getUIContext());
		mItemManager.setDefinition(definition);
	}

	@Override
	protected void showActionGroup()
	{
		Anchor anchor = getContext().getAnchor();
		if (anchor != null && anchor.getView() != null)
		{
			// Create the Menu.
			MenuBuilder menu = new MenuBuilder(getActivity());
			menu.setCallback(mMenuCallback);
			mItemManager.initializeMenu(menu);

			// Show it with a Popup.
			mCurrentPopup = new MenuPopupHelper(SherlockHelper.getActionBarThemedContext(getActivity()), menu, anchor.getView());
			mCurrentPopup.show();
		}
	}

	@Override
	protected void hideActionGroup()
	{
		if (mCurrentPopup != null)
		{
			mCurrentPopup.dismiss();
			mCurrentPopup = null;
		}

		mItemManager.finalizeMenu();
	}

	private final MenuBuilder.Callback mMenuCallback = new MenuBuilder.Callback()
	{
		@Override
		public boolean onMenuItemSelected(@NonNull MenuBuilder menu, MenuItem item)
		{
			ActionDefinition action = mItemManager.getItemEvent(item.getItemId());
			runAction(action);
			return true;
		}

		@Override
		public void onMenuModeChange(@NonNull MenuBuilder menu) { }
	};

	@Override
	public IGxControl getControl(String name)
	{
		return mItemManager.getControl(name);
	}
}
