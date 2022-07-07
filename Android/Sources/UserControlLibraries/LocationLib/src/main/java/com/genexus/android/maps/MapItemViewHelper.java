package com.genexus.android.maps;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.resources.BuiltInResources;

public class MapItemViewHelper
{
	private final ViewGroup mMapView;
	private RelativeLayout mItemViewContainer;
	private final int mBackgroundColor;

	private static final int MARKER_INFO_WINDOW_OPACITY = 184; // of 256
	private static final int MARKER_INFO_WINDOW_CORNER_RADIUS = 8; // px
	public static final float MARKER_INFO_WINDOW_OFF_CENTER_FACTOR = 0.1f; // 15%
	private static final int MARKER_INFO_WINDOW_BOTTOM_MARGIN = 50; // dips

	@SuppressWarnings("deprecation")
	public MapItemViewHelper(ViewGroup mapView)
	{
		mMapView = mapView;

		int backgroundColorResource = BuiltInResources.getResource(mapView.getContext(), android.R.color.background_dark, android.R.color.background_light, android.R.color.background_light);
		mBackgroundColor = mapView.getContext().getResources().getColor(backgroundColorResource);
	}

	private Context getContext()
	{
		return mMapView.getContext();
	}

	public void displayItem(View itemView)
	{
		if (mItemViewContainer == null)
		{
			mItemViewContainer = new RelativeLayout(getContext());
			mItemViewContainer.setBackgroundColor(Color.TRANSPARENT);
			mMapView.addView(mItemViewContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}
		else
			mItemViewContainer.removeAllViews();

		// Use a semi-transparent background for the item.
		LinearLayout itemViewHolder = new LinearLayout(getContext());

		GradientDrawable itemViewBackground = new GradientDrawable();
		itemViewBackground.setAlpha(MARKER_INFO_WINDOW_OPACITY);
		itemViewBackground.setColor(mBackgroundColor);
		itemViewBackground.setCornerRadius(MARKER_INFO_WINDOW_CORNER_RADIUS);
		itemViewHolder.setBackground(itemViewBackground);

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(5, 5, 5, 5);
		itemViewHolder.addView(itemView, llp);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
		rlp.bottomMargin = (int)(mMapView.getHeight() * (0.5 - MARKER_INFO_WINDOW_OFF_CENTER_FACTOR)) + Services.Device.dipsToPixels(MARKER_INFO_WINDOW_BOTTOM_MARGIN);

		mItemViewContainer.addView(itemViewHolder, rlp);
	}

	public void removeCurrentItem()
	{
		// Remove item view when the map is scrolled.
		if (mItemViewContainer != null && mItemViewContainer.getChildCount() != 0)
			mItemViewContainer.removeAllViews();
	}
}
