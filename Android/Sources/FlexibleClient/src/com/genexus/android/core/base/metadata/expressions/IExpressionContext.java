package com.genexus.android.core.base.metadata.expressions;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.base.metadata.expressions.Expression.Type;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.controls.IGxControl;

public interface IExpressionContext
{
	Value getValue(String name, Type expectedType);
	void setValue(@NonNull String name, Value value);
	IGxControl getControl(String name);
	ExecutionContext getExecutionContext();
	void updateUIAfterOutput(String name);
	Action getAction();

	// Activity result and request permissions result
	Value eval(Expression expression); // saves the state so the eval can wait
	boolean isActivityResult(Expression expression);
	boolean isRequestPermissionsResult(Expression expression);
	int requestCode();
	int resultCode();
	Intent result();
	String[] permissions();
	int[] grantResults();
}
