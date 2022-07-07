package com.genexus.android.core.base.metadata;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class WorkWithDefinition implements IPatternMetadata
{
	private static final long serialVersionUID = 1L;

	private String mName;
	private String mBCName = Strings.EMPTY;
	private StructureDefinition mBusinessComponent;

	private final List<WWLevelDefinition> mLevels = new ArrayList<>();

	private final InstanceProperties mInstanceProperties = new InstanceProperties();

	public List<WWLevelDefinition> getLevels()
	{
		return mLevels;
	}

	public WWLevelDefinition getLevel(int index)
	{
		return mLevels.get(index);
	}

	public WWLevelDefinition getLevel(String name)
	{
		if (!Services.Strings.hasValue(name))
			return getLevel(0);

		for (int i = 0; i < mLevels.size(); i++)
		{
			WWLevelDefinition level = getLevel(i);
			if (level.getName().equalsIgnoreCase(name))
				return level;
		}

		return null;
	}

	public void setBusinessComponent(String str)
	{
		mBCName = str;
	}

	public void setBusinessComponent(StructureDefinition def)
	{
		mBusinessComponent = def;
	}

	public StructureDefinition getBusinessComponent()
	{
		if (mBusinessComponent == null && mBCName != null)
			mBusinessComponent = Services.Application.getDefinition().getBusinessComponent(mBCName);

		return mBusinessComponent;
	}

	@Override
	public String getName() { return mName;	}

	@Override
	public void setName(String name) { mName = name; }

	public String getBusinessComponentName()
	{
		return mBCName;
	}

	public Iterable<IDataViewDefinition> getDataViews()
	{
		List<IDataViewDefinition> dataViews = new ArrayList<>();
		for (WWLevelDefinition level : mLevels)
		{
			if (level.getList() != null)
				dataViews.add(level.getList());
			
			if (level.getDetail() != null)
			{
				dataViews.add(level.getDetail());
				dataViews.addAll(level.getDetail().getSections());
			}
		}
		
		return dataViews;
	}

	public InstanceProperties getInstanceProperties()
	{
		return mInstanceProperties;
	}
}
