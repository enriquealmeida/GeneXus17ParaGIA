package com.genexus.android.core.actions;

import android.app.Activity;
import android.content.Intent;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.IGxGridControl;
import com.genexus.android.core.utils.Cast;

import java.util.ArrayList;

class ForEachLineAction extends Action
{
	private final boolean mIsComposite;
	private final ActionDefinition.MultipleSelectionInfo mInfo;
	private boolean mCatchOnActivityResult;
	private String mErrorMessage;

	ForEachLineAction(UIContext context, ActionDefinition definition, ActionParameters parameters, boolean isComposite)
	{
		super(context, definition, parameters);
		mIsComposite = isComposite;
		mInfo = definition.getMultipleSelectionInfo();

		// TODO: Enable this when "selection on demand" works.
		// Action needs a continuation if grid selection mode = "on action" and it's a "for each SELECTED line" action.
		// Otherwise Do() already has all the information needed and can proceed.
		// mSelectionOnDemand = (mInfo.useSelection() && mGrid != null && mGrid.getDefinition().getShowSelector() == GridDefinition.SELECTION_ON_ACTION);
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return "foreachline".equals(definition.optStringProperty(ActionHelper.STATEMENT_NAME));
	}

	@Override
	public boolean Do()
	{
		final IGxGridControl grid = Cast.as(IGxGridControl.class, findControl(mInfo.getGrid()));
		if (grid != null)
		{
			final EntityList entities = grid.getData();
			ArrayList<Entity> list = new ArrayList<>();
			for (Entity entity : entities) {
				if (!mInfo.useSelection() || entity.isSelected()) {
					list.add(entity);
				}
			}
			final ActionParameters parameters = new ActionParameters(list);

			if (parameters.getEntity() != null) {
				CompositeAction actionBlock = ActionFactory.getInnerActionChildren(getContext(), getDefinition(), parameters, mIsComposite);
				actionBlock.setLoopCondition(() -> {
					if (parameters.getEntities().isEmpty())
						return false; // this may be called after the loop has ended

					parameters.getEntities().remove(0); // next entity
					if (parameters.getEntity() != null) {
						entities.setCurrentItem(parameters.getEntity()); // Set "CurrentItem" so that selection can be evaluated over SDTs.
						return true;
					}
					else {
						mCatchOnActivityResult = false;

						// Either clear selection (if selection mode = "always") or end selection (if "on demand").
						Services.Device.runOnUiThread(() -> grid.setSelectionMode(false, null));
						return false;
					}
				});

				mCatchOnActivityResult = true;

				// Set "CurrentItem" so that selection can be evaluated over SDTs.
				entities.setCurrentItem(parameters.getEntity());
				ActionExecution exec = new ActionExecution(actionBlock);
				exec.executeAction();
			}

			mErrorMessage = "";
			return true;
		}
		else
		{
			mErrorMessage = String.format("Grid '%s' not found on UI context.", mInfo.getGrid());
			Services.Log.error(mErrorMessage);
			return false;
		}
	}

	@Override
	public String getErrorMessage() {
		return mErrorMessage;
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result)
	{
		return ActionResult.SUCCESS_CONTINUE;
	}

	@Override
	public boolean catchOnActivityResult()
	{
		return mCatchOnActivityResult;
	}

	@Override
	public Activity getActivity()
	{
		return super.getActivity();
	}
}
