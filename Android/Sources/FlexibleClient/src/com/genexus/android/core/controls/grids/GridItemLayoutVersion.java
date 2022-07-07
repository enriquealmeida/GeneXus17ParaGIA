package com.genexus.android.core.controls.grids;

import com.genexus.android.core.base.utils.Triplet;

class GridItemLayoutVersion
{
	private final Triplet<Integer, Integer, Integer> mValues;

	public final int itemWidth;
	public final int itemHeight;
	public final int itemReservedWidth;

	public GridItemLayoutVersion(int width, int height, int reservedWidth)
	{
		itemWidth = width;
		itemHeight = height;
		itemReservedWidth = reservedWidth;

		mValues = new Triplet<>(width, height, reservedWidth);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
            return true;

        if (!(obj instanceof GridItemLayoutVersion))
            return false;

        GridItemLayoutVersion other = (GridItemLayoutVersion)obj;
        return (mValues.equals(other.mValues));
	}

	@Override
	public int hashCode()
	{
		return mValues.hashCode();
	}
}
