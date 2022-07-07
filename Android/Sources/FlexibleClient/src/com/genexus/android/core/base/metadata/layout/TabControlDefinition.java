package com.genexus.android.core.base.metadata.layout;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.theme.TabControlClassExtensionKt;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class TabControlDefinition extends LayoutItemDefinition
{
	private static final long serialVersionUID = 1L;

	public enum TabStripKind { Fixed, Scrollable }

    // Constants of size
	private static final int TAB_HEIGHT_ICON_OR_TEXT_ONLY = 48; // dips
	private static final int TAB_HEIGHT_ICON_AND_TEXT = 72; // dips

	public static final int TAB_ICON_SIZE = 24; // dips, square

	private ArrayList<TabItemDefinition> mTabItems;
	private TabStripKind mTabStripKind;
	private Boolean mAllowSwipe;
	private Integer mHeight;

	public TabControlDefinition(LayoutDefinition layout, LayoutItemDefinition parent)
	{
		super(layout, parent);
	}

	//calculate bound of it childs tabs pages.
	public void calculateBounds(float absoluteWidth, float absoluteHeight)
	{
		// remove tab header to size.
		float absoluteHeightMinusTab = absoluteHeight - getTabStripHeight();
		if (absoluteHeightMinusTab < 0)
			absoluteHeightMinusTab = 0;

		//rest tab padding. tab not have padding anymore.
		float absoluteWidthMinusTab = absoluteWidth;
		if (absoluteWidthMinusTab < 0)
			absoluteWidthMinusTab = 0;

		for (TabItemDefinition item : getTabItems())
		{
			TableDefinition itemTable = item.getTable();
			itemTable.calculateBounds(absoluteWidthMinusTab, absoluteHeightMinusTab);
		}
	}

	public List<TabItemDefinition> getTabItems()
	{
		if (mTabItems == null)
		{
			ArrayList<TabItemDefinition> list = new ArrayList<>();
			for (LayoutItemDefinition item : getChildItems())
			{
				if (item instanceof TabItemDefinition)
					list.add((TabItemDefinition)item);
			}

			mTabItems = list;
		}

		return mTabItems;
	}

	public TabStripKind getTabStripKind()
	{
		if (mTabStripKind == null)
		{
			// Platform Default: Fixed if Tabs <= 3, scrollable otherwise.
			String strValue = optStringProperty("@TabsBehavior");
			if (Strings.hasValue(strValue) && strValue.equalsIgnoreCase("Show More Button"))
				mTabStripKind = TabStripKind.Fixed;
			else if (Strings.hasValue(strValue) && strValue.equalsIgnoreCase("Scroll"))
				mTabStripKind = TabStripKind.Scrollable;
			else // No value, unknown, or "Platform Default".
				mTabStripKind = (getTabItems().size() <= 3 ? TabStripKind.Fixed : TabStripKind.Scrollable);
		}

		return mTabStripKind;
	}

	public boolean getAllowSwipe()
	{
		if (mAllowSwipe == null)
			mAllowSwipe = getBooleanProperty("@tabsNavigation", true); // "Platform Default" == true.

		return mAllowSwipe;
	}

	// result in px
	public int getTabStripHeight()
	{
		if (mHeight == null)
		{
			// if height is override in the theme, use this value.
			int tabHeightFromTheme = getTabStripHeightFromTheme(this.getThemeClass());
			if (tabHeightFromTheme>0)
			{
				mHeight = tabHeightFromTheme;
				return tabHeightFromTheme;
			}

			int heightDips = TAB_HEIGHT_ICON_OR_TEXT_ONLY;

			for (TabItemDefinition tabItem : getTabItems())
			{
				boolean hasText = Strings.hasValue(tabItem.getCaption());
				boolean hasImage = Strings.hasValue(tabItem.getImage()) || Strings.hasValue(tabItem.getImageUnselected());
				boolean isImageTop = (tabItem.getImageAlignment() == Alignment.TOP || tabItem.getImageAlignment() == Alignment.BOTTOM);
				if (hasText && hasImage && isImageTop)
				{
					// At least one text with an image on top, so use the 72dip height as per spec.
					heightDips = TAB_HEIGHT_ICON_AND_TEXT;
					break;
				}
			}

			mHeight = Services.Device.dipsToPixels(heightDips);
		}

		return mHeight;
	}

	// result in px
	public static int getDefaultTabStripHeight(ThemeClassDefinition themeClass, boolean imageAndTextVertical)
	{
		int tabHeightFromTheme = getTabStripHeightFromTheme(themeClass);
		if (tabHeightFromTheme>0)
			return tabHeightFromTheme;

		// Default value
		int heightDips = TAB_HEIGHT_ICON_OR_TEXT_ONLY;
		if (imageAndTextVertical)
			heightDips = TAB_HEIGHT_ICON_AND_TEXT;
		return Services.Device.dipsToPixels(heightDips);
	}

	// result in px
	private static int getTabStripHeightFromTheme(ThemeClassDefinition tabControlClass)
	{
		if (tabControlClass != null)
		{
			int height = TabControlClassExtensionKt.getTabStripHeight(tabControlClass);
			//Services.Log.debug("TabControlDefinition getTabStripHeight theme height" + height);
			if (height > 0)
				return height;
		}
		else
		{
			Services.Log.warning("TabControlDefinition getTabStripHeight theme class not found");
		}
		return 0;
	}
}
