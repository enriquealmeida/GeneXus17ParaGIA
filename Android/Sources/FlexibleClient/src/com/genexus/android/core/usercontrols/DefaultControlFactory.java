package com.genexus.android.core.usercontrols;

import android.content.Context;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;

/**
 * Default User Control Factory. Uses reflection to instantiate views.
 */
class DefaultControlFactory implements IControlFactory
{
	private final UserControlDefinition mDefinition;
	private Class<?> mClass;
	private boolean mClassLoaded;

	public DefaultControlFactory(UserControlDefinition definition)
	{
		mDefinition = definition;
	}

	@Override
	public IGxUserControl create(Context context, Coordinator coordinator, LayoutItemDefinition definition)
	{
		if (!mClassLoaded)
		{
			tryLoadClass();
			mClassLoaded = true; // Even if failed, to avoid reflection check again.
		}

		if (mClass != null)
			return (IGxUserControl)UcFactory.createUserControl(mClass, context, coordinator, definition);
		else
			return null;
	}

	private void tryLoadClass()
	{
		try
		{
			mClass = Class.forName(mDefinition.ClassName);
		}
		catch (ClassNotFoundException e)
		{
			Services.Log.error(String.format("Java class '%s' (for user control '%s') could not be loaded via reflection.", mDefinition.ClassName, mDefinition.Name), e);
		}
	}
}
