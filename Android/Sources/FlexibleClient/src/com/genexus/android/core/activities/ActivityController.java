package com.genexus.android.core.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ActionFactory;
import com.genexus.android.core.actions.ActionParameters;
import com.genexus.android.core.actions.ActionResult;
import com.genexus.android.core.actions.CompositeAction;
import com.genexus.android.core.actions.ICustomActionRunner;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.analytics.Analytics;
import com.genexus.android.layout.OrientationLock;
import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.controls.IGxControlNotifyEvents;
import com.genexus.android.core.base.controls.IGxControlNotifyEvents.EventType;
import com.genexus.android.core.base.controls.IGxHandleActivityResult;
import com.genexus.android.core.base.controls.IGxHandleRequestPermissionsResult;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.Events;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityHelper;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.controllers.AutoRefreshManager;
import com.genexus.android.core.controllers.DataSourceController;
import com.genexus.android.core.controllers.DataViewController;
import com.genexus.android.core.controllers.IDataSourceController;
import com.genexus.android.core.controllers.IDataViewController;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.video.GxVideoView;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.LayoutFragmentActivityState;
import com.genexus.android.core.providers.EntityDataProvider;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.TaskRunner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityController
{
	// Model & View
	private final ActivityModel mModel;
	private final Activity mActivity;

	// An activity may display multiple data views at once. They are tracked here.
	// LinkedHashMap is used to preserve DV order.
	private final LinkedHashMap<IDataView, DataViewController> mControllers;
	private ActivityControllerState mRestoredState;

	// Current Context
	private boolean mIsRunning;
	private Refresh mPendingRefresh;
	private final AutoRefreshManager mAutoRefreshManager;
	private IGxHandleActivityResult mHandleOnActivityResult;
	private IGxHandleRequestPermissionsResult mHandleOnRequestPermissionsResult;
	private Action mActionWaitingForPermissionsResult;

	// Actions
	private final ActivityMenuManager mMenuManager;
	private final ActivityActionGroupManager mActionGroupManager;

	public ActivityController(Activity activity)
	{
		mActivity = activity;
		mModel = new ActivityModel();
		mControllers = new LinkedHashMap<>();

		mActionGroupManager = new ActivityActionGroupManager(activity);
		mMenuManager = new ActivityMenuManager(this, mActionGroupManager);

		mAutoRefreshManager = new AutoRefreshManager(this);
	}

	public static @Nullable ActivityController from(Activity activity)
	{
		if (activity instanceof IGxActivity)
			return ((IGxActivity)activity).getController();
		else
			return null;
	}

	public boolean initializeFrom(Intent intent)
	{
		Bundle intentData = intent.getExtras();
		return mModel.initializeFrom(mActivity, intentData);
	}

	public ActivityModel getModel() { return mModel; }

	public IDataViewController getController(IDataView dataView) {
		return mControllers.get(dataView);
	}

	public IDataViewController getController(UIContext context, IDataView dataView, ComponentId id, ComponentParameters params)
	{
		DataViewController controller = mControllers.get(dataView);
		if (controller == null)
		{
			// See if we can restore this controller from the previous state (e.g. before rotating).
			if (mRestoredState != null)
				controller = mRestoredState.restoreController(id, params, this, dataView);

			if (controller == null)
				controller = new DataViewController(this, id, mModel.createDataView(context, params), dataView);

			mControllers.put(dataView, controller);
			mActionGroupManager.addDataView(dataView);
		}

		return controller;
	}

	public void remove(IDataView dataView)
	{
		DataViewController controller = mControllers.get(dataView);
		if (controller != null)
		{
			controller.onPause();
			mAutoRefreshManager.removeAll(controller.getDataSources());

			notifyControlEvent(dataView.getUIContext(), EventType.ACTIVITY_PAUSED);
			notifyControlEvent(dataView.getUIContext(), EventType.ACTIVITY_DESTROYED);

			controller.onDestroy();
			mControllers.remove(dataView);

			mActionGroupManager.removeDataView(dataView);
		}
	}

	IDataSourceController getDataSource(int id)
	{
		// One of the controllers should have this data source.
		for (IDataViewController dataView : mControllers.values())
		{
			IDataSourceController dataSource = dataView.getDataSource(id);
			if (dataSource != null)
				return dataSource;
		}

		return null;
	}

	public void onResume()
	{
		// Notify controls that the activity has been resumed.
		notify(EventType.ACTIVITY_RESUMED);

		// Update search.
		Pair<IDataSourceController, String> searchInfo = SearchHelper.getCurrentSearch(this);
		if (searchInfo != null)
		{
			IDataSourceController dataSource = searchInfo.first;

			GxUri uri = dataSource.getModel().getUri();
			dataSource.getModel().setUri(uri.setSearch(searchInfo.second));
			onRefresh(dataSource, new RefreshParameters(RefreshParameters.Reason.SEARCH, false));
		}

		mIsRunning = true;
		List<DataViewController> controllersToResume = new ArrayList<>(mControllers.values());
		for (DataViewController controller : controllersToResume)
			controller.onResume();

		// If a refresh was asked when the activity was dormant, do it now.
		if (mPendingRefresh != null)
			mPendingRefresh.execute();

		mAutoRefreshManager.onResume();
	}

	private void afterPause()
	{
		mAutoRefreshManager.onPause();

		for (DataViewController controller : mControllers.values())
			controller.onPause();

		mIsRunning = false;
	}

	public void onPause()
	{
		notify(EventType.ACTIVITY_PAUSED);
		afterPause();
	}

	public void onStop() {
		notify(EventType.ACTIVITY_STOPPED);
	}

	public void onStart() {
		notify(EventType.ACTIVITY_STARTED);
	}

	public void onDestroy()
	{
		notify(EventType.ACTIVITY_DESTROYED);
		afterPause();

		mActionGroupManager.removeAll();
		mAutoRefreshManager.onDestroy();

		for (DataViewController controller : mControllers.values())
			controller.onDestroy();

		mControllers.clear();
	}

	public void notify(EventType event) {
		// Notify controls that the activity will change its state to @event
		for (IDataView dataView : getGxActivity().getActiveDataViews(false))
			notifyControlEvent(dataView.getUIContext(), event);
	}

	public boolean isRunning() { return mIsRunning; }

	/**
	 * Used to signal the ActivityController that a new data controller has been created.
	 */
	public void track(IDataSourceController controller)
	{
		mAutoRefreshManager.addDataSource(controller);
	}

	/**
	 * Refresh all data sources in the activity
	 * @param params Refresh parameters.
	 */
	public void onRefresh(RefreshParameters params)
	{
		onRefresh(new Refresh(params));
	}

	/**
	 * Refresh a data view in the activity (possibly including its children too).
	 * @param dataView Data view to refresh.
	 * @param params Refresh parameters.
	 * @param includeChildren Refresh child components as well as this one.
	 */
	public void onRefresh(IDataViewController dataView, RefreshParameters params, boolean includeChildren)
	{
		onRefresh(new Refresh(dataView, params, includeChildren));
	}

	/**
	 * Refresh a particular data source in the activity.
	 * @param dataSource Data source to refresh.
	 * @param params Refresh parameters.
	 */
	public void onRefresh(IDataSourceController dataSource, RefreshParameters params)
	{
		onRefresh(new Refresh(dataSource, params));
	}

	/**
	 * Executes a refresh operation; or saves it for later if the controller is currently disabled.
	 */
	private void onRefresh(Refresh refresh)
	{
		// Execute the refresh if controllers are enabled; save it as pending otherwise.
		if (mIsRunning)
			refresh.execute();
		else
			mPendingRefresh = refresh;
	}

	/**
	 * Class for a pending refresh operation.
	 */
	private class Refresh
	{
		/**
		 * Refresh of the whole activity.
		 */
		public Refresh(@NonNull RefreshParameters params)
		{
			this(null, null, params, true);
		}

		/**
		 * Refresh of a particular data view.
		 */
		public Refresh(@NonNull IDataViewController dataView, @NonNull RefreshParameters params, boolean includeChildren)
		{
			this(dataView, null, params, includeChildren);
		}

		/**
		 * Refresh of a particular data source.
		 */
		public Refresh(@NonNull IDataSourceController dataSource, @NonNull RefreshParameters params)
		{
			this(null, dataSource, params, false);
		}

		private Refresh(@Nullable IDataViewController dataView, @Nullable IDataSourceController dataSource, @NonNull RefreshParameters params, boolean includeChildren)
		{
			mDataView = dataView;
			mDataSource = dataSource;
			mIncludeChildren = includeChildren;
			mParams = params;
		}

		private final IDataViewController mDataView;
		private final IDataSourceController mDataSource;
		private final boolean mIncludeChildren;
		private final RefreshParameters mParams;

		public void execute()
		{
			mPendingRefresh = null;
			List<DataViewController> componentsToRefresh = new ArrayList<>();

			if (mDataSource != null && mDataSource instanceof DataSourceController)
			{
				// Specific refresh for a data source.
				((DataSourceController)mDataSource).onRefresh(mParams);
			}
			else if (mDataView != null && mDataView instanceof DataViewController)
			{
				// Specific refresh for a data view (possibly its children).
				componentsToRefresh.add((DataViewController)mDataView);
				if (mIncludeChildren)
				{
					for (DataViewController controller : mControllers.values())
						if (controller.getId().isDescendantOf(mDataView.getId()))
							componentsToRefresh.add(controller);
				}
			}
			else
			{
				// Generic refresh, all components in the activity.
				componentsToRefresh = new ArrayList<>(mControllers.values());
			}

			for (DataViewController controller : componentsToRefresh)
				controller.onRefresh(mParams);

			// Also notify controls that a refresh has occurred.
			for (IDataView dataView : getGxActivity().getActiveDataViews(false))
				notifyControlEvent(dataView.getUIContext(), EventType.REFRESH);
		}
	}

	public Activity getActivity() { return mActivity; }
	public IGxActivity getGxActivity() { return (IGxActivity)mActivity; }

	public void runAction(UIContext context, ActionDefinition action, Entity entity, final boolean allowDuplicate)
	{
		if (action == null)
			return;

		// Notify controls that wish to be invoked when an action is about to run.
		notifyControlEvent(context, EventType.ACTION_CALLED);

		// Analytics tracking.
		Analytics.onAction(context, action);

		// Execute action.
		runAction(context, action, prepareActionParameters(entity), allowDuplicate);
	}

	public void runCreatedAction(Action action, ActionDefinition actionDefinition) {
		// Analytics tracking.
		Analytics.onAction(action.getContext(), actionDefinition);

		// Execute action.
		new ActionExecution(action).executeAction();
	}

	private void notifyControlEvent(UIContext context, EventType event)
	{
		List<IGxControlNotifyEvents> notifiableControls = context.getViews(IGxControlNotifyEvents.class);
		for (IGxControlNotifyEvents control : notifiableControls)
			control.notifyEvent(event);
	}

	private void runAction(final UIContext context, final ActionDefinition action, final ActionParameters parameters, final boolean allowDuplicate)
	{
		// Ask the activity if it wants to handle this action in a special way.
		ICustomActionRunner customRunner = Cast.as(ICustomActionRunner.class, mActivity);
		if (customRunner != null && customRunner.runAction(action, parameters.getEntity()))
			return; // Already handled.

		Services.Log.debug(String.format("runAction Start executing %s", action.getName()));

		// if already running the same action , discard it.
		//  avoid duplicate events (double click).
		if (ActionExecution.isEventRunning(action, context, parameters)
			&& !allowDuplicate)   // allow run duplicate events if call allow that
		{
			Services.Log.warning("runAction Not execute event " + action.getName() + " because it is already running.");
			return;
		}

		// Lock activity orientation while action is running (released by ActionExecution.onEndEvent()).
		OrientationLock.lock(mActivity, OrientationLock.REASON_RUN_EVENT);

		CompositeAction compositeAction = ActionFactory.getAction(context, action, parameters, action.getIsComposite());
		if (!compositeAction.isEmpty()) {
			ActionExecution exec = new ActionExecution(compositeAction);
			Services.Log.debug(String.format("runAction Execute action %s", action.getName()));
			exec.executeAction();
		}
		else {
			Services.Log.debug(String.format("runAction Not execute empty action %s", action.getName()));
		}
	}

	private static ActionParameters prepareActionParameters(Entity from)
	{
		// Get the "TRUE" root entity for executing the action.
		// For a normal Entity (e.g. the Form entity, or an entity in a grid row with a DP) it's the same one.
		// For a "member" entity (e.g. an SDT variable or an SDT collection item) it's the first parent entity
		// that is not a member itself (i.e. one of the "normal" cases outlined above).
		Entity root = EntityHelper.forEvaluationCurrent(from);

		if (root != null)
			return new ActionParameters(root);
		else
			return new ActionParameters(from);
	}

	public boolean onSearchRequested()
	{
		mMenuManager.onSearchRequested();
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		mMenuManager.onCreateOptionsMenu(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		return mMenuManager.onOptionsItemSelected(item);
	}

	public IGxControl getControl(IDataView dataView, String controlName)
	{
		// Search in action bar and action groups (either the groups themselves or their controls).
		IGxControl groupControl = mActionGroupManager.getControl(dataView, controlName);
		if (groupControl != null)
			return groupControl;

		return null;
	}

	public boolean handleOnBackPressed()
	{
		// check for dataView actives to handle back event. Include data views with no layout.
		Iterable<IDataView> activeDataViews = getGxActivity().getActiveDataViews(true);

		return handleOnBackPressed(activeDataViews);
	}

	public boolean handleOnBackPressed(IDataView dataView)
	{
		ArrayList<IDataView> activeDataViews = new ArrayList<>();
		activeDataViews.add(dataView);
		return handleOnBackPressed(activeDataViews);
	}

	private boolean handleOnBackPressed(Iterable<IDataView> activeDataViews)
	{
		//noinspection SimplifiableIfStatement
		if (handleOnBackPressedAsRefresh(activeDataViews))
			return true;

		return handleOnBackPressedWithEvent(activeDataViews);

	}

	private boolean handleOnBackPressedAsRefresh(Iterable<IDataView> activeDataViews)
	{
		// Use back to remove filters/search (if present) instead of going back to previous activity.
		// Iterate over all ACTIVE data views, and prevent back if ANY of them had filters/search.
		boolean handledAsRefresh = false;
		for (IDataView dataView : activeDataViews)
		{
			IDataViewController dvController = mControllers.get(dataView);
			if (dvController != null)
			{
				boolean refreshDataView = false;
				for (IDataSourceController dsController : dvController.getDataSources())
				{
					GxUri dsUri = dsController.getModel().getUri();
					if (dsUri != null && (dsUri.resetFilter() || dsUri.resetSearch()))
					{
						dsController.getModel().setUri(dsUri);
						refreshDataView = true;
					}
				}

				if (refreshDataView)
					onRefresh(dvController, new RefreshParameters(RefreshParameters.Reason.RESET_SEARCH_OR_FILTER, false), false);

				handledAsRefresh |= refreshDataView;
			}
		}

		return handledAsRefresh;
	}

	private boolean handleOnBackPressedWithEvent(Iterable<IDataView> activeDataViews)
	{
		for (IDataView dataView : activeDataViews)
		{
			IViewDefinition definition = dataView.getDefinition();
			// check for dataView with definition and active to handle back event
			if (definition != null) {
				ActionDefinition backEvent = definition.getEvent(Events.BACK);
				if (backEvent != null)
				{
					dataView.runAction(backEvent, null);
					return true; // Runs only the FIRST Back event in case there are multiple ones.
				}
			}
		}

		return false; // No Back event found.
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		boolean avoidRefresh = false;

		// See if a particular culprit started this activity, and let it handle the result.
		if (mHandleOnActivityResult != null && mHandleOnActivityResult.handleOnActivityResult(requestCode, resultCode, data))
		{
			mHandleOnActivityResult = null;
			return;
		}

		if (resultCode == Activity.RESULT_OK || requestCode == RequestCodes.ACTION_ALWAYS_SUCCESSFUL)
		{
			if (requestCode == RequestCodes.PICKER)
			{
				//TODO: this need to be changed and use other method to get the sPickingElementId
				String pickingElementId = GxBaseActivity.sPickingElementId;
				for (IDataView dataView : mControllers.keySet())
				{
					List<IGxEdit> list = dataView.getUIContext().findControlsBoundTo(pickingElementId);
					if (list.size() >= 1)
						list.get(0).setValueFromIntent(data);
				}
			}

			if (requestCode == RequestCodes.FILTERS && data != null)
			{
				processFilterRequest(data);
				return;
			}

			if (ActivityHelper.isActionRequest(requestCode))
			{
				// An action continuation
				ActionResult actionResult = ActionExecution.continueCurrentFromActivityResult(mActivity, requestCode, resultCode, data);
				avoidRefresh = (actionResult == ActionResult.SUCCESS_CONTINUE_NO_REFRESH);
			}
			else if (requestCode == RequestCodes.PREFERENCE)
			{
				String serverUrl = data.getStringExtra(IntentParameters.SERVER_URL);
				if (serverUrl != null) {
					startAppNewUrl(serverUrl);
				}
			}
		}
		else if (requestCode == Activity.RESULT_FIRST_USER) {
			// Start of user-defined activity results.
			for (IDataView dataView : getGxActivity().getActiveDataViews(false))
			{
				List<GxVideoView> videoControls = dataView.getUIContext().getViews(GxVideoView.class);
				for (GxVideoView videoControl : videoControls)
				{
					videoControl.retryYoutubeInitialization();
				}
			}
		}
		else
		{
			// Not Activity.RESULT_OK, so perform tasks related to errors.
			if ( ActivityHelper.isActionRequest(requestCode) )
			{
				// special case where return to continue event from caller
				if (resultCode == ActivityFlowControl.RETURN_TO_FIRST)
				{
					// continue pending long running action
					Services.Log.debug("return to . Continue pending current");
					ActionExecution.continueCurrentFromActivityResultOnlyLeavingActivityOrCleanCurrent(mActivity, requestCode, resultCode, data);

				}
				else if (resultCode == ActivityFlowControl.RETURN_TO_OTHERS)
				{
					// do nothing with actions.
					Services.Log.debug("return to others");
					// Clean pending actions in the stack for evary return more.
					ActionExecution.cleanLastPendingActionFromActivityResult(requestCode, resultCode, data, mActivity);
				}
				else { // default case
					// Clean pending actions if one of them failed.
					CompositeAction compositeAction = ActionExecution.getCurrentCompositeAction();
					if (compositeAction != null && !compositeAction.getIsComposite()) {
						compositeAction.setErrorVariables(Action.ERROR_CODE_USER_CANCEL, ""); // empty message because no message is shown when composite is used either
						ActionExecution.continueCurrentFromActivityResult(mActivity, requestCode, resultCode, data);
					}
					else {
						ActionExecution.cleanCurrentOrLastPendingActionFromActivityResult(requestCode, resultCode, data, mActivity);
					}
				}
			}
			else if (requestCode == RequestCodes.LOGIN)
			{
				if (resultCode == ActivityFlowControl.RETURN_TO_FIRST || resultCode == ActivityFlowControl.RETURN_TO_OTHERS
						|| resultCode == ActivityFlowControl.RETURN_TO_CANCEL	)
					return;

				// finish ww?
				mActivity.finish();
				return;
			}
		}

		// Returning from any other activity - refresh if necessary
		// (UNLESS returning from picker - refreshing would lose inserted data).
		if (!avoidRefresh && requestCode != RequestCodes.PICKER && requestCode != RequestCodes.ACTIONNOREFRESH)
			onRefresh(RefreshParameters.IMPLICIT);
	}

	private void startAppNewUrl(String serverUrl) {
		TaskRunner.execute(new TaskRunner.BaseTask<Void>() {
			@Override
			public Void doInBackground()
			{
				//Background work here
				if (Services.Application.get().isSecure()) {
					SecurityHelper.logout();
				} else {
					EntityDataProvider.clearAllCaches();
				}
				return null;
			}

			@Override
			public void onPostExecute(Void result)
			{
				//UI Thread work here
				Intent intent = IntentFactory.getStartupActivityWithNewServicesURL(getActivity(), serverUrl);
				getActivity().startActivity(intent);
			}
		});

	}

	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		// See if a particular culprit asked for this permission, and let it handle the result.
		if (mHandleOnRequestPermissionsResult != null && mHandleOnRequestPermissionsResult.handleOnRequestPermissionsResult(requestCode, permissions, grantResults))
		{
			mHandleOnRequestPermissionsResult = null;
			return;
		}

		if (ActivityHelper.isActionRequest(requestCode))
		{
			ActionExecution.continueCurrentFromRequestPermissionsResult(mActivity, requestCode, permissions, grantResults, mActionWaitingForPermissionsResult);
			mActionWaitingForPermissionsResult = null;
		}
	}

	private void processFilterRequest(Intent data)
	{
		// Replace the Uri of the data source with the one with filter information.
		IDataSourceController dataSource = getDataSource(data.getIntExtra(IntentParameters.Filters.DATA_SOURCE_ID, 0));
		GxUri filterUri = IntentHelper.getObject(data, IntentParameters.Filters.URI, GxUri.class);

		if (dataSource != null && filterUri != null)
		{
			// Update URI...
			dataSource.getModel().setUri(filterUri);
			dataSource.getModel().setFilterExtraInfo(data.getStringExtra(IntentParameters.Filters.FILTERS_FK));

			// ... and fire data load.
			onRefresh(dataSource, new RefreshParameters(RefreshParameters.Reason.FILTER, false));
		}
	}

	private static final String STATE_CONTROLLER_STATE = "ActivityController::DataViewControllers";

	public void saveState(LayoutFragmentActivityState state)
	{
		ActivityControllerState controllerState = new ActivityControllerState();
		controllerState.save(mControllers);
		state.setProperty(STATE_CONTROLLER_STATE, controllerState);
	}

	public void restoreState(LayoutFragmentActivityState state)
	{
		if (state != null)
			mRestoredState = (ActivityControllerState)state.getProperty(STATE_CONTROLLER_STATE);
	}

	public void setActivityResultHandler(IGxHandleActivityResult handler)
	{
		mHandleOnActivityResult = handler;
	}

	public void setRequestPermissionsResultHandler(IGxHandleRequestPermissionsResult handler)
	{
		mHandleOnRequestPermissionsResult = handler;
	}

	public void setActionWaitingForPermissionsResult(Action action)
	{
		mActionWaitingForPermissionsResult = action;
	}
}
