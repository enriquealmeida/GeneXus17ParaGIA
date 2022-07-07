package com.genexus.android.core.base.metadata.loader.steps;

import android.util.Pair;

import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.metadata.loader.MetadataFile;
import com.genexus.android.core.base.metadata.loader.MetadataLoadStep;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

import java.util.ArrayList;
import java.util.List;

public class SDTsLoadStep implements MetadataLoadStep {
	private final MetadataFile mMetadataFile;

	public SDTsLoadStep(MetadataFile metadataFile) {
		mMetadataFile = metadataFile;
	}

	@Override
	public void load() {
		// We do two passes: first one creates all SDTs (with empty definition), second one deserializes them.
		// This is because SDT_A may have a member of type SDT_B, which is after it in the file.
		// If we did just one pass, the deserialized definition of SDT_A will be incomplete.
		INodeCollection arrSDTs = mMetadataFile.getSDTs();
		List<Pair<StructureDataType, INodeObject>> sdtDefinitions = new ArrayList<>();

		for (int i = 0; i < arrSDTs.length(); i++) {
			INodeObject jsonSdt = arrSDTs.getNode(i);
			StructureDataType sdt = new StructureDataType(jsonSdt);

			Services.Application.getDefinition().putSDT(sdt);
			sdtDefinitions.add(new Pair<>(sdt, jsonSdt));
		}

		for (Pair<StructureDataType, INodeObject> sdt : sdtDefinitions)
			sdt.first.deserialize(sdt.second);
	}
}
