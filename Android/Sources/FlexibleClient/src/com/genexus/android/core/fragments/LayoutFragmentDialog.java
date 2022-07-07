package com.genexus.android.core.fragments;

import androidx.appcompat.app.AppCompatDialog;

import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.services.Services;

public class LayoutFragmentDialog extends AppCompatDialog
{
	private final LayoutFragment mContent;

	public LayoutFragmentDialog(LayoutFragment content)
	{
		super(content.getActivity(), content.getTheme());
		mContent = content;
	}

	@Override
	public void onBackPressed()
	{
		IGxActivity activity = mContent.getGxActivity();
		if (activity != null && activity.getController() != null && activity.getController().handleOnBackPressed(mContent))
			return;

		// Standard behavior, close the dialog.
		super.onBackPressed();
	}

	@Override
	protected void onStart() {
		//FragmentDialog event is notified to Application so Live Editing knows about it in order to inform the IDE
		Services.Application.getLifecycle().notifyDialogStarted(this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		//FragmentDialog event is notified to Application so Live Editing knows about it in order to inform the IDE
		Services.Application.getLifecycle().notifyDialogStopped(this);
		super.onStop();
	}
}
