package com.genexus.android.core.base.controls;

import com.genexus.android.core.base.metadata.expressions.Expression.Value;

import java.util.List;

/**
 * Interface for user controls that support runtime properties, methods, and events.
 * NOTE: this can't be converted to Kotlin yet because defaults won't work
 */
public interface IGxControlRuntime {
	default void setPropertyValue(String name, Value value) { }
	default Value getPropertyValue(String name) { return null; }
	default Value callMethod(String name, List<Value> parameters) {	return null; }
}
