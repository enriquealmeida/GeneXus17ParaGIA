package com.genexus.android.core.controls.common;

import java.util.List;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class StaticValueItems extends ValueItems<ValueItem>
{
	public StaticValueItems(DataItem dataItem, ControlInfo controlInfo)
	{
		super();
		initializeItems(dataItem, controlInfo, null);
	}

	public StaticValueItems(DataItem dataItem, ControlInfo controlInfo, String controlValuesId)
	{
		super();
		initializeItems(dataItem, controlInfo, controlValuesId);
	}

	private void initializeItems(DataItem dataItem, ControlInfo controlInfo, String controlValuesId)
	{
		if (controlInfo.optBooleanProperty("@AddEmptyItem"))
		{
			Object emptyValue = (dataItem != null ? dataItem.getEmptyValue() : null);
			if (emptyValue == null)
				emptyValue = Strings.EMPTY;

			ValueItem emptyItem = new ValueItem(emptyValue.toString(), controlInfo.getTranslatedProperty("@EmptyItemText"));
			setEmptyItem(emptyItem);
		}

		String strControlValues = controlInfo.optStringProperty("@ControlValues");
		// Check for specific user control Control Values, use them if available.
		if (controlValuesId!=null) {
			String specificControlValues = controlInfo.optStringProperty(controlValuesId);
			if (Services.Strings.hasValue(specificControlValues))
				strControlValues = specificControlValues;
		}
		if (Services.Strings.hasValue(strControlValues))
		{
			List<String> controlValues = Services.Strings.decodeStringList(strControlValues, ',');
			for (String strItem : controlValues)
			{
				List<String> itemParts = Services.Strings.decodeStringList(strItem, ':');
				if (itemParts.size() == 2)
				{
					String itemValue = itemParts.get(1);
					String itemDescription = Services.Language.getTranslation(itemParts.get(0));

					add(new ValueItem(itemValue, itemDescription));
				}
			}
		}
	}
}
