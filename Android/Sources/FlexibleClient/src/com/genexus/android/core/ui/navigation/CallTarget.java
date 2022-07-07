package com.genexus.android.core.ui.navigation;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Possible Call Target for a CallOptions object.
 */
public class CallTarget
{
	// Some predefined targets. Well, one. For now.
	public static final CallTarget BLANK = new CallTarget("Blank");

	private final String mName;
	private final Set<String> mAliases;

	public CallTarget(String... names)
	{
		mName = names[0];

		Set<String> aliases = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Collections.addAll(aliases, names);

		mAliases = Collections.unmodifiableSet(aliases);
	}

	@Override
	public String toString()
	{
		return String.format("%s (%s)", mName, mAliases);
	}

	public String getName()
	{
		return mName;
	}

	public boolean isTarget(CallOptions options)
	{
		return (options != null && isTarget(options.getTargetName()));
	}

	public boolean isTarget(String name)
	{
		return (name != null && mAliases.contains(name));
	}
}
