package com.genexus.android.core.usercontrols.image;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.ControlPropertiesDefinition;

class GxAdvancedImageDefinition extends ControlPropertiesDefinition
{
	private boolean mEnableCopy;
	private boolean mZoomOutsideControl;
	private String mImageMaxZoomRel;
	private float mImageMaxZoom;

	public GxAdvancedImageDefinition(LayoutItemDefinition definition)
	{
		super(definition);
		ControlInfo ci = definition.getControlInfo();

		mEnableCopy = ci.getBooleanProperty("@SDAdvancedImageEnableCopy", ci.optBooleanProperty("@EnableCopy"));
		mZoomOutsideControl = ci.getBooleanProperty("@SDAdvancedImageZoomOutsideControl", ci.optBooleanProperty("@ZoomOutsideControl"));
		mImageMaxZoomRel = ci.getStringProperty("@SDAdvancedImageMaxZoomRel", ci.optStringProperty("@MaxZoomRel"));

		Float maxZoom = Services.Strings.tryParseFloat(ci.getStringProperty("@SDAdvancedImageMaxZoom", ci.optStringProperty("@MaxZoom")));
		if (maxZoom != null)
			mImageMaxZoom = maxZoom / 100f;
		else
			mImageMaxZoom = -1;
	}

	public boolean getEnableCopy()
	{
		return mEnableCopy;
	}

	public boolean getZoomOutsideControl() {
		return mZoomOutsideControl;
	}

	public float getImageMaxZoom()
	{
		return mImageMaxZoom;
	}

	public String getImageMaxZoomRel() {
		return mImageMaxZoomRel;
	}
}
