package com.genexus.android.core.base.services;

public interface IValuesFormatter
{
	boolean needsAsync();
	CharSequence format(String value);
}
