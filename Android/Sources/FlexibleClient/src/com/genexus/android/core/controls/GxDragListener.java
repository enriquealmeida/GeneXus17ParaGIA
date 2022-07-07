package com.genexus.android.core.controls;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.DragEvent;
import android.view.View;

import com.genexus.android.animations.Transformations;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.ui.DragHelper;
import com.genexus.android.core.ui.DragHelper.DragLocalState;
import com.genexus.android.core.utils.Cast;

public class GxDragListener implements View.OnDragListener
{
	private Coordinator mCoordinator;

	public GxDragListener(Coordinator coordinator)
	{
		super();
		mCoordinator = coordinator;
	}

	@Override
	public boolean onDrag(View v, DragEvent event)
	{
		ThemeClassDefinition classDef = null;
		ThemeClassDefinition classToTarget = null;
		IGxThemeable vt = Cast.as(IGxThemeable.class, v);
		if (vt != null)
			classDef = vt.getThemeClass();
		final DragLocalState dragState = (DragLocalState)event.getLocalState();
		IGxThemeable vsource = Cast.as(IGxThemeable.class, dragState.draggedControl);

		boolean acceptDrag = false;
		ActionDefinition dropAction = mCoordinator.getControlEventHandler(v, GxTouchEvents.DROP);
		ActionParameter parameterInput = null;
		ClipDescription clipDataDescription = event.getClipDescription();
		if (dropAction != null)
		{
			if (dropAction.getEventParameters().size() > 0 && clipDataDescription != null)
			{
				parameterInput = dropAction.getEventParameters().get(0);
				String expectingType = DragHelper.getDragDropType(dropAction);
				String draggingType = clipDataDescription.getLabel().toString();
				if (expectingType.equalsIgnoreCase(draggingType))
					acceptDrag = true;
			}
			else
				acceptDrag = clipDataDescription == null && dropAction.getEventParameters().size() == 0;
		}

		switch (event.getAction())
		{
			case DragEvent.ACTION_DRAG_STARTED:
				if (classDef != null)
					classToTarget = acceptDrag ? classDef.getAcceptDragClass() : classDef.getNoAcceptDragClass();

				if (!dragState.dragStarted)
				{
					dragState.draggedControl.setVisibility(View.INVISIBLE);
					dragState.dragStarted = true;
				}
				break;

			case DragEvent.ACTION_DRAG_EXITED:
				if (classDef != null && acceptDrag)
					classToTarget = classDef.getAcceptDragClass();
				break;

			case DragEvent.ACTION_DRAG_ENTERED:
				if (classDef != null && acceptDrag)
					classToTarget = classDef.getDragOverClass();
				break;

			case DragEvent.ACTION_DRAG_ENDED:
				if (classDef != null)
					classToTarget = classDef;

				if (!dragState.dragFinished)
				{
					vsource.setThemeClass(vsource.getThemeClass());

					if (!event.getResult())
					{
						// Drag canceled so restore visibility of the source control
						dragState.draggedControl.post(new Runnable()
						{
							@Override
							public void run()
							{
								dragState.draggedControl.setVisibility(View.VISIBLE);
								mCoordinator.runControlEvent(dragState.draggedControl, "DragCanceled");
							}
						});
						dragState.dragFinished = true;
					}
				}

				break;

			case DragEvent.ACTION_DROP:
			{
				if (acceptDrag)
				{
					if (clipDataDescription != null && parameterInput != null)
					{
						// Gets the item containing the dragged data
						ClipData.Item item = event.getClipData().getItemAt(0);
						if (item != null)
						{
							// Gets the text data from the item.
							String dragText = item.getText().toString();
							mCoordinator.setValue(parameterInput.getValue(), dragText);
						}
					}

					mCoordinator.runControlEvent(v, GxTouchEvents.DROP, null, new Runnable()
					{
						@Override
						public void run()
						{
							// must run on UI Thread
							Services.Device.runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									mCoordinator.runControlEvent(dragState.draggedControl, "DropAccepted");
								}
							});
						}
					});
					classToTarget = classDef;
				}

				break;
			}
		}

		if (classToTarget != null && vt != null) {
			// Don't use GxTheme.applyStyle() because it uses setThemeClass() which changes the class of the control given by gx user
			vt.applyClass(classToTarget);
			Transformations.apply(v, classToTarget);
		}

		return true;
 	}
}
