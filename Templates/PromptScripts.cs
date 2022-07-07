private string RuleList(string rule, string[] items)
{
	if (items.Length > 0)
		return rule + "(" + String.Join(", ", items) + ");";

	return String.Empty;
}

private string RuleAttributeList(string rule, int[] atts)
{
	return RuleList(rule, Array.ConvertAll<int, string>(atts, GetAttributeName));
}

private string GetAttributeName(int attId)
{
	Artech.Genexus.Common.Objects.Attribute att = GetAttribute(attId);
	return att.Name;
}

private Artech.Genexus.Common.Objects.Attribute GetAttribute(int attId)
{
	Artech.Genexus.Common.Objects.Attribute att = Artech.Genexus.Common.Objects.Attribute.Get(Model, attId);
	if (att == null)
		throw new GxException(String.Format("Attribute {0} not found", attId));

	return att;
}

private string GetParameterVarName(int attId)
{
	return GetParameterVarName(GetAttributeName(attId)); 
}

private string GetParameterVarName(string attName)
{
	return "p" + attName;
}

private string GetConditionVarName(int attId)
{
	return GetConditionVarName(GetAttributeName(attId));
}

private string GetConditionVarName(string attName)
{
	return "c" + attName; 
}

private const string SELECTION_EVENT = "Select";
