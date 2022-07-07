package com.genexus.android.core.actions;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.GxObjectTypes;
import com.genexus.android.core.utils.Cast;

public class ActionFactory
{
	public static CompositeAction getAction(UIContext context, ActionDefinition definition, ActionParameters parameters, boolean isComposite)
	{
		if (parameters == null)
			parameters = ActionParameters.EMPTY;

		CompositeAction composite = new CompositeAction(context, definition, parameters, isComposite);
		putActionAndChildren(composite, context, definition, parameters, true, isComposite);
		return composite;
	}

	private static void putActionAndChildren(CompositeAction composite, UIContext context, ActionDefinition definition, ActionParameters parameters, boolean readChildren, boolean isComposite)
	{
		// NOTE: Handlers are added here and not in getActionHandlers() because they must surround
		// the whole composite (unlike getPre/Post actions) which are for individual steps.
		ActionDefinitionWithHandlers actionWithHandlers = Cast.as(ActionDefinitionWithHandlers.class, definition);
		if (actionWithHandlers != null)
			definition = actionWithHandlers.getDefinition();

		// Add pre handler...
		if (actionWithHandlers != null && actionWithHandlers.getPreHandler() != null)
			composite.addAction(new RunnableAction(context, actionWithHandlers.getPreHandler(), definition, parameters));

		// ... then "root" action...
		composite.addAction(getSingleAction(context, definition, parameters, isComposite));

		// ... then any subordinated actions, if present...
		if (readChildren) {
			for (ActionDefinition subAction : definition.getNextActions())
				putActionAndChildren(composite, context, subAction, parameters, false, isComposite);
		}

		// ... then post handler.
		if (actionWithHandlers != null && actionWithHandlers.getPostHandler() != null)
			composite.addAction(new RunnableAction(context, actionWithHandlers.getPostHandler(), definition, parameters));
	}

	public static @NonNull CompositeAction getInnerActionChildren(UIContext context, ActionDefinition definition, ActionParameters parameters, boolean isComposite)
	{
		CompositeAction composite = new CompositeAction(context, definition, parameters, isComposite);
		for (ActionDefinition subAction : definition.getInnerActions())
			composite.addAction(getSingleAction(context, subAction, parameters, isComposite));
		return composite;
	}

	private static @NonNull Action getSingleAction(UIContext context, ActionDefinition definition, ActionParameters parameters, boolean isComposite)
	{
		if (SetControlPropertyAction.isAction(definition))
			return new SetControlPropertyAction(context, definition, parameters);

		if (GetControlPropertyAction.isAction(definition))
			return new GetControlPropertyAction(context, definition, parameters);

		if (ControlMethodAction.isAction(definition))
			return new ControlMethodAction(context, definition, parameters);

		if (AssignmentAction.isAction(definition))
			return new AssignmentAction(context, definition, parameters);

		if (CommandAction.isAction(definition))
			return new CommandAction(context, definition, parameters);

		if (ForEachLineAction.isAction(definition))
			return new ForEachLineAction(context, definition, parameters, isComposite);

		if (MultipleSelectionAction.isAction(definition))
			return new MultipleSelectionAction(context, definition, parameters);

		if (JumpAction.isAction(definition))
			return new JumpAction(context, definition, parameters);

		if (DependentValuesAction.isAction(definition))
			return new DependentValuesAction(context, definition, parameters);

		if (SetCallOptionsAction.isAction(definition))
			return new SetCallOptionsAction(context, definition, parameters);

		if (SynchronizationAction.isAction(definition))
			return new SynchronizationAction(context, definition, parameters);

		if (MethodCallAction.isAction(definition))
			return new MethodCallAction(context, definition, parameters);

		if (ForInAction.isAction(definition))
			return new ForInAction(context, definition, parameters, isComposite);

		if (CompositeBlockAction.isAction(definition))
			return new CompositeBlockAction(context, definition, parameters);

		if (EmptyAction.isAction(definition))
			return new EmptyAction(context, definition, parameters);

		if (definition.getGxObjectType() == GxObjectTypes.VARIABLE_OBJECT)
			return new DynamicCallAction(context, definition, parameters);

		if (definition.getGxObjectType() == GxObjectTypes.TRANSACTION)
			return new CallBCAction(context, definition, parameters);

		if (definition.getGxObjectType() == GxObjectTypes.PROCEDURE || definition.getGxObjectType() == GxObjectTypes.DATAPROVIDER)
			return new CallGxObjectAction(context, definition, parameters);

		if (definition.getGxObjectType() == GxObjectTypes.WEBPANEL)
			return new CallWebPanelAction(context, definition, parameters);

		if (definition.getGxObjectType() == GxObjectTypes.DASHBOARD)
			return new CallDashboardAction(context, definition, parameters);

		if (definition.getGxObjectType() == GxObjectTypes.SDPANEL)
			return new WorkWithAction(context, definition, parameters);

		if (definition.getGxObjectType() == GxObjectTypes.API)
		{
			ApiAction apiAction = new ApiAction(context, definition, parameters, isComposite);

			// "Special" API actions.
			if (apiAction.isLoginAction())
				return new CallLoginAction(context, definition, parameters);
			else if (apiAction.isLoginExternalAction())
				return new CallLoginExternalAction(context, definition, parameters);

			return apiAction;
		}

		throw new IllegalArgumentException(String.format("Unknown action %s", definition.getInternalProperties()));
	}
}
