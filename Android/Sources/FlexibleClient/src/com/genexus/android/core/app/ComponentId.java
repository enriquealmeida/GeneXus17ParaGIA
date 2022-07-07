package com.genexus.android.core.app;

public class ComponentId
{
	private final ComponentId mParent;
	private final String mId;

	public ComponentId(ComponentId parent, String id)
	{
		mParent = parent;
		mId = id;
	}

	public static final ComponentId ROOT = new ComponentId(null, "ROOT");

	@Override
	public String toString()
	{
		return (mParent != null ? mParent.toString() + "." : "::") + mId;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;

		if (!(o instanceof ComponentId))
			return false;

		ComponentId that = (ComponentId) o;

		if (mParent != null ? !mParent.equals(that.mParent) : that.mParent != null)
			return false;

		return mId.equals(that.mId);
	}

	@Override
	public int hashCode()
	{
		int result = mParent != null ? mParent.hashCode() : 0;
		result = 31 * result + mId.hashCode();
		return result;
	}

	public boolean isDescendantOf(ComponentId ancestor)
	{
		if (mParent == null)
			return false;

		if (mParent.equals(ancestor))
			return true;

		return mParent.isDescendantOf(ancestor);
	}
}
