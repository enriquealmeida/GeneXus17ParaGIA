package com.genexus.android.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.view.View;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ActionDefinitionWithHandlers;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.layout.ControlViewHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.PromptHelper;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.GxDragListener;
import com.genexus.android.core.controls.GxGestureListener;
import com.genexus.android.core.controls.GxTouchEvents;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.IGxEditWithDependencies;
import com.genexus.android.core.utils.Cast;

public abstract class CoordinatorBase implements CoordinatorAdvanced
{
	private static final String EVENT_CONTROL_VALUE_CHANGED = "ControlValueChanged";
	private static final String EVENT_CONTROL_VALUE_CHANGING = "ControlValueChanging";

	private UIContext mContext;
	private final ArrayList<View> mControls;
	private final NameMap<View> mControlsByName = new NameMap<>();
	private final TreeMap<String, List<IGxEditWithDependencies>> mCachedDependencies;

	private Entity mData;
	private final Entity.OnPropertyValueChangeListener mPropertyValueChangeListener;
	private GxDragListener mDragDropListener = null;
	private final GxGestureListener mGestureListener;

	protected CoordinatorBase(UIContext context)
	{
		mContext = context;
		mControls = new ArrayList<>();

		mCachedDependencies = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		mPropertyValueChangeListener = new EntityPropertyValueChangeListener();
		mDragDropListener = new GxDragListener(this);

		mGestureListener = new GxGestureListener(mContext.getBaseContext(), this);
 	}

	public static boolean isInternalEvent(String actionName) {
		return actionName.endsWith("." + EVENT_CONTROL_VALUE_CHANGING);
	}

	protected abstract IViewDefinition getContainerDefinition();

	public Entity getData() { return mData; }

	public void setData(Entity data)
	{
		if (mData != data)
		{
			if (mData != null)
				mData.setOnPropertyValueChangeListener(null);

			mData = data;

			if (mData != null)
				mData.setOnPropertyValueChangeListener(mPropertyValueChangeListener);
		}
	}


	@Override
	public UIContext getUIContext()
	{
		return mContext;
	}

	protected void setUIContext(UIContext context)
	{
		mContext = context;
	}

	@Override
	public void addControl(View control, LayoutItemDefinition definition)
	{
		mControls.add(control);
		mControlsByName.put(definition.getName(), control);
		mGestureListener.addView(control);

		View focusView = control;
		if (control instanceof DataBoundControl)
		{
			View editView = ((DataBoundControl)control).getAttribute();
			if (editView != null)
				focusView = editView;
		}

		if (hasAnyEventHandler(control, new String[] { GxTouchEvents.DROP }))
			control.setOnDragListener(mDragDropListener);

		// Clear calculated dependencies if new controls are registered.
		mCachedDependencies.clear();
	}

	@Override
	public final Object getValue(String name)
	{
		if (mData == null)
		{
			Services.Log.warning(String.format("Asking for '%s' before Coordinator's data is set.", name));
			return null;
		}

		return mData.getProperty(name);
	}

	@Override
	public void setValue(String name, Object value)
	{
		if (mData == null)
		{
			Services.Log.warning(String.format("Trying to set '%s' and no Coordinator's data is set.", name));
			return;
		}

		mData.setProperty(name, value);
	}

	@Override
	public View getControl(String name)
	{
		return mControlsByName.get(name);
	}

	@Override
	public final String getStringValue(String name)
	{
		Object value = getValue(name);
		return (value != null ? value.toString() : Strings.EMPTY);
	}

	@Override
	public final void onValueChanged(IGxEdit edit, boolean fireControlValueChanged) {
		onValueChanged(edit, fireControlValueChanged, null, null);
	}

	@Override
	public void onValueChanged(IGxEdit edit, boolean fireControlValueChanged, String newValue) {
		onValueChanged(edit, fireControlValueChanged, newValue, null, null);
	}

	@Override
	public void onValueChanged(IGxEdit edit, boolean fireControlValueChanged, Runnable preAction, Runnable postAction) {
		onValueChanged(edit, fireControlValueChanged, null, preAction, postAction);
	}

	private void onValueChanged(IGxEdit edit, boolean fireControlValueChanged, String newValue, Runnable preAction, Runnable postAction) {
		if (newValue != null)
			edit.setGxValue(newValue);

		// Post the supplied value.
		// This will probably fire onDataValueChanged() below.
		if (mData != null)
			AdaptersHelper.saveEditValue(edit, mData);

		if (fireControlValueChanged)
			runControlEvent((View) edit, EVENT_CONTROL_VALUE_CHANGED, preAction, postAction);
	}

