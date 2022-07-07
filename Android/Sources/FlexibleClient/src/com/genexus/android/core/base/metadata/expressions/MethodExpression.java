package com.genexus.android.core.base.metadata.expressions;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

import com.genexus.android.core.actions.BCMethodHandler;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.layout.ControlHelper;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.enums.GxObjectTypes;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.model.BaseCollection;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.EntityParentInfo;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.ImageActionsHelper;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.utils.Cast;

import static com.genexus.android.core.base.metadata.expressions.ExpressionFormatHelper.toUrlParameterString;

public class MethodExpression implements Expression, ITargetedExpression, IMethodCall
{
	static final String TYPE = "method";

	private final Expression mTarget;
	private final String mMethod;
	private final List<Expression> mParameters;

	private static final String METHOD_TO_STRING = "ToString";
	private static final String METHOD_TO_FORMATTED_STRING = "ToFormattedString";
	private static final String METHOD_CREATE = "Create";
	private static final String METHOD_LINK = "Link";
	private static final String METHOD_CONVERT = "Convert";
	private static final String METHOD_JSON_LINK = "JsonLink";

	public MethodExpression(INodeObject node)
	{
		mTarget = ExpressionFactory.parse(node.getNode("target"));
		mMethod = node.getString("@methName");
		mParameters = ExpressionFactory.parseParameters(node);
	}

	public MethodExpression(Expression target, String method, List<Expression> parameters)
	{
		mTarget = target;
		mMethod = method;
		mParameters = parameters;
	}

	@Override
	public String toString()
	{
		String parameters = mParameters.toString();
		parameters = parameters.substring(1, parameters.length() - 1); // Remove the [ ]
		return String.format("%s.%s(%s)", mTarget, mMethod, parameters);
	}

	@Override
	public Expression getTarget()
	{
		return mTarget;
	}

	@Override
	public String getMethod()
	{
		return mMethod;
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		Value target = context.eval(mTarget);
		if (target.mustReturn())
			return target;

		List<Value> parameters = ExpressionHelper.evalExpressions(mParameters, context);
		if (!parameters.isEmpty()) {
			Value last = parameters.get(parameters.size() - 1);
			if (last.mustReturn())
				return last;
		}

		// Special cases that are not mapped to actual methods.
		if (target.getType() == Type.COLLECTION)
		{
			BaseCollection<?> collection = target.coerceToCollection();
			EntityParentInfo parentInfo = null;
			if (collection instanceof EntityList) {
				Entity entity = context.getExecutionContext().getData();
				VariableExpression expr = Cast.as(VariableExpression.class, mTarget);
				if (expr != null)
					parentInfo = EntityParentInfo.collectionMemberOf(entity, expr.getName(), (EntityList)collection);
			}
			String variableName = null;
			if (mTarget instanceof IAssignableExpression)
			{
				variableName = ((IAssignableExpression)mTarget).getRootName();
			}
			else if (mTarget instanceof ValueExpression)
			{
				variableName = ((ValueExpression)mTarget).getName();
			}

			return evalCollectionMethod(context, collection, mMethod, parentInfo, parameters, variableName);
		}
		else if (target.getType() == Type.SDT)
		{
			return evalSdtMethod(target, mMethod, parameters);
		}
		else if (target.getType() == Type.DOMAIN)
		{
			return evalDomainMethod(target, mMethod, parameters);
		}
		else if (target.getType() == Type.BC)
		{
			return evalBCMethod(context, target, mMethod, parameters);
		}
		else if (target.getType() == Type.PANEL)
		{
			WorkWithDefinition workwith = (WorkWithDefinition)target.getValue();
			return evalWorkWithMethod(workwith, mMethod, parameters);
		}
		else if (target.getType() == Type.API)
		{
			ExternalApi api = (ExternalApi)target.getValue();
			if (api.getAction() == null)
				api.setAction(context.getAction());
			List<Object> values = ExpressionValueBridge.convertValuesToEntityFormat(parameters);
			return api.execute(this, context, mMethod, values);
		}
		else if (target.getType() == Type.OBJECT_LINK)
		{
			return evalObjectLink(target.coerceToString(), mMethod, parameters);
		}
		else if (target.getType() == Type.CONTROL)
		{
			IGxControl control = (IGxControl)target.getValue();
			return ControlHelper.callMethod(context.getExecutionContext(), control, mMethod, parameters);
		}
		else if (METHOD_TO_STRING.equalsIgnoreCase(mMethod) && (target.getType().isNumeric() || target.getType().isDateTime()))
		{
			return Value.newString(ExpressionFormatHelper.toString(target));
		}
		else if (METHOD_TO_FORMATTED_STRING.equalsIgnoreCase(mMethod) && parameters.isEmpty())
		{
			return Value.newString(ExpressionFormatHelper.toFormattedString(target));
		}
		else if (target.getDefinition() != null && DataTypes.isImage(target.getDefinition().getDataTypeName().getDataType())) {
			//Image data type is an exception because it has more methods defined aside from IsEmpty, SetEmpty and FromURL
			return ImageActionsHelper.handleImageMethod(ActivityHelper.getCurrentActivity(), target, mMethod, parameters);
		}

		// Generic methods
		return MethodHelper.call(target, mMethod, parameters);
	}

