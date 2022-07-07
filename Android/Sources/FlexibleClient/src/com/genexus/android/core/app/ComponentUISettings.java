package com.genexus.android.core.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.fragments.BaseFragment;

public class ComponentUISettings
{
	public boolean isMain;
	public BaseFragment parent;
	public Size size;
	public boolean isContent;

	public ComponentUISettings(boolean isMain, BaseFragment parent, Size size)
	{
		this.isMain = isMain;
		this.parent = parent;
		this.size = size;
	}

	public static @NonNull ComponentUISettings main()
	{
		return new ComponentUISettings(true, null, null);
	}

	public static @NonNull ComponentUISettings childOf(@NonNull BaseFragment parent, @Nullable Size size)
	{
		return new ComponentUISettings(false, parent, size);
	}
}
