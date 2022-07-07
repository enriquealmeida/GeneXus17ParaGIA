package com.genexus.android.core.base.metadata.expressions;

/**
 * Interface for expression classes that can also be l-values in an assignment.
 * Assignable: variables, sdt fields, read-write properties (such as &SDTCollection.CurrentItem).
 * Non-assignable: constants, arithmetic/boolean expressions, methods, read-only properties (such as &SDTCollection.Count)
 */
public interface IAssignableExpression extends Expression
{
	boolean setValue(IExpressionContext context, Value value);
	String getRootName();
	String getFieldName();
}
