package com.genexus.android.core.base.metadata;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.utils.Strings;

public class StructureDefinition implements IPatternMetadata, IGxObjectDefinition
{
	private static final long serialVersionUID = 1L;

	public static final StructureDefinition EMPTY = new StructureDefinition(Strings.EMPTY);

	private String mName;
	private Connectivity mConnectivitySupport = Connectivity.Inherit;
	@SuppressWarnings("checkstyle:MemberName")
	public LevelDefinition Root = new LevelDefinition(null);
	@SuppressWarnings("checkstyle:MemberName")
	public List<RelationDefinition> ManyToOneRelations = new ArrayList<>();

	public StructureDefinition(String name)
	{
		mName = name;
	}

	@Override
	public String getName() { return mName; }

	@Override
	public void setName(String name) { mName = name; }

	public DataItem getAttribute(String name)
	{
		return Root.getAttribute(name);
	}

	public DataItem getAttributeByExternalName(String name)
	{
		for (DataItem item : Root.Items)
			if (item.getExternalName().equalsIgnoreCase(name))
				return item;

		return null;
	}

	public DataItem getDescriptionAttribute() {
		return Root.getDescriptionAttribute();
	}

	public LevelDefinition getLevel(String code)
	{
		if (Root.getName().equalsIgnoreCase(code))
			return Root;
		return getSubLevel(code);
	}

	private LevelDefinition getSubLevel(String name)
	{
		return Root.getLevel(name);
	}

	public List<DataItem> getItems()
	{
		return Root.getAttributes();
	}

	public void merge(StructureDefinition bc)
	{
		//this.m_Name = bc.getName();
		//this.Root.setName(bc.Root.getName());
		Root.merge(bc.Root);
		ManyToOneRelations = bc.ManyToOneRelations;
	}

	public boolean isEmpty()
	{
		return (getItems().size() == 0);
	}

	@Override
	public Connectivity getConnectivitySupport()
	{
		return mConnectivitySupport;
	}

	public void setConnectivitySupport(Connectivity val) {
		mConnectivitySupport = val;
	}
}
