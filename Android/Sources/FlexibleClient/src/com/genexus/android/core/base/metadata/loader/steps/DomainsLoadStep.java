package com.genexus.android.core.base.metadata.loader.steps;

import android.content.Context;

import com.genexus.android.core.base.metadata.DomainDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoadStep;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class DomainsLoadStep implements MetadataLoadStep {
	private final Context mContext;

	public DomainsLoadStep(Context context) {
		mContext = context;
	}

	@Override
	public void load() {
		INodeObject domains = MetadataLoader.getDefinition(mContext, "domains");
		if (domains != null) {
			INodeCollection arrEntities = domains.getCollection("Domains");
			for (int i = 0; i < arrEntities.length(); i++) {
				INodeObject obj = arrEntities.getNode(i);
				DomainDefinition def = new DomainDefinition(obj);
				Services.Application.getDefinition().putDomain(def);
			}
		}
	}
}
