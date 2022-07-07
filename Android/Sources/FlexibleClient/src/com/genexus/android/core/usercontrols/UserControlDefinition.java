package com.genexus.android.core.usercontrols;

import androidx.annotation.NonNull;
import android.view.View;

@SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
public class UserControlDefinition
{
	private IControlFactory mFactory;

	private UserControlDefinition(String name, String cls, boolean isScrollable)
	{
		Name = name;
		ClassName = cls;
		IsScrollable = isScrollable;
	}

	public UserControlDefinition(String name, Class<? extends View> clazz, boolean isScrollable)
	{
		this(name, clazz.getName(), isScrollable);
	}

	public UserControlDefinition(String name, Class<? extends View> clazz)
	{
		this(name, clazz.getName(), false);
	}

	public UserControlDefinition(String name, IControlFactory factory)
	{
		Name = name;
		mFactory = factory;
	}

	public UserControlDefinition()
	{
	}

	public String Name;
	public String ClassName;
	public boolean IsScrollable;
	public boolean SupportAutoGrow;

	public @NonNull IControlFactory getFactory()
	{
		if (mFactory == null)
			mFactory = new DefaultControlFactory(this);

		return mFactory;
	}
}
