package com.genexus.android.core.base.metadata;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.base.utils.ListUtils;

public abstract class GxObjectDefinition implements IGxObjectDefinition
{
	private final short mType;
	private final String mName;
	private final List<ObjectParameterDefinition> mParameters;
	private Connectivity mConnectivitySupport;

	public GxObjectDefinition(short type, String name)
	{
		mType = type;
		mName = name;
		mParameters = new ArrayList<>();
		mConnectivitySupport = Connectivity.Inherit;
	}
	
	public void setConnectivitySupport(Connectivity conn) {
		mConnectivitySupport = conn;
	}

	@Override
	public Connectivity getConnectivitySupport() {
		return mConnectivitySupport;
	}

	public short getType()
	{
		return mType;
	}

	@Override
	public String getName()
	{
		return mName;
	}

	public List<ObjectParameterDefinition> getParameters()
	{
		return mParameters;
	}

	public ObjectParameterDefinition getParameter(int position)
	{
		return mParameters.get(position);
	}

	private List<ObjectParameterDefinition> mOutParameters;

	public List<ObjectParameterDefinition> getOutParameters()
	{
		if (mOutParameters == null)
		{
			mOutParameters = ListUtils.select(mParameters, new Function<ObjectParameterDefinition, Boolean>()
			{
				@Override
				public Boolean run(ObjectParameterDefinition p) { return p.isOutput(); }
			});
		}

		return mOutParameters;
	}

	public StructureDefinition getParametersStructure()
	{
		StructureDefinition structure = new StructureDefinition(mName);
		structure.Root.setName(mName);

		List<DataItem> dataItems = new ArrayList<>();

		for (ObjectParameterDefinition dataItem : mParameters)
		{
			dataItems.add(dataItem);
		}

		structure.Root.Items.addAll(dataItems);

		return structure;
	}


}
