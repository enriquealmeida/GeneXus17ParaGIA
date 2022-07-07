package com.genexus.android.ui.animation;

import java.text.NumberFormat;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.genexus.android.core.base.services.Services;

/**
 * <p>A dialog showing a progress indicator and an optional text message or view.
 * Only a text message or a view can be used at the same time.</p>
 * <p>The dialog can be made cancelable on back key press.</p>
 * <p>The progress range is 0..10000.</p>
 */
public class CustomProgressDialog extends AlertDialog {
	/**
	 * Creates a ProgressDialog with a circular, spinning progress
	 * bar. This is the default.
	 */
	public static final int STYLE_SPINNER = 0;

	/**
	 * Creates a ProgressDialog with a horizontal progress bar.
	 */
	public static final int STYLE_HORIZONTAL = 1;

	private ProgressBar mProgress;
	private TextView mMessageView;

	private int mProgressStyle = STYLE_SPINNER;
	private TextView mProgressNumber;
	private String mProgressNumberFormat;
	private TextView mProgressPercent;
	private NumberFormat mProgressPercentFormat;

	private int mMax;
	private int mProgressVal;
	private int mSecondaryProgressVal;
	private int mIncrementBy;
	private int mIncrementSecondaryBy;
	private Drawable mProgressDrawable;
	private Drawable mIndeterminateDrawable;
	private CharSequence mMessage;
	private boolean mIndeterminate;
	private CharSequence mTitle;

	private boolean mHasStarted;
	private Handler mViewUpdateHandler;

	private final Context mContext;

	private LottieAnimationView mCustomProgress;
	//private LinearLayout mBody;

	private String mAnimation;
	private boolean mLoop;

	public CustomProgressDialog(Context context) {
		super(context);
		mContext = context;
		initFormats();
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
		initFormats();
	}

	private void initFormats() {
		mProgressNumberFormat = "%1d/%2d";
		mProgressPercentFormat = NumberFormat.getPercentInstance();
		mProgressPercentFormat.setMaximumFractionDigits(0);
	}

	public static CustomProgressDialog show(Context context, CharSequence title,
											CharSequence message) {
		return show(context, title, message, false);
	}

	public static CustomProgressDialog show(Context context, CharSequence title,
											CharSequence message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}

