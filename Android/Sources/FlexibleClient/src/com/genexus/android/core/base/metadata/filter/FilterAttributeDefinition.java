package com.genexus.android.core.base.metadata.filter;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.metadata.DataSourceMemberDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class FilterAttributeDefinition extends DataSourceMemberDefinition
{
	private static final long serialVersionUID = 1L;

	private final String mName;
	private final String mDescription;
	private final String mType;

	private final String mDefaultValue;
	private final String mDefaultBeginValue;
	private final String mDefaultEndValue;
	private final boolean mAllValue;

	private final String mCustomCondition;

	public static final String TYPE_STANDARD = "Standard";
	public static final String TYPE_RANGE = "Range";

	public FilterAttributeDefinition(IDataSourceDefinition parent, INodeObject jsonData)
	{
		super(parent);
		String name = MetadataLoader.getAttributeName(jsonData.getString("@attribute"));

		mName = DataItemHelper.getNormalizedName(name);
		mDescription = jsonData.optString("@description");
		mType = jsonData.optString("@type");

		mDefaultValue = parseExpression(jsonData.optString("@default"));
		mDefaultBeginValue = parseExpression(jsonData.optString("@defaultBeginRange"));
		mDefaultEndValue = parseExpression(jsonData.optString("@defaultEndRange"));
		mAllValue = jsonData.optBoolean("@allValue");

		mCustomCondition = jsonData.optString("@customCondition");
	}

	@Override
	public String getName() { return mName; }
	public String getDescription() { return Services.Language.getTranslation(mDescription); }
	public String getType() { return mType; }
	public String getDefaultValue() { return mDefaultValue; }
	public String getDefaultBeginValue() { return mDefaultBeginValue; }
	public String getDefaultEndValue() { return mDefaultEndValue; }
	public boolean getAllValue() { return mAllValue; }
	public String getCustomCondition() { return mCustomCondition; }

	private static String parseExpression(String expression)
	{
		// For now, we do not support "real" expressions here, just constants.
		// So the only processing we do is to remove quotes from string constants.
		if (Strings.hasValue(expression))
		{
			expression = expression.trim();
			if (expression.length() >= 2)
			{
				if ((expression.startsWith(Strings.SINGLE_QUOTE) && expression.endsWith(Strings.SINGLE_QUOTE)) ||
					(expression.startsWith(Strings.DOUBLE_QUOTE) && expression.endsWith(Strings.DOUBLE_QUOTE)))
				{
					// GX accepts both types of quotes.
					expression = expression.substring(1, expression.length() - 1);
				}
			}
		}

		return expression;
	}

	public List<String> getParameterNames()
	{
		List<String> names = new ArrayList<>();

		if (mType.equals(TYPE_RANGE))
		{
			names.add("C" + Strings.toLowerCase(mName) + "from");
			names.add("C" + Strings.toLowerCase(mName) + "to");
		}
		else //  if (mType.equals(TYPE_STANDARD))
		{
			names.add("C" + Strings.toLowerCase(mName));
		}

		return names;
	}
}
