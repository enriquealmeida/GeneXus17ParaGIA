package com.genexus.android.core.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genexus.android.core.controls.common.IAdjustContentSizeText;
import com.genexus.android.layout.GxLayout;
import com.genexus.android.layout.GxTheme;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxControlRuntimeContext;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.controls.IGxOverrideThemeable;
import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.controls.common.TextViewUtils;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;

import java.util.Collections;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DataBoundControl extends GxLinearLayout implements IGxEdit,
																IGxEditWithDependencies,
																IGxControlRuntime,
																IGxControlRuntimeContext,
																IGxLocalizable,
																IGxOverrideThemeable,
		IAdjustContentSizeText {
	private final LayoutItemDefinition mDefinition;

	private final String mLabelPosition;
	private final GxTextView mLabelView;
	private final View mAttrView;
	private final ThemeOverrideProperties mThemeOverrideProperties;
	private final boolean mReadOnly;

	private Entity mEntity; // Optional, only for "in grid" domain actions.

	public DataBoundControl(@NonNull Context context,
							@NonNull Coordinator coordinator,
							@NonNull LayoutItemDefinition definition,
							@NonNull GxTextView labelView,
							@NonNull View attrView,
							@Nullable ImageView actionImageView,
							boolean readonly) {
		super(context, definition);

		LinearLayout dataContainer;
		if (actionImageView == null) {
			dataContainer = this;
		} else {
			// In order to add an action icon to the right, we've to wrap the data (label + attr) in
			// another linear layout, and set the DataBoundControl's layout with HORIZONTAL orientation
			dataContainer = new LinearLayout(context);
			setOrientation(HORIZONTAL);
			// The data container takes all horizontal space available except for the one needed for the action icon
			addView(dataContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
			// The action icon is centered vertically and takes space horizontally as needed
			addView(actionImageView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		}

		String position = definition.getLabelPosition();

		// Configure the data container LinearLayout depending on the label position
		switch (position) {
			case Properties.LabelPositionType.PLATFORM_DEFAULT:
			case Properties.LabelPositionType.TOP:
				dataContainer.setOrientation(LinearLayout.VERTICAL);
				dataContainer.addView(labelView);
				dataContainer.addView(attrView);
				break;
			case Properties.LabelPositionType.BOTTOM:
				dataContainer.setOrientation(LinearLayout.VERTICAL);
				dataContainer.addView(attrView);
				dataContainer.addView(labelView);
				break;
			case Properties.LabelPositionType.RIGHT:
				dataContainer.setOrientation(LinearLayout.HORIZONTAL);
				dataContainer.addView(attrView);
				dataContainer.addView(labelView);
				break;
			case Properties.LabelPositionType.LEFT:
				dataContainer.setOrientation(LinearLayout.HORIZONTAL);
				dataContainer.addView(labelView);
				dataContainer.addView(attrView);
				break;
			case Properties.LabelPositionType.FLOATING:
			case Properties.LabelPositionType.NONE:
			default:
				//set fill parent to unique data field.
				if (!definition.hasAutoGrow() || definition.isHtml())
					dataContainer.addView(attrView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				else
					dataContainer.addView(attrView);
				break;
		}

		mDefinition = definition;
		mThemeOverrideProperties = new ThemeOverrideProperties();
		mLabelPosition = position;
		mLabelView = labelView;
		mAttrView = attrView;
		mReadOnly = readonly;

		setCaption(definition.getCaption());
	}

	public void setCaption(String caption) {
		if (!Properties.LabelPositionType.FLOATING.equalsIgnoreCase(mLabelPosition)
				&& !Properties.LabelPositionType.NONE.equalsIgnoreCase(mLabelPosition)) {
			TextViewUtils.setText(mLabelView, caption, mDefinition);
			if (getParent() instanceof GxLayout) {
				GxLayout parent = (GxLayout) getParent();
				parent.requestAlignFieldLabels();
			}
		} else if (Properties.LabelPositionType.FLOATING.equalsIgnoreCase(mLabelPosition)) {
			GxTextInputLayout view = (GxTextInputLayout) mAttrView;
			view.setCaption(caption);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (mAttrView instanceof IManualAlignment) { // GxSwitch requires it, it seems... See issue #64865
			IManualAlignment view = (IManualAlignment) mAttrView;
			view.applyAlignment(); // For controls that require to change the margin to simulate alignment, here it has all sizes required
		}
	}

	public LayoutItemDefinition getDefinition() {
		return mDefinition;
	}

	public GxTextView getLabel() {
		return mLabelView;
	}

	public View getAttribute() {
		return mAttrView;
	}

	// Optional, only for "in grid" domain actions.
	public Entity getEntity() {
		return mEntity;
	}

	public void setEntity(Entity entity) {
		mEntity = entity;
	}

	@Override
	public String getGxValue() {
		return ((IGxEdit) mAttrView).getGxValue();
	}

	@Override
	public void setGxValue(String value) {
		((IGxEdit) mAttrView).setGxValue(value);
	}

	@Override
	public String getGxTag() {
		return ((IGxEdit) mAttrView).getGxTag();
	}

	@Override
	public void setGxTag(String tag) {
		((IGxEdit) mAttrView).setGxTag(tag);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mAttrView.setEnabled(enabled);
	}

	@Override
	public void setValueFromIntent(Intent data) {
		((IGxEdit) mAttrView).setValueFromIntent(data);
	}

	@Override
	public IGxEdit getViewControl() {
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@SuppressLint("RtlHardcoded")
	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		if (themeClass == null) {
			themeClass = mDefinition.getThemeClass();
		}

		View attrView = getAttribute();
		attrView.setTag(LayoutTag.CONTROL_THEME_CLASS, themeClass);

		boolean setLabelAlignment = false;

		if (themeClass != null) {
			// Apply Theme classes for the container of the Label + Attribute
			super.setThemeClass(themeClass);

			// Apply read only class
			if (mReadOnly && themeClass.getReadonlyClass() != null)
				themeClass = themeClass.getReadonlyClass();

			if (mLabelView != null) {
				// Apply Theme for the Label
				if (themeClass.getLabelClass() != null) {
					GxTheme.applyStyle(mLabelView, themeClass.getLabelClass());
				}

				// Apply Alignment for the Label

				//set gravity of label
				if (Services.Application.isRTLLanguage() )
					mLabelView.setGravity( themeClass.getLabelAlignmentInRTL());
				else
					mLabelView.setGravity(themeClass.getHorizontalLabelAlignment() | themeClass.getVerticalLabelAlignment());

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
					mLabelView.setJustificationMode(themeClass.getJustificationMode());

				LayoutParams params = (LayoutParams) mLabelView.getLayoutParams();
				if (params != null) {
					//noinspection ResourceType
					if (Services.Application.isRTLLanguage())
						params.gravity = themeClass.getLabelAlignmentInRTL();
					else
						params.gravity = themeClass.getLabelAlignment();

					mLabelView.setLayoutParams(params);
				}

				if (mLabelView.getGravity() != Gravity.NO_GRAVITY) {
					setLabelAlignment = true;
				}

				if (themeClass.getLabelWidth() != null && mLabelView.getLayoutParams()!=null) {
					ViewGroup.LayoutParams labelParams = mLabelView.getLayoutParams();
					labelParams.width = themeClass.getLabelWidth();
					mLabelView.setLayoutParams(labelParams);
				}

			}

			// Let the Edit itself apply the theme class too.
			if (attrView instanceof IGxEditThemeable)
				((IGxEditThemeable) attrView).applyEditClass(themeClass);
		} else {
			setGravity(mDefinition.getCellGravity());
		}


		if (setLabelAlignment) {
			//set gravity to att field
			LayoutParams parameters = (LayoutParams) mAttrView.getLayoutParams();
			//noinspection ResourceType
			parameters.gravity = mDefinition.getCellGravity();
			parameters.weight = 1;
			mAttrView.setLayoutParams(parameters);
			setGravityIfNeeded(attrView);
		} else {
			if (mDefinition.getLabelPosition().equals(Properties.LabelPositionType.FLOATING)
					&& mDefinition.getCellGravity() == Alignment.NONE
					&& (attrView instanceof GxEditTextNumeric)) {
				// set default align here
				setGravity(Gravity.RIGHT);
				((TextView) attrView).setGravity(Gravity.RIGHT);
			} else {
				setGravity(mDefinition.getCellGravity());
				setGravityIfNeeded(attrView);
			}
		}
	}

	private void setGravityIfNeeded(View attrView) {
		if (Services.Application.isRTLLanguage() ||
			(mDefinition.getCellGravity() != Alignment.CENTER_VERTICAL &&
			mDefinition.getCellGravity() != Alignment.NONE)
			) {
			if (attrView instanceof TextView)  //or EditText
				setGravity((TextView) attrView);
			if (attrView instanceof GxTextInputLayout)
				setGravity((GxTextInputLayout) attrView);
		}
	}

	private void setGravity(TextView view)
	{
		if (Services.Application.isRTLLanguage())
			view.setGravity(mDefinition.getCellGravityInRTL());
		else
			view.setGravity(mDefinition.getCellGravity());
	}

	private void setGravity(GxTextInputLayout view)
	{
		if (Services.Application.isRTLLanguage() )
			view.setGravity(mDefinition.getCellGravityInRTL());
		else
			view.setGravity(mDefinition.getCellGravity());
	}

	// needed for override Att controls BackColor / ForeColor . For forecolor send the override to inner Edit
	@Override
	public void setOverride(String propertyName, String propertyValue) {
		mThemeOverrideProperties.setOverride(propertyName, propertyValue);
		if (mAttrView instanceof IGxOverrideThemeable) {
			IGxOverrideThemeable edit = (IGxOverrideThemeable) mAttrView;
			edit.setOverride(propertyName, propertyValue);
		}
		applyClass(mThemedHelper.getThemeClass());
	}

	@Override
	public ThemeOverrideProperties getThemeOverrideProperties() {
		return mThemeOverrideProperties;
	}

	@Override
	protected void setBackgroundBorderProperties(ThemeClassDefinition themeClass) {
		// Override to pass layout item definition (GxLinearLayout does not always correspond to a layout item).
		ThemeUtils.setBackgroundBorderProperties(this, themeClass, BackgroundOptions.defaultFor(mDefinition));
	}

	@Override
	public List<String> getDependencies() {
		if (mAttrView instanceof IGxEditWithDependencies) {
			IGxEditWithDependencies edit = (IGxEditWithDependencies) mAttrView;
			return edit.getDependencies();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void onDependencyValueChanged(String name, Object value) {
		if (mAttrView instanceof IGxEditWithDependencies) {
			IGxEditWithDependencies edit = (IGxEditWithDependencies) mAttrView;
			edit.onDependencyValueChanged(name, value);
		}
	}

	@Override
	public void setExecutionContext(ExecutionContext context) {
		if (mAttrView instanceof IGxControlRuntimeContext) {
			IGxControlRuntimeContext edit = (IGxControlRuntimeContext) mAttrView;
			edit.setExecutionContext(context);
		}
	}

	@Override
	public Value getPropertyValue(String name) {
		// Retrieve properties from custom control if it supports them.
		if (mAttrView instanceof IGxControlRuntime) {
			IGxControlRuntime edit = (IGxControlRuntime) mAttrView;
			return edit.getPropertyValue(name);
		} else {
			return null;
		}
	}

	@Override
	public void setPropertyValue(String name, Value value) {
		if (mAttrView instanceof IGxControlRuntime) {
			IGxControlRuntime edit = (IGxControlRuntime) mAttrView;
			edit.setPropertyValue(name, value);
		}
	}

	@Override
	public Value callMethod(String name, List<Value> parameters) {
		if (mAttrView instanceof IGxControlRuntime) {
			IGxControlRuntime edit = (IGxControlRuntime) mAttrView;
			return edit.callMethod(name, parameters);
		} else {
			return null;
		}
	}

	@Override
	public boolean isEditable() {
		return ((IGxEdit) mAttrView).isEditable();
	}

	@Override
	public void onTranslationChanged() {
		TextViewUtils.setText(mLabelView, mDefinition.getCaption(), mDefinition);
	}

	@Override
	public void adjustContentSize() {
		mAttrView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
		if (mAttrView instanceof GxTextView)
			((GxTextView)mAttrView).setSetMaxSize(false);
	}

	@Override
	public void setVisibility(int visibility) {
		mAttrView.setVisibility(visibility);
		super.setVisibility(visibility);
	}
}
