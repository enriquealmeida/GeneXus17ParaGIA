package com.genexus.android.core.base.metadata.filter;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.DataSourceMemberDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class SearchDefinition extends DataSourceMemberDefinition
{
	private static final long serialVersionUID = 1L;

	public static final int OPERATOR_CONTAINS = 1;
	public static final int OPERATOR_BEGINS_WITH = 2;

	private final String mCaption;
	private final boolean mAlwaysVisible;
	private final boolean mHasOptionForIndividualFields;
	private final List<SearchAttributeDefinition> mAttributes;
	private final int mOperator;
	private final boolean mShowBreakBy;

	SearchDefinition(IDataSourceDefinition parent, INodeObject jsonData)
	{
		super(parent);

		mCaption = jsonData.optString("@caption");
		mHasOptionForIndividualFields = jsonData.optBoolean("@optionForIndividualFields");
		mAlwaysVisible = jsonData.optBoolean("@alwaysVisible");

		mAttributes = new ArrayList<>();
		INodeCollection jsonAttributes = jsonData.optCollection("attribute");
		for (int i = 0; i < jsonAttributes.length(); i++)
		{
			SearchAttributeDefinition searchAtt = new SearchAttributeDefinition(parent, jsonAttributes.getNode(i));
			mAttributes.add(searchAtt);
		}

		String operator = jsonData.optString("@filterOperator");
		mOperator = (operator.equalsIgnoreCase("Begins with") ? OPERATOR_BEGINS_WITH : OPERATOR_CONTAINS);

		String showBreakBy = jsonData.optString("@breakBy");
		mShowBreakBy = !showBreakBy.equalsIgnoreCase("Disabled");

	}

	@Override
	public String getName()
	{
		return "Search";
	}

	public String getCaption() { return Services.Language.getTranslation(mCaption); }
	public boolean isAlwaysVisible() { return mAlwaysVisible; }
	public boolean hasOptionForIndividualFields() { return mHasOptionForIndividualFields; }
	public List<SearchAttributeDefinition> getAttributes() { return mAttributes; }
	public int getOperator() { return mOperator; }

	public boolean showBreakBy() { return mShowBreakBy; }

	public List<String> getAttributeNames()
	{
		ArrayList<String> names = new ArrayList<>();
		for (SearchAttributeDefinition attribute : mAttributes)
			names.add(attribute.getName());

		return names;
	}
}
