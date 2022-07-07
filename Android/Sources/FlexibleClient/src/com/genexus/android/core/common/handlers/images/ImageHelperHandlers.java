package com.genexus.android.core.common.handlers.images;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.controls.GxGradientDrawable;
import com.genexus.android.core.controls.common.IViewDisplayImage;

import org.jetbrains.annotations.Nullable;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;

import static com.genexus.android.core.utils.ThemeUtils.PROPERTY_APPLICATION_BAR_ALIGNMENT;
import static com.genexus.android.core.utils.ThemeUtils.VALUE_APPBAR_ALIGN_CENTER_SCREEN;
import static com.genexus.android.core.utils.ThemeUtils.VALUE_APPBAR_ALIGN_CENTER_SPACE;
import static com.genexus.android.core.utils.ThemeUtils.VALUE_APPBAR_ALIGN_RIGHT;

public class ImageHelperHandlers {
	public static class SetViewBackgroundHandler extends ImagesForPostOnUiThread {
		private final View mView;

		public SetViewBackgroundHandler(View view) {
			mView = view;
		}

		@Override
		protected void posted(@Nullable Drawable d) {
			if (d != null)
				mView.setBackground(d);
		}
	}

	public static class SetViewBackgroundWithClassHandler extends ImagesForPostOnUiThread {
		private final View mView;
		private final ThemeClassDefinition mClass;

		public SetViewBackgroundWithClassHandler(View view, ThemeClassDefinition themeClassDefinition) {
			mView = view;
			mClass = themeClassDefinition;
		}

		@Override
		protected void posted(@Nullable Drawable d) {
			Drawable drawable = d;
			if (drawable != null) {
				if (mClass != null) {
					GxGradientDrawable gradientDrawable = new GxGradientDrawable();
					gradientDrawable.setThemeClass(mView.getContext(), mClass, false, null, null);
					gradientDrawable.setBackground(drawable);
					drawable = gradientDrawable;
				}

				mView.setBackground(drawable);
			}
		}
	}

	public static class SetImageViewHandler extends ImagesForPostOnUiThread {
		private final IViewDisplayImage mView;

		public SetImageViewHandler(IViewDisplayImage view) {
			mView = view;
		}

		@Override
		protected void posted(@Nullable Drawable d) {
			if (d != null)
				mView.setImageDrawable(d);
		}
	}

	public static class SetActionBarHomeAsUpIndicatorHandler extends ImagesForPostOnUiThread {
		private final ActionBar mBar;

		public SetActionBarHomeAsUpIndicatorHandler(ActionBar bar) {
			mBar = bar;
		}

		@Override
		protected void posted(@Nullable Drawable drawable) {
			if (drawable != null) mBar.setHomeAsUpIndicator(drawable);
		}
	}

	public static class SetActionBarIconHandler extends ImagesForPostOnUiThread {
		private final ActionBar mBar;

		public SetActionBarIconHandler(ActionBar bar) {
			mBar = bar;
		}

		@Override
		protected void posted(@Nullable Drawable drawable) {
			if (drawable != null) mBar.setIcon(drawable);
		}
	}

	public static class SetActionBarTitleImageHandler extends ImagesForPostOnUiThread {
		private final Context mContext;
		private final ActionBar mBar;
		private final ThemeClassDefinition mClass;

		public SetActionBarTitleImageHandler(Context context, ActionBar bar, ThemeClassDefinition appBarClass) {
			mContext = context;
			mBar = bar;
			mClass = appBarClass;
		}

		@Override
		protected void posted(@Nullable Drawable drawable) {
			if (drawable == null) {
				mBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
				return;
			}

			ImageView imageView = new ImageView(mContext);
			imageView.setImageDrawable(drawable);
			imageView.setAdjustViewBounds(true);

			ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

			// set gravity using custom aligns
			final String alignment = mClass.optStringProperty(PROPERTY_APPLICATION_BAR_ALIGNMENT);
			if (alignment.equalsIgnoreCase(VALUE_APPBAR_ALIGN_RIGHT))
				params.gravity = GravityCompat.END | Gravity.CENTER_VERTICAL;
			else if (alignment.equalsIgnoreCase(VALUE_APPBAR_ALIGN_CENTER_SPACE) ||
					alignment.equalsIgnoreCase(VALUE_APPBAR_ALIGN_CENTER_SCREEN))
				params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
			else
				params.gravity = GravityCompat.START | Gravity.CENTER_VERTICAL; // default and pd = left

			mBar.setCustomView(imageView, params);

			// Show image, hide title
			mBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		}
	}
}
