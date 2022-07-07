package com.genexus.android.core.base.metadata.expressions;

/**
 * Interface for method calls (can be either expressions or statements).
 */
public interface IMethodCall extends Expression
{
	Expression getTarget();
	String getMethod();
}
