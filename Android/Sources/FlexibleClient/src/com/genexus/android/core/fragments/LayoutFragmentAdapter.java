package com.genexus.android.core.fragments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.genexus.android.core.actions.ICustomMenuManager;
import com.genexus.android.core.activities.IGxBaseActivity;
import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.ViewHierarchyVisitor;
import com.genexus.android.layout.DynamicProperties;
import com.genexus.android.layout.IGxRootLayout;
import com.genexus.android.layout.LayoutBuilder;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.PromptHelper;
import com.genexus.android.core.controllers.IDataSourceBoundView;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.GxSectionLink;
import com.genexus.android.core.controls.IDataViewHosted;
import com.genexus.android.core.controls.IGxActionControl;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.IGxGridControl;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.FormCoordinator;
import com.genexus.android.core.utils.Cast;

public class LayoutFragmentAdapter
{
	private final IGxBaseActivity mActivity;
	private final LayoutFragment mFragment;
	private final LayoutDefinition mLayout;
	private final FormCoordinator mCoordinator;
	private final short mLayoutMode;
	private final short mMode;

	// Scanned special views.
	private ArrayList<View> mBoundViews;
	private ArrayList<IGxEdit> mEditables;
	private ArrayList<IGxActionControl> mActionControls;
	private ArrayList<IDataSourceBoundView> mDataSourceBoundViews;
	private ArrayList<IDataSourceBoundView> mVirtualDataSourceBoundViews;
	private ArrayList<ComponentContainer> mComponentContainers;
	private ArrayList<ICustomMenuManager> mCustomMenuManagers;
	private HashSet<IDataViewHosted> mHostedControls;

	private IGxRootLayout mHolder;

	private Entity mEntity;

	public LayoutFragmentAdapter(LayoutFragment fragment, IGxBaseActivity activity, LayoutDefinition layout, short layoutMode, short trnMode)
	{
		mActivity = activity;
		mFragment = fragment;
		mLayout = layout;
		mCoordinator = new FormCoordinator(fragment.getUIContext(), fragment);
		mLayoutMode = layoutMode;
		mMode = trnMode;

		mBoundViews = new ArrayList<>();
		mEditables = new ArrayList<>();
		mActionControls = new ArrayList<>();
		mDataSourceBoundViews = new ArrayList<>();
		mVirtualDataSourceBoundViews = new ArrayList<>();
		mComponentContainers = new ArrayList<>();
		mCustomMenuManagers = new ArrayList<>();
		mHostedControls = new HashSet<>();
	}

	// public List<View> getBoundViews() { return mBoundViews; }
	public List<IDataSourceBoundView> getDataSourceBoundViews() { return mDataSourceBoundViews; }
	public List<ComponentContainer> getComponentContainers() { return mComponentContainers; }
	public List<ICustomMenuManager> getCustomMenuManagers() { return mCustomMenuManagers; }

	public void expandLayout(IGxRootLayout rootLayout, Size size)
	{
		((ViewGroup)rootLayout).removeAllViews();
		prepareForExpand(size);

		TableDefinition rootTable = getRootTable();
		if (rootTable != null)
		{
			TableDefinition def = rootTable.getContent();
			rootLayout.setLayout(mCoordinator, def);
		}

		LayoutBuilder builder = new LayoutBuilder((Context)mActivity, mCoordinator, mLayoutMode, mMode);
		builder.expandLayout(rootLayout, rootTable);
		rootLayout.afterExpandLayout();

    	processSpecialViews((View)rootLayout, builder.getBoundViews(), builder.getDataSourceBoundViews(), builder.getComponentContainers());
    	mHolder = rootLayout;
	}

	private TableDefinition getRootTable()
	{
		return (mLayout != null ? mLayout.getTable() : null);
	}

	private TableDefinition getContentTable()
	{
		TableDefinition rootTable = getRootTable();
		if (rootTable != null)
			return rootTable.getContent();

		return null;
	}

	public boolean getContentHasMargin()
	{
		TableDefinition def = getContentTable();
		if (def != null && def.getThemeClass() != null)
			return def.getThemeClass().hasMarginSet();

		return false;
	}

	private void prepareForExpand(Size size)
	{
		mBoundViews.clear();
		mEditables.clear();
		mActionControls.clear();
		mDataSourceBoundViews.clear();
		mVirtualDataSourceBoundViews.clear();
		mComponentContainers.clear();
		mCustomMenuManagers.clear();
		mHostedControls.clear();

		setBounds(size);
	}

	private void setBounds(Size size)
	{
		if (mLayout != null)
			AdaptersHelper.setBounds(mLayout, size);
	}

