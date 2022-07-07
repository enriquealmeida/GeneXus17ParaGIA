package com.genexus.android.core.usercontrols.imagegallery;

import android.content.Context;
import android.graphics.Rect;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataTypeName;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.grids.CustomGridDefinition;

public class SDImageGalleryDefinition extends CustomGridDefinition
{
	private String mBehavior;
   	private String mImageAttribute;
	private String mThumbnailAttribute;
	private String mTitleAttribute;
	private String mSubtitleAttribute;
	private Rect mThumbnailSize;
	private boolean mHasShareAction;

	public SDImageGalleryDefinition(Context context, GridDefinition grid)
	{
		super(context, grid);
	}

	@Override
	protected void init(GridDefinition grid, ControlInfo controlInfo)
	{
		mBehavior = controlInfo.optStringProperty("@SDImageGalleryGridBehavior");

		mImageAttribute = readDataExpression("@SDImageGalleryDataAtt", "@SDImageGalleryDataField");
		// mThumbnailAttribute = readDataExpression(...)
		mTitleAttribute = readDataExpression("@SDImageGalleryTitleAtt", "@SDImageGalleryTitleField");
		mSubtitleAttribute = readDataExpression("@SDImageGallerySubtitleAtt", "@SDImageGallerySubtitleField");

		// Default image attribute, if not set, is first attribute of type image.
		if (!Services.Strings.hasValue(mImageAttribute))
			mImageAttribute = getDefaultImageAttribute(grid);

		// Remove this when the thumbnail attribute is added in definition.
		if (!Services.Strings.hasValue(mThumbnailAttribute))
			mThumbnailAttribute = mImageAttribute;

		if (mThumbnailSize == null)
		{
			int sideRight = 150;
			int sideBottom = 120;
			mThumbnailSize = new Rect(0, 0, sideRight, sideBottom);
		}

		mHasShareAction = controlInfo.optBooleanProperty("@SDImageGalleryEnableShare");
	}

	private static String getDefaultImageAttribute(GridDefinition grid)
	{
    	for (DataItem dataAtt : grid.getDataSourceItems())
    	{
    		if (dataAtt.getBaseType() != null)
    		{
    			DataTypeName attDataType = dataAtt.getDataTypeName();
    			if (attDataType != null && DataTypes.isImage(attDataType.getDataType()))
    				return dataAtt.getName();
    		}
    	}

    	return null;
	}

	public String getBehavior() { return mBehavior; }
	public String getImageAttribute() { return mImageAttribute; }
	public String getThumbnailAttribute() { return mThumbnailAttribute; }
	public String getTitleAttribute() { return mTitleAttribute; }
	public String getSubtitleAttribute() { return mSubtitleAttribute; }
	public Rect getThumbnailSize() { return mThumbnailSize; }
	public boolean hasShareAction() { return mHasShareAction; }
}
