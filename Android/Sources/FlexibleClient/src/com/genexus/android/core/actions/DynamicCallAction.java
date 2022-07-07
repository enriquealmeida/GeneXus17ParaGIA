package com.genexus.android.core.actions;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.IPatternMetadata;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.GxObjectTypes;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.ui.navigation.CallOptions;
import com.genexus.android.core.ui.navigation.CallOptionsHelper;
import com.genexus.android.core.ui.navigation.CallType;
import com.genexus.android.core.utils.Cast;

import java.util.ArrayList;
import java.util.List;

public class DynamicCallAction extends Action {
	private StructureDefinition mBusinessComponent;
	private Action mComputedAction;
	private boolean mIsRedirect;
	private boolean mIsComposite;

	public static @NonNull DynamicCallAction redirect(UIContext context, Entity data, String dynamicCall, boolean isComposite) {
		return new DynamicCallAction(context, data, dynamicCall, false, isComposite);
	}

	public static @NonNull DynamicCallAction redirect(UIContext context, Entity data, String dynamicCall, boolean supportNominatedParameters, boolean isComposite) {
		return new DynamicCallAction(context, data, dynamicCall, supportNominatedParameters, isComposite);
	}

	public static ActionDefinition parse(UIContext context, String dynamicCall)
	{
		return new DynamicCallAction(context, null, null).getActionDefinitionFromString(dynamicCall, null);
	}

	private DynamicCallAction(UIContext context, Entity entity, String dynamicCall, boolean supportNominatedParameters, boolean isComposite) {
		super(context, null, new ActionParameters(entity));
		mIsComposite = isComposite;
		if (entity != null && Services.Strings.hasValue(dynamicCall)) {
			ActionDefinition staticActionDefinition = getActionDefinitionFromString(dynamicCall, null);
			mComputedAction = ActionFactory.getAction(context, staticActionDefinition, new ActionParameters(entity), isComposite);
			mIsRedirect = true; // Assuming that only calls to SD objects are made on start (otherwise it wouldn't have arrived from server).

			if (supportNominatedParameters && hasNominatedParameter(staticActionDefinition))
				processNominatedParameters(mComputedAction);
		}
	}

	public DynamicCallAction(UIContext context, ActionDefinition definition, ActionParameters parameters) {
		super(context, definition, parameters);
		// Do NOT compute action here, because object or parameters may be set by previous action in a composite.
	}

	private static boolean hasNominatedParameter(ActionDefinition actionDefinition) {
		for (ActionParameter parameter : actionDefinition.getParameters()) {
			if (parameter.getValue().contains("="))
				return true;
		}
		return false;
	}

	private static void processNominatedParameters(Action action) {
		WorkWithAction callAction = Cast.as(WorkWithAction.class, action);
		if (callAction == null && action instanceof CompositeAction)
			callAction = Cast.as(WorkWithAction.class, ((CompositeAction) action).getNextActionToExecute());

		if (callAction != null) {
			IViewDefinition calledObject = callAction.getObject();
			if (calledObject != null) {
				List<ActionParameter> parameters = new ArrayList<>(calledObject.getParameters().size());
				for (int n = calledObject.getParameters().size(); n > 0; n--)
					parameters.add(new ActionParameter(""));
				int parmPosition = 0;
				for (ActionParameter param : action.getDefinition().getParameters()) {
					String paramValue = param.getValue().substring(1, param.getValue().length() - 1);
					int index = paramValue.indexOf('=');
					if (index != -1) {
						String name = paramValue.substring(0, index);
						String value = "\"" + paramValue.substring(index + 1) + "\"";
						int objParamPosition = 0;
						for (ObjectParameterDefinition objParam : calledObject.getParameters()) {
							if (objParam.isInput() &&
									(objParam.getName().compareToIgnoreCase(name) == 0 ||
											(objParam.getName().startsWith("&") && objParam.getName().substring(1).compareToIgnoreCase(name) == 0))) {
								parameters.set(objParamPosition, new ActionParameter(objParam.getName(), value, null));
								break;
							}
							objParamPosition++;
						}
					}
					else {
						parameters.set(parmPosition, new ActionParameter(param.getValue()));
					}
					parmPosition++;
				}
				action.getDefinition().setParameters(parameters);
			}
		}
	}

