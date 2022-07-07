package com.genexus.android.core.base.metadata.expressions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.expressions.Expression.Type;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.DecimalUtil;

class ExpressionHelper
{
	public static List<Value> evalExpressions(List<Expression> expressions, IExpressionContext context)
	{
		ArrayList<Value> values = new ArrayList<>();

		for (Expression expression : expressions) {
			Value value = context.eval(expression);
			values.add(value);
			if (value.mustReturn())
				break;
		}

		return values;
	}

	static List<Type> getTypesOfValues(List<Value> values)
	{
		ArrayList<Type> types = new ArrayList<>();
		for (Value value : values)
			types.add(value.getType());

		return types;
	}

	/**
	 * Some types have implicit conversions between them in GX.
	 * Some of these conversions are to compatible types (e.g. int to decimal, date to datetime)
	 * while others involve loss or precision (e.g. decimal to int).
	 *
	 * If the conversion is not necessary, the same Value object is returned.
	 * If the conversion is not possible (e.g. date to int) this method returns null.
	 *
	 * @param value Value to be converted.
	 * @param targetType Target type.
	 * @return A new value, possibly converted to the target type.
	 */
	static @Nullable Value applyImplicitConversion(@NonNull Value value, @NonNull Type targetType)
	{
		// Exact same type? No conversion necessary.
		if (value.getType() == targetType)
			return value;

		// Compatible type? Just Get use the new type with the same object.
		for (Type compatibleType : getCompatibleTypesForType(targetType))
			if (compatibleType == value.getType())
				return new Value(targetType, value.getValue());

		// Specific conversion?
		if (targetType == Type.INTEGER && value.getType() == Type.DECIMAL)
			return Value.newInteger(value.coerceToInt());

		return null;
	}

	/**
	 * Read as "a parameter of type <type>" can be passed (possibly with automatic conversion)
	 * a value of any of the returned types.
	 * E.g. a function that receives DECIMAL can be called with DECIMAL or INTEGER.
	 */
	static @NonNull Type[] getCompatibleTypesForType(Type type)
	{
		switch (type)
		{
			case DECIMAL : return new Type[] { Type.DECIMAL, Type.INTEGER };
			case DATETIME : return new Type[] { Type.DATETIME, Type.DATE, Type.TIME };
			default : return new Type[] { type };
		}
	}

	static Class<?> typeToJavaClass(Type type)
	{
		switch (type)
		{
			case STRING : return String.class;
			case INTEGER : return int.class;
			case DECIMAL : return BigDecimal.class;
			case BOOLEAN : return boolean.class;
			case DATE : return Date.class;
			case DATETIME : return Date.class;
			case TIME : return Date.class;
			case GUID : return UUID.class;
			case GEOPOINT : return String.class;
			case GEOLINE: return String.class;
			case GEOPOLYGON: return String.class;
			case GEOGRAPHY: return String.class;
			case IMAGE : return String.class;
			default : throw new IllegalArgumentException(String.format("No Java class known for type '%s'.", type));
		}
	}

	static Object valueToJavaObject(Value value, Type expectedType)
	{
		switch (expectedType)
		{
			case STRING : return value.coerceToString();
			case INTEGER : return value.coerceToInt();
			case DECIMAL : return value.coerceToNumber();
			case BOOLEAN : return value.coerceToBoolean();
			case DATE : return value.coerceToDate();
			case DATETIME : return value.coerceToDate();
			case TIME : return value.coerceToDate();
			case GUID : return value.coerceToGuid();
			case GEOPOINT : return value.coerceToString();
			case GEOLINE : return value.coerceToString();
			case GEOPOLYGON : return value.coerceToString();
			case GEOGRAPHY : return value.coerceToString();
			case IMAGE : return value.coerceToString();
			default : throw new IllegalArgumentException(String.format("No Java class known for type '%s'.", value.getType()));
		}
	}

	static Value javaObjectToValue(Type type, Object obj)
	{
		switch (type)
		{
			case STRING : return Value.newString((String)obj);
			case INTEGER : return Value.newInteger(javaObjectToInteger(obj));
			case DECIMAL : return Value.newDecimal(javaObjectToDecimal(obj));
			case BOOLEAN : return Value.newBoolean((Boolean)obj);
			case DATE : return new Value(Type.DATE, obj);
			case DATETIME : return new Value(Type.DATETIME, obj);
			case TIME : return new Value(Type.TIME, obj);
			case GUID : return Value.newGuid((UUID)obj);
			case GEOPOINT : return new Value(Type.GEOPOINT, obj);
			case GEOLINE : return new Value(Type.GEOLINE, obj);
			case GEOPOLYGON : return new Value(Type.GEOPOLYGON, obj);
			case GEOGRAPHY : return new Value(Type.GEOGRAPHY, obj);
			case IMAGE : return new Value(Type.IMAGE, obj);
			default : throw new IllegalArgumentException(String.format("No Java class known for type '%s'.", type));
		}
	}

	private static long javaObjectToInteger(Object obj)
	{
		if (obj instanceof Long)
			return (Long)obj;
		else if (obj instanceof Integer)
			return (Integer)obj;
		else if (obj instanceof Short)
			return (Short)obj;
		else if (obj instanceof Byte)
			return (Byte)obj;
		else
			throw new IllegalArgumentException(String.format("Unexpected Java class for Integer type: '%s'.", obj.getClass().getName()));
	}

	private static BigDecimal javaObjectToDecimal(Object obj)
	{
		if (obj instanceof BigDecimal)
			return (BigDecimal)obj;
		if (obj instanceof Double)
			return DecimalUtil.doubleToDec((Double)obj);
		else
			throw new IllegalArgumentException(String.format("Unexpected Java class for Decimal type: '%s'.", obj.getClass().getName()));
	}
}
