package com.genexus.android.core.controls;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.Action;
import com.genexus.android.ActivityResourceBase;
import com.genexus.android.ActivityResources;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.utils.ThemeUtils;


/**
 * View for a "Loading Indicator" inside a layout. Follows the "Activity Circle" guidelines.
 * See http://developer.android.com/design/building-blocks/progress.html
 */
public class ProgressDialogFactory
{
	private static ProgressViewProvider mCurrentViewProvider;
	private static ProgressViewProvider sViewDefaultProvider = new DefaultViewProvider();
	private static HashMap<String, ProgressViewProvider> sViewProviders = new HashMap<>();

	public static final int TYPE_INDETERMINATE = 0;
	public static final int TYPE_DETERMINATE = 1;

	public ProgressDialogFactory()
	{

	}

	public static void registerProgressDialogProvider(@NonNull ProgressViewProvider provider)
	{
		sViewProviders.put(provider.getType(), provider);
	}

	public interface ProgressViewProvider
	{
		//void createProgressDialog();
		String getType();

		void setAnimationName(Activity activity, String animationName);

		void showProgressIndicator(@NonNull Activity activity, String title, String description);

		void hideProgressIndicator(Activity activity);

		void setTitle(Activity activity, String title);

		void setMessage(Activity activity, String description);

		void setProgressType(Activity activity, int progressDialogStyle);

		void setMaxValue(Activity activity, int maxValue);

		void setValue(Activity activity, int value);

		ProgressIndicatorData getCurrentIndicatorData(Activity activity);

		void updateProgressIndicator(Activity activity);

	}


	// Provider Class
	public void setThemeClass(@NonNull Activity activity, ThemeClassDefinition themeClassDefinition)
	{
		// apply loading provider from theme if exits.
		if (themeClassDefinition != null)
		{
			if (themeClassDefinition.getProgressThemeAnimationClass() != null)
			{
				ThemeClassDefinition progressAnimation = themeClassDefinition.getProgressThemeAnimationClass();
				// Only use lottie provider if class has a lottie animation setted.
				// get animation from theme class and use this provider is exists and is new.
				if (sViewProviders.containsKey(progressAnimation.getAnimationType()))
				{
					ProgressViewProvider newProvider = sViewProviders.get(progressAnimation.getAnimationType());

					if (newProvider != null && !newProvider.equals(mCurrentViewProvider))
					{
						// when change provider copy data from the old one , to the new one.
						mCurrentViewProvider = newProvider;

						mCurrentViewProvider.setAnimationName(activity, progressAnimation.getName());

						//  set animationclass, fonts, etc from theme to current provider.
						mCurrentViewProvider.getCurrentIndicatorData(activity).ProgressThemeClassDefinition = themeClassDefinition;
					}
					else if (newProvider!=null)
					{
						//same provider , update data.
						mCurrentViewProvider.setAnimationName(activity, progressAnimation.getName());
						//  set animationclass, fonts, etc from theme to current provider.
						mCurrentViewProvider.getCurrentIndicatorData(activity).ProgressThemeClassDefinition = themeClassDefinition;
					}

					return;
				}
			}

			// if theme class dont have provider , return to default.
			if (mCurrentViewProvider != null)
			{
				mCurrentViewProvider = null;

				// set animation class, fonts, etc from theme to current default provider.
				sViewDefaultProvider.getCurrentIndicatorData(activity).ProgressThemeClassDefinition = themeClassDefinition;
			}
		}

	}

	public ProgressViewProvider getViewProvider()
	{
		if (mCurrentViewProvider != null)
			return mCurrentViewProvider;
		return sViewDefaultProvider;

	}


	private static class DefaultViewProvider implements ProgressViewProvider
	{
		//private ProgressDialog progressDialog = null;
		//private ProgressIndicatorData progressIndicatorData = null;

		@Override
		public String getType()
		{
			return "default";
		}


