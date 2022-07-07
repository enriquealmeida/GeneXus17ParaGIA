package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.actions.ActionFactory;
import com.genexus.android.core.actions.ActionParameters;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.CallGxObjectAction;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.GxObjectTypes;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.loader.WorkWithMetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class GxObjectExpression implements Expression
{
	static final String TYPE = "gxobject";
	private final String mName;
	private final List<ActionParameter> mParameters;

	public GxObjectExpression(INodeObject node)
	{
		mName = node.getString("@exprValue");
		INodeObject jsonParameters = node.getNode("parameters");
		if (jsonParameters != null) {
			mParameters = new ArrayList<>();
			WorkWithMetadataLoader.readActionParameterList(null, mParameters, jsonParameters);
		} else {
			mParameters = null;
		}
	}

	public String getName()
	{
		return mName;
	}

	@Override
	public String toString()
	{
		return mName;
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		short type = GxObjectTypes.getGxObjectTypeFromName(mName);
		switch (type) {
			case GxObjectTypes.SDPANEL: {
				String name = MetadataLoader.getObjectName(mName);
				WorkWithDefinition workwith = (WorkWithDefinition) Services.Application.getDefinition().getPattern(name);
				if (workwith == null || workwith.getLevels().size() == 0)
					throw new IllegalArgumentException(String.format("Could not found panel '%s'.", name));
				return new Value(Type.PANEL, workwith);
			}
			case GxObjectTypes.API:
			case GxObjectTypes.PROCEDURE:
			case GxObjectTypes.DATAPROVIDER: {
				String name = MetadataLoader.getObjectName(mName);
				ActionDefinition definition = new ActionDefinition(null);
				definition.setGxObjectType(type);
				definition.setGxObject(name);
				if (mParameters != null)
					definition.setParameters(mParameters);
				UIContext uiContext = context.getExecutionContext().getUIContext();
				ActionParameters actionParameters = new ActionParameters(context.getExecutionContext().getData());
				boolean isComposite = true; // composite is not handled in expressions so use any value
				Action action = ActionFactory.getAction(uiContext, definition, actionParameters, isComposite).getComponents().iterator().next();
				if (type == GxObjectTypes.API) {
					ExternalApi api = Services.Application.getExternalApiFactory().getInstance(name, (ApiAction)action);
					if (api == null)
						throw new IllegalArgumentException(String.format("Could not found api '%s'.", name));
					return new Value(Type.API, api);
				} else {
					CallGxObjectAction callAction = (CallGxObjectAction)action;
					callAction.Do();
					if (callAction.getOutput().isOk() && callAction.getOutputValue() != null)
						return callAction.getOutputValue();
					else
						return Value.newFail(callAction.getOutput());
				}
			}
			default:
				return null;
		}
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) { }

	@Override
	public boolean needsBackgroundThread() {
		switch (GxObjectTypes.getGxObjectTypeFromName(mName)) {
			case GxObjectTypes.PROCEDURE:
			case GxObjectTypes.DATAPROVIDER:
				return true;
			default:
				return false;
		}
	}
}
