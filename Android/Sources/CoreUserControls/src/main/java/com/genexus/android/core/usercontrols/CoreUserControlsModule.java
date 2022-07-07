package com.genexus.android.core.usercontrols;

import android.content.Context;

import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.LaunchScreenViewProviderFactory;
import com.genexus.android.core.controls.video.GxVideoView;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.gauge.RangeControl;
import com.genexus.android.core.usercontrols.image.GxAdvancedImage;
import com.genexus.android.core.usercontrols.image.GxImageAnnotations;
import com.genexus.android.core.usercontrols.imagemap.GxImageMap;
import com.genexus.android.core.usercontrols.rating.RatingControl;
import com.genexus.android.core.usercontrols.sparkline.GxSparkLine;
import com.genexus.android.core.usercontrols.imagegallery.ImageGallery;
import com.genexus.android.core.usercontrols.matrixgrid.MatrixGrid;

public class CoreUserControlsModule implements GenexusModule {
	private static final String LANDSCAPE_LAUNCH_IMAGE_NAME = "appwelcomedefaultlandscape";
	private static final String LAUNCH_IMAGE_NAME = "appwelcomedefault";

	@Override
	public void initialize(Context context) {
		UcFactory.addControl(new UserControlDefinition(GxSwitch.NAME, GxSwitch.class));
		UcFactory.addControl(new UserControlDefinition(GxChronometer.NAME, GxChronometer.class));
		UcFactory.addControl(new UserControlDefinition(SeekBarControl.NAME, SeekBarControl.class));
		UcFactory.addControl(new UserControlDefinition(RadioGroupControl.NAME, RadioGroupControl.class));
		UcFactory.addControl(new UserControlDefinition(StaticSpinnerControl.NAME, StaticSpinnerControl.class));
		UcFactory.addControl(new UserControlDefinition(RatingControl.NAME, RatingControl.class));
		UcFactory.addControl(new UserControlDefinition(GxSparkLine.NAME, GxSparkLine.class));
		UcFactory.addControl(new UserControlDefinition(RangeControl.NAME, RangeControl.class));
		UcFactory.addControl(new UserControlDefinition(MatrixGrid.NAME, MatrixGrid.class, true));
		UcFactory.addControl(new UserControlDefinition(GxAdvancedImage.NAME, GxAdvancedImage.class, true));
		UcFactory.addControl(new UserControlDefinition(GxImageAnnotations.NAME, GxImageAnnotations.class));
		UcFactory.addControl(new UserControlDefinition(GxImageMap.NAME, GxImageMap.class));
		UcFactory.addControl(new UserControlDefinition(ImageGallery.NAME, ImageGallery.class));
		UcFactory.addControl(new UserControlDefinition(GxInPlaceDatePicker.NAME, GxInPlaceDatePicker.class));
		UcFactory.addControl(new UserControlDefinition(GxVideoView.NAME, GxVideoView.class));

		int imageResId = 0;
		if (Services.Device.getScreenOrientation() == Orientation.LANDSCAPE)
			imageResId = Services.Resources.getResourceId(LANDSCAPE_LAUNCH_IMAGE_NAME, "drawable");
		if (imageResId <= 0)
			imageResId = Services.Resources.getResourceId(LAUNCH_IMAGE_NAME, "drawable");

		if (imageResId > 0) {
			LaunchScreenViewProviderFactory.setProvider(new StaticImageLaunchScreenViewProvider(imageResId));
		} else {
			if (Services.Device.getScreenOrientation() == Orientation.LANDSCAPE)
				imageResId = Services.Resources.getResourceId(LANDSCAPE_LAUNCH_IMAGE_NAME, "raw");
			if (imageResId <= 0)
				imageResId = Services.Resources.getResourceId(LAUNCH_IMAGE_NAME, "raw");
			if (imageResId > 0)
				LaunchScreenViewProviderFactory.setProvider(new SvgImageLaunchScreenViewProvider(imageResId));
		}
	}
}
