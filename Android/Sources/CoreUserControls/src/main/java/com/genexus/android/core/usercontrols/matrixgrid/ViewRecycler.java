package com.genexus.android.core.usercontrols.matrixgrid;

import java.util.ArrayDeque;
import java.util.HashMap;

import android.view.View;

class ViewRecycler<KeyT, ViewT extends View>
{
	private HashMap<KeyT, ArrayDeque<ViewT>> mDump;

	public ViewRecycler()
	{
		mDump = new HashMap<>();
	}

	public void put(KeyT viewKey, ViewT view)
	{
		ArrayDeque<ViewT> typeViews = mDump.get(viewKey);
		if (typeViews == null)
		{
			typeViews = new ArrayDeque<>();
			mDump.put(viewKey, typeViews);
		}

		typeViews.addFirst(view);
	}

	public ViewT get(KeyT viewKey)
	{
		ArrayDeque<ViewT> typeViews = mDump.get(viewKey);
		if (typeViews != null && ! typeViews.isEmpty())
			return typeViews.removeFirst();
		else
			return null;
	}
}