	private void calculateComputedActionFromEntity()
	{
		List<Expression.Value> parameters = getParameterValues();
		if (parameters.size() >= 1) {
			String dynamicCallString = parameters.get(0).coerceToString();
			if (Services.Strings.hasValue(dynamicCallString)) {
				ActionDefinition staticActionDefinition = getActionDefinitionFromString(dynamicCallString, getDefinition());
				mComputedAction = ActionFactory.getAction(getContext(), staticActionDefinition, getParameters(), mIsComposite);
			}
		}
	}

	private ActionDefinition getActionDefinitionFromString(String dynamicCallString, ActionDefinition actionDef)
	{
		ActionDefinition def;
		if (actionDef != null)
			def = new ActionDefinition(actionDef.getViewDefinition());
		else
			def = new ActionDefinition(null);

		try {
			// Now check first if it is a call to a Smart Device object
			populateAction(dynamicCallString, def);

			// The first parameter of the dynamic call is the variable, but after the call can contain additionally parameters
			if (actionDef != null) {
				for (int i = 1; i < actionDef.getParameters().size(); i++) {
					def.getParameters().add(actionDef.getParameter(i));
				}
				// If Insert , Update, Delete parameters in Dynamic call Haven't the name so we take them from the keys names
				if (mBusinessComponent != null) {
					for (int i = 0; i < mBusinessComponent.Root.getKeys().size(); i++) {
						DataItem di = mBusinessComponent.Root.getKeys().get(i);
						if (i < def.getParameters().size()) {
							ActionParameter parm = def.getParameter(i);
							parm.setName(di.getName());
						} else {
							// Something was wrong, parameters should be the same that the keys.
							break;
						}
					}
				}
			}
		} catch (@SuppressWarnings("checkstyle:IllegalCatch") Exception e) {
			// TODO: We should investigate why and which exception we're catching here
			Services.Log.error("Invalid Parsing for DynamicCall: " + dynamicCallString, e);
		}

		return def;
	}

