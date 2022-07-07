package com.genexus.android;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Misc View utilities.
 */
public class ViewUtil
{
	public static void runWhenMeasured(@NonNull final View view, @NonNull final Runnable runnable)
	{
		if (view.getMeasuredWidth() > 0 && view.getMeasuredHeight() > 0)
		{
			runnable.run();
		}
		else
		{
			// Wait until the width is known.
			view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
			{
				@Override
				public void onGlobalLayout()
				{
					if (view.getMeasuredWidth() > 0 && view.getMeasuredHeight() > 0)
					{
						view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						runnable.run();
					}
				}
			});
		}
	}
}
