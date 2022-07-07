package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.utils.Strings;

/**
 * Data Type of an expression.
 * Can be: simple types (e.g. integer), complex types, (e.g. SDT:Customer), or
 * a parameterized type (e.g. Collection[integer] or Collection[SDT:Customer]).
 */
public class DataType
{
	public final Expression.Type type;
	public final DataType typeParameter;
	public final String typeInfo;

	public DataType(@NonNull Expression.Type type)
	{
		this(type, null, null);
	}

	public DataType(@NonNull Expression.Type type, @Nullable DataType typeParameter)
	{
		this(type, typeParameter, null);
	}

	public DataType(@NonNull Expression.Type type, @Nullable String typeInfo)
	{
		this(type, null, typeInfo);
	}

	private DataType(@NonNull Expression.Type type, @Nullable DataType typeParameter, @Nullable String typeInfo)
	{
		if (type.isParameterized() && typeParameter == null)
			throw new IllegalStateException("Missing type parameter for parameterized type!");

		if ((type == Expression.Type.SDT || type == Expression.Type.BC) && !Strings.hasValue(typeInfo))
			throw new IllegalStateException("Missing type info for structured type!");

		this.type = type;
		this.typeParameter = typeParameter;
		this.typeInfo = typeInfo;
	}

	@Override
	public String toString()
	{
		if (typeParameter != null)
			return type + "<" + typeParameter + ">";
		else if (Strings.hasValue(typeInfo))
			return type + "(" + typeInfo + ")";
		else
			return type.toString();
	}
}
