package com.genexus.android.core.controls.common;

/**
 * Value item for a control that provides options, either static or dynamic
 * (such as combo box, radio group, dynamic combo).
 */
@SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
public class ValueItem
{
	public final String Value;
	public final String Description;
	// TODO: Add support for public final String Image;

	public ValueItem(String value, String description)
	{
		Value = value;
		Description = description;
	}

	@Override
	public String toString() { return Description; }
}
