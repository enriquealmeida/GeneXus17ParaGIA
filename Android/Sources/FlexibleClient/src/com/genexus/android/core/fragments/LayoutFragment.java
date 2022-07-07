package com.genexus.android.core.fragments;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.genexus.android.R;
import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ActionParametersHelper;
import com.genexus.android.core.actions.CompositeAction;
import com.genexus.android.core.actions.DynamicCallAction;
import com.genexus.android.core.actions.ICustomMenuManager;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.actions.WorkWithAction;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.activities.IntentParameters;
import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.ViewHierarchyVisitor;
import com.genexus.android.analytics.Analytics;
import com.genexus.android.api.EventDispatcher;
import com.genexus.android.layout.ControlProperties;
import com.genexus.android.layout.GridsLayoutVisitor;
import com.genexus.android.layout.GxLayout.LayoutParams;
import com.genexus.android.layout.IGxRootLayout;
import com.genexus.android.layout.LayoutControlFactory;
import com.genexus.android.layout.OrientationLock;
import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.app.ComponentUISettings;
import com.genexus.android.core.base.controls.IGxControlPreserveState;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.DataViewDefinition;
import com.genexus.android.core.base.metadata.DynamicCallDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.metadata.SectionDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.LayoutModes;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.layout.ComponentDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ListUtils;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.controllers.IDataSourceBoundView;
import com.genexus.android.core.controllers.IDataSourceController;
import com.genexus.android.core.controllers.IDataViewController;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.LoadingIndicatorView;
import com.genexus.android.core.ui.FormCoordinator;
import com.genexus.android.core.utils.Cast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static android.view.View.NO_ID;

public class LayoutFragment extends BaseFragment implements IDataView, IDataSourceBoundView, ICustomMenuManager
{
	// Definition & plumbing
	private IDataViewHost mHost;
	private IDataView mParent;
	private LayoutDefinition mLayout;
	private IDataViewController mController;
	private IDataViewDefinition mDefinition;
	private short mMode;
	private String mUri;
	private boolean mCanHaveScroll = true;
	private boolean mHaveScroll = false;

	private View mRootCellView = null;

	// Layout helpers and controls.
	private LayoutFragmentAdapter mAdapter;
	private LoadingIndicatorView mLoadingIndicator;

	private GxLinearLayout mContentViewMargin;
	private IGxRootLayout mContentView;
	private View mContentRoot;
	private boolean mUseMarginView = false;

	// Status
	private boolean mIsActive;
	private boolean mIsViewCreated;
	private boolean mIsLayoutExpandedAndDataLoaded;
	private Entity mCurrentEntity;
	private ControlProperties mRuntimeProperties;
	private boolean mHasDataArrived;
	private ViewData mPendingUpdate;
	private LayoutFragmentState mPendingRestoreLayoutState;
	private final SecurityHelper.Token mSecurityToken;
	private Connectivity mConnectivity;
	private boolean mDialogDismissed;

	// Events Handling
	public static final String GENEXUS_EVENTS = "GxEvents";
	private IntentFilter mEventsFilter = new IntentFilter(GENEXUS_EVENTS);

	public LayoutFragment()
	{
		// TODO: For now, cannot enable setRetainInstance (fragment needs to be recreated).
		// setRetainInstance(true);
		mSecurityToken = new SecurityHelper.Token();
		mRuntimeProperties = new ControlProperties();
	}

	public void initialize(Connectivity connectivity, IDataViewHost host, IDataView parent, IDataViewController controller)
	{
		mConnectivity = Connectivity.getConnectivitySupport(connectivity, controller.getDefinition().getConnectivitySupport());
		mHost = host;
		mParent = parent;
		mController = controller;
		mDefinition = controller.getDefinition();
		mMode = controller.getComponentParams().Mode;
		mUri = controller.getComponentParams().toString();

		if (mDefinition != null)
		{
			mLayout = mDefinition.getLayoutForMode(mMode);
			if (mDefinition instanceof SectionDefinition)
			{
				//just for calculate fk. should call to match relation directly.
				((SectionDefinition)mDefinition).getVisibleItems(LayoutDefinition.TYPE_VIEW);
			}
		}
	}


