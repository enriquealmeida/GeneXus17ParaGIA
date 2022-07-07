package com.genexus.android.core.controls.tabs;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.View;
import android.widget.LinearLayout;

import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.layout.LayoutControlFactory;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.TabControlClassExtensionKt;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.utils.Cast;

class GxTabPageTextView extends LinearLayout implements IGxControl, IGxThemeable
{
	private final GxTabControl mParent;
	private final GxTabControl.TabItemInfo mTabItemInfo;

	private ThemeClassDefinition mUnselectedClass;
	private ThemeClassDefinition mSelectedClass;
	private ThemeClassDefinition mCurrentlyAppliedClass;

	private static final String PROPERTY_SELECTED_CLASS = "SelectedClass";

	private final AppCompatTextView mInternalTextView;

	public GxTabPageTextView(Context context, GxTabControl parent, GxTabControl.TabItemInfo itemInfo)
	{
		super(context);
		mParent = parent;
		mTabItemInfo = itemInfo;
		LayoutControlFactory.setTags(this, itemInfo.definition, true);

		mSelectedClass = itemInfo.definition.getSelectedClass();
		mUnselectedClass = itemInfo.definition.getUnselectedClass();

		mInternalTextView = new AppCompatTextView(context);
		this.addView(mInternalTextView);
	}

	@Override
	public String getName()
	{
		return mTabItemInfo.definition.getName();
	}

	public AppCompatTextView getInternalTextView()
	{
		return mInternalTextView;
	}

	@Override
	public LayoutItemDefinition getDefinition()
	{
		return mTabItemInfo.definition;
	}

	@Override
	public boolean isVisible()
	{
		return mTabItemInfo.visible;
	}

	@Override
	public void setVisible(boolean visible)
	{
		if (mTabItemInfo.visible != visible)
		{
			mTabItemInfo.visible = visible;
			if (mTabItemInfo.contentView!=null)
				mTabItemInfo.contentView.setIsVisibleTab(visible);

			mParent.notifyTabsChanged();
		}
	}

	@Override
	public String getCaption()
	{
		return mInternalTextView.getText().toString();
	}

	@Override
	public void setCaption(String caption)
	{
		mInternalTextView.setText(Services.Strings.attemptFromHtml(caption));
	}

    @Override
	public void setSelected(boolean selected)
    {
    	super.setSelected(selected);
		mInternalTextView.setSelected(selected);
    	applyThemeClasses();
    }

	@Override
	public Object getInterface(Class c) {
		return Cast.as(c, this);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mUnselectedClass;
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		mUnselectedClass = themeClass;
		applyThemeClasses();
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass)
	{
	}

	public void applyParentThemeClass(ThemeClassDefinition tabControlClass)
	{
		if (tabControlClass == null)
			return;

		// The parent class is applied ONLY if the child does not have selected/unselected classes itself.
		if (mTabItemInfo.definition.getUnselectedClass() == null)
			mUnselectedClass = TabControlClassExtensionKt.getUnselectedPageClass(tabControlClass);

		if (mTabItemInfo.definition.getSelectedClass() == null)
			mSelectedClass = TabControlClassExtensionKt.getSelectedPageClass(tabControlClass);

		applyThemeClasses();
	}

	public void applyThemeClasses()
	{
		ThemeClassDefinition appliedClass = TabUtils.applyTabItemClass(this, mUnselectedClass, mSelectedClass, mCurrentlyAppliedClass);
		mCurrentlyAppliedClass = appliedClass;
	}

	@Override
	public Value getPropertyValue(String name)
	{
		if (PROPERTY_SELECTED_CLASS.equalsIgnoreCase(name))
			return Value.newString(mSelectedClass != null ? mSelectedClass.getName() : Strings.EMPTY);

		return null;
	}

	@Override
	public void setPropertyValue(String name, Value value)
	{
		if (PROPERTY_SELECTED_CLASS.equalsIgnoreCase(name))
		{
			mSelectedClass = Services.Themes.getThemeClass(value.coerceToString());
			applyThemeClasses();
		}
	}

	@Override
	public void setFocus(boolean showKeyboard) { }

	@Override
	public void setExecutionContext(ExecutionContext context) { }

	@Override
	public View getView() {
		return this;
	}
}
