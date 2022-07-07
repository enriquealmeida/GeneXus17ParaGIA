
private string EscapeXml(string text)
{
	return System.Security.SecurityElement.Escape(text);
}

private string GetSDAttributeElements(string elementName, int[] atts)
{
	string[] attElements = new string[atts.Length];
	for (int i = 0; i < atts.Length; i++)
		attElements[i] = GetSDAttributeElement(elementName, atts[i]);

	return String.Join("\r\n", attElements);
}

private string GetSDAttributeElement(string elementName, int attId)
{
	return String.Format("<{0} attribute=\"{1}-{2}\" />", elementName, ObjClass.Attribute, GetAttributeName(attId));
}

private bool FilterAttributeIsSearch(int attId)
{
	Artech.Genexus.Common.Objects.Attribute att = GetAttribute(attId);

	// Exclude enumerated attributes.
	if (att.GetPropertyValue(Artech.Genexus.Common.Properties.ATT.EnumValues) != null)
		return false;

	// Exclude non-character data types
	if (!eDBTypeConstants.IsString(att.Type))
		return false;

	return true;
}