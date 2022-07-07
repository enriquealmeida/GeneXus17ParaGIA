package com.genexus.android.core.base.metadata.types;

import java.io.Serializable;
import java.util.List;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataTypeDefinition;
import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;

public class SdtCollectionItemDataType extends DataTypeDefinition implements Serializable, IStructuredDataType
{
	private static final long serialVersionUID = 1L;

	private final StructureDataType mSDT;
	private final LevelDefinition mLevel;
	private final boolean mIsCollection;

	public SdtCollectionItemDataType(StructureDataType sdt) {
		this(sdt, sdt.getStructure().Root, false);
	}

	public SdtCollectionItemDataType(StructureDataType sdt, LevelDefinition level, boolean isCollection)
	{
		super(null);
		mSDT = sdt;
		mLevel = level;
		mIsCollection = isCollection;
	}

	@Override
	public String getName()
	{
		return mSDT.getName() + "." + mLevel.getCollectionItemName();
	}

	@Override
	public String getType() { return DataTypes.SDT; }

	@Override
	public StructureDefinition getStructure() { return mSDT.getStructure(); }

	@Override
	public boolean isCollection() { return mIsCollection; }

	@Override
	public List<DataItem> getItems()
	{
		return mLevel.getAttributes();
	}

	@Override
	public DataItem getItem(String name)
	{
		return mLevel.getAttribute(name);
	}

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
		if (isCollection)
		{
			return new EntityList();
		}
		else
		{
			Entity entity = EntityFactory.newEntity(getStructure(), mLevel);
			entity.initialize();
			return entity;
		}
	}
}
