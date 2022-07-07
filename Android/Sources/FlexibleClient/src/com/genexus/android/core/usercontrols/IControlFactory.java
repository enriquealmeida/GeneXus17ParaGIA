package com.genexus.android.core.usercontrols;

import android.content.Context;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.ui.Coordinator;

/**
 * Interface for UC factory.
 */
public interface IControlFactory
{
	IGxUserControl create(Context context, Coordinator coordinator, LayoutItemDefinition definition);
}
