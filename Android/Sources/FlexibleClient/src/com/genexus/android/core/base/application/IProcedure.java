package com.genexus.android.core.base.application;

import java.util.List;

import com.genexus.android.core.base.model.PropertiesObject;

public interface IProcedure extends IGxObject
{
	OutputResult executeMultiple(List<PropertiesObject> parameters);

	// apply replicator timeout if correspond
	OutputResult executeReplicator(PropertiesObject parameters);
}
