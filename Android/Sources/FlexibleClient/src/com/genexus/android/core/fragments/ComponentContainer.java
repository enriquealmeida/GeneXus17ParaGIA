package com.genexus.android.core.fragments;

import java.util.List;

import android.content.Context;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.genexus.android.core.actions.DynamicCallAction;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.layout.GxLayoutInTab;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ComponentDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.RefreshParameters;

public class ComponentContainer extends LinearLayout implements IGxControlRuntime
{
	//status
	public static final int INACTIVE = 0;
	public static final int TOACTIVATED = 1;
	public static final int ACTIVE = 2;
	public static final int TOINACTIVATED = 3;

	private static final String METHOD_REFRESH = "Refresh";
	private static final String METHOD_CLEAR = "Clear";
	private static final String PROPERTY_OBJECT = "Object";

	private static int sLastId = 95;

	private final ComponentDefinition mDefinition;

	private int mId;
	private LayoutFragment mParentFragment;
	private BaseFragment mFragment;
	private Size mSize;
	private int mStatus = INACTIVE;
	private boolean mPendingAttach = false;

	public ComponentContainer(Context context, ComponentDefinition definition)
	{
		super(context);
		setWillNotDraw(true);
		mDefinition = definition;
	}

	public ComponentDefinition getDefinition()
	{
		return mDefinition;
	}

	public void setComponentSize(Size size)
	{
		mSize = size;
	}

	public Size getComponentSize()
	{
		return mSize;
	}

	public int getContentControlId()
	{
		if (mId==0)
		{
			mId = sLastId;
			sLastId++;
		}
		return mId;
	}

	public int getStatus()
	{
		return mStatus;
	}

	public void setStatus(int status)
	{
		mStatus = status;
	}

	public boolean getPendingAttach()
	{
		return mPendingAttach;
	}

	public void setPendingAttach(boolean pendingAttach)
	{
		mPendingAttach = pendingAttach;
	}

	public void setActive(boolean active)
	{
		if (mFragment != null)
			mFragment.setActive(active);

		setStatus(active ? ACTIVE : INACTIVE);
	}

	public BaseFragment getFragment()
	{
		return mFragment;
	}

	public void setFragment(BaseFragment fragment)
	{
		mFragment = fragment;
	}

	public LayoutFragment getParentFragment()
	{
		return mParentFragment;
	}

	public void setParentFragment(LayoutFragment fragment)
	{
		mParentFragment = fragment;
	}

	public boolean hasDirectTabParent()
	{
		return (getDirectTabParent(getParent()) != null);
	}

	public boolean hasTabParentWithScroll()
	{
		GxLayoutInTab gxLayout = getDirectTabParent(getParent());
		return gxLayout != null && gxLayout.getHasScroll();
	}

	private GxLayoutInTab getDirectTabParent(ViewParent parent)
	{
		if (parent != null)
		{
			if (parent instanceof GxLayoutInTab)
				return (GxLayoutInTab) parent;

			// If we're inside another component then we don't have a DIRECT tab parent.
			if (parent instanceof ComponentContainer)
				return null;

			return getDirectTabParent(parent.getParent());
		}
		else
			return null;
	}

	public boolean hasTabParentDisconected()
	{
		if (getParent()!=null)
		{
			GxLayoutInTab tabParent = getDirectTabParent(getParent());
			if (tabParent!=null && tabParent.getParent()==null)
				return true;
			if (tabParent!=null && tabParent.getParent()!=null && tabParent.getParent().getParent()==null)
				return true;
		}
		return false;
	}

	public boolean getHasTabParentVisible()
	{
		if (getParent()!=null)
		{
			GxLayoutInTab tabParent = getDirectTabParent(getParent());
			if (tabParent!=null)
				return tabParent.isVisibleTab();

		}
		return false;
	}

	@Override
	public void setPropertyValue(String name, Value value) {
		if (name.equalsIgnoreCase(PROPERTY_OBJECT))
		{
			if (mParentFragment != null)
			{
				mParentFragment.replaceInnerComponent(this, value.coerceToString());
			}
			else
			{
				// TODO: identify this is a client start assign. This could happen in other cases?
				Services.Log.debug(PROPERTY_OBJECT + " in client start " + name + "  " + value);
				// .Object in client start.
				ActionDefinition actionDefinition = DynamicCallAction.parse(new UIContext(getContext(), Connectivity.Inherit), value.coerceToString());
				// call
				mDefinition.setClientStartObject(actionDefinition.getGxObject());
				// parameters
				mDefinition.setClientStartParameters(actionDefinition.getParameters());

			}
		}
	}

	@Override
	public Value callMethod(String name, List<Value> parameters)
	{
		if (METHOD_REFRESH.equalsIgnoreCase(name))
		{
			if (mFragment instanceof IDataView)
				((IDataView)mFragment).refreshData(new RefreshParameters(RefreshParameters.Reason.MANUAL, false));
		}
		else if (METHOD_CLEAR.equalsIgnoreCase(name))
		{
			if (mParentFragment != null && mFragment != null)
				mParentFragment.removeInnerComponent(this);
		}
		return null;
	}
}
