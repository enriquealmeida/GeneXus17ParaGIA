package com.genexus.android.core.base.metadata.layout;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.loader.WorkWithMetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class ComponentDefinition extends LayoutItemDefinition
{
	private String mDesignObject;
	private List<ActionParameter> mDesignParameters;

	private String mClientStartObject;
	private List<ActionParameter> mClientStartParameters;

	public ComponentDefinition(LayoutDefinition layout, LayoutItemDefinition itemParent)
	{
		super(layout, itemParent);
		mDesignParameters = new ArrayList<>();
	}

	@Override
	public void readData(INodeObject node)
	{
		super.readData(node);
		String designObject = MetadataLoader.getObjectName(node.optString("@object"));
		String designObjectSub = node.optString("@objectPanel");
		if (Strings.hasValue(designObjectSub))
			designObject += "." + designObjectSub;

		// This is the info for a "fixed" component.
		// It may not be present if the component is dynamically initialized.
		mDesignObject = designObject;
		WorkWithMetadataLoader.readActionParameterList(null, mDesignParameters, node.getNode("parameters"));
	}

	public IViewDefinition getObject()
	{
		if (Strings.hasValue(mDesignObject))
			return Services.Application.getDefinition().getView(mDesignObject);
		if (Strings.hasValue(mClientStartObject))
			return Services.Application.getDefinition().getView(mClientStartObject);
		else
			return null;
	}

	public List<ActionParameter> getParameters()
	{
		if (mDesignParameters==null && mClientStartParameters!=null)
			return mClientStartParameters;
		return mDesignParameters;
	}

	public void setClientStartObject(String clientStartObject)
	{
		mClientStartObject = clientStartObject;
	}

	public void setClientStartParameters(List<ActionParameter> clientStartParameters)
	{
		mClientStartParameters = clientStartParameters;
	}
}
