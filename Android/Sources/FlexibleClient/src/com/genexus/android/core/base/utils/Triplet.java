package com.genexus.android.core.base.utils;

public final class Triplet<T1, T2, T3>
{
	public final T1 first;
	public final T2 second;
	public final T3 third;

	public Triplet(T1 v1, T2 v2, T3 v3)
	{
		first = v1;
		second = v2;
		third = v3;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Triplet<T1, T2, T3> other = (Triplet<T1, T2, T3>)obj;
        return (equalValues(first, other.first) &&
        		equalValues(second, other.second) &&
        		equalValues(third, other.third));
	}

	private static <T> boolean equalValues(T v1, T v2)
	{
		if (v1 != null)
			return v1.equals(v2);
		else
			return (v2 == null);
	}

	@Override
	public int hashCode()
	{
		return ((first != null ? first.hashCode() : 0) ^
				(second != null ? second.hashCode() : 0) ^
				(third != null ? third.hashCode() : 0));
	}

	@Override
	public String toString()
	{
		return String.format("[%s;%s;%s]", first, second, third);
	}
}
