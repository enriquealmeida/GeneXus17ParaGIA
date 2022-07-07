package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class ActionGroupItem
{
	public static final int TYPE_ACTION = 1;
	public static final int TYPE_DATA = 2;
	public static final int TYPE_GROUP = 3;
	public static final int TYPE_SEARCH = 4;

	static ActionGroupItemDefinition create(ActionGroupDefinition parent, INodeObject json)
	{
		if (json == null)
			return null;

		if (Strings.hasValue(json.optString("@actionElement")))
			return new ActionGroupActionDefinition(parent, json);

		INodeObject jsonSearch = json.getNode("search");
		if (jsonSearch != null)
			return new ActionGroupSearchDefinition(parent, jsonSearch);

		INodeObject jsonData = json.getNode("data");
		if (jsonData != null)
			return new ActionGroupDataItemDefinition(parent, jsonData);

		INodeObject jsonGroup = json.getNode("actionGroup");
		if (jsonGroup != null)
			return new ActionGroupSubgroupDefinition(parent, jsonGroup);

		Services.Log.warning("Unknown member in action group: " + json.toString());
		return null;
	}
}
