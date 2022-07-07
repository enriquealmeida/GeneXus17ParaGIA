package com.genexus.android.core.ui.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityHelper;

/**
 * https://code.google.com/p/android/issues/detail?id=63777
 *
 * When using a translucent status bar on API 19+, the window will not
 * resize to make room for input methods (i.e.
 * {@link android.view.WindowManager.LayoutParams#SOFT_INPUT_ADJUST_RESIZE} and
 * {@link android.view.WindowManager.LayoutParams#SOFT_INPUT_ADJUST_PAN} are
 * ignored).
 *
 * To work around this; override {@link #fitSystemWindows(android.graphics.Rect)},
 * capture and override the system insets, and then call through to FrameLayout's
 * implementation.
 *
 * For reasons yet unknown, modifying the bottom inset causes this workaround to
 * fail. Modifying the top, left, and right insets works as expected.
 */
public final class CustomInsetsRelativeLayout extends RelativeLayout
{
	private int[] mInsets = new int[4];

	private boolean mDrawShadow = false;
	private int mElevation = 0;

	public CustomInsetsRelativeLayout(Context context)
	{
		super(context);
	}

	public CustomInsetsRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public final int[] getInsets() {
		return mInsets;
	}

	// Intentionally do not modify the bottom inset. For some reason,
	// if the bottom inset is modified, window resizing stops working.
	@Override
	@SuppressWarnings("deprecation")
	public final WindowInsets onApplyWindowInsets(WindowInsets insets)
	{
		mInsets[0] = insets.getSystemWindowInsetLeft();
		mInsets[1] = insets.getSystemWindowInsetTop();
		mInsets[2] = insets.getSystemWindowInsetRight();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
			insets = new WindowInsets.Builder().setSystemWindowInsets(Insets.of(0, 0, 0, insets.getSystemWindowInsetBottom())).build();
		else
			insets = insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom());
		return super.onApplyWindowInsets(insets);
	}

	public void setElevation(int elevation)
	{
		this.mElevation = elevation;
	}

	public boolean getDrawShadow()
	{
		return mDrawShadow;
	}

	public void setDrawShadow(boolean drawShadow)
	{
		this.mDrawShadow = drawShadow;
		if (drawShadow)
			setWillNotDraw(false); // need to draw for action bar shadow
		else
			setWillNotDraw(true); // no need to draw action bar shadow
	}

	private Activity getActivity()
	{
		Context host = this.getContext();
		if (host instanceof Activity)
		{
			return (Activity) host;
		}
		return null;
	}

	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (mElevation>0 && getDrawShadow() &&
				getActivity()!=null && ActivityHelper.hasActionBar(getActivity()))
		{
			// draw action bar "shadow" in this container
			TypedArray a = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarShadowBackground});
			int attributeResourceId = a.getResourceId(0, 0);
			Drawable windowContentOverlay = ContextCompat.getDrawable(getContext(), attributeResourceId);

			if (windowContentOverlay != null)
			{
				View mActionBarTop = this.findViewById(R.id.action_bar_toolbar);
				if (mActionBarTop != null)
				{
					final int top = mActionBarTop.getVisibility() == VISIBLE ?
							(int) (mActionBarTop.getBottom() + mActionBarTop.getTranslationY() + 0.5f)
							: 0;

					windowContentOverlay.setBounds(0, top, getWidth(), top + mElevation);
					windowContentOverlay.draw(canvas);
				}
			}
		}

	}
}
