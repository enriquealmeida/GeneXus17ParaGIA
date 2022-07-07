package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import com.genexus.android.layout.ControlHelper;
import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.WWLevelDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.model.BaseCollection;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.AudioPropertiesHelper;
import com.genexus.android.core.common.FilePropertiesHelper;
import com.genexus.android.core.common.ImageActionsHelper;
import com.genexus.android.core.common.StorageHelper;
import com.genexus.android.core.common.VideoPropertiesHelper;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.utils.Cast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class PropertyExpression implements Expression, IAssignableExpression, ITargetedExpression
{
	static final String TYPE = "property";

	private Expression mTarget;
	private @NonNull String mProperty;
	private DataType mType;

	public PropertyExpression(INodeObject node)
	{
		mTarget = ExpressionFactory.parse(node.getNode("target"));
		mProperty = node.getString("@propName");

		// Control properties do not have a data type, but SDT properties do.
		String exprDataType = node.optString("@exprDataType");
		if (Strings.hasValue(exprDataType))
			mType = ExpressionFactory.parseGxDataType(node.optString("@exprDataType"));
		else
			mType = new DataType(Type.UNKNOWN);
	}

	@Override
	public String toString()
	{
		return String.format("%s.%s", mTarget, mProperty);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		Value target = context.eval(mTarget);
		if (target.mustReturn())
			return target;

		if (target.getType() == Type.CONTROL) {
			return ControlHelper.getProperty(context.getExecutionContext(), (IGxControl)target.getValue(), mProperty);
		}
		else if (target.getType() == Type.COLLECTION) {
			BaseCollection<?> collection = target.coerceToCollection();
			if (mProperty.equalsIgnoreCase(BaseCollection.PROPERTY_COUNT))
				return Value.newInteger(collection.size());
			else if (mProperty.equalsIgnoreCase(BaseCollection.PROPERTY_CURRENT_ITEM))
				return ExpressionValueBridge.convertCollectionItemToValue(collection, collection.getCurrentItem());
		}
		else if (target.getType() == Type.SDT || target.getType() == Type.BC) {
			// The second part of this condition shouldn't be necessary.
			// However, in entity deserialization we are assuming that ALL levels are collections.
			// It's that way for BCs, but not necessarily for SDTs.
			Entity entity = target.coerceToEntity();

			// Check for sublevel first
			// TODO: This should not exist, but getProperty() doesn't return sublevels.
			EntityList subLevel = entity.getLevel(mProperty);
			if (subLevel != null)
				return Value.newCollection(subLevel);

			// It doesn't exist, add it. Only do it if sub level is collection.
			// Do not do it for normal sdt items (non collection), because it will put an invalid value.
			LevelDefinition subLevelDefinition = entity.getLevel().getLevel(mProperty);
			if (subLevelDefinition != null && subLevelDefinition.isCollection())
			{
				subLevel = new EntityList();
				subLevel.setItemType(Type.SDT);
				entity.putLevel(mProperty, subLevel);
				return Value.newCollection(subLevel);
			}

			// Generic property of entity.
			return ExpressionValueBridge.convertEntityFormatToValue(entity, mProperty, mType.type);
		}
		else if (target.getType() == Type.PANEL) {
			WorkWithDefinition wwd = Cast.as(WorkWithDefinition.class, target.getValue());
			if (wwd != null) {
				WWLevelDefinition level = wwd.getLevel(mProperty);
				if (level != null)
					return new Value(Type.PANEL, level);
			}
			else {
				WWLevelDefinition level = Cast.as(WWLevelDefinition.class, target.getValue());
				if (level != null) {
					if ("list".equalsIgnoreCase(mProperty))
						return new Value(Type.PANEL, level.getList());
					else if ("detail".equalsIgnoreCase(mProperty))
						return new Value(Type.PANEL, level.getDetail());
				}
			}
		}
		else if (target.getType() == Type.API) {
			ExternalApi api = (ExternalApi)target.getValue();
			return api.execute(this, context, mProperty, Collections.emptyList());
		}
		else if (target.getType() == Type.DIRECTORY) {
			if ("ApplicationDataPath".equalsIgnoreCase(mProperty))
				return Value.newDirectory(StorageHelper.getApplicationDataPath());
			if ("TemporaryFilesPath".equalsIgnoreCase(mProperty))
				return Value.newDirectory(StorageHelper.getTemporaryFilesPath());
			if ("ExternalFilesPath".equalsIgnoreCase(mProperty))
				return Value.newDirectory(StorageHelper.getExternalFilesPath());
			if ("CacheFilesPath".equalsIgnoreCase(mProperty))
				return Value.newDirectory(StorageHelper.getTemporaryFilesPath());
		}
		else if (DataTypes.isImage(target.getDefinition().getDataTypeName().getDataType()))
			return ImageActionsHelper.handleImageProperty(context.getExecutionContext().getUIContext().getActivity(), target, mProperty);
		else if (DataTypes.isVideo(target.getDefinition().getDataTypeName().getDataType()))
			return VideoPropertiesHelper.handleVideoProperty(target, mProperty);
		else if (DataTypes.isAudio(target.getDefinition().getDataTypeName().getDataType()))
			return AudioPropertiesHelper.handleAudioProperty(target, mProperty);
		else if (DataTypes.isFile(target.getDefinition().getDataTypeName().getDataType()))
			return FilePropertiesHelper.handleFileProperty(target, mProperty);

		throw new IllegalArgumentException(String.format("Unknown property ('%s').", toString()));
	}

	public DataType getType()
	{
		return mType;
	}

	@Override
	public Expression getTarget()
	{
		return mTarget;
	}

	@Override
	public boolean setValue(IExpressionContext context, Value value)
	{
		Value target = context.eval(mTarget);
		if (target.mustReturn())
			return false;

		if (target.getType() == Type.COLLECTION && mProperty.equalsIgnoreCase(BaseCollection.PROPERTY_CURRENT_ITEM))
		{
			// Handle special case: setting &SDTCollection.CurrentItem = &SDTItem.
			EntityList collection = Cast.as(EntityList.class, target.coerceToCollection());
			Entity setCurrentItem = value.coerceToEntity();
			if (collection != null && setCurrentItem != null && collection.contains(setCurrentItem))
			{
				collection.setCurrentItem(setCurrentItem);
				return true;
			}
		}
		else if (target.getType() == Type.SDT || target.getType() == Type.BC || target.getType() == Type.COLLECTION)
		{
			Entity entity = target.coerceToEntity();
			Object objValue = ExpressionValueBridge.convertValueToEntityFormat(value, entity.getPropertyDefinition(mProperty));
			return entity.setProperty(mProperty, objValue);
		}
		// For the "Uri" members of variables of media types, assign the variable itself
		else if (DataTypes.isMedia(target.getDefinition().getDataTypeName().getDataType()) && DataTypeProperties.MEDIA_URIS.contains(mProperty))
		{
			if (mTarget instanceof IAssignableExpression)
			{
				// Target might be an assignable expression itself (e.g. &sdt.image.imageuri = "x").
				return ((IAssignableExpression)mTarget).setValue(context, value);
			}
			else if (mTarget instanceof ValueExpression)
			{
				// Target is a variable (e.g. &theimage.imageuri = "x").
				context.setValue(((ValueExpression)mTarget).getName(), value);
				return true;
			}
		}
		else if (target.getType() == Type.API)
		{
			ExternalApi api = (ExternalApi)target.getValue();
			List<Object> values = new ArrayList<>();
			Object objValue = ExpressionValueBridge.convertValueToEntityFormat(value, null);
			values.add(objValue);
			ExternalApiResult result = api.execute("set" + mProperty, values);
			return result.getActionResult().isSuccess();
		}

		return false;
	}

	@Override
	public String getRootName()
	{
		return getRootName(this);
	}

	private static String getRootName(Expression expression)
	{
		if (expression instanceof ValueExpression)
			return ((ValueExpression)expression).getName();

		if (expression instanceof ITargetedExpression)
			return getRootName(((ITargetedExpression)expression).getTarget());

		return null;
	}

	@Override
	public String getFieldName()
	{
		String field = null;
		PropertyExpression expression = this;
		while (expression != null)
		{
			if (field != null)
				field = String.format("%s.%s", expression.mProperty, field);
			else
				field = expression.mProperty;

			ValueExpression valueExpression = Cast.as(ValueExpression.class, expression.getTarget());
			if (valueExpression != null)
			{
				String valueName = valueExpression.getName();
				if (valueExpression.getType() == Type.SDT)
					return String.format("%s.%s", valueName, field); // if SDT then we need the field path
				else
					return valueName;
			}
			else
			{
				expression = Cast.as(PropertyExpression.class, expression.getTarget());
			}
		}
		return null;
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) {
		mTarget.values(nameTypes);
	}

	@Override
	public boolean needsBackgroundThread() {
		return mTarget.needsBackgroundThread();
	}
}
