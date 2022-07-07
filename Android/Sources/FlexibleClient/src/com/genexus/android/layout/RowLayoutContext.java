package com.genexus.android.layout;

@SuppressWarnings("checkstyle:MemberName")
class RowLayoutContext
{
    int RowsHiddenHeightSum = 0;
	int MaxHeightVisibleInCurrentRow = 0;
	int MaxHeightHiddenInCurrentRow = 0;
	
	public void clear()
	{
	    RowsHiddenHeightSum = 0;
		MaxHeightVisibleInCurrentRow = 0;
		MaxHeightHiddenInCurrentRow = 0;
	}
}
