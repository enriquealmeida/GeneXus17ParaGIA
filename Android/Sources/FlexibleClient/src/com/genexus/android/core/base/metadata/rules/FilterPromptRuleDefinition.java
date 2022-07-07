package com.genexus.android.core.base.metadata.rules;

import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.serialization.INodeObject;

/**
 * Prompt rule that applies to a filter attribute (not a control on a "real" form).
 */
public class FilterPromptRuleDefinition extends AbstractPromptRuleDefinition
{
	private static final long serialVersionUID = 1L;
	
	static final String RULE_NAME = "filterPrompt";

	public FilterPromptRuleDefinition(IDataViewDefinition parent, INodeObject jsonRule)
	{
		super(parent, jsonRule);
	}

	@Override
	public String toString()
	{
		return "[Filter Prompt] " + super.toString();
	}
}
