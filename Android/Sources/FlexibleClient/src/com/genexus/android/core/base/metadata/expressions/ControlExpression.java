package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.serialization.INodeObject;

import java.util.HashMap;

class ControlExpression implements Expression
{
	static final String TYPE = "control";

	private final String mControlName;

	public ControlExpression(INodeObject node)
	{
		mControlName = node.getString("@exprValue");
	}

	@Override
	public String toString()
	{
		return mControlName;
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		return new Value(Type.CONTROL, context.getControl(mControlName));
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) { }

	@Override
	public boolean needsBackgroundThread() {
		return false;
	}
}
