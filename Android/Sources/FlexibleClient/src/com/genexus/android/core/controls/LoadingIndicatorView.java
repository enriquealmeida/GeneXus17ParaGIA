package com.genexus.android.core.controls;

import java.util.HashMap;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

/**
 * View for a "Loading Indicator" inside a layout. Follows the "Activity Circle" guidelines.
 * See http://developer.android.com/design/building-blocks/progress.html
 */
public class LoadingIndicatorView extends RelativeLayout
{
	private View mLoading;
	private Style mStyle;
	private ViewProvider mCurrentViewProvider;
	private String mCurrentThemeName;

	private GxTextBlockTextView mTextView;

	private static ViewProvider sViewDefaultProvider = new DefaultViewProvider();

	private static HashMap<String, ViewProvider> sViewProviders = new HashMap<>();

	public static void registerLoadingViewProvider(@NonNull ViewProvider provider)
	{
		sViewProviders.put(provider.getType(), provider);
	}

	public enum Style
	{
		SMALL, LARGE
	}

	public interface ViewProvider
	{
		View newLoadingView(@NonNull Context context, @NonNull Style style, @NonNull LayoutParams layoutParams, String animationName);

		String getType();
	}

	private static class DefaultViewProvider implements ViewProvider
	{
		@Override
		public String getType()
		{
			return "default";
		}

		@Override
		public View newLoadingView(@NonNull Context context, @NonNull Style style, @NonNull LayoutParams layoutParams, String animationName)
		{
			int defStyleAttr = (style == Style.LARGE ? android.R.attr.progressBarStyleLarge : android.R.attr.progressBarStyle);

			ProgressBar circle = new ProgressBar(context, null, defStyleAttr);
			circle.setIndeterminate(true);

			return circle;
		}
	}

	public LoadingIndicatorView(Context context)
	{
		super(context);
		initialize();
	}

	public LoadingIndicatorView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}

	public LoadingIndicatorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}

	private void initialize()
	{
		setStyle(Style.LARGE);

		mTextView = new GxTextBlockTextView(getContext(), null, android.R.style.TextAppearance_Material);
		mTextView.setVisibility(View.GONE);
		addView(mTextView, getCenteredLayoutParams());
	}

	private LayoutParams getCenteredLayoutParams()
	{
		LayoutParams centered = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		centered.addRule(RelativeLayout.CENTER_IN_PARENT);

		if (!isInEditMode())
		{
			int pixelMargin = Services.Device.dipsToPixels(10);
			centered.topMargin = pixelMargin;
			centered.bottomMargin = pixelMargin;
			centered.leftMargin = pixelMargin;
			centered.rightMargin = pixelMargin;
		}

		return centered;
	}

	public void setStyle(@NonNull Style style)
	{
		if (mLoading == null || mStyle != style)
		{
			if (mLoading != null)
				removeView(mLoading);

			// Ask the provider to create the view (and give it a chance to modify the LayoutParams)
			LayoutParams lp = getCenteredLayoutParams();
			mLoading = sViewDefaultProvider.newLoadingView(getContext(), style, lp, Strings.EMPTY);
			addView(mLoading, lp);

			mStyle = style;
		}
	}

	public void setThemeClass(ThemeClassDefinition themeClassDefinition) {
		// apply loading provider from theme if exits.
		if (themeClassDefinition != null) {
			// get animation from theme class and use this provider is exists and is new.
			if (sViewProviders.containsKey(themeClassDefinition.getAnimationType())) {
				ViewProvider newProvider = sViewProviders.get(themeClassDefinition.getAnimationType());
				if (newProvider != null && !newProvider.equals(mCurrentViewProvider)) {
					// Ask the provider to create the view (and give it a chance to modify the LayoutParams)
					LayoutParams lp = getCenteredLayoutParams();
					String animationName = themeClassDefinition.getName();
					View loadingView = newProvider.newLoadingView(getContext(), mStyle, lp, animationName);
					if (loadingView == null) {
						Services.Log.error(String.format("'%s' animation couldn't be loaded", animationName));
						return;
					}

					if (mLoading != null)
						removeView(mLoading);

					addView(loadingView, lp);
					mCurrentViewProvider = newProvider;
					mCurrentThemeName = themeClassDefinition.getName();
					mLoading = loadingView;
				}
			}
		}
	}

	public void setText(CharSequence text)
	{
		mLoading.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setText(text);
	}

	public void releaseLoadingView()
	{
		// release if using custom provider.
		if (mLoading != null && mCurrentViewProvider != null)
		{
			removeView(mLoading);

			// Ask the default provider to create the view
			LayoutParams lp = getCenteredLayoutParams();
			mLoading = sViewDefaultProvider.newLoadingView(getContext(), mStyle, lp, Strings.EMPTY);
			addView(mLoading, lp);
		}
	}

	public void restoreLoadingView()
	{
		if (mLoading != null && mCurrentViewProvider != null)
		{
			removeView(mLoading);

			// Ask the current provider to create the view
			LayoutParams lp = getCenteredLayoutParams();
			mLoading = mCurrentViewProvider.newLoadingView(getContext(), mStyle, lp, mCurrentThemeName);
			addView(mLoading, lp);
		}
	}
}
