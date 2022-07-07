package com.genexus.android.core.base.utils;

import java.io.Serializable;

import androidx.annotation.NonNull;

@SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
public class Version implements Comparable<Version>, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final int Major;
	public final int Minor;
	public final int Build;
	public final int Revision;

	public Version(int major, int minor)
	{
		this(major, minor, 0);
	}

	public Version(int major, int minor, int build)
	{
		this(major, minor, build, 0);
	}

	public Version(int major, int minor, int build, int revision)
	{
		Major = major;
		Minor = minor;
		Build = build;
		Revision = revision;
	}

	public Version(String str)
	{
		int major = 0;
		int minor = 0;
		int build = 0;
		int revision = 0;

		if (Strings.hasValue(str))
		{
			String[] parts = str.split("\\.", -1);

			if (parts.length > 0)
				major = parseInt(parts[0]);
			if (parts.length > 1)
				minor = parseInt(parts[1]);
			if (parts.length > 2)
				build = parseInt(parts[2]);
			if (parts.length > 3)
				revision = parseInt(parts[3]);
		}

		Major = major;
		Minor = minor;
		Build = build;
		Revision = revision;
	}

	public static Version empty() {
		return new Version(0, 0);
	}

	private static int parseInt(String str)
	{
		try
		{
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	@Override
	public String toString()
	{
		return String.format("%s.%s.%s.%s", Major, Minor, Build, Revision);
	}

	public boolean isEmpty()
	{
		return (Major == 0 && Minor == 0 && Build == 0 && Revision == 0);
	}

	public boolean isLessThan(Version other)
	{
		return (compareTo(other) < 0);
	}

	public boolean isGreaterThan(Version other)
	{
		return (compareTo(other) > 0);
	}

	public boolean isEqualTo(Version other) { return (compareTo(other) == 0); }

	@Override
	public int compareTo(@NonNull Version another)
	{
		if (another == null)
			throw new NullPointerException();

		int[] thisV = new int[] { Major, Minor, Build, Revision };
		int[] otherV = new int[] { another.Major, another.Minor, another.Build, another.Revision };

		for (int i = 0; i < thisV.length; i++)
		{
			if (thisV[i] < otherV[i])
				return -1;
			else if (thisV[i] > otherV[i])
				return +1;
			// else, loop to consider less significant parts.
		}

		return 0; // All equal.
	}
}
