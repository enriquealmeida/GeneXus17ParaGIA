package com.genexus.android.core.actions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.annotation.Nullable;

import com.genexus.android.core.activities.ActivityController;
import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.ViewHierarchyVisitor;
import com.genexus.android.core.base.metadata.DashboardItem;
import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controllers.IDataSourceBoundView;
import com.genexus.android.core.controls.ApplicationBarControl;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.FormControl;
import com.genexus.android.core.controls.GxControlViewWrapper;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.MenuControl;
import com.genexus.android.core.controls.NavigationControl;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.navigation.INavigationActivity;
import com.genexus.android.core.ui.navigation.NavigationController;
import com.genexus.android.core.ui.navigation.tabbed.TabbedNavigationController;

public class UIContext extends ContextWrapper
{
	private final Activity mActivity;
	private IDataView mDataView;
	private WeakReference<View> mRootView;
	private UIContext mParent;
	private Connectivity mConnectivitySupport;
	private Anchor mAnchor;
	private NameMap<Object> mTags;

	/**
	 * Constructs an UIContext from an Activity, without specifying a root view.
	 * Actions executed under this context won't be able to update the UI.
	 */
	public static UIContext base(Activity activity, Connectivity connectivitySupport)
	{
		return new UIContext(activity, null, null, connectivitySupport);
	}

	public UIContext(UIContext context, Connectivity c)
	{
		super(context.getActivity());
		mActivity = context.getActivity();
		mDataView = context.getDataView();
		mRootView = new WeakReference<>(context.getRootView());
		mParent = context.getParent();
		mConnectivitySupport = c;
		mAnchor = context.getAnchor();
		mTags = context.getTags();
	}

	public UIContext(Activity activity, IDataView dataView, View rootView, Connectivity c)
	{
		super(activity);
		mActivity = activity;
		mDataView = dataView;
		setRootView(rootView);
		mConnectivitySupport = c;
		mTags = new NameMap<>();
	}

	public UIContext(Context context, Connectivity c)
	{
		super(context);
		mActivity = null;
		mDataView = null;
		mConnectivitySupport = c;
		mTags = new NameMap<>();
	}

	protected void setRootView(View rootView)
	{
		mRootView = new WeakReference<>(rootView);
	}

	private View getRootView()
	{
		return (mRootView != null ? mRootView.get() : null);
	}

	public Activity getActivity()
	{
		return mActivity;
	}

	public ActivityController getActivityController()
	{
		if (mDataView != null && mDataView.getController() != null)
			return mDataView.getController().getParent();
		else
			return null;
	}
	
	public IDataView getDataView()
	{
		return mDataView;
	}

	protected void setParent(UIContext parent)
	{
		mParent = parent;

		if (mDataView == null)
			mDataView = parent.getDataView();
	}

	public UIContext getParent()
	{
		return mParent;
	}

	public <ViewT> List<ViewT> getViews(Class<ViewT> viewType)
	{
		View root = getRootView();
		if (root != null)
			return ViewHierarchyVisitor.getViews(viewType, root);
		else
			return new ArrayList<>();
	}

	public IGxControl findControl(String name)
	{
		// "Form" is a special control that maps to the activity itself.
		if (FormControl.CONTROL_NAME.equalsIgnoreCase(name))
			return new FormControl(getActivity(), mDataView);

		if (ApplicationBarControl.CONTROL_NAME.equalsIgnoreCase(name))
			return new ApplicationBarControl(getActivity());

		if (MenuControl.CONTROL_NAME.equalsIgnoreCase(name) &&
				getActivity() instanceof INavigationActivity &&
				((INavigationActivity)getActivity()).getNavigationController() instanceof TabbedNavigationController) {
			return new MenuControl(getActivity()); // ActivePage property
		}

		// Check for controls that map to action bar or action group buttons.
		if (getActivity() instanceof IGxActivity)
		{
			IGxControl control = ((IGxActivity)getActivity()).getController().getControl(mDataView, name);
			if (control != null)
				return control;
		}

		View rootView = getRootView();
		if (rootView != null)
		{
			View view = ViewHierarchyVisitor.findGenexusControl(rootView, name);

			// TODO: Change this when our controls implement IGxControl directly.
			if (view instanceof IGxControl)
				return (IGxControl)view;
			else if (view != null)
				return new GxControlViewWrapper(view);
		}

		if (getActivity() instanceof GenexusActivity)
		{
			// get navigation controller and if control is an item create a navigation control for it.
			NavigationController navigationController =((GenexusActivity)getActivity()).getNavigationController();
			if (navigationController!=null)
			{
				DashboardItem item = navigationController.getMenuItemDefinition(name);
				if (item!=null)
				{
					return new NavigationControl(getActivity(), navigationController, item);
				}
			}
		}

		return null;
	}

	public List<IGxEdit> findControlsBoundTo(String name)
	{
		// Might be more than one if "name" is a structure (e.g. SDT fields on screen).
		ArrayList<IGxEdit> list = new ArrayList<>();

		View rootView = getRootView();
		if (Services.Strings.hasValue(name) && rootView != null)
		{
			// TODO: Remove this for variables/attributes with same name.
			name = DataItemHelper.getNormalizedName(name);

			for (IGxEdit edit : ViewHierarchyVisitor.getViews(DataBoundControl.class, rootView))
			{
				String editTag = edit.getGxTag();
				if (Services.Strings.hasValue(editTag))
				{
					// Either an exact match, or a "field superset match" (e.g. name is "&sdt" and tag is "&sdt.name".
					if (editTag.equalsIgnoreCase(name)
							|| (editTag.startsWith(name) && editTag.substring(name.length()).startsWith(Strings.DOT)))
						list.add(edit);
				}
			}
		}

		return list;
	}

	public List<IDataSourceBoundView> findBoundGrids()
	{
		View rootView = getRootView();
		if (rootView != null)
			return ViewHierarchyVisitor.getViews(IDataSourceBoundView.class, rootView);
		else
			return new ArrayList<>();
	}

	public Connectivity getConnectivitySupport()
	{
		return mConnectivitySupport;
	}

	public Anchor getAnchor()
	{
		return mAnchor;
	}

	public void setAnchor(@Nullable Anchor anchor)
	{
		mAnchor = anchor;
	}
	
	public Object getTag(String key)
	{
		return mTags.get(key);
	}
	
	public void setTag(String key, Object tag)
	{
		mTags.put(key, tag);
	}

	private NameMap<Object> getTags()
	{
		return mTags;
	}
}