	// Our handler for received Intents. This will be called whenever an Intent
	// with an action from GeneXus is broadcasted.
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String actionName = intent.getExtras().getString(EventDispatcher.ACTION_NAME);
				 for (ActionDefinition adef : mDefinition.getActions()) {
					if (adef.getName().equalsIgnoreCase(actionName))	{
						Entity e = getContextEntity();
						int i = 0;
						for (ActionParameter par : adef.getEventParameters()) {
							e.setProperty(par.getValue(), intent.getExtras().getString(String.valueOf(i)));
							i++;
						}
						// action for broadcast, allow run duplicate.
						// they are called from EventDispatcher.
						runAction(adef, null, true);
					 }
				}
			}
		}
	};

    @Override
	public void onPause()
	{
		// Unregister to receive events from GeneXus
		LocalBroadcastManager.getInstance(getUIContext()).unregisterReceiver(mMessageReceiver);
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// Register to receive events from GeneXus
		LocalBroadcastManager.getInstance(getUIContext()).registerReceiver(mMessageReceiver, mEventsFilter);
	}

	@Override
	public @NonNull Dialog onCreateDialog(Bundle savedInstanceState)
    {
    	// Use a custom Dialog subclass to intercept onBackPressed()
        return new LayoutFragmentDialog(this);
    }

	@Override
	public void onStart()
	{
		super.onStart();
		if (mController != null)
			mController.onFragmentStart(this);

		Analytics.onComponentStart(getActivity(), mDefinition);
	}

	@Override
	public @NonNull String getUri()
	{
		if (mUri == null)
			throw new IllegalStateException("Cannot call this method before initialize()");

		return mUri;
	}

	@Override
	public IViewDefinition getDefinition()
	{
		return mDefinition;
	}

	@Override
	public short getMode()
	{
		return mMode;
	}

	protected short getLayoutMode()
	{
		return LayoutModes.VIEW;
	}

	@Override
	public IDataViewController getController()
	{
		return mController;
	}

	@Override
	public UIContext getUIContext()
	{
		return new UIContext(getActivity(), this, getContentView(), mConnectivity);
	}

	IGxActivity getGxActivity()
	{
		return Cast.as(IGxActivity.class, getActivity());
	}

	public @NonNull ControlProperties getRuntimeProperties()
	{
		// TODO: Stopgap. This should be stored in Entity
		return mRuntimeProperties;
	}

	protected LayoutFragmentAdapter getAdapter()
	{
		return mAdapter;
	}

	public FormCoordinator getFormCoordinator()
	{
		if (mAdapter!=null)
		{
			return mAdapter.getCoordinator();
		}
		return null;
	}

	private static final String CONTROL_STATE = "ControlState::";
	private static final String STATE_RUNTIME_PROPERTIES = "RuntimeControlProperties";

	@Override
	public void saveFragmentState(LayoutFragmentState state)
	{
		for (IGxControlPreserveState control : ViewHierarchyVisitor.getViews(IGxControlPreserveState.class, getContentView()))
		{
			Map<String, Object> controlState = new HashMap<>();
			control.saveState(controlState);

			if (controlState.size() != 0)
				state.setProperty(CONTROL_STATE + control.getControlId(), controlState);
		}

		state.setProperty(STATE_RUNTIME_PROPERTIES, mRuntimeProperties);
	}

	@Override
	public void restoreFragmentState(LayoutFragmentState state)
	{
		if (state == null)
			return;

		if (mIsLayoutExpandedAndDataLoaded)
		{
			// Restore layout now.
			restoreLayoutState(state);
		}
		else
		{
			// Keep state in local variable so we can restore layout properties when layout is expanded.
			mPendingRestoreLayoutState = state;
		}
	}

	private void restoreLayoutState(LayoutFragmentState state)
	{
		// Reapply runtime control properties from previous state.
		ControlProperties runtimeProperties = (ControlProperties)state.getProperty(STATE_RUNTIME_PROPERTIES);
		if (runtimeProperties != null)
		{
			mRuntimeProperties = runtimeProperties;
			mRuntimeProperties.apply(getUIContext());
		}

		// Reapply custom control state.
		for (IGxControlPreserveState control : ViewHierarchyVisitor.getViews(IGxControlPreserveState.class, getContentView()))
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> controlState = (Map<String, Object>) state.getProperty(CONTROL_STATE + control.getControlId());
			if (controlState != null)
				control.restoreState(controlState);
		}
	}

	public void setCanHaveScroll(boolean value)
	{
		mCanHaveScroll = value;
	}

	@Override
	public boolean isActive()
	{
		//noinspection SimplifiableIfStatement
		if (mParent != null && !mParent.isActive())
			return false;

		return mIsActive;
	}

	@Override
	public void setActive(boolean value)
	{
		if (mIsActive != value)
		{
			mIsActive = value;
			setHasOptionsMenu(value);
		}
	}

	@Override
	public boolean isDataReady()
	{
		return mHasDataArrived;
	}

	@Override
	public void refreshData(RefreshParameters params)
	{
		if (mController != null)
			mController.getParent().onRefresh(mController, params, true);
	}

	@Override
	public LayoutDefinition getLayout()
	{
		return mLayout;
	}

	@Override
	public Entity getContextEntity()
	{
		if (mCurrentEntity != null)
		{
			if (mAdapter != null)
				mAdapter.controlstoData(mCurrentEntity);
		}
		else
		{
			StructureDefinition structure = StructureDefinition.EMPTY;
			if (mDefinition != null && mDefinition.getMainDataSource() != null)
				structure = mDefinition.getMainDataSource().getStructure();

			mCurrentEntity = EntityFactory.newEntity(structure);
			mCurrentEntity.setExtraMembers(mDefinition.getVariables());
		}
		return mCurrentEntity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mController == null)
			return new View(inflater.getContext()); // This is necessary to prevent a crash with the child FragmentManager

		// Expand the layout
		mHaveScroll = mCanHaveScroll && mLayout != null && !GridsLayoutVisitor.hasAnyScrollable(mLayout);
		int layoutResourceId = mHaveScroll ? R.layout.layoutfragmentscroll :R.layout.layoutfragment;

		View layoutHolder = inflater.inflate(layoutResourceId, container, false);

		// Hack to avoid flicker when a YouTube player is present, as suggested in https://code.google.com/p/gdata-issues/issues/detail?id=4722
		if (Strings.hasValue(Services.Strings.getResource(R.string.GoogleServicesApiKey))) {
			SurfaceView surfaceView = new SurfaceView(inflater.getContext());
			surfaceView.setLayoutParams(new LayoutParams(0, 0, 0, 0));
			surfaceView.setVisibility(View.GONE);
			((ViewGroup) layoutHolder).addView(surfaceView);
		}

		NestedScrollView mainScrollView = layoutHolder.findViewById(R.id.scrollViewLayoutContentScroll);

		mContentView = LayoutControlFactory.createRootView(inflater.getContext(), mLayout, null);

		if (mHaveScroll) {
			NestedScrollView.LayoutParams params = new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			((View)mContentView).setLayoutParams(params);
			mainScrollView.addView((View) mContentView);
		}
		else {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
			((View)mContentView).setLayoutParams(params);
			((ViewGroup) layoutHolder).addView((View) mContentView);
		}

      	mLoadingIndicator = layoutHolder.findViewById(R.id.loadingIndicatorView);

		// initialize loading indicator. mLayout could be null?
		if (mLayout!=null && mLayout.getThemeClass()!=null)
			mLoadingIndicator.setThemeClass(mLayout.getThemeClass().getThemeAnimationClass());

    	mAdapter = new LayoutFragmentAdapter(this, getGxActivity(), mLayout, getLayoutMode(), mMode);

		// Add scroll listener
		// not do that in slide menu drawer fragment.
		if (this.isAllowHeaderRow() && mHaveScroll && mLayout.getEnableHeaderRowPattern())
		{
			mainScrollView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);

			if (getActivity()!=null && mLayout!=null)
			{
				// use header row theme, transparent and elevation=0
				ActivityHelper.setActionBarHeaderRowToTransparentMode(getActivity(), mLayout, false);
			}
		}

    	// Change the Content to GxLinearLayout if necessary
      	if (mAdapter.getContentHasMargin())
      	{
      		ViewGroup parent = (ViewGroup) ((ViewGroup)mContentView).getParent();
      		parent.removeView((ViewGroup)mContentView);
      		mContentViewMargin = new GxLinearLayout(getActivity());
      		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

      		parent.addView(mContentViewMargin, params);
      		ViewGroup.LayoutParams paramMargin = mContentViewMargin.getLayoutParams();
      		if (paramMargin instanceof LinearLayout.LayoutParams)
      			((LinearLayout.LayoutParams) paramMargin).weight = 1;

      		// Place the original GxRootLayout inside this wrapper.
      		mContentViewMargin.addView((ViewGroup)mContentView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

      		mUseMarginView = true;
      	}

      	mContentRoot = mainScrollView;
      	if (mContentRoot == null)
      		mContentRoot = getContentView();

		changeContentVisible(View.GONE);

		// draw layout controllers
		drawLayout();

		// attach main data controller
		setMainDataController();

		// update fragment data from pending update
		mIsViewCreated = true;
		if (mPendingUpdate != null)
		{
			update(mPendingUpdate);
			mPendingUpdate = null;
		}

		// attach children data controllers, do it after update(mPendingUpdate) to preserve data loading order.
		setChildrenDataControllers();
		// mark layout and data is done
		mIsLayoutExpandedAndDataLoaded = true;

		// Important: do this AFTER update(mPendingUpdate).
		//  Otherwise runtime properties set in start would overwrite those applied via events later.
		if (mPendingRestoreLayoutState != null)
		{
			restoreLayoutState(mPendingRestoreLayoutState);
			mPendingRestoreLayoutState = null;
		}

		return layoutHolder;
	}

	private void changeContentVisible(int visible)
	{
		mContentRoot.setVisibility(visible);
	}

	private View getContentView()
	{
		if (mUseMarginView)
			return mContentViewMargin;
		else
			return (View)mContentView;
	}

	private void drawLayout()
	{
		// do not do it again if already done expandLayout.
		if (mIsLayoutExpandedAndDataLoaded)
			return;

		Size desiredSize = getDesiredSize();
		if (desiredSize == null)
			desiredSize = AdaptersHelper.getWindowSize(getActivity(), mHost.getMainLayout());

		mAdapter.expandLayout(mContentView, desiredSize);

	}

	private void setMainDataController()
	{
		// do not do it again if already done attachDataController.
		if (mIsLayoutExpandedAndDataLoaded)
			return;

		// Fire data loading, only main controller.
		mController.attachDataController(this);

	}

	private void setChildrenDataControllers()
	{
		// do not do it again if already done attachDataController.
		if (mIsLayoutExpandedAndDataLoaded)
			return;

		// Fire data loading, children data controllers.
		for (IDataSourceBoundView boundView : mAdapter.getDataSourceBoundViews())
			if (!this.equals(boundView))
				mController.attachDataController(boundView);
	}

	@Override
	public void setController(IDataSourceController controller)
	{
		// No need to keep track of controller for this view.
		// mMainDataController = controller;
	}

	@Override
	public String getDataSourceId()
	{
		IDataSourceDefinition dataSource = getDataSource();
		if (dataSource != null)
			return dataSource.getName();
		else if (mDefinition != null)
			return mDefinition.getName();
		else
			return Strings.EMPTY;
	}

	@Override
	public IDataSourceDefinition getDataSource()
	{
		return (mLayout != null ? mLayout.getDataSource() : null);
	}

	@Override
	public String getDataSourceMember()
	{
		return null; // Whole layout is not associated to a member.
	}

	@Override
	public int getDataSourceRowsPerPage()
	{
		return 0; // Data source does not have paging.
	}

	// IDataBoundView implementation.

	@Override
	public void onBeforeRefresh(RefreshParameters params)
	{
		// We do nothing, for the moment.
	}

	@Override
	public void update(ViewData data)
	{
		if (data.getDataUnchanged())
		{
			// should hide loading indicator if exists.
			return;
		}

		if (!mIsViewCreated)
		{
			// In case it's called before the view has been created.
			mPendingUpdate = data;
			return;
		}

		if (getActivity() != null && getActivity().isDestroyed())
			return; // Discard result for activity that is being torn down.

		// Redirect to login on authentication error.
		if (SecurityHelper.handleSecurityError(getUIContext(), data.getStatusCode(), data.getStatusMessage(), mSecurityToken) != SecurityHelper.Handled.NOT_HANDLED)
			return;

		// Show error if we have an error message.
		if (Services.Strings.hasValue(data.getStatusMessage()))
			mLoadingIndicator.setText(data.getStatusMessage());

		if (data.getStatusCode() == DataRequest.ERROR_SECURITY_AUTHORIZATION)
			changeContentVisible(View.GONE); // Hide any previous data when authorization fails (e.g. refresh after changing permissions).

		if (data.getSingleEntity() != null)
		{
			mCurrentEntity = data.getSingleEntity();
			mHasDataArrived = true;

			// Handle Calls on Start Event
			if (redirect(getUIContext(), mCurrentEntity))
				return;

			// need to be before mAdapter.drawData since it can be a Component.Object assignation in the start event
			createComponents(); // if tab with component has ActivePage, will works as PendingAttach

			beforeDrawData(data);
			mAdapter.drawData(data);

			// Since content changed visibility, refresh menu options.
			SherlockHelper.invalidateOptionsMenu(getActivity());
		}
	}

	// Handle Calls on Start Event
	private static boolean redirect(UIContext context, Entity data)
	{
		List<DynamicCallDefinition> calls = DynamicCallDefinition.from(data);
		if (calls.size() > 0)
		{
			boolean isComposite = true; // for compatibility
			CompositeAction actions = new CompositeAction(context, null, null, isComposite);
			for (DynamicCallDefinition call : calls)
			{
				DynamicCallAction action;
				try {
					action = DynamicCallAction.redirect(context, data, call.getCallString(), isComposite);
				}
				catch (IllegalArgumentException ex) // unknown action in redirect.
				{
					// ignore Gxdyncall that cannot be parsed
					Services.Log.error("Cannot parse redirect from server, ignore Dyncall: "+ call.getCallString());
					return false;
				}
				actions.addAction(action);
			}

			ActionExecution exec = new ActionExecution(actions);
			exec.executeAction(); // Will also finish() the current activity.
			return true;

		}
		else
			return false;
	}

	@SuppressWarnings("UnusedParameters")
	private void beforeDrawData(ViewData data)
	{
		mLoadingIndicator.setVisibility(View.GONE);
		// release loading View
		mLoadingIndicator.releaseLoadingView();

		changeContentVisible(View.VISIBLE);
	}

	private void createComponents()
	{
		// For each ComponentContainer that has a design-time component, create it.
		for (ComponentContainer container : mAdapter.getComponentContainers())
		{
			if (container.getStatus() == ComponentContainer.INACTIVE)
			{
				container.setParentFragment(this);

				if (!container.hasDirectTabParent())
					container.setStatus(ComponentContainer.TOACTIVATED);
			}
		}

		attachContentContainers();
	}

	public void attachContentContainers()
	{
		if (getActivity() != null && getActivity().isDestroyed())
			return; // Trying to manipulate fragments in this state will lead to a crash.

		// Attach Content containers a.k.a. inline sections.
		for (ComponentContainer container : mAdapter.getComponentContainers())
		{
			if (container.getStatus() == ComponentContainer.TOACTIVATED)
			{
				if (!isAdded()) {
					Services.Log.warning("Fragment has not been attached yet");
					continue;
				}

				// Activate or create fragment
				FragmentManager fragmentManager = getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				if (container.getFragment() == null)
				{
					//Create fragment.
					container.setId(container.getContentControlId());

					//Create only if ContentContainer exist in activity.
					//View contentView = this.getActivity().findViewById(content.getContentControlId());
					boolean isConected = !container.hasTabParentDisconected();

					if (isConected || container.getPendingAttach())
					{
						BaseFragment innerFragment = createInnerComponent(container);
						if (innerFragment != null)
						{
							if ((innerFragment instanceof LayoutFragment) && (mHaveScroll || container.hasTabParentWithScroll()))
								((LayoutFragment)innerFragment).setCanHaveScroll(false);

							fragmentTransaction.add(container.getId(), innerFragment);
							container.setFragment(innerFragment);
						}
						if (container.getPendingAttach())
						{
							container.setPendingAttach(false);
						}
					}
					else
					{
						Services.Log.warning("Not activating ComponentContainer because is not visible id: " + container.getContentControlId());
					}
				}

				if (container.getFragment() != null)
				{
					fragmentTransaction.commitAllowingStateLoss();
					container.setActive(true);
				}
			}

			if (container.getStatus() == ComponentContainer.TOINACTIVATED)
				container.setActive(false);
		}
	}

	private BaseFragment createInnerComponent(ComponentContainer container)
	{
		if (container.getFragment() != null)
			throw new IllegalStateException("ComponentContainer already has a Fragment.");

		ComponentDefinition definition = container.getDefinition();
		if (definition != null && definition.getObject() != null)
		{
			// Build component according to its container's definition.
			ComponentId innerId = new ComponentId(mController.getId(), definition.getName());
			ComponentParameters innerParams = new ComponentParameters(definition.getObject(), mMode, getInnerDataViewParameters(definition), mController.getComponentParams().NamedParameters);
			ComponentUISettings innerSettings = ComponentUISettings.childOf(this, container.getComponentSize());

			return mHost.createComponent(innerId, innerParams, innerSettings, null);
		}
		else
			return null;
	}

	public void replaceInnerComponent(ComponentContainer container, String dynamicCall)
	{
		if (container.getFragment() != null)
			removeInnerComponent(container);

		ComponentDefinition definition = container.getDefinition();
		if (Services.Strings.hasValue(dynamicCall) && definition != null)
		{
			ActionDefinition actionDefinition = DynamicCallAction.parse(getUIContext(), dynamicCall);
			String component = actionDefinition.optStringProperty("@instanceComponent");
			IViewDefinition object = null;
			if (Strings.hasValue(component)) // get object using full component name.
				object = Services.Application.getDefinition().getView(actionDefinition.getGxObject()+ Strings.DOT + component);
			if (object==null)
				object = Services.Application.getDefinition().getView(actionDefinition.getGxObject());

			if (object != null) {
				List<String> parameters = ActionParametersHelper.getParametersForDataView(actionDefinition.getParameters(), mCurrentEntity);
				fixDateParameters(parameters, object);
				ComponentId innerId = new ComponentId(mController.getId(), definition.getName());
				ComponentParameters innerParams = new ComponentParameters(object, mMode, parameters, null);
				ComponentUISettings innerSettings = ComponentUISettings.childOf(this, container.getComponentSize());

				BaseFragment fragment = mHost.createComponent(innerId, innerParams, innerSettings, null);
				if (fragment != null)
				{
					// new fragment create it
					if (container.getFragment() == null && container.getId() == NO_ID)
					{
						Services.Log.debug("replace dynamic component fragment in a non inicialize container");

						// Activate or create fragment
						FragmentManager fragmentManager = getChildFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

						if (container.getPendingAttach())
						{
							container.setPendingAttach(false);
						}
						//Create fragment.
						container.setId(container.getContentControlId());

						if ((fragment instanceof LayoutFragment) && (mHaveScroll || container.hasTabParentWithScroll()))
							((LayoutFragment)fragment).setCanHaveScroll(false);

						fragmentTransaction.add(container.getId(), fragment);
						container.setFragment(fragment);

						fragmentTransaction.commitAllowingStateLoss();
						// not needed?
						//container.setActive(true);

					}
					else
					{
						Services.Log.debug("replace dynamic component fragment in container");
						// normal replace.
						getChildFragmentManager().beginTransaction().add(container.getId(), fragment).commit();
						container.setFragment(fragment);
					}
				}
			}
		}
	}

	private void fixDateParameters(List<String> parameters, IViewDefinition object) {
		DataViewDefinition dvd = Cast.as(DataViewDefinition.class, object);
		if (dvd != null) {
			List<ObjectParameterDefinition> parametersList = dvd.getParameters();
			for (int n = 0; n < parameters.size() && n < parametersList.size(); n++) {
				String dataType = parametersList.get(n).getDataTypeName().getDataType();
				if (DataTypes.isDateTime(dataType)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					Date d = df.parse(parameters.get(n), new ParsePosition(0));
					if (d != null) {
						String newValue;
						if (dataType.equalsIgnoreCase(DataTypes.DATE))
							newValue = Services.Strings.getDateStringForServer(d);
						else {
							boolean isOnlyTime = DataTypes.isTime(dataType, parametersList.get(n).getLength());
							boolean hasMillis = parametersList.get(n).getDecimals() == 12;
							newValue = Services.Strings.getDateTimeStringForServer(d, isOnlyTime, hasMillis);
						}

						parameters.set(n, newValue);
					}
				}
			}
		}
	}

	public void removeInnerComponent(ComponentContainer container)
	{
		if (container.getFragment() == null)
			throw new IllegalStateException("ComponentContainer doesn't have a Fragment.");

		// Detach and destroy the inner fragment.
		getChildFragmentManager().beginTransaction().remove(container.getFragment()).commit();
		mHost.destroyComponent(container.getFragment());
		container.setFragment(null);
	}

	public List<String> getInnerDataViewParameters(ComponentDefinition definition)
	{
		// Default is that inner DV parameters are the same as outer DV parameters.
		List<String> parameters = mController.getComponentParams().Parameters;
		if (definition.getParameters().size() != 0 && mCurrentEntity != null) {
			try {
				parameters = ActionParametersHelper.getParametersForDataView(definition.getParameters(), mCurrentEntity);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Illegal Argument in Panel " + getUri(), e);
			}
		}

		return parameters;
	}

	@Override
	public boolean needsMoreData()
	{
		return !mHasDataArrived;
	}

	@Override
	public List<BaseFragment> getChildFragments()
	{
		ArrayList<BaseFragment> list = new ArrayList<>();
		if (mAdapter != null)
		{
			for (ComponentContainer container : mAdapter.getComponentContainers())
			{
				BaseFragment child = container.getFragment();
				if (child != null)
				{
					list.add(child);
					list.addAll(child.getChildFragments());
				}
			}
		}

		return list;
	}

	@Override
	public void onCustomCreateOptionsMenu(Menu menu)
	{
		if (mAdapter != null)
		{
			for (ICustomMenuManager customMenuManager : mAdapter.getCustomMenuManagers())
				customMenuManager.onCustomCreateOptionsMenu(menu);
		}
	}

	public void setReturnResult(Intent data)
	{
		if (getActivity() != null)
		{
			Intent srcIntent = getActivity().getIntent();
			if (srcIntent.getShortExtra(IntentParameters.MODE, DisplayModes.VIEW) == DisplayModes.INSERT)
			{
				// This doesn't use the object's parm rule.
				// Either return the inserted entity, or nothing.
				if (srcIntent.getBooleanExtra(WorkWithAction.EXTRA_IS_BC_INSERT, false))
				{
					for (BaseFragment childFragment : getChildFragments())
					{
						if (childFragment instanceof LayoutFragmentEditBC)
						{
							((LayoutFragmentEditBC)childFragment).setReturnResult(data);
							break;
						}
					}
				}

				return;
			}
		}

		// Standard call. Use the parm rule to return out/inout parameter values.
		// The list to be placed in an Intent must have parcelable data, so we convert it to String.
		List<String> serializableParameters = ListUtils.toStringList(getOutputParameters());
		IntentHelper.putList(data, IntentParameters.PARAMETERS, serializableParameters);
	}

	private List<Object> getOutputParameters()
	{
		Entity data = getContextEntity();
		List<Object> output = new ArrayList<>();

		if (data != null)
		{
			for (ObjectParameterDefinition parameter : getDefinition().getParameters())
				output.add(data.getProperty(parameter.getName()));
		}

		return output;
	}

	public void returnOK()
	{
		Intent result = new Intent();
		setReturnResult(result);

		LayoutFragmentActivity activity = Cast.as(LayoutFragmentActivity.class, getActivity());
		if (activity != null)
			activity.onActivityResult(RequestCodes.ACTION, Activity.RESULT_OK, result);

		destroyDialog();
	}

	public void returnCancel()
	{
		LayoutFragmentActivity activity = Cast.as(GenexusActivity.class, getActivity());
	    if (activity != null)
		{
			// remove current dismiss dialog from this activity
			activity.finalizeLayoutFragment(LayoutFragment.this);
			activity.onActivityResult(RequestCodes.ACTION, Activity.RESULT_CANCELED, null);
		}
	    destroyDialog();
	}

	private void destroyDialog()
	{
		Services.Device.invokeOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (getActivity() != null)
				{
				    GenexusActivity activity = Cast.as(GenexusActivity.class, getActivity());
				    if (activity != null)
				    	activity.destroyComponent(LayoutFragment.this);

					mDialogDismissed = true;
					dismissAllowingStateLoss();

					OrientationLock.unlock(getActivity(), OrientationLock.REASON_SHOW_POPUP);
				}
			}
		});
	}

	@Override
	public void onDismiss(DialogInterface dialog)
	{
		super.onDismiss(dialog);

		if (!mDialogDismissed)
			returnCancel();
	}

	@Override
	public void onDestroy()
	{
		if (mAdapter != null)
			mAdapter.onDestroyFragment();

		super.onDestroy();
	}

	@Override
	public View getRootView() {
		return getContentView();
	}

	// EnableHeaderRowPattern
	private final ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener()
	{
		private boolean mIsTransparent = true;

		@Override
		public void onScrollChanged()
		{
			if (getActivity() == null || mLayout == null)
				return;

			//change the theme when scroll down and up
			if (mRootCellView == null && mContentView != null)
				mRootCellView = mContentView.getFirstChild();

			if (mRootCellView != null)
			{
				// get the first row of main content.
				Rect rect = new Rect();
				boolean isVisible = mRootCellView.getGlobalVisibleRect(rect);
				int bottomY = rect.bottom;

				//change action bar theme
				int statusActionBarHeight = AdaptersHelper.getStatusAndActionBarHeight(getActivity(), mLayout);

				if (bottomY < statusActionBarHeight && mIsTransparent && isVisible)
				{
					// use default theme, for application bar
					ActivityHelper.setActionBarHeaderRowToNormalMode(getActivity(), mLayout, true);
					mIsTransparent = false;
				}
				else if (bottomY > statusActionBarHeight && !mIsTransparent && isVisible)
				{
					// use header row theme, transparent and elevation=0
					ActivityHelper.setActionBarHeaderRowToTransparentMode(getActivity(), mLayout, true);
					mIsTransparent = true;
				}
			}
		}
	};

	@Override
	public void updateActionBar()
	{
		if (getActivity() == null || mLayout == null)
			return;

		//change the theme when scroll down and up
		if (mRootCellView == null && mContentView != null)
			mRootCellView = mContentView.getFirstChild();

		if (mRootCellView != null)
		{
			// get the first row of main content.
			Rect rect = new Rect();
			boolean isVisible = mRootCellView.getGlobalVisibleRect(rect);
			int bottomY = rect.bottom;

			//change action bar theme
			int statusActionBarHeight = AdaptersHelper.getStatusAndActionBarHeight(getActivity(), mLayout);

			if (mLayout.getEnableHeaderRowPattern())
			{
				if (bottomY < statusActionBarHeight)
				{
					// use default theme, for application bar
					ActivityHelper.setActionBarHeaderRowToNormalMode(getActivity(), mLayout, true);
				} else if (bottomY > statusActionBarHeight)
				{
					// use header row theme, transparent and elevation=0
					ActivityHelper.setActionBarHeaderRowToTransparentMode(getActivity(), mLayout, true);
				}
			}
			else
			{
				//restore standard action bar.
				// use default theme, for application bar
				ActivityHelper.setActionBarHeaderRowToNormalMode(getActivity(), mLayout, true);
			}
		}
	}

}