	@SuppressLint("DefaultLocale")
	private static @NonNull Value evalCollectionMethod(IExpressionContext context, BaseCollection<?> collection, String method, EntityParentInfo parentInfo, List<Value> parameters, String targetVariable)
	{
		boolean collectionModified = false;
		Value returnValue = Value.UNKNOWN; //Void
		if (BaseCollection.METHOD_GET_ITEM.equalsIgnoreCase(method) && parameters.size() == 1)
		{
			int position = parameters.get(0).coerceToInt();
			Object item = collection.get(position - 1);
			returnValue = ExpressionValueBridge.convertCollectionItemToValue(collection, item);
		}
		else if (BaseCollection.METHOD_ADD.equalsIgnoreCase(method) && parameters.size() >= 1)
		{
			Value item = ExpressionHelper.applyImplicitConversion(parameters.get(0), collection.getItemType());
			if (item == null)
				throw new IllegalArgumentException(String.format("Type mismatch! Trying to add item of type '%s' to collection of '%s'. " +
						"Types differ and no implicit conversion is available.", parameters.get(0).getType(), collection.getItemType()));

			int position = 0;
			if (parameters.size() >= 2)
				position = parameters.get(1).coerceToInt();

			if (position == 0)
				position = collection.size() + 1;

			// We could avoid this by receiving a BaseCollection with no captures, because type
			// compatibility has been already checked. However, we take the extra step for
			// better (Java) type safety.
			if (collection instanceof EntityList)
			{
				Entity typedItem = item.coerceToEntity();
				EntityList typedCollection = (EntityList)collection;
				if (parentInfo != null)
					typedItem.addParentInfo(parentInfo);
				typedCollection.add(position - 1, typedItem);
			}
			else
			{
				String typedItem = ExpressionValueBridge.convertValueToEntityFormat(item, null).toString();
				ValueCollection typedCollection = (ValueCollection)collection;
				typedCollection.add(position - 1, typedItem);
			}

			collectionModified = true;
		}
		else if (BaseCollection.METHOD_REMOVE.equalsIgnoreCase(method) && parameters.size() == 1)
		{
			int position = parameters.get(0).coerceToInt() - 1;
			if (collection.size() < position || position < 0) {
				if (targetVariable != null)
					Services.Log.error(String.format("Cannot remove item at index '%d' in '%s'", position, targetVariable));
			} else {
				collection.remove(position);
				collectionModified = true;
			}
		}
		else if (BaseCollection.METHOD_CLEAR.equalsIgnoreCase(method))
		{
			collection.clear();
			collectionModified = true;
		}
		else if (BaseCollection.METHOD_TO_JSON.equalsIgnoreCase(method))
		{
			String json = collection.serialize(Entity.JSONFORMAT_EXTERNAL).toString();
			returnValue = Value.newString(json);
		}
		else if (BaseCollection.METHOD_FROM_JSON.equalsIgnoreCase(method) && parameters.size() >= 1)
		{
			String json = parameters.get(0).coerceToString();
			collection.deserialize(Services.Serializer.createCollection(json), Entity.JSONFORMAT_EXTERNAL);
			if (collection instanceof EntityList && parentInfo != null)
				((EntityList)collection).setParentInfo(parentInfo);

			collectionModified = true;
		}
		else if (BaseCollection.METHOD_INDEXOF.equalsIgnoreCase(method) && parameters.size() == 1)
		{
			Value item = ExpressionHelper.applyImplicitConversion(parameters.get(0), collection.getItemType());
			if (item == null)
				throw new IllegalArgumentException(String.format("Type mismatch! Trying to get index of item of type '%s' to collection of '%s'. " +
						"Types differ and no implicit conversion is available.", parameters.get(0).getType(), collection.getItemType()));

			int position;
			if (collection instanceof EntityList)
			{
				Entity typedItem = item.coerceToEntity();
				position = collection.indexOf(typedItem) + 1;
			}
			else
			{
				String typedItem = ExpressionValueBridge.convertValueToEntityFormat(item, null).toString();
				ValueCollection typedCollection = (ValueCollection)collection;
				position = typedCollection.indexOf(typedItem) + 1;
			}

			returnValue = Value.newInteger(position);
		}
		else
			throw new IllegalArgumentException(String.format("Unexpected collection method: '%s'", method));

		if (collectionModified && targetVariable != null)
			context.updateUIAfterOutput(targetVariable);

		return returnValue;
	}

