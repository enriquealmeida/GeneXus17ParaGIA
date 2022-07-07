package com.genexus.android.core.base.metadata;

import java.io.Serializable;

public class WWLevelDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String mId;
	private String mName;
	private String mDescription;
	private final WorkWithDefinition mParent;

	private WWListDefinition mList;
	private DetailDefinition mDetail;

	public WWLevelDefinition(WorkWithDefinition parent, String id)
	{
		mParent = parent;
		mId = id;
	}

	public WorkWithDefinition getParent() { return mParent; }
	public String getId() { return mId; }

	public String getName() { return mName; }
	public void setName(String name) { mName = name; }

	public String getDescription() { return mDescription; }
	public void setDescription(String description) { mDescription = description; }

	public WWListDefinition getList() { return mList; }
	public void setList(WWListDefinition list) { mList = list; }

	public DetailDefinition getDetail() { return mDetail; }
	public void setDetail(DetailDefinition detail) { mDetail = detail; }
}
