package com.genexus.android.core.base.metadata.layout;

import java.util.List;

import com.genexus.android.core.base.metadata.DetailDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.layout.SectionsLayoutVisitor;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.LayoutHelper;

/**
 * Metadata definition for the "all content" control.
 */
public class AllContentDefinition extends LayoutItemDefinition
{
	private int mDisplay;

	public static final int DISPLAY_TABS = 0;
	public static final int DISPLAY_INLINE = 1;
	public static final int DISPLAY_LINK = 2;

	public AllContentDefinition(LayoutDefinition layout, LayoutItemDefinition itemParent)
	{
		super(layout, itemParent);
	}

	@Override
	public void readData(INodeObject node)
	{
		super.readData(node);
		mDisplay = Services.Strings.parseIntEnum(node.optString("@display"), DISPLAY_TABS, "Tab", "Inline", "Link");
	}

	public int getDisplay()
	{
		return mDisplay;
	}

	// DisplayModes
	private short mTrnMode = 0;

	public void setTrnMode(short trnMode)
	{
		mTrnMode = trnMode;
	}

	@Override
	public boolean hasAutoGrow()
	{
		if (mDisplay == DISPLAY_INLINE)
			return super.hasAutoGrow();
		else
		{
			this.getChildItems().size();
			IDataViewDefinition dataView = this.getLayout().getParent();
			if (dataView != null && dataView instanceof DetailDefinition)
			{
				List<SectionsLayoutVisitor.LayoutSection> tabsSections = LayoutHelper.getDetailSections((DetailDefinition) dataView, mTrnMode);
				if (tabsSections.size() == 1)
				{
					// One section container , need autogrow = true to scroll to work. In the metadata nothing is coming?
					//
					return true;
					//return super.hasAutoGrow();
				}
			}
			return false; // Tabs and Link do not have the autogrow property.
		}

	}
}
