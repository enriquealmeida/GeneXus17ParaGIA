package com.genexus.android.core.base.metadata.rules;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.serialization.INodeObject;

/**
 * Standard prompt rule; applies to an attribute/variable inside a form.
 */
public class PromptRuleDefinition extends AbstractPromptRuleDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	static final String RULE_NAME = "prompt";

	public PromptRuleDefinition(IDataViewDefinition parent, INodeObject jsonRule)
	{
		super(parent, jsonRule);
	}
}
