package com.genexus.android.core.controls;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;

public class GxImageViewDataCustom extends GxImageViewData
{
	private View mCustomView;

	public GxImageViewDataCustom(Context context, LayoutItemDefinition layoutItem)
	{
		super(context, layoutItem);
	}

	protected void removeCustomView() {
		if (mCustomView != null) {
			removeView(mCustomView);
			mCustomView = null;
		}
	}

	public void setCustomView(View mapView) {
		// remove imageView if exists
		removeImageView();

		// remove older custom View
		removeCustomView();

		//Add Custom View
		ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		addView((View)mapView, params);
		mCustomView = mapView;
	}
}