	private void processSpecialViews(View root, List<View> boundViews, List<IDataSourceBoundView> dsBoundViews, List<ComponentContainer> componentContainers)
	{
		mComponentContainers.addAll(componentContainers);

		// Separate bound views into actions and fields.
		for (View v : boundViews)
		{
			if (v instanceof IDataViewHosted)
				mHostedControls.add((IDataViewHosted)v);

			if (v instanceof IGxActionControl)
			{
				mActionControls.add((IGxActionControl)v);
				v.setOnClickListener(mActionHandler);
			}
			else if (v instanceof GxSectionLink)
			{
				v.setOnClickListener(mRelationActionHandler2);
			}
			else
				mBoundViews.add(v);

			if (v instanceof IGxEdit)
				mEditables.add((IGxEdit)v);
		}

		// The fragment itself is also a DSBV.
		mDataSourceBoundViews.add(mFragment);

    	// Separate "real" bound views (which have their own controller and adapter) from "virtual"
    	// ones (which share a controller and are handled by this adapter, and therefore not exposed).
    	for (IDataSourceBoundView dsbView : dsBoundViews)
    	{
			if (dsbView instanceof IDataViewHosted)
				mHostedControls.add((IDataViewHosted)dsbView);

    		if (Services.Strings.hasValue(dsbView.getDataSourceMember()))
    		{
    			if (dsbView.getDataSource() == mFragment.getDataSource())
        			mVirtualDataSourceBoundViews.add(dsbView);
    			else
    				Services.Log.warning("DSBV with DS member but with a different DS than the host is not supported.");
    		}
    		else if (dsbView.getDataSource() != mFragment.getDataSource())
    			mDataSourceBoundViews.add(dsbView);
    	}

    	// Set host to any hosted controls.
    	for (IDataViewHosted hosted : mHostedControls)
    		hosted.setHost(mFragment);

    	// Look up CustomMenuManagers.
    	mCustomMenuManagers.addAll(ViewHierarchyVisitor.getViews(ICustomMenuManager.class, root));
	}

	public void controlstoData(Entity currentEntity)
	{
		if (currentEntity == null)
			return;

		// Save form values.
		AdaptersHelper.saveEditValues(mBoundViews, currentEntity);

		// Save grid values (for grids with editable controls)
		for (View view : mBoundViews)
		{
			if (view instanceof GridContainer)
				((GridContainer)view).saveEditValues();
		}
	}

	public IGxEdit getEdit(String id)
	{
		for (IGxEdit edit : mEditables)
		{
			if (edit.getGxTag() != null && edit.getGxTag().equalsIgnoreCase(id))
				return edit;
		}

		return null;
	}

	public void drawData(ViewData data)
	{
		mEntity = data.getSingleEntity();
		mCoordinator.setData(mEntity);

		DynamicProperties props = DynamicProperties.get(mEntity);

		// Bind single views.
		for (View view : mBoundViews)
		{
			loadEdit(view, mEntity);

			DataBoundControl dataControl = Cast.as(DataBoundControl.class, view);
			if (dataControl != null && AdaptersHelper.hasOnClickAction(dataControl, mLayoutMode, mMode)
					&& !PromptHelper.hasPrompt(dataControl) )  //give priority to prompt.
				dataControl.setOnClickListener(mDomainActionHandler);
		}

		// Bind actions
		for (IGxActionControl action : mActionControls)
			loadAction(action, mEntity);

		for (IDataSourceBoundView dsBoundView : mVirtualDataSourceBoundViews)
			AdaptersHelper.loadGrid(dsBoundView, data, mEntity, false);

		if (mHolder != null)
			props.apply(mFragment.getUIContext());
	}

	private boolean loadEdit(View view, Entity entity)
	{
		IGxEdit edit = Cast.as(IGxEdit.class, view);
		if (edit == null)
			return false;

		AdaptersHelper.setEditValue(edit, entity);
		return true;
	}

	private static void loadAction(IGxActionControl action, Entity entity)
	{
		action.setEntity(entity);
	}

	// Executed when an "action in layout" is fired.
	private OnClickListener mActionHandler = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			IGxActionControl action = Cast.as(IGxActionControl.class, v);
			if (action != null) {
				IGxGridControl gridControl = null;
				String gridName = GridDefinition.getMultipleSelectionAction(action.getAction());
				if (gridName != null)
					gridControl = Cast.as(IGxGridControl.class, mFragment.getFormCoordinator().getControl(gridName));

				if (gridControl != null && gridControl.getDefinition().getShowSelector() == GridDefinition.ShowSelector.OnAction)
					gridControl.setSelectionMode(true, action.getAction());
				else
					mFragment.runAction(action.getAction(), new Anchor(v), false);
			}
		}
	};

	// Executed when an "domain" action is fired.
	private OnClickListener mDomainActionHandler = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			AdaptersHelper.launchDomainAction(mFragment.getUIContext(), v, mEntity);
		}
	};

	private OnClickListener mRelationActionHandler2 = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			GxSectionLink sectionLink = (GxSectionLink)view;
			sectionLink.callDataView(mFragment);
		}
	};

	public void onDestroyFragment()
	{
		// Remove reference from Entity to Coordinator because the Entity may outlive this fragment.
		mCoordinator.setData(null);
	}

	public FormCoordinator getCoordinator() {
		return mCoordinator;
	}
}
