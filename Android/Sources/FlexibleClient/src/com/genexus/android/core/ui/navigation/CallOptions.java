package com.genexus.android.core.ui.navigation;

import java.io.Serializable;

import com.genexus.android.animations.Transition;
import com.genexus.android.animations.Transitions;
import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.theme.FormClassExtensionKt;
import com.genexus.android.core.base.metadata.theme.TargetSize;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;

/**
 * Call Options class.
 */
public class CallOptions implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String OPTION_TYPE = "Type";
	public static final String OPTION_ENTER_EFFECT = "EnterEffect";
	public static final String OPTION_EXIT_EFFECT = "ExitEffect";
	public static final String OPTION_TARGET = "Target";
	public static final String OPTION_TARGET_SIZE = "TargetSize";
	public static final String OPTION_TARGET_HEIGHT = "TargetHeight";
	public static final String OPTION_TARGET_WIDTH = "TargetWidth";

	private static final int MAX_POPUP_PERCENTAGE = 90;

	private CallType mCallType;
	private Transition mEnterEffect;
	private Transition mExitEffect;
	private String mTargetName;
	private DimensionValue mTargetWidth;
	private DimensionValue mTargetHeight;

	/**
	 * Initializes default call options.
	 */
	public CallOptions()
	{
		mCallType = CallType.DEFAULT;
	}

	public void setFromClass(ThemeClassDefinition formClass)
	{
		if (formClass != null)
		{
			mCallType = CallType.tryParse(FormClassExtensionKt.getCallType(formClass));
			mEnterEffect = Transitions.get(FormClassExtensionKt.getEnterEffect(formClass));
			mExitEffect = Transitions.get(FormClassExtensionKt.getExitEffect(formClass));
			mTargetName = FormClassExtensionKt.getTargetName(formClass);
			setTargetSize(FormClassExtensionKt.getTargetSize(formClass));
		}
	}

	private void setTargetSize(TargetSize size)
	{
		setTargetSize(size.getName(), size.getCustomWidth(), size.getCustomHeight());
	}

	public void setTargetSize(String name)
	{
		setTargetSize(name, null, null);
	}

	private void setTargetSize(String name, DimensionValue customWidth, DimensionValue customHeight)
	{
		// Magic numbers galore! More or less derived from their predetermined iOS counterparts
		// (UIModalPresentationFormSheet, UIModalPresentationPageSheet, UIModalPresentationFullScreen).

		if (TargetSize.SIZE_DEFAULT.equalsIgnoreCase(name))
		{
			// "Default" size is calculated later, possibly considering the layout size in dips.
			mTargetWidth = null;
			mTargetHeight = null;
		}
		else if (TargetSize.SIZE_CUSTOM.equalsIgnoreCase(name))
		{
			mTargetWidth = customWidth;
			mTargetHeight = customHeight;
		}
		else if (TargetSize.SIZE_SMALL.equalsIgnoreCase(name))
		{
			Orientation orientation = Services.Device.getScreenOrientation();
			if (orientation == Orientation.PORTRAIT)
			{
				mTargetWidth = DimensionValue.percent(70);
				mTargetHeight = DimensionValue.percent(60);
			}
			else
			{
				mTargetWidth = DimensionValue.percent(55);
				mTargetHeight = DimensionValue.percent(80);
			}
		}
		else if (TargetSize.SIZE_MEDIUM.equalsIgnoreCase(name))
		{
			mTargetWidth = getDefaultTargetWidth();
			mTargetHeight = getDefaultTargetHeight();
		}
		else if (TargetSize.SIZE_LARGE.equalsIgnoreCase(name))
		{
			mTargetWidth = DimensionValue.percent(MAX_POPUP_PERCENTAGE);
			mTargetHeight = DimensionValue.percent(MAX_POPUP_PERCENTAGE);
		}
	}

	static DimensionValue getDefaultTargetWidth()
	{
		if (Services.Device.getScreenOrientation() == Orientation.PORTRAIT)
			return DimensionValue.percent(MAX_POPUP_PERCENTAGE);
		else
			return DimensionValue.percent(60);
	}

	static DimensionValue getDefaultTargetHeight()
	{
		if (Services.Device.getScreenOrientation() == Orientation.PORTRAIT)
			return DimensionValue.percent(60);
		else
			return DimensionValue.percent(MAX_POPUP_PERCENTAGE);
	}

	void setOverrides(CallOptions overrides)
	{
		if (overrides != null)
		{
			if (overrides.getCallType() != null)
				mCallType = overrides.getCallType();

			if (overrides.getEnterEffect() != null)
				mEnterEffect = overrides.getEnterEffect();

			if (overrides.getExitEffect() != null)
				mExitEffect = overrides.getExitEffect();

			if (overrides.getTargetName() != null)
				mTargetName = overrides.getTargetName();

			if (overrides.getTargetWidth() != null)
				mTargetWidth = overrides.getTargetWidth();

			if (overrides.getTargetHeight() != null)
				mTargetHeight = overrides.getTargetHeight();
		}
	}

	public CallType getCallType() { return mCallType; }
	public void setCallType(CallType value) { mCallType = value; }

	public Transition getEnterEffect() { return mEnterEffect; }
	public void setEnterEffect(Transition value) { mEnterEffect = value; }

	public Transition getExitEffect() { return mExitEffect; }
	public void setExitEffect(Transition value) { mExitEffect = value; }

	public String getTargetName() { return mTargetName; }
	public void setTargetName(String value) { mTargetName = value; }

	public DimensionValue getTargetWidth() { return mTargetWidth; }
	public void setTargetWidth(DimensionValue value) { mTargetWidth = value; }

	public DimensionValue getTargetHeight() { return mTargetHeight; }
	public void setTargetHeight(DimensionValue value) { mTargetHeight = value; }
}
