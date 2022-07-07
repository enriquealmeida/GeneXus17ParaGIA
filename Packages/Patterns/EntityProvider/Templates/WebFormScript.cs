private static class WebFormScript
{
	public static string GridColumn(IAttributesItem item)
	{
		return GridColumn(item, false, false);
	}

	public static string GridColumn(IAttributesItem item, bool isAbstractForm, bool isDescriptionAttribute)
	{
		if (item.Id == null)
			return null;

		if (item is AttributeElement)
			return GridAttribute((AttributeElement)item, isAbstractForm, isDescriptionAttribute);

		if (item is VariableElement)
			return GridVariable((VariableElement)item, isAbstractForm);

		throw new PatternApplicationException("Unexpected attributes item: not an attribute or variable");
	}

	public static string GridAttribute(AttributeElement att)
	{
		return GridAttribute(att, false, false);
	}

	public static string GridAttribute(AttributeElement att, bool isAbstractForm, bool isDescriptionAttribute)
	{
		string defaultThemeClass = att.Instance.Settings.Theme.ReadOnlyGridAttribute;
		if (att.Attribute.Type == eDBType.BINARY)
			defaultThemeClass = att.Instance.Settings.Theme.ReadOnlyGridBlobAttribute;

		string themeClass = GetThemeClass(att.ThemeClass, defaultThemeClass);
		Dictionary<string, object> properties = new Dictionary<string, object>();
		GetControlInfo(att, properties);
		GetFormat(att, properties);

		if (isAbstractForm)
		{
			string columnClass = Constants.ColumnClass;
			if (isDescriptionAttribute)
				themeClass += " " + Constants.DescriptionAttributeClass;
			else
				columnClass = att.Instance.Settings.Theme.OptionalColumn;

			columnClass = GetThemeClass(att.ThemeColumnClass, columnClass);
			if (columnClass != Constants.ColumnClass)
				columnClass = Constants.ColumnClass + " " + columnClass;
			properties[Properties.HTMLSFLCOL.ColumnClass] = columnClass;
			properties[Properties.HTMLSFLCOL.Visible] = att.Visible;
			return WebLayout.SimpleGridAttribute(att.AttributeName, themeClass, att.Description, null, properties);
		}
		else
		{
			return WebForm.GridAttribute(att.AttributeName, themeClass, att.Description, att.Visible, null, properties);
		}
	}

	public static string GridVariable(VariableElement var)
	{
		return GridVariable(var, false);
	}

	public static string GridVariable(VariableElement var, bool isAbstractForm)
	{
		string themeClass = GetThemeClass(var.ThemeClass, var.Instance.Settings.Theme.ReadOnlyGridAttribute);
		Dictionary<string, object> properties = new Dictionary<string, object>();
		properties[Properties.HTMLSFLCOL.ReadOnly] = true;
		GetControlInfo(var, properties);
		GetFormat(var, properties);

		if (isAbstractForm)
		{
			string columnClass = GetThemeClass(var.ThemeColumnClass, var.Instance.Settings.Theme.OptionalColumn);
			if (columnClass != Constants.ColumnClass)
				columnClass = Constants.ColumnClass + " " + columnClass;
			properties[Properties.HTMLSFLCOL.ColumnClass] = columnClass;
			properties[Properties.HTMLSFLCOL.Visible] = var.Visible;
			return WebLayout.SimpleGridVariable(var.VariableName, themeClass, var.Description, null, properties);
		}
		else
		{
			return WebForm.GridVariable(var.VariableName, themeClass, var.Description, var.Visible, null, properties);
		}
	}

	public static string Item(IAttributesItem item)
	{
		return Item(item, false, false);
	}

	public static string Item(IAttributesItem item, bool isAbstractForm, bool showLabel)
	{
		if (item.Id == null)
			return null;

		if (item is AttributeElement)
			return Attribute((AttributeElement)item, isAbstractForm, showLabel);

		if (item is VariableElement)
			return Variable((VariableElement)item, isAbstractForm, showLabel);

		throw new PatternApplicationException("Unexpected attributes item: not an attribute or variable");
	}

	public static string Attribute(AttributeElement att)
	{
		return Attribute(att, false, false);
	}

	public static string Attribute(AttributeElement att, bool isAbstractForm, bool showLabel)
	{
		string defaultThemeClass = att.Instance.Settings.Theme.ReadOnlyAttribute;
		if (att.Attribute.Type == eDBType.BINARY)
			defaultThemeClass = att.Instance.Settings.Theme.ReadOnlyBlobAttribute;
		if (att.Attribute.Type == eDBType.VIDEO)
			defaultThemeClass = att.Instance.Settings.Theme.ReadOnlyVideoAttribute;
		if (att.Attribute.Type == eDBType.AUDIO)
			defaultThemeClass = att.Instance.Settings.Theme.ReadOnlyAudioAttribute;
		if (att.Attribute.Type == eDBType.BINARYFILE)
			defaultThemeClass = att.Instance.Settings.Theme.ReadOnlyDownloadAttribute;
		
		string themeClass = GetThemeClass(att.ThemeClass, defaultThemeClass);
		Dictionary<string, object> properties = new Dictionary<string, object>();
		GetControlInfo(att, properties);
		GetFormat(att, properties);

		if (isAbstractForm)
		{
			if (!showLabel)
				properties[Artech.Patterns.WorkWithDevices.Copy.InstanceAttributes.LayoutDataItem.LabelPosition] = "None";
			if (((IAttributesItem)att).TypeInfo.Type == eDBType.BITMAP)
				themeClass += " " + Constants.ResponsiveImageAttributeClass;
			return WebLayout.Attribute(att.AttributeName, themeClass, att.Description, null, properties);
		}
		else
		{
			return WebForm.Attribute(att.AttributeName, themeClass, null, properties);
		}
	}

	public static string HiddenAttribute(string attName)
	{
		return HiddenAttribute(attName, false);
	}

	public static string HiddenAttribute(string attName, bool isAbstractForm)
	{
		Dictionary<string, object> properties = new Dictionary<string, object>();
		properties[Properties.ATT.ControlType] = Properties.ATT.ControlType_Values.Edit;
		properties[Properties.ATT.InputType] = Properties.ATT.InputType_Values.Values;
		if (isAbstractForm)
		{
			properties[Artech.Patterns.WorkWithDevices.Copy.InstanceAttributes.LayoutDataItem.LabelPosition] = "None";
			return WebLayout.Attribute(attName, null, null, null, properties);
		}
		else
		{
			return WebForm.Attribute(attName, null, null, properties);
		}
	}

	public static string Variable(VariableElement var)
	{
		return Variable(var, false, false);
	}

	public static string Variable(VariableElement var, bool isAbstractForm, bool showLabel)
	{
		string themeClass = GetThemeClass(var.ThemeClass, var.Instance.Settings.Theme.ReadOnlyAttribute);
		Dictionary<string, object> properties = new Dictionary<string, object>();
		properties[Properties.HTMLATT.ReadOnly] = true;
		GetControlInfo(var, properties);
		GetFormat(var, properties);

		if (isAbstractForm)
		{
			if (!showLabel)
				properties[Artech.Patterns.WorkWithDevices.Copy.InstanceAttributes.LayoutDataItem.LabelPosition] = "None";
			if (((IAttributesItem)var).TypeInfo.Type == eDBType.BITMAP)
				themeClass += " " + Constants.ResponsiveImageClass;
			return WebLayout.Variable(var.VariableName, themeClass, var.Description, null, properties);
		}
		else
		{
			return WebForm.Variable(var.VariableName, themeClass, null, properties);
		}
	}

	private static void GetControlInfo(IAttributesItem item, Dictionary<string, object> properties)
	{
		ITypedObject typeInfo = item.TypeInfo;
		if (typeInfo != null && typeInfo is Artech.Common.Properties.PropertiesObject)
		{
			Artech.Common.Properties.PropertiesObject props = (Artech.Common.Properties.PropertiesObject)typeInfo;
			int controlType = props.GetPropertyValue<int>(Properties.ATT.ControlType);
			int editSuggest = props.GetPropertyValue<int>(Properties.ATT.Suggest);
			int editInputType = props.GetPropertyValue<int>(Properties.ATT.InputType);

			if ((controlType == Properties.ATT.ControlType_Values.DynamicComboBox) ||
				(controlType == Properties.ATT.ControlType_Values.DynamicListBox) ||
				(controlType == Properties.ATT.ControlType_Values.Edit && (editInputType == Properties.ATT.InputType_Values.Descriptions || editSuggest != Properties.ATT.Suggest_Values.No)))
			{
				properties[Properties.ATT.ControlType] = Properties.ATT.ControlType_Values.Edit;
				properties[Properties.ATT.InputType] = Properties.ATT.InputType_Values.Values;
			}
		}
	}

	private static void GetFormat(IAttributesItem item, Dictionary<string, object> properties)
	{
		bool setFormat = true;
		int formatValue = 0;

		switch (item.Format)
		{
			case AttributeElement.FormatValue.Text:
				formatValue = Properties.ATT.Format_Values.Text;
				break;
			case AttributeElement.FormatValue.HTML:
				formatValue = Properties.ATT.Format_Values.Html;
				break;
			case AttributeElement.FormatValue.RawHTML:
				formatValue = Properties.ATT.Format_Values.RawHtml;
				break;
			case AttributeElement.FormatValue.TextWithMeaningfulSpaces:
				formatValue = Properties.ATT.Format_Values.TextWithMeaningfulSpaces;
				break;
			default:
				setFormat = false;
				break;
		}

		if (setFormat)
			properties[Properties.ATT.Format] = formatValue;
	}

	private static string GetThemeClass(string themeClass, string defaultClass)
	{
		if (!String.IsNullOrEmpty(themeClass))
			return themeClass;

		if (!String.IsNullOrEmpty(defaultClass))
			return defaultClass;

		return null;
	}
}
