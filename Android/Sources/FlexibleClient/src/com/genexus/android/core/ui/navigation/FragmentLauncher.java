package com.genexus.android.core.ui.navigation;

import androidx.fragment.app.FragmentTransaction;

import com.genexus.android.core.base.metadata.IViewDefinition;

public class FragmentLauncher
{
	public static void applyCallOptions(FragmentTransaction transaction, IViewDefinition view, CallOptions callOptions)
	{
		if (callOptions != null)
		{
			if (callOptions.getEnterEffect() != null)
				callOptions.getEnterEffect().onCall(transaction);

			// Because we don't have a back stack yet, it's not necessary to set the exit effect.
			// And because of that too, we don't have replace/push, only replace.
			// And we are also missing popup/callout.
		}

		// Remove global configured CallOptions after call.
		CallOptionsHelper.resetCallOptions(view.getObjectName());
	}
}
