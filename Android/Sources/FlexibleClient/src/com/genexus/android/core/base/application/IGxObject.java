package com.genexus.android.core.base.application;

import com.genexus.android.core.base.model.PropertiesObject;

/**
 * Interface of callable GeneXus objects.
 */
public interface IGxObject
{
	OutputResult execute(PropertiesObject parameters);
}
