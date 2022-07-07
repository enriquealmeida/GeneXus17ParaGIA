package com.genexus.android.core.controls.grids;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.ControlPropertiesDefinition;

public abstract class CustomGridDefinition extends ControlPropertiesDefinition
{
	private final GridDefinition mGrid;
	private final Context mContext;

	public CustomGridDefinition(Context context, GridDefinition grid)
	{
		super(grid);
		mContext = context;
		mGrid = grid;

		init(grid, grid.getControlInfo());
	}

	public GridDefinition getGrid() { return (GridDefinition) getItem(); }

	protected abstract void init(GridDefinition grid, ControlInfo controlInfo);

	protected DataItem getAttribute(String name)
	{
		if (Services.Strings.hasValue(name))
		{
			for (DataItem dataItem : mGrid.getDataSourceItems())
				if (name.equalsIgnoreCase(dataItem.getName()))
					return dataItem;

			// Not found?
			Services.Log.warning(String.format("Attribute or variable '%s' referenced in definition of control '%s' is not present in data.", name, mGrid.getName()));
		}

		return null;
	}

	protected Drawable getDrawable(String imageName)
	{
		return getDrawable(imageName, 0);
	}

	protected Drawable getDrawable(String imageName, int defaultImageId)
	{
		Drawable drawable = Services.Images.getStaticImage(mContext, imageName, true);
		if (drawable == null && defaultImageId > 0)
			drawable = ContextCompat.getDrawable(mContext, defaultImageId);

		return drawable;
	}
}
