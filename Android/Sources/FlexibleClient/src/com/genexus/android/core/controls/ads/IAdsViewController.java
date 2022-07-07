package com.genexus.android.core.controls.ads;

import androidx.annotation.Nullable;
import android.view.View;

import com.genexus.android.core.base.controls.IGxControlRuntime;

/**
 * Ad View Controller interface.
 */
public interface IAdsViewController extends IGxControlRuntime
{
	@Nullable View createView();
	void setViewSize(int width, int height);
}