		@Override
		public void showProgressIndicator(Activity activity, String title, String description)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			// if activity is Finishing cannot show the dialog.
			if (activity!=null && activity.isFinishing())
			{
				return;
			}

			if (title != null)
				progressIndicatorData.Title = title;
			if (description != null)
				progressIndicatorData.Description = description;

			// show indicato with new paramters
			showIndicator(activity, progressIndicatorData);

			// Show the new dialog.
			//if (progressDialog!=null)
			//	progressDialog.show();
		}

		@Override
		public void hideProgressIndicator(Activity activity)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			hideIndicator(progressIndicatorData);
		}

		@Override
		public void setAnimationName(Activity activity, String animationName)
		{
			// do nothing here , in this default provider.
		}

		@Override
		public void setTitle(Activity activity, String title)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			progressIndicatorData.Title = title;

			updateIndicator(progressIndicatorData);

		}

		@Override
		public void setMessage(Activity activity, String description)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			progressIndicatorData.Description = description;

			updateIndicator(progressIndicatorData);

		}

		@Override
		public void setProgressType(Activity activity, int progressDialogStyle)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			progressIndicatorData.Type = progressDialogStyle;

			updateIndicator(progressIndicatorData);

		}

		@Override
		public void setMaxValue(Activity activity, int maxValue)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			progressIndicatorData.MaxValue = maxValue;

			updateIndicator(progressIndicatorData);

		}

		@Override
		public void setValue(Activity activity, int value)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			progressIndicatorData.Value = value;

			updateIndicator(progressIndicatorData);

		}

		@Override
		public void updateProgressIndicator(Activity activity)
		{
			ProgressIndicatorData progressIndicatorData = getCurrentIndicator(activity);

			updateIndicator(progressIndicatorData);
		}

		@Override
		public ProgressIndicatorData getCurrentIndicatorData(Activity activity)
		{
			return getCurrentIndicator(activity);
		}

		@SuppressWarnings("deprecation")
		public void showIndicator(@NonNull Activity activity, final ProgressIndicatorData data)
		{
			Services.Device.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					// Hide previous dialog, if any.
					hideIndicator(data);

					// Create dialog from configured data (and store it for later operations).
					data.Dialog = new android.app.ProgressDialog(activity);
					data.Dialog.setCancelable(false);
					updateIndicator(data);

					// Show the new dialog.
					data.Dialog.show();
					Services.Application.getLifecycle().notifyDialogStarted(data.Dialog);
					applyThemeToIndicator(data.ProgressThemeClassDefinition, data.Dialog, false);
				}
			});
		}

		@SuppressWarnings("deprecation")
		public void updateIndicator(final ProgressIndicatorData data)
		{
			final AlertDialog dialog = data.Dialog;
			if (dialog == null)
				return;

			Services.Device.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					dialog.setTitle(data.Title);
					dialog.setMessage(data.Description);
					if (dialog instanceof android.app.ProgressDialog)
					{
						android.app.ProgressDialog progressDialogTemp = (android.app.ProgressDialog) dialog;
						progressDialogTemp.setProgressStyle(data.Type == TYPE_DETERMINATE ? android.app.ProgressDialog.STYLE_HORIZONTAL : android.app.ProgressDialog.STYLE_SPINNER);
						progressDialogTemp.setIndeterminate(data.Type == TYPE_INDETERMINATE);
						progressDialogTemp.setMax(data.MaxValue);
						progressDialogTemp.setProgress(data.Value);

					}

				}
			});
		}


		private static ProgressIndicatorData getCurrentIndicator(Activity activity)
		{
			// Get the current indicator or create a new one.
			return ActivityResources.getResource(activity, ProgressIndicatorData.class,
					new Function<Activity, ProgressIndicatorData>()
					{
						@Override
						public ProgressIndicatorData run(Activity activity)
						{
							return new ProgressIndicatorData();
						}
					});
		}

	}
	// End Default Provider


	// Class for ProgressIndicator info.
	@SuppressWarnings("checkstyle:MemberName")
	public static class ProgressIndicatorData extends ActivityResourceBase
	{
		public int Type = TYPE_INDETERMINATE;
		public String Title = null;
		public String Description = null; //Services.Strings.getResource(R.string.GXM_PleaseWait);
		public int MaxValue = 100;
		public int Value = 0;
		public String AnimationName = null;
		public ThemeClassDefinition ProgressThemeClassDefinition = null;

		public AlertDialog Dialog;
		public String EventName = null;

		@Override
		public void onDestroy(Activity activity)
		{
			// A dialog attached to a destroyed activity causes a memory leak, and will cause a crash if accessed later on.
			if (Dialog != null)
				hideIndicator(this);
		}

		public boolean showOnlyAnimation()
		{
            return !(Services.Strings.hasValue(Title)
                    || Services.Strings.hasValue(Description));
        }

	}


	public static void hideIndicator(final ProgressIndicatorData data)
	{
		final AlertDialog dialog = data.Dialog;
		if (dialog == null)
			return;

		Services.Device.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					dialog.dismiss();
				}
				catch (IllegalArgumentException ex) {
					Services.Log.debug("Cannot dismiss progress dialog " + ex.getMessage());
				}
				Services.Application.getLifecycle().notifyDialogStopped(data.Dialog);
				data.Dialog = null;
			}
		});
	}

	public static void applyThemeToIndicator(ThemeClassDefinition progressThemeClassDefinition, final AlertDialog dialog, boolean showOnlyAnimation )
	{
		if (progressThemeClassDefinition!=null)
		{
			// set Title Color and Font.
			ThemeUtils.setAlertDialogTitleFontProperties(progressThemeClassDefinition, dialog);

			// set Message Color and Font.
			ThemeClassDefinition descriptionThemeClass = progressThemeClassDefinition.getProgressThemeDescriptionClass();
			if (descriptionThemeClass!=null)
			{
				ThemeUtils.setAlertDialogMessageFontProperties(descriptionThemeClass, dialog);
			}

			// set Background.
			String backgroundColor = progressThemeClassDefinition.getProgressThemeBackgroundColor();
			Integer backgroundColorId = ThemeUtils.getColorId(backgroundColor);

			if (backgroundColorId != null && dialog.getWindow()!=null)
			{
				// change background
				dialog.getWindow().getDecorView().setBackgroundColor(backgroundColorId);
			}
		}
	}


	// static methos used from actions
	public static boolean isShowing(Activity activity)
	{
		return getCurrentIndicator(activity).Dialog != null;
	}

	public static void onEndEvent(Action action, boolean success)
	{
		Activity activity = action.getContext().getActivity();

		ProgressDialogFactory progressDialogFactory = new ProgressDialogFactory();
		ProgressDialogFactory.ProgressViewProvider currentProgressProvider = progressDialogFactory.getViewProvider();

		//auto hide is from same event that show it.
		String eventName = null;
		if (action.getDefinition()!=null)
			eventName = action.getDefinition().getName();
		ProgressDialogFactory.ProgressIndicatorData progressData = currentProgressProvider.getCurrentIndicatorData(activity);
		if (progressData!=null && Services.Strings.hasValue(progressData.EventName)) {
			//if are from different events ignore it.
			if (!progressData.EventName.equalsIgnoreCase(eventName))
				return;
		}
		//hide progress
		currentProgressProvider.hideProgressIndicator(activity);
	}

	public static ProgressDialogFactory.ProgressIndicatorData  getCurrentIndicator(Activity activity)
	{
		ProgressDialogFactory progressDialogFactory = new ProgressDialogFactory();
		ProgressDialogFactory.ProgressViewProvider currentProgressProvider = progressDialogFactory.getViewProvider();

		return currentProgressProvider.getCurrentIndicatorData(activity);
	}

}
