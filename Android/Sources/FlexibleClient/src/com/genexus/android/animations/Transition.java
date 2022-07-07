package com.genexus.android.animations;

import java.io.Serializable;

import android.app.Activity;
import androidx.fragment.app.FragmentTransaction;

public class Transition implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String mName;
	private final int mEnterActivityOut;
	private final int mEnterFragmentOut;
	private final int mEnterIn;
	private final int mCloseOut;
	private final int mCloseIn;

	/**
	 * Defines a symmetrical transition (close out = enter out, close in = enter in).
	 */
	public Transition(String name, int out, int in)
	{
		this(name, out, in, out, in);
	}

	/**
	 * Defines a (possibly) asymmetrical transition.
	 */
	public Transition(String name, int enterOut, int enterIn, int closeOut, int closeIn)
	{
		this(name, enterOut, enterOut, enterIn, closeOut, closeIn);
	}

	/**
	 * Defines a transition in which the effect for fragments is not the same as the corresponding
	 * effect for activities (mainly as a workaround for fragment animations, which do not
	 * respect z-order).
	 */
	public Transition(String name, int enterActivityOut, int enterFragmentOut, int enterIn, int closeOut, int closeIn)
	{
		mName = name;
		mEnterActivityOut = enterActivityOut;
		mEnterFragmentOut = enterFragmentOut;
		mEnterIn = enterIn;
		mCloseOut = closeOut;
		mCloseIn = closeIn;
	}

	@Override
	public String toString()
	{
		return mName;
	}

	public String getName() { return mName; }

	public void onCall(Activity from)
	{
		from.overridePendingTransition(mEnterIn, mEnterActivityOut);
	}

	public void onCall(FragmentTransaction transaction)
	{
		transaction.setCustomAnimations(mEnterIn, mEnterFragmentOut);
	}

	public void onReturn(Activity from)
	{
		from.overridePendingTransition(mCloseIn, mCloseOut);
	}
}