	private static @Nullable Value evalEntityMethod(Entity entity, Type valueType, String method, List<Value> parameters)
	{
		if (BaseCollection.METHOD_TO_JSON.equalsIgnoreCase(method)) {
			String json = entity.serialize(Entity.JSONFORMAT_EXTERNAL).toString();
			return Value.newString(json);
		}
		else if (BaseCollection.METHOD_FROM_JSON.equalsIgnoreCase(method) && parameters.size() >= 1) {
			entity = EntityFactory.newEntity(entity.getDefinition());
			entity.initialize(); // So it initialize members that are not in the json

			String json = parameters.get(0).coerceToString();
			INodeObject node = Services.Serializer.createNode(json);
			if (node != null) // null means error parsing
				entity.deserialize(node, Entity.JSONFORMAT_EXTERNAL);

			return new Value(valueType, entity); // it will be assigned in MethodCallAction.Do()
		}
		return null;
	}

	private static @NonNull Value evalSdtMethod(Value value, String method, List<Value> parameters)
	{
		Value result = evalEntityMethod(value.coerceToEntity(), Type.SDT, method, parameters);
		if (result != null)
			return result;
		else
			throw new IllegalArgumentException(String.format("Unexpected SDT method: '%s'", method));
	}

	private static @NonNull Value evalBCMethod(IExpressionContext context, Value value, String method, List<Value> parameters)
	{
		Value result = evalEntityMethod(value.coerceToEntity(), Type.BC, method, parameters);
		if (result != null)
			return result;
		else
			return BCMethodHandler.eval(context.getExecutionContext().getUIContext(), value, method, parameters);
	}

	private static @NonNull Value evalDomainMethod(Value value, String method, List<Value> parameters)
	{
		if (METHOD_CONVERT.equalsIgnoreCase(method) && parameters.size() == 1) {
			return parameters.get(0); // No conversion needed because we use the value for domains
		}
		return Value.UNKNOWN;
	}

	private static @NonNull Value evalWorkWithMethod(WorkWithDefinition wwd, String method, List<Value> parameters)
	{
		if (METHOD_CREATE.equalsIgnoreCase(method) || METHOD_LINK.equalsIgnoreCase(method)) {
			StringBuilder builder = new StringBuilder();
			builder.append("sd:");
			builder.append(wwd.getName());
			builder.append(Strings.QUESTION);
			int count = parameters.size();
			for (int n = 0; n < count; n++) {
				builder.append(Services.HttpService.uriEncode(toUrlParameterString(parameters.get(n))) );
				if (n+1 < count)
					builder.append(',');
			}
			return Value.newString(builder.toString());
		}
		else
			throw new IllegalArgumentException(String.format("Unexpected PANEL method: '%s'", method));
	}

	private static @NonNull Value evalObjectLink(String target, String method, List<Value> parameters) {
		if (METHOD_JSON_LINK.equalsIgnoreCase(method)) {
			boolean isDataProvider = GxObjectTypes.getGxObjectTypeFromName(target) == GxObjectTypes.DATAPROVIDER;
			boolean isQuery = GxObjectTypes.getGxObjectTypeFromName(target) == GxObjectTypes.QUERY;
			if (isDataProvider || isQuery) {
				String objName = MetadataLoader.getObjectName(target);
				INodeCollection collection = Services.Serializer.createCollection();
				collection.put(isQuery ? "query" : "dp");
				collection.put(objName);
				for (Value parameter : parameters) {
					Object value = ExpressionValueBridge.convertValueToEntityFormat(parameter, null);
					collection.put(value.toString());
				}
				return Value.newString(collection.toString());
			} else {
				// This should never happend, they are filtered in GxObjectLinkExpression
				throw new IllegalArgumentException("Unexpected type for link");
			}
		} else {
			throw new IllegalArgumentException(String.format("Unexpected method for link: '%s'", method));
		}
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) {
		mTarget.values(nameTypes);
		for (Expression e : mParameters)
			e.values(nameTypes);
	}

	@Override
	public boolean needsBackgroundThread() {
		if (mTarget.needsBackgroundThread())
			return true;
		for (Expression e : mParameters) {
			if (e.needsBackgroundThread())
				return true;
		}
		return false;
	}
}
