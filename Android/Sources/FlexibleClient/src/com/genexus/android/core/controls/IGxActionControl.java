package com.genexus.android.core.controls;

import android.view.View;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.model.Entity;

public interface IGxActionControl
{
	ActionDefinition getAction();
	void setOnClickListener(View.OnClickListener listener);
	Entity getEntity();
	void setEntity(Entity entity);
}
