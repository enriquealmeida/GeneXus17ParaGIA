private static class SearchButton
{
	public static ActionElement Prepare(KBObject kbObject, IGridObject grid)
	{
		if (IsNeeded(kbObject))
		{
			ActionElement search = Artech.Patterns.WorkWith.Helpers.SpecialAction.CreateSearch(grid);
			return search;
		}
		else
			return null;
	}

	private static bool IsNeeded(KBObject kbObject)
	{
		System.Diagnostics.Debug.Assert(kbObject is WebPanel);
		string autoRefreshValue = kbObject.GetPropertyValue<string>(Properties.WBP.AutomaticRefresh);

		return (autoRefreshValue == Properties.WBP.AutomaticRefresh_Values.No);
	}
}
