package com.genexus.android.core.controls;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.ui.DragHelper;
import com.genexus.android.core.ui.DragHelper.DragLocalState;
import com.genexus.android.core.ui.ICustomTouchListener;
import com.genexus.android.core.ui.ViewTouchCompat;
import com.genexus.android.core.utils.Cast;

import static com.genexus.android.core.controls.GxTouchEvents.DOUBLE_TAP;
import static com.genexus.android.core.controls.GxTouchEvents.LONG_TAP;
import static com.genexus.android.core.controls.GxTouchEvents.START_DRAG;
import static com.genexus.android.core.controls.GxTouchEvents.SWIPE;
import static com.genexus.android.core.controls.GxTouchEvents.SWIPE_DOWN;
import static com.genexus.android.core.controls.GxTouchEvents.SWIPE_LEFT;
import static com.genexus.android.core.controls.GxTouchEvents.SWIPE_RIGHT;
import static com.genexus.android.core.controls.GxTouchEvents.SWIPE_UP;
import static com.genexus.android.core.controls.GxTouchEvents.TAP;

/***
 * This class is on charge of handling all the standard touch events for controls.
 */
public class GxGestureListener
{
	private final Coordinator mCoordinator;

	// "Standard" events do not need to be handled with a TouchListener, since Android provides
	// events for them (which, additionally, manage the feedback). "Advanced" events need a
	// TouchListener (and manually firing feedback).
	private static final String[] STANDARD_EVENTS = new String[] { TAP, LONG_TAP };
	private static final String[] ADVANCED_TOUCH_EVENTS = new String[] { DOUBLE_TAP, SWIPE, SWIPE_LEFT, SWIPE_RIGHT, SWIPE_UP, SWIPE_DOWN, START_DRAG };

	private final int mSwipeThresholdVelocity;
	private final int mSwipeMinDistance;
	private final double mSwipeMaxOffPathFactor;

	public GxGestureListener(Context context, Coordinator coordinator)
	{
		mCoordinator = coordinator;

		ViewConfiguration vc = ViewConfiguration.get(context);
		mSwipeMinDistance = vc.getScaledTouchSlop();
		mSwipeThresholdVelocity = vc.getScaledMinimumFlingVelocity();
		mSwipeMaxOffPathFactor = 0.2; // Can veer at most 20% off path.
	}

	public void addView(View control)
	{
		if (mCoordinator.hasAnyEventHandler(control, GxTouchEvents.getAllEvents()))
		{
			// Defensive programming, we are avoiding to override touch listener for grids.
			// Additionally we don't override listener for controls that implement the flag interface ICustomTouchListener
			if (!IGridView.class.isInstance(control) && !ICustomTouchListener.class.isInstance(control))
				addTouchEventHandlers(control);
		}
	}

	private void addTouchEventHandlers(View control)
	{
		if (mCoordinator.hasAnyEventHandler(control, ADVANCED_TOUCH_EVENTS))
		{
			// At least one complex event. Handle with a View.OnTouchListener
			ViewTouchListener listener = new ViewTouchListener(control);
			control.setOnTouchListener(listener);
		}
		else if (mCoordinator.hasAnyEventHandler(control, STANDARD_EVENTS))
		{
			// Only standard events. Handle these with standard listeners.
			ViewStandardListener listener = new ViewStandardListener(control);

			if (mCoordinator.hasAnyEventHandler(control, new String[] { TAP }))
				control.setOnClickListener(listener);

			if (mCoordinator.hasAnyEventHandler(control, new String[] { LONG_TAP }))
				control.setOnLongClickListener(listener);
		}
	}

	private class ViewStandardListener implements View.OnClickListener, View.OnLongClickListener
	{
		private final View mView;

		private ViewStandardListener(View view)
		{
			mView = view;
		}

		@Override
		public void onClick(View v)
		{
			mCoordinator.runControlEvent(mView, TAP);
		}

		@Override
		public boolean onLongClick(View v)
		{
			mCoordinator.runControlEvent(mView, LONG_TAP);
			return true;
		}
	}

	/**
	 * OnTouchListener for each view.
	 */
	private class ViewTouchListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener
	{
		// Take into account that in this context the GestureDetector returning true in a function
		// means that the detector will continue processing events, so for example the OnDown method
		// must return true if we want to continue processing the following events. Any events start
		// with a OnDown followed of other events. Returning false in OnDown means that we stop
		// processing events and so the event is handled by the corresponding parent view. Another
		// issue is that swipe up/down are not working when a control is included in a scrollableview.

		private final View mView;
		private final GestureDetector mGestureDetector;
		private final boolean mShowFeedback;

		private ViewTouchListener(View view)
		{
			mView = view;

			if (!mCoordinator.hasAnyEventHandler(view, GxTouchEvents.getAllEvents()))
				throw new IllegalArgumentException("This view does not have events!");

			mGestureDetector = new GestureDetector(view.getContext(), this);
			String[] feedbackEvents = new String[]{TAP, LONG_TAP, DOUBLE_TAP};
			mShowFeedback = mCoordinator.hasAnyEventHandler(view, feedbackEvents);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			showTouchFeedback(v, event);
			return mGestureDetector.onTouchEvent(event);
		}

		private void showTouchFeedback(View view, MotionEvent event)
		{
			// Show feedback for UI interactions.
			// These are not automatically handled when an TouchListener is attached.
			if (mShowFeedback)
			{
				ViewTouchCompat touchCompat = ViewTouchCompat.get(view);
				touchCompat.onTouchEvent(event);
			}
		}

