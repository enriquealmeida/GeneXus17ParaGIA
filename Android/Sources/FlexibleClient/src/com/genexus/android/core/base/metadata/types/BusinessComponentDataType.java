package com.genexus.android.core.base.metadata.types;

import java.io.Serializable;
import java.util.List;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataTypeDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;

public class BusinessComponentDataType extends DataTypeDefinition implements Serializable, IStructuredDataType
{
	private static final long serialVersionUID = 1L;

	private final StructureDefinition mStructure;

	public BusinessComponentDataType(StructureDefinition structure)
	{
		super(null);
		mStructure = structure;
	}

	@Override
	public String getName() { return mStructure.getName(); }

	@Override
	public String getType() { return DataTypes.BUSINESS_COMPONENT; }

	@Override
	public StructureDefinition getStructure() { return mStructure; }

	@Override
	public boolean isCollection() { return false; }

	@Override
	public List<DataItem> getItems()
	{
		return mStructure.getItems();
	}

	@Override
	public DataItem getItem(String name)
	{
		return mStructure.getAttribute(name);
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
			EntityList list = new EntityList(getStructure());
			list.setItemType(Expression.Type.BC);
			return list;
		}
		else
		{
			Entity entity = EntityFactory.newEntity(getStructure());
			entity.initialize();
			return entity;
		}
	}
}
