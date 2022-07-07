package com.genexus.android.core.fragments;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.genexus.android.R;
import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ActionFactory;
import com.genexus.android.core.actions.ActionParameters;
import com.genexus.android.core.actions.CompositeAction;
import com.genexus.android.core.actions.CompositeAction.IEventListener;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.adapters.DashBoardAdapter;
import com.genexus.android.analytics.Analytics;
import com.genexus.android.layout.GxTheme;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.IDataViewController;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.ImageViewDisplayImageWrapper;

public class DashboardFragment extends BaseFragment implements IDataView
{
	private DashboardMetadata mDefinition;
	private Connectivity mConnectivity;
	private DashBoardAdapter mAdapter;
	private Entity mData;
	private boolean mClientStartExecuted;

	private View mContentView;
	private AdapterView<ListAdapter> mDashboardView;
	private boolean mIsActive;

	public void initialize(DashboardMetadata definition, Connectivity connectivity)
	{
		mDefinition = definition;
		mData = EntityFactory.newEntity();
		mData.setExtraMembers(mDefinition.getVariables());

		mConnectivity = Connectivity.getConnectivitySupport(connectivity, definition.getConnectivitySupport());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mDefinition == null)
			return null;

		mContentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		mAdapter = new DashBoardAdapter(getUIContext(), mData);
		mAdapter.setDefinition(mDefinition);

		// Hide both controls here, show the correct one in showDashboardOptions().
		GridView grid = mContentView.findViewById(R.id.DashBoardGridView);
		ListView list = mContentView.findViewById(R.id.DashBoardListView);
		grid.setVisibility(View.GONE);
		list.setVisibility(View.GONE);

		if (mDefinition.getControl().equalsIgnoreCase(DashboardMetadata.CONTROL_LIST))
			mDashboardView = list;
		else
			mDashboardView = grid;

		applyStyle();
		startDashboard();
		return mContentView;
	}

	private void startDashboard()
	{
		if (!mClientStartExecuted)
		{
			ActionDefinition clientStartDefinition = mDefinition.getClientStart();
			if (clientStartDefinition != null)
			{
				// Run ClientStart, show dashboard afterwards.
				CompositeAction clientStart = ActionFactory.getAction(getUIContext(), clientStartDefinition, new ActionParameters(mData), clientStartDefinition.getIsComposite());
				clientStart.setEventListener(mClientStartEventListener);
				new ActionExecution(clientStart).executeAction();
			}
			else
			{
				mClientStartExecuted = true;
				showDashboardOptions();
			}
		}
		else
			showDashboardOptions();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		Analytics.onComponentStart(getActivity(), mDefinition);
	}

	private final IEventListener mClientStartEventListener = new IEventListener()
	{
		@Override
		public void onEndEvent(CompositeAction event, boolean successful)
		{
			mClientStartExecuted = true;
			Services.Device.runOnUiThread(() -> showDashboardOptions());
		}
	};

	private void showDashboardOptions()
	{
		mDashboardView.setVisibility(View.VISIBLE);
		mDashboardView.setAdapter(mAdapter);
		mDashboardView.setOnItemClickListener(mAdapter);
	}

	private void applyStyle()
	{
		// Set dashboard background and header images.
		GxLinearLayout root = mContentView.findViewById(R.id.DashBoardMainLinearLayout);
		Services.Images.displayBackground(root, mDefinition.getBackgroundImage());
		ImageView header = mContentView.findViewById(R.id.DashBoardHeaderImage);
		Services.Images.displayImage(ImageViewDisplayImageWrapper.to(header),
				mDefinition.getHeaderImage());

		// Apply dashboard theme class.
		ThemeClassDefinition gridThemeClass = mDefinition.getThemeClassForGrid();
		if (root != null && gridThemeClass != null)
			GxTheme.applyStyle(root, gridThemeClass);
	}

	@Override
	public @NonNull String getUri()
	{
		return mDefinition.getName();
	}

	@Override
	public IViewDefinition getDefinition()
	{
		return mDefinition;
	}

	@Override
	public short getMode()
	{
		return DisplayModes.VIEW;
	}

	@Override
	public LayoutDefinition getLayout()
	{
		return null;
	}

	@Override
	public IDataViewController getController()
	{
		// TODO Not needed for now, but should return one.
		return null;
	}

	@Override
	public UIContext getUIContext()
	{
		return new UIContext(getActivity(), this, mContentView, mConnectivity);
	}

	@Override
	public Entity getContextEntity()
	{
		return mData;
	}

	@Override
	public boolean isActive()
	{
		return mIsActive;
	}

	@Override
	public void setActive(boolean value)
	{
		mIsActive = value;
	}

	@Override
	public boolean isDataReady()
	{
		return mClientStartExecuted;
	}

	@Override
	public void refreshData(RefreshParameters params)
	{
		// Dashboards have no refresh event (as of now) so this does nothing.
	}

	@Override
	public void updateActionBar()
	{
		// Dashboards do nothing
	}

	@Override
	public void saveFragmentState(LayoutFragmentState state)
	{
		if (mClientStartExecuted)
			state.setData(mData);
	}

	@Override
	public void restoreFragmentState(LayoutFragmentState state)
	{
		Entity data = state.getData();
		if (data != null && !data.isEmpty())
		{
			mClientStartExecuted = true;
			mData = data;
		}
	}

	@Override
	public List<BaseFragment> getChildFragments()
	{
		return Collections.emptyList();
	}

	@Override
	public View getRootView()
	{
		return mDashboardView;
	}
}
