package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class TabItemDefinition extends LayoutItemDefinition
{
	private static final long serialVersionUID = 1L;

	private String mControlName;
	private String mCaption;
	private String mImage;
	private String mImageUnSelected;
	private int mImageAlignment;
	private String mSelectedClass;

	public TabItemDefinition(LayoutDefinition layout, LayoutItemDefinition parent)
	{
		super(layout, parent);

		if (!(parent instanceof TabControlDefinition))
			throw new IllegalArgumentException("Tab item cannot be created outside of a tab control.");
	}

	@Override
	public String getName()
	{
		return mControlName;
	}

	@Override
	public String getCaption() { return Services.Language.getTranslation(mCaption); }
	public String getImage() { return mImage; }
	public String getImageUnselected() { return mImageUnSelected; }
	public int getImageAlignment() { return mImageAlignment; }

	@Override
	public TabControlDefinition getParent()
	{
		return (TabControlDefinition)super.getParent();
	}

	@Override
	public void readData(INodeObject node)
	{
		super.readData(node);
		mControlName = node.optString("@itemControlName");
		mCaption = node.optString("@caption");
		mImage = MetadataLoader.getObjectName(node.optString("@image"));
		mImageUnSelected = MetadataLoader.getObjectName(node.optString("@unselectedImage"));
		mImageAlignment = Alignment.parseImagePosition(node.optString("@imagePosition"), Alignment.START);
		mSelectedClass = node.optString("@selClass");
	}

	public TableDefinition getTable()
	{
		return (TableDefinition) getChildItems().get(0);
	}
	
	public ThemeClassDefinition getSelectedClass() {
		return Services.Themes.getThemeClass(mSelectedClass);
	}
	
	public ThemeClassDefinition getUnselectedClass()
	{
		return super.getThemeClass();
	}
}
