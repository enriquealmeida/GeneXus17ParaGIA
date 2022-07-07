package com.genexus.android.core.base.metadata.rules;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ListUtils;

public class RulesDefinition implements Serializable
{
	private static HashMap<String, Class<? extends RuleDefinition>> sRuleTypes;

	static
	{
		sRuleTypes = new HashMap<>();
		sRuleTypes.put(PromptRuleDefinition.RULE_NAME, PromptRuleDefinition.class);
		sRuleTypes.put(FilterPromptRuleDefinition.RULE_NAME, FilterPromptRuleDefinition.class);
	}

	private final IDataViewDefinition mParent;
	private final ArrayList<Object> mRules;

	public RulesDefinition(IDataViewDefinition parent)
	{
		mParent = parent;
		mRules = new ArrayList<>();
	}

	public void deserialize(INodeObject jsonRules)
	{
		if (jsonRules == null)
			return;

		for (String ruleName : jsonRules.names())
		{
			Class<? extends RuleDefinition> ruleType = sRuleTypes.get(ruleName);
			if (ruleType != null)
			{
				for (INodeObject jsonRule : jsonRules.optCollection(ruleName))
				{
					try
					{
						Constructor<? extends RuleDefinition> constructor = ruleType.getConstructor(IDataViewDefinition.class, INodeObject.class);
						RuleDefinition rule = constructor.newInstance(mParent, jsonRule);
						mRules.add(rule);
					}
					catch (InstantiationException | InvocationTargetException
							| NoSuchMethodException | IllegalAccessException ex)
					{
						Services.Log.error(String.format("Error deserializing rule '%s'.", jsonRule), ex);
					}
				}
			}
			else
				Services.Log.warning(String.format("Unknown rule type: '%s'.", ruleName));
		}
	}

	public <RuleTypeT extends RuleDefinition> List<RuleTypeT> getRules(Class<RuleTypeT> type)
	{
		return ListUtils.itemsOfType(mRules, type);
	}
}