	private void populateAction(String callString, ActionDefinition def)
	{
		String dynamicCallString = callString.trim();

		String objType = "sd";
		if (Strings.starsWithIgnoreCase(callString, "sd:"))  {
			dynamicCallString = callString.substring(3);
		}
		else if (callString.startsWith("eo:"))
		{
			objType = "eo";
			dynamicCallString = callString.substring(3);
		}
		else { // wbp: | prc:
			if (callString.startsWith("prc:") || callString.startsWith("wbp:")) {
				dynamicCallString = callString.substring(4);
				objType = callString.substring(0, 3);
			}
		}

		// Parse call
		String[] callParts = Services.Strings.split(dynamicCallString, Strings.QUESTION);

		// Object Name
		String objName = Strings.EMPTY;
		if (callParts.length > 0)
			objName = callParts[0];
		def.setGxObject(objName);

		// Object Parameters
		String [] objParameters = null;
		String objComponent = Strings.EMPTY;

		if (callParts.length > 1)
			objParameters = Services.Strings.split(callParts[1].trim(), ',');

		if (objParameters != null)
		{
			for (String objParameter : objParameters)
			{
				// The parameters are not *exactly* URI-encoded, GX uses '+' for spaces instead of '%20'.
				String parameterValue = objParameter.replace("+", Strings.SPACE);
				parameterValue = Services.HttpService.uriDecode(parameterValue);

				// These parameters are always constants.
				ActionParameter parm = new ActionParameter("\"" + parameterValue + "\"");
				def.getParameters().add(parm);
			}
		}

		// Object Type
		if (objType.equalsIgnoreCase("prc"))
			def.setGxObjectType(GxObjectTypes.PROCEDURE);
		else if (objType.equalsIgnoreCase("wbp"))
			def.setGxObjectType(GxObjectTypes.WEBPANEL);
		else if (objType.equalsIgnoreCase("eo"))
		{
			def.setGxObjectType( GxObjectTypes.API);
			int lastIndex = objName.lastIndexOf(Strings.DOT);
			if (lastIndex>1)
			{
				String 	gxObject = objName.substring(0, lastIndex);
				def.setGxObject(gxObject);
				String 	method = objName.substring(lastIndex+1);
				def.setProperty("@exoMethod", method);
			}
		}
		else if (objType.equalsIgnoreCase("sd")) {
			String[] objNameParts = Services.Strings.split(objName, Strings.DOT);
			if (objNameParts.length > 1) {
				String objNameAux = objNameParts[0];
				int objSeparIndex = 0;
				while (objSeparIndex < objNameParts.length)
				{
					if (Services.Application.getDefinition().getPattern(objNameAux) != null) {
						objName = objNameAux;
						break;
					}
					objSeparIndex++;
					if (objSeparIndex < objNameParts.length)
						objNameAux += Strings.DOT + objNameParts[objSeparIndex];
				}
				// objSeparIndex = last index of a objName part
				
				def.setGxObject(objName);

				ArrayList<String> values = new ArrayList<>();
				for (int i = objSeparIndex + 1; i < objNameParts.length && i <= objSeparIndex + 2; i++) // Only 2 parts, level = objSeparIndex + 1 and list|detail = objSeparIndex + 2
					values.add(objNameParts[i]);
				objComponent = Services.Strings.join(values, Strings.DOT);

				if (objNameParts.length > objSeparIndex + 3) { // mode is on the 3rd part after objName
					// Remove ( from mode, could be wrong in the metadata
					String bcMode = objNameParts[objSeparIndex + 3].replace("(", "");
					def.setProperty("@bcMode", bcMode);
				}
			}
			IPatternMetadata metadata = Services.Application.getDefinition().getPattern(objName);
			WorkWithDefinition workWith = Cast.as(WorkWithDefinition.class, metadata);
			if (workWith != null) {
				def.setGxObjectType(GxObjectTypes.SDPANEL);
				def.setProperty("@instanceComponent", objComponent);
				if (Services.Strings.hasValue(def.optStringProperty("@bcMode"))) // Ask for the bc only when Update, Insert, Delete mode.
					mBusinessComponent = workWith.getBusinessComponent();
			}
			else if (metadata!=null)
			{
				def.setGxObjectType(GxObjectTypes.DASHBOARD);
			}
			else
			{
				Services.Log.error("Could not execute dyncall to " + callString + " .The referenced object does not exist or is not included in the application call tree; check documentation for more information");
			}
		}
	}

	@Override
	public boolean catchOnActivityResult()
	{
		if (mComputedAction == null)
			calculateComputedActionFromEntity(); // Should not execute before Do() but, defensively, calculate anyway.

		if (mComputedAction == null)
			return false;

		return mComputedAction.catchOnActivityResult();
	}

	@Override
	public boolean Do()
	{
		if (mComputedAction == null)
			calculateComputedActionFromEntity();

		if (mComputedAction == null)
		{
			Services.Log.debug("Could not execute DynamicCallAction ");
			// Allow continue
			return true;
		}

		if (mIsRedirect)
			setupForRedirect(mComputedAction);

		return mComputedAction.Do();
	}

	@Override
	public String getErrorMessage() {
		if (mComputedAction != null)
			return mComputedAction.getErrorMessage();
		else
			return "";
	}

	private static void setupForRedirect(Action action)
	{
		WorkWithAction callAction = Cast.as(WorkWithAction.class, action);
		if (callAction == null && action instanceof CompositeAction)
			callAction = Cast.as(WorkWithAction.class, ((CompositeAction)action).getNextActionToExecute());

		if (callAction != null)
		{
			IViewDefinition calledObject = callAction.getObject();
			if (calledObject != null)
				CallOptionsHelper.setCallOption(calledObject.getObjectName(), CallOptions.OPTION_TYPE, CallType.REPLACE.name());
		}
	}

	@Override
	public boolean isActivityEnding()
	{
		if (mComputedAction != null)
			return mComputedAction.isActivityEnding();
		else
			return super.isActivityEnding();
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result) 
	{
		return super.afterActivityResult(requestCode, resultCode, result);
	}
	
	
}