	public static CustomProgressDialog show(Context context, CharSequence title,
											CharSequence message, boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable, null);
	}

	public static CustomProgressDialog show(Context context, CharSequence title,
											CharSequence message, boolean indeterminate,
											boolean cancelable, OnCancelListener cancelListener) {
		CustomProgressDialog dialog = new CustomProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.show();
		return dialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		//TypedArray a = mContext.obtainStyledAttributes(null,
		//        com.android.internal.R.styleable.AlertDialog,
		//        com.android.internal.R.attr.alertDialogStyle, 0);

		// horizontal mode not supported
		// only indeterminate vertical layout.

		// only use local layout...
		//View view = inflater.inflate(a.getResourceId(
		//            com.android.internal.R.styleable.AlertDialog_progressLayout,
		//            R.layout.custom_progress_dialog), null);

		View view = inflater.inflate(R.layout.custom_progress_dialog, null);

		boolean showOnlyAnimation = showOnlyAnimation();
		// use another layout if use show only the animation.
		if (showOnlyAnimation)
			view = inflater.inflate(R.layout.custom_progress_dialog_loading, null);

		mCustomProgress = view.findViewById(R.id.customprogress);
		mMessageView = view.findViewById(R.id.message);

		if (!showOnlyAnimation)
			setView(view);

		//a.recycle();

		if (mMax > 0) {
			setMax(mMax);
		}
		if (mProgressVal > 0) {
			setProgress(mProgressVal);
		}
		if (mSecondaryProgressVal > 0) {
			setSecondaryProgress(mSecondaryProgressVal);
		}
		if (mIncrementBy > 0) {
			incrementProgressBy(mIncrementBy);
		}
		if (mIncrementSecondaryBy > 0) {
			incrementSecondaryProgressBy(mIncrementSecondaryBy);
		}
		if (mProgressDrawable != null) {
			setProgressDrawable(mProgressDrawable);
		}
		if (mIndeterminateDrawable != null) {
			setIndeterminateDrawable(mIndeterminateDrawable);
		}
		if (mMessage != null) {
			setMessage(mMessage);
		}


		if (mAnimation != null) {
			setGxAnimation(mAnimation, mLoop);
			playAnimation();
		}
		setIndeterminate(mIndeterminate);
		onProgressChanged();
		super.onCreate(savedInstanceState);

		if (showOnlyAnimation())
			setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	@Override
	public void onStart() {
		super.onStart();
		mHasStarted = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHasStarted = false;
	}

	public void setProgress(int value) {
		if (mHasStarted) {
			if (mProgress != null) {
				mProgress.setProgress(value);
				onProgressChanged();
			} else {
				mProgressVal = value;
			}
		} else {
			mProgressVal = value;
		}
	}

	public void setSecondaryProgress(int secondaryProgress) {
		if (mProgress != null) {
			mProgress.setSecondaryProgress(secondaryProgress);
			onProgressChanged();
		} else {
			mSecondaryProgressVal = secondaryProgress;
		}
	}

	public int getProgress() {
		if (mProgress != null) {
			return mProgress.getProgress();
		}
		return mProgressVal;
	}

	public int getSecondaryProgress() {
		if (mProgress != null) {
			return mProgress.getSecondaryProgress();
		}
		return mSecondaryProgressVal;
	}

	public int getMax() {
		if (mProgress != null) {
			return mProgress.getMax();
		}
		return mMax;
	}

	public void setMax(int max) {
		if (mProgress != null) {
			mProgress.setMax(max);
			onProgressChanged();
		} else {
			mMax = max;
		}
	}

	public void incrementProgressBy(int diff) {
		if (mProgress != null) {
			mProgress.incrementProgressBy(diff);
			onProgressChanged();
		} else {
			mIncrementBy += diff;
		}
	}

	public void incrementSecondaryProgressBy(int diff) {
		if (mProgress != null) {
			mProgress.incrementSecondaryProgressBy(diff);
			onProgressChanged();
		} else {
			mIncrementSecondaryBy += diff;
		}
	}

	public void setProgressDrawable(Drawable d) {
		if (mProgress != null) {
			mProgress.setProgressDrawable(d);
		} else {
			mProgressDrawable = d;
		}
	}

	public void setIndeterminateDrawable(Drawable d) {
		if (mProgress != null) {
			mProgress.setIndeterminateDrawable(d);
		} else {
			mIndeterminateDrawable = d;
		}
	}

	public void setIndeterminate(boolean indeterminate) {
		if (mProgress != null) {
			mProgress.setIndeterminate(indeterminate);
		} else {
			mIndeterminate = indeterminate;
		}
	}

	public boolean isIndeterminate() {
		if (mProgress != null) {
			return mProgress.isIndeterminate();
		}
		return mIndeterminate;
	}

	@Override
	public void setMessage(CharSequence message) {
		if (mMessageView != null) {
			mMessageView.setText(message);

			// Visibility of message part
			//if (message==null || message.length()==0)
			//	mMessageView.setVisibility(View.GONE);
			//else
			//	mMessageView.setVisibility(View.VISIBLE);

		} else {
			mMessage = message;
		}

	}

	public void setProgressStyle(int style) {
		mProgressStyle = style;
	}

	/**
	 * Change the format of the small text showing current and maximum units
	 * of progress.  The default is "%1d/%2d".
	 * Should not be called during the number is progressing.
	 *
	 * @param format A string passed to {@link String#format String.format()};
	 *               use "%1d" for the current number and "%2d" for the maximum.  If null,
	 *               nothing will be shown.
	 */
	public void setProgressNumberFormat(String format) {
		mProgressNumberFormat = format;
		onProgressChanged();
	}

	/**
	 * Change the format of the small text showing the percentage of progress.
	 * The default is
	 * {@link NumberFormat#getPercentInstance() NumberFormat.getPercentageInstnace().}
	 * Should not be called during the number is progressing.
	 *
	 * @param format An instance of a {@link NumberFormat} to generate the
	 *               percentage text.  If null, nothing will be shown.
	 */
	public void setProgressPercentFormat(NumberFormat format) {
		mProgressPercentFormat = format;
		onProgressChanged();
	}

	private void onProgressChanged() {
		if (mProgressStyle == STYLE_HORIZONTAL) {
			if (mViewUpdateHandler != null && !mViewUpdateHandler.hasMessages(0)) {
				mViewUpdateHandler.sendEmptyMessage(0);
			}
		}
		if (!mIndeterminate) {
			playAnimation();
		}
	}

	// animation methods
	public void setGxAnimation(String animationName, boolean looping) {
		// animation should be a .json file in assets
		String fileName = Services.Assets.getAnimationFilename(animationName);
		if (mCustomProgress != null) {
			mCustomProgress.setAnimation(fileName); // Will throw exception if animation is not in assets.

			// default scale properties.

			mCustomProgress.setScale(0.1f);
			mCustomProgress.setScaleType(ImageView.ScaleType.FIT_CENTER);

			mCustomProgress.setRepeatCount(looping ? ValueAnimator.INFINITE : 0);

		} else {
			mAnimation = animationName;
			mLoop = looping;
		}
	}

	public void playAnimation() {
		if (mCustomProgress != null) {
			if (!mIndeterminate) {
				float currentEnd = (float) (mProgressVal + 1) / mMax;
				mCustomProgress.setMaxProgress(currentEnd);
			}
			mCustomProgress.playAnimation();
		}
	}

	private boolean showOnlyAnimation() {
		return !(Services.Strings.hasValue(mTitle)
				|| Services.Strings.hasValue(mMessage));
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		mTitle = title;
	}
}
