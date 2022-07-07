package com.genexus.android.core.base.metadata.expressions;

/**
 * Interface for expression classes that have a target.something structure (such as methods or properties).
 */
public interface ITargetedExpression
{
	Expression getTarget();
}
