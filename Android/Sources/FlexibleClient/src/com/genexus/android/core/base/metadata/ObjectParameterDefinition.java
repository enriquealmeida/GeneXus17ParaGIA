package com.genexus.android.core.base.metadata;

import androidx.annotation.NonNull;

import java.io.Serializable;

import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.utils.Strings;

public class ObjectParameterDefinition extends DataItem implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String mName;
	private final String mMode;

	public ObjectParameterDefinition(@NonNull String name, String mode)
	{
		super(null);

		if (!Strings.hasValue(mode))
			mode = MODE_IN;

		mName = name;
		mMode = mode;
	}

	public void readDataType(INodeObject json)
	{
		DataItemTypeReader.readDataType(this, json);
	}

	@Override
	public @NonNull String getName() { return mName; }
	public String getMode() { return mMode; }

	@Override
	public String toString()
	{
		return String.format("%s:%s (%s)", mMode, mName, getType());
	}

	private static final String MODE_IN = "in";
	private static final String MODE_INOUT = "inout";
	private static final String MODE_OUT = "out";

	public boolean isInput()
	{
		return (MODE_IN.equalsIgnoreCase(mMode) || MODE_INOUT.equalsIgnoreCase(mMode));
	}

	public boolean isOutput()
	{
		return (MODE_OUT.equalsIgnoreCase(mMode) || MODE_INOUT.equalsIgnoreCase(mMode));
	}
}
