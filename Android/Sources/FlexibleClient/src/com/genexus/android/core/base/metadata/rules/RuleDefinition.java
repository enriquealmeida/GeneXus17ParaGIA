package com.genexus.android.core.base.metadata.rules;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.IDataViewDefinition;

/**
 * Base class for rules.
 */
public abstract class RuleDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final IDataViewDefinition mParent;

	protected RuleDefinition(IDataViewDefinition parent)
	{
		mParent = parent;
	}

	protected IDataViewDefinition getParent()
	{
		return mParent;
	}
}
