package com.genexus.android.core.base.metadata;

import java.util.ArrayList;
import java.util.List;

public class DetailDefinition extends DataViewDefinition
{
	private static final long serialVersionUID = 1L;

	private final WWLevelDefinition mParent;
	private final List<SectionDefinition> mSections = new ArrayList<>();

	public DetailDefinition(WWLevelDefinition parent)
	{
		mParent = parent;
	}

	@Override
	public String getName()
	{
		// WWSDCustomer.Customer.Detail
		return String.format("%s.%s.%s", getPattern().getName(), mParent.getName(), "Detail");
	}

	@Override
	public WorkWithDefinition getPattern() { return mParent.getParent(); }

	public WWLevelDefinition getParent() { return mParent; }

	public List<SectionDefinition> getSections() { return mSections; }

	public SectionDefinition getSection(String sectionCode)
	{
		for (SectionDefinition sec : mSections)
		{
			if (sec.getCode().equals(sectionCode))
				return sec;
		}

		return null;
	}
}