	@Override
	public void onValueChanging(IGxEdit edit, String newValue)
	{
		View control = (View) edit;

		ActionDefinition actionDef = getControlEventHandler(control, EVENT_CONTROL_VALUE_CHANGING);

		// Check if the control implements this event.
		if (actionDef != null) {
			List<ActionParameter> params = actionDef.getEventParameters();
			if (params.size() != 1) {
				throw new IllegalArgumentException(EVENT_CONTROL_VALUE_CHANGING + " requires a parameter.");
			}
			String paramName = params.get(0).getValue();
			setValue(paramName, newValue);
			runControlEvent(control, EVENT_CONTROL_VALUE_CHANGING);
		}
	}

	private class EntityPropertyValueChangeListener implements Entity.OnPropertyValueChangeListener
	{
		@Override
		public void onPropertyValueChange(@NonNull String propertyName, Object oldValue, Object newValue)
		{
			// A data value has changed.
			// This may have been fired by exiting a control, by the control itself, or by modifying a value via code.
			// If this property has dependant controls, update the value in the Entity and notify them.
			List<IGxEditWithDependencies> dependantControls = getDependantControls(propertyName);
			for (IGxEditWithDependencies dependant : dependantControls)
				dependant.onDependencyValueChanged(propertyName, newValue);
		}
	}

	private List<IGxEditWithDependencies> getDependantControls(String propertyName)
	{
		// Ignore attribute/variable names mismatch.
		propertyName = DataItemHelper.getNormalizedName(propertyName);

		List<IGxEditWithDependencies> dependants = mCachedDependencies.get(propertyName);
		if (dependants == null)
		{
			// Search for edits which can have dependencies and match them against the supplied control.
			dependants = new ArrayList<>();
			for (IGxEditWithDependencies otherEdit : Cast.iterateAs(IGxEditWithDependencies.class, mControls))
			{
				if (isDependant(otherEdit, propertyName))
					dependants.add(otherEdit);
			}

			// Store calculated dependencies in cache for later reuse.
			mCachedDependencies.put(propertyName, dependants);
		}

		return dependants;
	}

	private static boolean isDependant(IGxEditWithDependencies edit, String onProperty)
	{
		if (edit.getDependencies() != null)
		{
			for (String dependency : edit.getDependencies())
			{
				// Ignore attribute/variable names mismatch.
				dependency = DataItemHelper.getNormalizedName(dependency);

				if (dependency.equalsIgnoreCase(onProperty))
					return true;
			}
		}

		return false;
	}

	@Override
	public final boolean hasAnyEventHandler(View control, String[] eventNames)
	{
		for (String eventName : eventNames)
		{
			if (getControlEventHandler(control, eventName) != null)
				return true;
		}

		// Some controls have implicit Tap events (such as those that act as prompt controls).
		//noinspection RedundantIfStatement
		if (Strings.arrayContains(eventNames, GxTouchEvents.TAP, true) && hasImplicitTapHandler(control))
			return true;

		return false;
	}

	@Override
	public ActionDefinition getControlEventHandler(View control, String eventName)
	{
		LayoutItemDefinition controlDefinition = ControlViewHelper.getDefinition(control);
		if (controlDefinition != null)
			return controlDefinition.getEventHandler(eventName);
		else
			return null;
	}

	@Override
	public ActionDefinition getFormEventHandler(String eventName)
	{
		IDataViewDefinition containerDefinition = Cast.as(IDataViewDefinition.class, getContainerDefinition());
		if (containerDefinition != null)
			return containerDefinition.getEvent(eventName);

		return null;
	}

	@Override
	public IDataViewDefinition getFormDataViewDefinition()
	{
		IDataViewDefinition containerDefinition = Cast.as(IDataViewDefinition.class, getContainerDefinition());
		if (containerDefinition != null)
		{
			return containerDefinition;
		}
		return null;
	}


	@Override
	public final boolean runAction(String action, Anchor anchor)
	{
		IDataViewDefinition containerDefinition = Cast.as(IDataViewDefinition.class, getContainerDefinition());
		if (containerDefinition != null)
		{
			ActionDefinition actionDefinition = containerDefinition.getEvent(action);
			if (actionDefinition != null)
				return runAction(actionDefinition, anchor);
		}

		return false;
	}

	@Override
	public final boolean runControlEvent(View control, String eventName)
	{
		return runControlEvent(control, eventName, null, null);
	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	public final boolean runControlEvent(View control, String eventName, Runnable preAction, Runnable postAction)
	{
		if (GxTouchEvents.TAP.equals(eventName) && runImplicitTapHandler(control))
			return true;

		ActionDefinition action = getControlEventHandler(control, eventName);
		if (action != null)
		{
			if (preAction != null || postAction != null)
				action = new ActionDefinitionWithHandlers(action, preAction, postAction);

			return runAction(action, new Anchor(control));
		}

		return false;
	}

	private boolean hasImplicitTapHandler(View control)
	{
		// Only prompts, for now.
		return PromptHelper.hasPrompt(control);
	}

	private boolean runImplicitTapHandler(View control)
	{
		// Only prompts, for now.
		return PromptHelper.callPrompt(this, control);
	}
}
