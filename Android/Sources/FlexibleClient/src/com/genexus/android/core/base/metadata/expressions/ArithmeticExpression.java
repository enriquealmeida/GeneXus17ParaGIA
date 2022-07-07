package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import java.math.MathContext;

import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.DecimalUtil;
import com.genexus.GXutil;

class ArithmeticExpression extends BinaryExpression
{
	static final String TYPE = "arithmetic";

	private static final String OPERATOR_ADD = "+";
	private static final String OPERATOR_SUBTRACT = "-";
	private static final String OPERATOR_MULTIPLY = "*";
	private static final String OPERATOR_DIVIDE = "/";
	private static final String OPERATOR_EXPONENTIATE = "^";

	private static final int MAXIMUM_PRECISION = 30;
	private static final MathContext MATH_CONTEXT = new MathContext(MAXIMUM_PRECISION);

	public ArithmeticExpression(INodeObject node)
	{
		super(node);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		String operator = getOperator();
		Value left = (getLeft() != null ? context.eval(getLeft()) : null);
		if (left != null && left.mustReturn())
			return left;
		Value right = (getRight() != null ? context.eval(getRight()) : null);
		if (right != null && right.mustReturn())
			return right;

		// Arithmetic operations are always performed as decimal operations, never as integer ones.
		// If an integer result is needed, that is solved by casting the result to an integer variable.
		if (OPERATOR_ADD.equalsIgnoreCase(operator))
		{
			validateTwoOperands(operator, left, right);

			// Overload: Date + numeric.
			if (left.getType() == Type.DATE && right.getType().isNumeric())
				return new Value(Type.DATE, GXutil.dadd(left.coerceToDate(), right.coerceToInt()));

			// Overload: String + X or X + String -> string concatenation.
			if (left.getType() == Type.STRING || right.getType() == Type.STRING)
				return new Value(Type.STRING, left.coerceToString().concat(right.coerceToString()));

			// Standard: Numeric + Numeric
			return new Value(Type.DECIMAL, left.coerceToNumber().add(right.coerceToNumber()));
		}
		else if (OPERATOR_SUBTRACT.equalsIgnoreCase(operator))
		{
			// Pseudo-overload: no left side (in the case of a "-&Var" expression).
			if (left == null)
				return new Value(right.getType(), right.coerceToNumber().negate());

			validateTwoOperands(operator, left, right);

			// Overload: Date - Date
			if (left.getType() == Type.DATE && right.getType() == Type.DATE)
				return Value.newInteger(GXutil.ddiff(left.coerceToDate(), right.coerceToDate()));

			// Overload: Date - Numeric
			if (left.getType() == Type.DATE && right.getType().isNumeric())
				return new Value(Type.DATE, GXutil.dadd(left.coerceToDate(), -right.coerceToInt()));

			// Standard: Numeric - Numeric
			return new Value(Type.DECIMAL, left.coerceToNumber().subtract(right.coerceToNumber()));
		}
		else if (OPERATOR_MULTIPLY.equalsIgnoreCase(operator))
		{
			validateTwoOperands(operator, left, right);
			return new Value(Type.DECIMAL, left.coerceToNumber().multiply(right.coerceToNumber()));
		}
		else if (OPERATOR_DIVIDE.equalsIgnoreCase(operator))
		{
			validateTwoOperands(operator, left, right);

			// Precision and rounding mode are necessary to avoid ArithmeticException for periodic numbers in decimal (e.g. 1/3).
			return new Value(Type.DECIMAL, left.coerceToNumber().divide(right.coerceToNumber(), MATH_CONTEXT));
		}
		else if (OPERATOR_EXPONENTIATE.equalsIgnoreCase(operator))
		{
			validateTwoOperands(operator, left, right);
			return new Value(Type.DECIMAL, DecimalUtil.pow(left.coerceToNumber(), right.coerceToNumber()));
		}
		else
			throw new IllegalArgumentException(String.format("Unknown arithmetic operator: '%s'.", operator));
	}

	private static void validateTwoOperands(String operator, Expression.Value left, Expression.Value right)
	{
		if (left == null)
			throw new IllegalArgumentException("Operator '" + operator + "' has a null value as its left operand.");

		if (right == null)
			throw new IllegalArgumentException("Operator '" + operator + "' has a null value as its right operand.");
	}
}
