package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;
import com.genexus.android.core.base.serialization.INodeObject;
import java.util.HashMap;

abstract class BinaryExpression implements Expression
{
	private final String mOperator;
	private final Expression mLeft;
	private final Expression mRight;

	public BinaryExpression(INodeObject node)
	{
		mOperator = node.getString("@operator");

		// Although this is supposed to be a BINARY expression, some UNARY ones (such as not or -value) fall here.
		INodeObject leftNode = node.getNode("left");
		mLeft = (leftNode != null ? ExpressionFactory.parse(leftNode) : null);

		INodeObject rightNode = node.getNode("right");
		mRight = (rightNode != null ? ExpressionFactory.parse(rightNode) : null);
	}

	@Override
	public String toString()
	{
		if (mLeft != null)
			return String.format("(%s) %s (%s)", mLeft, mOperator, mRight);
		else
			return String.format("%s(%s)", mOperator, mRight);
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) {
		if (mLeft != null)
			mLeft.values(nameTypes);
		mRight.values(nameTypes);
	}

	@Override
	public boolean needsBackgroundThread() {
		return (mLeft != null && mLeft.needsBackgroundThread()) || mRight.needsBackgroundThread();
	}

	protected String getOperator()
	{
		return mOperator;
	}

	protected Expression getLeft()
	{
		return mLeft;
	}

	protected Expression getRight()
	{
		return mRight;
	}
}
