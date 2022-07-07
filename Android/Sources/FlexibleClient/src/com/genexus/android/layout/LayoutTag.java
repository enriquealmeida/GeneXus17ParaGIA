package com.genexus.android.layout;

import com.genexus.android.R;

public class LayoutTag
{
	/**
	 * Tag id for the LayoutItemDefinition of a view.
	 * Use with view.getTag(key) or ViewHierarchyVisitor.findViewByTag(view, key, value).
	 */
	public static final int CONTROL_DEFINITION = R.id.tag_control_definition;

	/**
	 * Tag id for the current ThemeClassDefinition of a view.
	 * Use with view.getTag(key) or ViewHierarchyVisitor.findViewByTag(view, key, value).
	 */
	public static final int CONTROL_THEME_CLASS = R.id.tag_control_theme_class;

	/**
	 * Tag id for the prompt information associated to a control with a prompt rule.
	 */
	public static final int CONTROL_PROMPT_INFO = R.id.tag_control_prompt_info;

	/**
	 * Tag that represents whether or not a view represents a Genexus control.
	 */
	public static final int CONTROL_GENEXUS = R.id.tag_control_genexus;

	/**
	 * Tag that represents the Multiple Selection Grid Checkbox.
	 */
	public static final int GRID_SELECTION_CHECKBOX = R.id.tag_grid_selection_checkbox;
}
