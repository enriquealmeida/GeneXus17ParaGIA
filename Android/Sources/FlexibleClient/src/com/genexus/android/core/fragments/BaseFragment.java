package com.genexus.android.core.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.controls.IGxEditFinishAware;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.ActionContext;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.CoordinatorBase;
import com.genexus.android.core.utils.Cast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public abstract class BaseFragment extends AppCompatDialogFragment implements ActionContext, IInspectableComponent
{
	private final ArrayList<ActionDefinition> mPendingActions = new ArrayList<>();
	private Anchor mAnchor;
	private Size mDesiredSize;

	private boolean mAllowHeaderRow = false;

	@Override
	public void onStart()
	{
		super.onStart();
		if (getDialog() == null)
			return;

		if (mAnchor != null)
		{
			// A callout. Not modal, and (TODO) should be shown at specified position.
			getDialog().setCanceledOnTouchOutside(true);

			// WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
			// p.x =  mAnchorRect.left;
			// p.y = mAnchorRect.top + (mAbsoluteHeightForTable / 2) - mAnchorRect.height() / 2 - (screenSize.getHeight()/2); // 0 is the center of the screen
			// getDialog().getWindow().setAttributes(p);
		}
		else
		{
			// A popup is modal.
		    getDialog().setCanceledOnTouchOutside(false);

			// set transparent to window , to allow corner radius work if set
			setDialogTransparentIfNeeded();
		}

		if (mDesiredSize != null)
		{
			// Set the dialog size. The frame adds padding (actually, insets); see
			// android\support\v7\appcompat\res\drawable\abc_dialog_material_background_dark.xml
			// so we need to add these pixels back to account for it; otherwise the content is cut.
			final int DIALOG_INSETS = Services.Device.dipsToPixels(16);
			int dialogWidth = mDesiredSize.getWidth() + 2 * DIALOG_INSETS;
			int dialogHeight = mDesiredSize.getHeight() + 2 * DIALOG_INSETS;

			// Normally the dialog would auto-size itself to match its content, but unfortunately
			// it does not grow beyond a certain width (hence, this code).
			getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
		}
	}

	public void setDialogAnchor(Anchor anchor)
	{
		mAnchor = anchor;
	}

	public void setDesiredSize(Size size)
	{
		mDesiredSize = size;
	}

	protected Size getDesiredSize()
	{
		return mDesiredSize;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (mPendingActions.size() != 0)
		{
			ArrayList<ActionDefinition> pendingActions = new ArrayList<>(mPendingActions);
			mPendingActions.clear();

			for (ActionDefinition action : pendingActions)
				runAction(action, null, false);
		}
	}

	public void finishEdit() {
		IGxEditFinishAware control = null;
		if (getDialog() != null)
			control = Cast.as(IGxEditFinishAware.class, getDialog().getCurrentFocus());
		if (control == null && getActivity() != null)
			control = Cast.as(IGxEditFinishAware.class, getActivity().getCurrentFocus());
		if (control != null)
			control.finishEdit();
	}

	@Override
	public void runAction(ActionDefinition action, Anchor anchor)
	{
		runAction(action, anchor, false);
	}

	@Override
	public void runAction(ActionDefinition action, Anchor anchor, boolean allowDuplicate)
	{
		// Enqueue action if it's fired before the fragment is attached to the activity
		// (Activity is necessary for building UIContext).
		if (getActivity() != null)
		{
			if (!CoordinatorBase.isInternalEvent(action.getName()))
				finishEdit();
			UIContext context = getUIContext();
			context.setAnchor(anchor);
			((IGxActivity)getActivity()).getController().runAction(context, action, getContextEntity(), allowDuplicate);
		}
		else
			mPendingActions.add(action);
	}

	public abstract @NonNull String getUri();
	public abstract UIContext getUIContext();
	public abstract void setActive(boolean active);

	public abstract void saveFragmentState(LayoutFragmentState state);
	public abstract void restoreFragmentState(LayoutFragmentState state);
	public abstract List<BaseFragment> getChildFragments();

	public boolean isAllowHeaderRow()
	{
		return mAllowHeaderRow;
	}

	public void setAllowHeaderRow(boolean mAllowHeaderRow)
	{
		this.mAllowHeaderRow = mAllowHeaderRow;
	}

	private void setDialogTransparentIfNeeded() {
		if (this instanceof LayoutFragment) {
			LayoutFragment dialogLayoutFragment = ((LayoutFragment)this);
			if (dialogLayoutFragment.getLayout()!=null && dialogLayoutFragment.getLayout().getTable()!=null ) {
				ThemeClassDefinition themeClass = dialogLayoutFragment.getLayout().getTable().getThemeClass();
				if (themeClass!=null) {
					final int cornersRadius = themeClass.getMaxCornersRadius();
					if (cornersRadius > 0) {
						// set transparent to dialog window, from https://stackoverflow.com/a/28937224
						getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					}
				}
			}
		}
	}
}
