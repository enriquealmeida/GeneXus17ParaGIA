package com.genexus.android.core.base.metadata.loader.steps;

import com.genexus.android.core.base.metadata.GxObjectDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataFile;
import com.genexus.android.core.base.metadata.loader.MetadataLoadStep;
import com.genexus.android.core.base.metadata.loader.MetadataParser;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class ProceduresLoadStep implements MetadataLoadStep {
	private final MetadataFile mMetadata;

	public ProceduresLoadStep(MetadataFile metadata) {
		mMetadata = metadata;
	}

	@Override
	public void load() {
		INodeCollection arrProcs = mMetadata.getProcedures();
		for (int i = 0; i < arrProcs.length(); i++) {
			INodeObject obj = arrProcs.getNode(i);
			GxObjectDefinition def = MetadataParser.readOneGxObject(obj);
			Services.Application.getDefinition().putGxObject(def);
		}
	}
}
