package com.genexus.android.core.actions;

import com.genexus.android.core.base.metadata.ActionDefinition;

class EmptyAction extends Action {
    protected EmptyAction(UIContext context, ActionDefinition definition, ActionParameters parameters) {
        super(context, definition, parameters);
    }

    public static boolean isAction(ActionDefinition definition) {
        return "empty".equals(definition.optStringProperty(ActionHelper.STATEMENT_NAME));
    }

    @Override
    public boolean Do() {
        return true;
    }

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}
}
