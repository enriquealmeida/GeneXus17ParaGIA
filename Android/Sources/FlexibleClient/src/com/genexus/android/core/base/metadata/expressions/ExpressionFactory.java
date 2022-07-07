package com.genexus.android.core.base.metadata.expressions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.expressions.Expression.Type;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class ExpressionFactory
{
	public static Expression parse(INodeObject node)
	{
		String exprType = node.getString("@exprType");

		if (ArithmeticExpression.TYPE.equalsIgnoreCase(exprType))
			return new ArithmeticExpression(node);

		if (AttributeExpression.TYPE.equalsIgnoreCase(exprType))
			return new AttributeExpression(node);

		if (BooleanExpression.TYPE.equalsIgnoreCase(exprType))
			return new BooleanExpression(node);

		if (ConstantBooleanExpression.TYPE.equalsIgnoreCase(exprType))
			return new ConstantBooleanExpression(node);

		if (ConstructorExpression.TYPE.equalsIgnoreCase(exprType))
			return new ConstructorExpression(node);

		if (ConstantDateExpression.TYPE.equalsIgnoreCase(exprType))
			return new ConstantDateExpression(node);

		if (ConstantImageExpression.TYPE.equalsIgnoreCase(exprType))
			return new ConstantImageExpression(node);

		if (ConstantNumberExpression.TYPE.equalsIgnoreCase(exprType))
			return new ConstantNumberExpression(node);

		if (ConstantStringExpression.TYPE.equalsIgnoreCase(exprType))
			return new ConstantStringExpression(node);

		if (ConstantCollectionExpression.TYPE.equalsIgnoreCase(exprType))
			return new ConstantCollectionExpression(node);

		if (ControlExpression.TYPE.equalsIgnoreCase(exprType))
			return new ControlExpression(node);

		if (FunctionExpression.TYPE.equalsIgnoreCase(exprType))
			return new FunctionExpression(node);

		if (KeywordExpression.TYPE.equalsIgnoreCase(exprType))
			return new KeywordExpression(node);

		if (MethodExpression.TYPE.equalsIgnoreCase(exprType))
			return new MethodExpression(node);

		if (PropertyExpression.TYPE.equalsIgnoreCase(exprType))
			return new PropertyExpression(node);

		if (StaticReferenceExpression.TYPE_DATATYPE.equalsIgnoreCase(exprType))
			return new StaticReferenceExpression(node);

		if (VariableExpression.TYPE.equalsIgnoreCase(exprType))
			return new VariableExpression(node);

		if (DomainExpression.TYPE.equalsIgnoreCase(exprType))
			return new DomainExpression(node);

		if (GxObjectExpression.TYPE.equalsIgnoreCase(exprType))
			return new GxObjectExpression(node);

		if (GxObjectLinkExpression.TYPE.equalsIgnoreCase(exprType))
			return new GxObjectLinkExpression(node);

		if (StyleClassExpression.TYPE.equalsIgnoreCase(exprType))
			return new StyleClassExpression(node);

		throw new IllegalArgumentException(String.format("Unknown expression type: '%s'.", exprType));
	}

	static List<Expression> parseParameters(INodeObject node)
	{
		ArrayList<Expression> parameters = new ArrayList<>();

		INodeObject parametersNode = node.getNode("parameters");
		if (parametersNode == null)
			parametersNode = node.getNode("Parameters"); // Difference in case between panel and dashboard.

		if (parametersNode != null) {
			for (INodeObject parameterNode : parametersNode.optCollection("parameter"))
				parameters.add(ExpressionFactory.parse(parameterNode.getNode("expression")));
		}

		return parameters;
	}

	private static final String DATA_TYPE_PREFIX_COLLECTION = "Collection/";
	private static final String DATA_TYPE_PREFIX_SDT = "SDT/";
	private static final String DATA_TYPE_PREFIX_BC = "BC/";
	private static final String DATA_TYPE_PREFIX_TYP = "TYP/";

	static @NonNull DataType parseGxDataType(String dataType)
	{
		if (Strings.hasValue(dataType))
		{
			if (dataType.startsWith(DATA_TYPE_PREFIX_COLLECTION))
			{
				DataType itemType = parseGxDataType(dataType.substring(DATA_TYPE_PREFIX_COLLECTION.length()));
				return new DataType(Type.COLLECTION, itemType);
			}
			else if (dataType.equalsIgnoreCase("Character") || dataType.equalsIgnoreCase("VarChar") || dataType.equalsIgnoreCase("LongVarChar"))
				return new DataType(Type.STRING);
			else if (Strings.starsWithIgnoreCase(dataType, "Numeric"))
				return (dataType.endsWith(",0)") || dataType.endsWith(",0-)") ? new DataType(Type.INTEGER) : new DataType(Type.DECIMAL));
			else if (dataType.equalsIgnoreCase("Boolean"))
				return new DataType(Type.BOOLEAN);
			else if (dataType.equalsIgnoreCase("Date"))
				return new DataType(Type.DATE);
			else if (dataType.equalsIgnoreCase("DateTime"))
				return new DataType(Type.DATETIME);
			else if (dataType.equalsIgnoreCase("GUID"))
				return new DataType(Type.GUID);
			else if (dataType.equalsIgnoreCase("GeoPoint"))
				return new DataType(Type.GEOPOINT);
			else if (dataType.equalsIgnoreCase("GeoLine"))
				return new DataType(Type.GEOLINE);
			else if (dataType.equalsIgnoreCase("GeoPolygon"))
				return new DataType(Type.GEOPOLYGON);
			else if (dataType.equalsIgnoreCase("Geography"))
				return new DataType(Type.GEOGRAPHY);
			else if (dataType.equalsIgnoreCase("Image"))
				return new DataType(Type.IMAGE);
			else if (dataType.equalsIgnoreCase("Audio"))
				return new DataType(Type.AUDIO);
			else if (dataType.equalsIgnoreCase("Video"))
				return new DataType(Type.VIDEO);
			else if (dataType.equalsIgnoreCase("BlobFile"))
				return new DataType(Type.BLOBFILE);
			else if (dataType.equalsIgnoreCase("Blob"))
				return new DataType(Type.BLOB);
			else if (Strings.starsWithIgnoreCase(dataType, DATA_TYPE_PREFIX_BC))
				return new DataType(Type.BC, dataType.substring(DATA_TYPE_PREFIX_BC.length()));
			else if (Strings.starsWithIgnoreCase(dataType, DATA_TYPE_PREFIX_SDT))
				return new DataType(Type.SDT, dataType.substring(DATA_TYPE_PREFIX_SDT.length()));
			else if (Strings.starsWithIgnoreCase(dataType, DATA_TYPE_PREFIX_TYP))
				return new DataType(Type.API, dataType.substring(DATA_TYPE_PREFIX_TYP.length()));
		}

		Services.Log.error(String.format("Unknown expression data type: '%s'.", dataType));
		return new DataType(Type.UNKNOWN);
	}

	public static IMethodCall newMethodCall(Expression target, String method, List<Expression> parameters)
	{
		return new MethodExpression(target, method, parameters);
	}
}
