package com.genexus.android.core.base.metadata;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Base interface for ALL objects that are shown as forms.
 * Includes IDataViews (Panels, WWSD List/Details/Sections) and Dashboards.
 */
public interface IViewDefinition extends IGxObjectDefinition
{
	/**
	 * Object name (may be different from view name if object contains multiple views, e.g. WWSD with many Lists and Details).
	 * @return
	 */
	String getObjectName();

	/**
	 * View is secure (i.e. requires being logged in).
	 */
	boolean isSecure();

	/**
	 * View caption (shown in application bar).
	 */
	String getCaption();

	/**
	 * View parameters.
	 */
	List<ObjectParameterDefinition> getParameters();

	/**
	 * Variables of DV events
	 */
	@NonNull List<VariableDefinition> getVariables();

	/**
	 * Return the required variable, <name> can be with or without '&'
	 */
	VariableDefinition getVariable(String name);

	/**
	 * Returns the definition of the ClientStart event (or null if it doesn't exist).
	 */
	ActionDefinition getClientStart();

	/**
	 * Returns the definition of an event, given its name (or null if it doesn't exist).
	 */
	ActionDefinition getEvent(String name);

	/**
	 * Returns the properties of the associated instance.
	 */
	InstanceProperties getInstanceProperties();
}
