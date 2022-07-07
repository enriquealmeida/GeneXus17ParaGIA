package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.UUID;

import com.genexus.android.core.base.serialization.INodeObject;

import org.jetbrains.annotations.NotNull;

/**
 * Static Reference to an object or data type in an expression.
 * E.g. "Guid" in "Guid.NewGuid()".
 */
public class StaticReferenceExpression implements Expression
{
	static final String TYPE_DATATYPE = "datatype";

	private final String mReferenceType;
	private final String mReferenceName;

	public StaticReferenceExpression(INodeObject node)
	{
		mReferenceType = node.getString("@exprType");
		mReferenceName = node.getString("@exprValue");
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		// For now the only static references we support are to data types.
		if (TYPE_DATATYPE.equalsIgnoreCase(mReferenceType))
		{
			// Return a value of the specific data type in order to use the general method matching algorithm.
			return resolveDataTypeReference(mReferenceName);
		}

		throw new IllegalStateException(String.format("Unknown static reference type: %s", mReferenceType));
	}

	private static Value resolveDataTypeReference(String referenceName)
	{
		switch (referenceName.toLowerCase())
		{
			case "guid" : return Value.newGuid(new UUID(0, 0));
			case "directory" : return Value.newDirectory("");
			default : throw new IllegalArgumentException(String.format("Unsupported static type reference: %s", referenceName));
		}
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) { }

	@Override
	public boolean needsBackgroundThread() {
		return false;
	}

	@Override
	public @NotNull String toString() {
		return mReferenceName;
	}
}