		@Override
		public boolean onDown(MotionEvent ev)
		{
			return true; // This view has at least one event handler.
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e)
		{
			return true; // This view has at least one event handler.
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			return true; // This view has at least one event handler.
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e)
		{
			return fireTouchEvent(TAP);
		}

		@Override
		public boolean onDoubleTap(MotionEvent ev)
		{
			return fireTouchEvent(DOUBLE_TAP);
		}

		@Override
		public void onLongPress(MotionEvent event)
		{
			if (mCoordinator.hasAnyEventHandler(mView, new String[] { START_DRAG }))
			{
				final Point p = new Point((int)event.getX(), (int)event.getY());
				mView.playSoundEffect(SoundEffectConstants.CLICK);

				ActionDefinition actionDef = mCoordinator.getControlEventHandler(mView, LONG_TAP);
		    	if (actionDef != null)
		    	{
			    	mCoordinator.runControlEvent(mView, LONG_TAP, new Runnable()
			    	{
						@Override
						public void run() { startDrag(mCoordinator, mView, p); }
					}, null);
		    	}
		    	else
		    		startDrag(mCoordinator, mView, p);
			}
			else
				fireTouchEvent(LONG_TAP);
		}

		private class DragInfo
		{
			private ClipData data;
			private boolean acceptDrag = true;
		}

		private DragInfo getDataToDrag(final Coordinator coordinator, final View control)
		{
			DragInfo info = new DragInfo();

			// The ClipData data type is taken from the first parameter type of the GeneXus Drag event associated to the control
			// Take the second parameter to check if the drag should be canceled or not.
			ActionDefinition action = coordinator.getControlEventHandler(control, START_DRAG);
			if (action.getEventParameters().size() > 0)
			{
				boolean acceptDrag = true;
				if (action.getEventParameters().size() > 1)
				{
					ActionParameter startDragParameter = action.getEventParameters().get(1);
					String acceptDragString = (String) coordinator.getValue(DataItemHelper.getNormalizedName(startDragParameter.getValue())) ;
					acceptDrag = (acceptDragString == null || acceptDragString.equalsIgnoreCase("true"));
				}

				if (acceptDrag)
				{
					ActionParameter parameter = action.getEventParameters().get(0);
					Object propertyValue = coordinator.getValue(DataItemHelper.getNormalizedName(parameter.getValue()));
					String valueText = "";
					if (propertyValue != null)
						valueText = propertyValue.toString();

					info.data = ClipData.newPlainText(DragHelper.getDragDropType(action), valueText);
				}
				else
					info.acceptDrag = false;
			}

			return info;
		}

		@SuppressWarnings("deprecation")
		public final void startDrag(final Coordinator coordinator, final View control, final Point initialPoint)
		{
			coordinator.runControlEvent(control, START_DRAG, null, new Runnable()
			{
				@Override
				public void run()
				{
				    Services.Device.runOnUiThread(new Runnable()
				    {
						@Override
						public void run()
						{
							IGxThemeable sourceControl = Cast.as(IGxThemeable.class, control);
							if (sourceControl != null)
							{
								ThemeClassDefinition classDef = sourceControl.getThemeClass();
								if (classDef != null)
								{
									ThemeClassDefinition startDragClasDef = classDef.getStartDragClass();
									if (startDragClasDef != null)
										sourceControl.applyClass(startDragClasDef);
								}
							}

							DragInfo data = getDataToDrag(coordinator, control);
							if (data.acceptDrag)
								control.startDrag(data.data, new GxShadowBuilder(control, initialPoint), new DragLocalState(control), 0);
						}
				    });
				}
			});
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			double deltaX = e2.getX() - e1.getX();
			double deltaY = e2.getY() - e1.getY();

			boolean isHorizontal = Math.abs(deltaX) > mSwipeMinDistance && Math.abs(velocityX) > mSwipeThresholdVelocity;
			boolean isVertical = Math.abs(deltaY) > mSwipeMinDistance && Math.abs(velocityY) > mSwipeThresholdVelocity;

			if (!isHorizontal && !isVertical)
				return false; // Didn't move enough in either direction. Not a swipe.

			if (isHorizontal && isVertical)
			{
				// Moved in diagonal, choose one (or none).
				// Can veer (at most) a specified factor from the wanted direction.
				if (Math.abs(deltaY / deltaX) > mSwipeMaxOffPathFactor)
					isHorizontal = false;

				if (Math.abs(deltaX / deltaY) > mSwipeMaxOffPathFactor)
					isVertical = false;
			}

			if (isHorizontal)
			{
				if (deltaX > 0)
					return swipe(SWIPE_RIGHT);
				else
					return swipe(SWIPE_LEFT);
			}
			else if (isVertical)
			{
				if (deltaY > 0)
					return swipe(SWIPE_DOWN);
				else
					return swipe(SWIPE_UP);
			}
			else
			{
				// It was a swipe, but a diagonal one. Fire 'Swipe'.
				return swipe(null);
			}
		}

		private boolean swipe(String direction)
		{
			// Specific, then generic swipe event.
			//noinspection SimplifiableIfStatement
			if (direction != null && mCoordinator.runControlEvent(mView, direction))
				return true;

			return mCoordinator.runControlEvent(mView, SWIPE);

		}

		private boolean fireTouchEvent(String eventName)
		{
			if (mCoordinator.hasAnyEventHandler(mView, new String[] { eventName }))
				mView.playSoundEffect(SoundEffectConstants.CLICK);

			return mCoordinator.runControlEvent(mView, eventName);
		}
	}
}
