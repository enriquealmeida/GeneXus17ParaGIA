package com.genexus.android.core.controls.grids;

import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.layout.ControlHelper;
import com.genexus.android.layout.ControlProperties;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.controls.IGxControl;

/**
 * UIContext specialization associated to a Grid item.
 */
class GridItemUIContext extends UIContext
{
	private final GridHelper mGridHelper;
	private final ControlProperties mControlProperties;
	private boolean mControlPropertiesTrackingEnabled;

	public GridItemUIContext(@NonNull UIContext parentUIContext, @NonNull GridHelper gridHelper)
	{
		super(parentUIContext.getActivity(), parentUIContext.getDataView(), null, parentUIContext.getConnectivitySupport());
		setParent(parentUIContext);

		mControlProperties = new ControlProperties();
		mGridHelper = gridHelper;
		mControlPropertiesTrackingEnabled = true;
	}

	void setGridItem(GridItemViewInfo gridItem)
	{
		// Update the reference to the view. Needed because of reuse.
		setRootView(gridItem.getView());
	}

	@Override
	public IGxControl findControl(String name)
	{
		IGxControl control = super.findControl(name);

		// Whenever we gain access to a particular grid item control, we need to
		// track its changes to properties, to apply them if the control is redrawn.
		if (control != null)
			return new GridControlWrapper(control);
		else
			return null;
	}

	public ControlProperties getAssignedControlProperties()
	{
		return mControlProperties;
	}

	private class GridControlWrapper implements IGxControl
	{
		private final IGxControl mControl;

		public GridControlWrapper(IGxControl control)
		{
			mControl = control;
		}

		@Override
		public String getName()
		{
			return mControl.getName();
		}

		@Override
		public LayoutItemDefinition getDefinition()
		{
			return mControl.getDefinition();
		}

		@Override
		public void requestLayout()
		{
			mControl.requestLayout();
		}

		@Override
		public Object getInterface(Class c) {
			return mControl.getInterface(c);
		}

		@Override
		public void setEnabled(boolean enabled)
		{
			if (mControlPropertiesTrackingEnabled)
				mControlProperties.putProperty(getName(), ControlHelper.PROPERTY_ENABLED, Value.newBoolean(enabled));

			mControl.setEnabled(enabled);
		}

		@Override
		public void setFocus(boolean showKeyboard)
		{
			mControl.setFocus(showKeyboard);
		}

		@Override
		public void setThemeClass(ThemeClassDefinition themeClass)
		{
			if (themeClass != null)
			{
				if (mControlPropertiesTrackingEnabled)
					mControlProperties.putProperty(getName(), ControlHelper.PROPERTY_CLASS, Value.newString(themeClass.getName()));

				mControl.setThemeClass(themeClass);
			}
		}

		@Override
		public void setVisible(boolean visible)
		{
			if (mControlPropertiesTrackingEnabled)
				mControlProperties.putProperty(getName(), ControlHelper.PROPERTY_VISIBLE, Value.newBoolean(visible));

			mControl.setVisible(visible);
		}

		@Override
		public void setCaption(String caption)
		{
			if (mControlPropertiesTrackingEnabled)
				mControlProperties.putProperty(getName(), ControlHelper.PROPERTY_CAPTION, Value.newString(caption));

			mControl.setCaption(caption);
		}

		@Override
		public boolean isEnabled()
		{
			return mControl.isEnabled();
		}

		@Override
		public ThemeClassDefinition getThemeClass()
		{
			return mControl.getThemeClass();
		}

		@Override
		public boolean isVisible()
		{
			return mControl.isVisible();
		}

		@Override
		public String getCaption()
		{
			return mControl.getCaption();
		}

		@Override
		public void setExecutionContext(ExecutionContext context)
		{
			mControl.setExecutionContext(context);
		}

		@Override
		public Value getPropertyValue(String name) {
			return mControl.getPropertyValue(name);
		}

		@Override
		public void setPropertyValue(String name, Value value) {
			if (mControlPropertiesTrackingEnabled)
				mControlProperties.putProperty(getName(), name, value);

			mControl.setPropertyValue(name, value);
		}

		@Override
		public Value callMethod(String name, List<Value> parameters)
		{
			if (mControlPropertiesTrackingEnabled)
				mGridHelper.disableViewReuse();

			// TODO: Should these be preserved too?
			return mControl.callMethod(name, parameters);
		}

		@Override
		public View getView() {
			return mControl.getView();
		}
	}

	public void setControlPropertiesTrackingEnabled(boolean value)
	{
		mControlPropertiesTrackingEnabled = value;
	}
}
