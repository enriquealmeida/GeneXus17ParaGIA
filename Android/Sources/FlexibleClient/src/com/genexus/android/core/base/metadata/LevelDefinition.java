package com.genexus.android.core.base.metadata;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.types.IStructuredDataType;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class LevelDefinition extends DataItem
{
	private static final long serialVersionUID = 1L;

	public LevelDefinition(ITypeDefinition attribute)
	{
		super(attribute);
	}

	private String mDescription = Strings.EMPTY;
	private LevelDefinition mParent;
	@SuppressWarnings("checkstyle:MemberName")
	public List<DataItem> Items = new ArrayList<>();
	@SuppressWarnings("checkstyle:MemberName")
	public List<LevelDefinition> Levels = new ArrayList<>();
	private List<DataItem> keys;
	private String mCollectionItemName;
	private boolean mNoParentLevel; // Used by non-first grid levels
	private StructureDefinition mStructure;

	public void setParent(LevelDefinition parent)
	{
		mParent = parent;
	}

	public LevelDefinition getParent()
	{
		return mParent;
	}

	public void setNoParentLevel(boolean noParentLevel) {
		mNoParentLevel = noParentLevel;
	}

	public boolean getNoParentLevel() {
		return mNoParentLevel;
	}

	public void setStructure(StructureDefinition structure) {
		mStructure = structure;
	}

	public StructureDefinition getStructure() {
		return mStructure;
	}

	public List<DataItem> getKeys()
	{
		if (keys == null)
		{
			keys = new ArrayList<>();
			for(int i = 0; i < Items.size(); i++)
			{
				DataItem att = Items.get(i);
				if (att.isKey())
					keys.add(att);
			}
		}

		return keys;
	}

	public DataItem getAttribute(String name)
	{
		for (int i = 0; i < Items.size(); i++)
		{
			DataItem def = Items.get(i);
			if (def.getName().equalsIgnoreCase(name))
				return def;
		}

		for (int i = 0; i < Levels.size(); i++)
		{
			LevelDefinition def = Levels.get(i);
			DataItem subAtt = def.getAttribute(name);
			if (subAtt!=null)
				return subAtt;
		}

		// Not found as a single data item, but might be a composite expression.
		int dotIndex = name.indexOf(Strings.DOT);
		if (dotIndex != -1)
		{
			String nameFirst = name.substring(0, dotIndex);
			String nameRest = name.substring(dotIndex + 1, name.length());
			DataItem first = getAttribute(nameFirst);
			if (first != null)
			{
				IStructuredDataType firstStructureType = first.getTypeInfo(IStructuredDataType.class);
				if (firstStructureType != null)
					return firstStructureType.getItem(nameRest);
			}
		}
		
		return null;
	}

	public void setDescription(String string)
	{
		mDescription = string;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public DataItem getDescriptionAttribute()
	{
		for(int i = 0; i < Items.size(); i++){
			DataItem att = Items.get(i);
			if (att.isDescription())
				return att;
		}
		return null;
	}

	public LevelDefinition getLevel(String name)
	{
		// Special case: An SDT that is a "root collection" defines a type for its collection item too.
		if (mParent == null && isCollection() && mCollectionItemName != null && mCollectionItemName.equalsIgnoreCase(name))
			return this;

		for (LevelDefinition level : Levels)
		{
			String levelName = level.getName();
			String levelItemName = level.getCollectionItemName();

			if (levelName.equalsIgnoreCase(name)
					|| (Services.Strings.hasValue(levelItemName) && levelItemName.equalsIgnoreCase(name)))
				return level;
		}

		return null;
	}

	public List<DataItem> getAttributes()
	{
		return Items;
	}

	public void merge(LevelDefinition levelDefinition)
	{
		// Prefer order of attributes in BC, not in DP definition.
		List<DataItem> mergedAttributes = new ArrayList<>();
		for (DataItem item : levelDefinition.Items)
		{
			DataItem di = getAttribute(item.getName());
			if (di != null)
				di.merge(item);
			else
				di = item.getCopy();

			mergedAttributes.add(di);
		}

		for (DataItem item : Items)
		{
			if (!mergedAttributes.contains(item))
				mergedAttributes.add(item);
		}

		Items = mergedAttributes;

		//TODO: Deep Merge of levels
		Levels = levelDefinition.Levels;
	}

	public void setCollectionItemName(String value)
	{
		mCollectionItemName = value;
	}

	public String getCollectionItemName()
	{
		return mCollectionItemName;
	}
}
