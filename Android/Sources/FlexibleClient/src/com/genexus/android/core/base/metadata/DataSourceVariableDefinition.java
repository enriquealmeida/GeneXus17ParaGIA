package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.serialization.INodeObject;

class DataSourceVariableDefinition extends VariableDefinition
{
	private static final long serialVersionUID = 1L;

	public DataSourceVariableDefinition(INodeObject varNode)
	{
		super(varNode);
		setStorageType(varNode.optInt("@internalType"));
	}

	@Override
	public boolean isKey()
	{
		// Variables can never be part of the primary key.
		return false;
	}
}
