package com.genexus.android.ui.animation;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.ActivityResources;
import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;
import com.genexus.android.core.controls.ProgressDialogFactory;

import static com.genexus.android.core.controls.ProgressDialogFactory.TYPE_DETERMINATE;
import static com.genexus.android.core.controls.ProgressDialogFactory.TYPE_INDETERMINATE;
import static com.genexus.android.core.controls.ProgressDialogFactory.applyThemeToIndicator;

/**
 * Loading Animation provider using the Lottie library.
 */
class ProgressDialogAnimationProvider implements ProgressDialogFactory.ProgressViewProvider {

	@Override
	public String getType() {
		return "idLottie";
	}

	@Override
	public void setAnimationName(Activity activity, String animationName) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		progressIndicatorData.AnimationName = animationName;
		updateIndicator(progressIndicatorData);
	}

	@Override
	public void showProgressIndicator(@NonNull Activity activity, String title, String description) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

		// if activity is Finishing cannot show the dialog.
		if (activity.isFinishing()) {
			return;
		}

		if (title != null)
			progressIndicatorData.Title = title;
		if (description != null)
			progressIndicatorData.Description = description;

		// show indicato with new paramters
		showIndicator(activity, progressIndicatorData);
	}

	@Override
	public void hideProgressIndicator(Activity activity) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		ProgressDialogFactory.hideIndicator(progressIndicatorData);
	}

	@Override
	public void setTitle(Activity activity, String title) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		progressIndicatorData.Title = title;
		updateIndicator(progressIndicatorData);
	}

	@Override
	public void setMessage(Activity activity, String description) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		progressIndicatorData.Description = description;
		updateIndicator(progressIndicatorData);
	}

	@Override
	public void setProgressType(Activity activity, int progressDialogStyle) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		progressIndicatorData.Type = progressDialogStyle;
		updateIndicator(progressIndicatorData);
	}

	@Override
	public void setMaxValue(Activity activity, int maxValue) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		progressIndicatorData.MaxValue = maxValue;
		updateIndicator(progressIndicatorData);
	}

	@Override
	public void setValue(Activity activity, int value) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		progressIndicatorData.Value = value;
		updateIndicator(progressIndicatorData);
	}

	@Override
	public ProgressDialogFactory.ProgressIndicatorData getCurrentIndicatorData(Activity activity) {
		return getCurrentIndicator(activity);
	}

	@Override
	public void updateProgressIndicator(Activity activity) {
		ProgressDialogFactory.ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);
		updateIndicator(progressIndicatorData);
	}

	public void showIndicator(@NonNull Activity activity, final ProgressDialogFactory.ProgressIndicatorData data) {
		Services.Device.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Hide previous dialog, if any.
				ProgressDialogFactory.hideIndicator(data);

				// Create dialog from configured data (and store it for later operations).
				data.Dialog = new CustomProgressDialog(activity);
				data.Dialog.setCancelable(false);
				updateIndicator(data);

				// Show the new dialog.
				data.Dialog.show();

				int width = Services.Device.dipsToPixels(124);
				int height = Services.Device.dipsToPixels(124);

				// set the size when only a lottie is showing
				if (data.showOnlyAnimation() && data.Dialog.getWindow() != null) {
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(data.Dialog.getWindow().getAttributes());
					// show loading in fixed size
					//TODO: set size from theme if setted?
					if (data.ProgressThemeClassDefinition != null) {
						ThemeClassDefinition animationClass = data.ProgressThemeClassDefinition.getProgressThemeAnimationClass();
						if (animationClass != null) {
							DimensionValue valueWidth = animationClass.getAnimationWidth();
							DimensionValue valueHeight = animationClass.getAnimationHeight();

							if (valueWidth != null || valueHeight != null) {
								//set dimention value.
								int displayWidth = AdaptersHelper.getWindowWidth(activity);
								int displayHeight = AdaptersHelper.getWindowHeight(activity, null);

								if (valueWidth != null)
									width = MathUtils.round(DimensionValue.toPixels(valueWidth, displayWidth));
								if (valueHeight != null)
									height = MathUtils.round(DimensionValue.toPixels(valueHeight, displayHeight));

							}
						}
					}
					lp.width = width;
					lp.height = height; //Controlling width and height.
					data.Dialog.getWindow().setAttributes(lp);
				}
				applyThemeToIndicator(data.ProgressThemeClassDefinition, data.Dialog, data.showOnlyAnimation());
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void updateIndicator(final ProgressDialogFactory.ProgressIndicatorData data) {
		final AlertDialog dialog = data.Dialog;
		if (dialog == null)
			return;

		Services.Device.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.setTitle(data.Title);
				dialog.setMessage(data.Description);
				if (dialog instanceof CustomProgressDialog) {
					CustomProgressDialog customProgressDialog = (CustomProgressDialog) dialog;
					customProgressDialog.setProgressStyle(data.Type == TYPE_DETERMINATE ? android.app.ProgressDialog.STYLE_HORIZONTAL : android.app.ProgressDialog.STYLE_SPINNER);
					customProgressDialog.setIndeterminate(data.Type == TYPE_INDETERMINATE);
					customProgressDialog.setMax(data.MaxValue);
					customProgressDialog.setProgress(data.Value);

					if (Services.Strings.hasValue(data.AnimationName)) {
						if (data.Type == TYPE_DETERMINATE)
							customProgressDialog.setGxAnimation(data.AnimationName, false);
						else
							customProgressDialog.setGxAnimation(data.AnimationName, true);
						customProgressDialog.playAnimation();
					}
				}

			}
		});
	}

	private ProgressDialogFactory.ProgressIndicatorData getCurrentIndicator(Activity activity) {
		// Get the current indicator or create a new one.
		return ActivityResources.getResource(
				activity,
				ProgressDialogFactory.ProgressIndicatorData.class,
				activity1 -> new ProgressDialogFactory.ProgressIndicatorData());
	}
}
