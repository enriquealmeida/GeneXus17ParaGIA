package com.genexus.android.core.base.metadata.loader.steps;

import com.genexus.android.core.base.metadata.AttributeDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataFile;
import com.genexus.android.core.base.metadata.loader.MetadataLoadStep;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class AttributesLoadStep implements MetadataLoadStep {
	private final MetadataFile mMetadataFile;

	public AttributesLoadStep(MetadataFile metadataFile) {
		mMetadataFile = metadataFile;
	}

	@Override
	public void load() {
		for (INodeObject obj : mMetadataFile.getAttributes()) {
			AttributeDefinition def = new AttributeDefinition(obj);
			Services.Application.getDefinition().putAttribute(def);
		}
	}
}
