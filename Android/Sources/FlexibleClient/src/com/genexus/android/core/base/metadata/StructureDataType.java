package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.List;

import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.types.IStructuredDataType;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;

public class StructureDataType extends DataTypeDefinition implements Serializable, IStructuredDataType
{
	private static final long serialVersionUID = 1L;
	private StructureDefinition mStructure;

	public StructureDataType(INodeObject jsonData)
	{
		super(jsonData);
	}

	private LevelDefinition mRoot;

	public LevelDefinition getRoot()
	{
		if (mRoot == null)
			mRoot = new LevelDefinition(this);

		return mRoot;
	}

	public LevelDefinition getLevel(String code)
	{
		if (getRoot().getName().equalsIgnoreCase(code))
			return getRoot();

		return getRoot().getLevel(code);
	}

	@Override
	public void deserialize(INodeObject obj)
	{
		LevelDefinition rootLevel = getRoot();
		rootLevel.setName(obj.getString("Name"));
		rootLevel.setIsCollection(obj.optBoolean("IsCollection"));
		rootLevel.setCollectionItemName(obj.optString("CollectionItemName"));
		rootLevel.setJsonNullSerialization(obj.optString("JsonNullSerialization"));

		INodeCollection items = obj.optCollection("Items");
		deserializeLevel(rootLevel, items);
	}

	private void deserializeLevel(LevelDefinition levelDef, INodeCollection items)
	{
		for (int i = 0; i < items.length(); i++)
		{
			INodeObject item = items.getNode(i);
			INodeCollection subItems = item.getCollection("Items");
			boolean isCollection = item.optBoolean("IsCollection");
			String jsonNullSerialization = item.optString("JsonNullSerialization");

			if (subItems != null)
			{
				LevelDefinition subLevel = new LevelDefinition(this);
				subLevel.setParent(levelDef);

				subLevel.setName(item.getString("Name"));
				subLevel.setIsCollection(isCollection);
				subLevel.setCollectionItemName(item.optString("CollectionItemName"));
				subLevel.setJsonNullSerialization(jsonNullSerialization);

				levelDef.Levels.add(subLevel);
				deserializeLevel(subLevel, subItems);
			}
			else
			{
				PropertiesObject props = new PropertiesObject();
				props.deserialize(item);
				ITypeDefinition tdef = DataTypes.getDataTypeOf(props.getInternalProperties());
				DataItem subItem = new DataItem(tdef);
				subItem.setIsCollection(isCollection);
				subItem.setJsonNullSerialization(jsonNullSerialization);

				levelDef.Items.add(subItem);
				subItem.deserialize(item);
			}
		}
	}

	@Override
	public StructureDefinition getStructure()
	{
		if (mStructure == null)
		{
			StructureDefinition sdtStructure = new StructureDefinition(getName());
			sdtStructure.Root = getRoot();
			mStructure = sdtStructure;
		}

		return mStructure;
	}

	@Override
	public boolean isCollection() { return getRoot().isCollection(); }

	@Override
	public List<DataItem> getItems()
	{
		return getRoot().getAttributes();
	}

	/**
	 * Searches for the specified subitem in the SDT structure
	 * @param name Item name (generally an individual item but may be a level).
	 */
	@Override
	public DataItem getItem(String name)
	{
		return getRoot().getAttribute(name);
	}

	@Override
	public String getType() { return DataTypes.SDT; }

	@Override
	public int getLength() { return 0; }

	@Override
	public int getDecimals() { return 0; }

	@Override
	public boolean getSigned() { return false; }

	@Override
	public boolean getIsEnumeration() { return false; }

	@Override
	public Object getEmptyValue(boolean isCollection)
	{
		if (isCollection || isCollection())
		{
			EntityList list = new EntityList(getStructure());
			list.setItemType(Expression.Type.SDT);
			return list;
		}
		else
		{
			Entity entity = EntityFactory.newEntity(getStructure());
			entity.initialize();
			return entity;
		}
	}

	@Override
	public boolean isEmptyValue(Object value)
	{
		if (value instanceof Entity)
		{
			Entity entity = (Entity)value;
			for (DataItem rootItem : mRoot.Items)
			{
				Object itemValue = entity.getProperty(rootItem.getName());
				if (!rootItem.isEmptyValue(itemValue))
					return false;
			}

			for (LevelDefinition level : mRoot.Levels)
			{
				Object levelValue = entity.getLevel(level.getName());
				if (!level.isEmptyValue(levelValue))
					return false;
			}

			return true; // All items are empty, so the structure itself is empty.
		}
		else if (value instanceof List)
		{
			return ((List)value).size() == 0;
		}
		else
			return (value == null);
	}
}
