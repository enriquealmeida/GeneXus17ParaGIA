package com.genexus.android.core.controls;

import java.util.List;

import android.app.Activity;
import android.view.View;

import com.genexus.android.core.activities.DataViewHelper;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.utils.Cast;

public class FormControl implements IGxControl
{
	public static final String CONTROL_NAME = "Form";
	private final Activity mActivity;
	private final IDataView mFromDataView;

	public FormControl(Activity activity, IDataView fromDataView)
	{
		mActivity = activity;
		mFromDataView = fromDataView;
	}

	private static final String METHOD_REFRESH = "Refresh";

	@Override
	public Value callMethod(String name, List<Value> parameters)
	{
		if (METHOD_REFRESH.equalsIgnoreCase(name))
		{
			// Form.Refresh() means refresh EVERYTHING, ignoring the calling component.
			if (mActivity instanceof IGxActivity)
				((IGxActivity)mActivity).refreshData(new RefreshParameters(RefreshParameters.Reason.MANUAL, false));
		}
		return null;
	}

	@Override
	public String getName() { return CONTROL_NAME; }

	@Override
	public LayoutItemDefinition getDefinition() { return null; }

	@Override
	public void setEnabled(boolean enabled) { }

	@Override
	public void setFocus(boolean showKeyboard) { }

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) { }

	@Override
	public void setVisible(boolean visible) { }

	@Override
	public void requestLayout() { }

	@Override
	public Object getInterface(Class c) {
		return Cast.as(c, this);
	}

	@Override
	public void setCaption(String caption)
	{
		// Hack to support (old) DetailActivity, sets child title if only one child is shown.
		CharSequence text = Services.Strings.attemptFromHtml(caption);
		DataViewHelper.setTitle(mActivity, mFromDataView, text);
	}

	@Override
	public void setExecutionContext(ExecutionContext context) { }

	@Override
	public boolean isEnabled() { return true; }

	@Override
	public ThemeClassDefinition getThemeClass() { return null; }

	@Override
	public boolean isVisible() { return true; }

	@Override
	public String getCaption() { return mActivity.getTitle().toString(); }

	@Override
	public View getView() {
		return null;
	}
}
