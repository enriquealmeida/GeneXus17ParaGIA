package com.genexus.android.core.resources;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.genexus.android.core.base.metadata.enums.ActionTypes;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.GxImageViewData;
import com.genexus.android.core.controls.ImageViewDisplayImageWrapper;
import com.genexus.android.core.controls.common.IViewDisplayImage;

import static com.genexus.android.core.base.metadata.theme.ApplicationClassExtensionKt.PLACEHOLDER_IMAGE;
import static com.genexus.android.core.base.metadata.theme.ApplicationClassExtensionKt.PROMPT_IMAGE;

public class StandardImages
{
	public static void startLoading(IViewDisplayImage view)
	{
		// Clear any previous image.
		view.setImageDrawable(null);

		// For data images, show a progress indicator (maybe).
		if (view instanceof GxImageViewData)
		{
			ThemeClassDefinition imageClass = getImageClass(view, null);
			if (imageClass != null && imageClass.getBooleanProperty("image_loading_indicator", true))
				((GxImageViewData)view).setLoading(true);
		}
	}

	public static void stopLoading(IViewDisplayImage view)
	{
		if (view instanceof GxImageViewData)
			((GxImageViewData)view).setLoading(false);
		else
			view.setImageDrawable(null);
	}

	public static void showPlaceholderImage(IViewDisplayImage view, Drawable drawable, boolean placeholderRequired)
	{
		setImage(view, PLACEHOLDER_IMAGE, placeholderRequired ? drawable : null);
	}

	public static void setLinkImage(ImageView view)
	{
		Drawable defaultImageDrawable = Services.Resources.getContentDrawableFor(view.getContext(), ActionTypes.LINK);
		view.setScaleType(ScaleType.CENTER);
		setImage(ImageViewDisplayImageWrapper.to(view), PROMPT_IMAGE, defaultImageDrawable);
	}

	public static void setPromptImage(ImageView view)
	{
		Drawable defaultImageDrawable = Services.Resources.getContentDrawableFor(view.getContext(), ActionTypes.PROMPT);
		view.setScaleType(ScaleType.CENTER);
		setImage(ImageViewDisplayImageWrapper.to(view), PROMPT_IMAGE, defaultImageDrawable);
	}

	public static void setActionImage(ImageView view, String action)
	{
		Drawable drawable = Services.Resources.getContentDrawableFor(view.getContext(), action);
		view.setScaleType(ScaleType.CENTER);
		setImage(ImageViewDisplayImageWrapper.to(view), null, drawable);
		ImageViewDisplayImageWrapper.to(view).setImageDrawable(drawable);
	}

	private static void setImage(IViewDisplayImage view, String customImageName, Drawable defaultImageDrawable)
	{
		if (customImageName != null) {
			ThemeClassDefinition imageClass = getImageClass(view, customImageName);
			if (imageClass != null) {
				String imageName = imageClass.getImage(customImageName);
				if (Services.Strings.hasValue(imageName)) {
					Services.Images.displayImage(view, imageName);
					return;
				}
			}
		}
		view.setImageDrawable(defaultImageDrawable);
	}

	public static ThemeClassDefinition getImageClass(IViewDisplayImage view, String imageName) {
		// Image properties are read from the specific class image, if set, or from the application class otherwise.
		ThemeClassDefinition imageClass = view.getThemeClass();

		if (imageClass != null && PLACEHOLDER_IMAGE.equals(imageName)) {
			if (TextUtils.isEmpty(imageClass.getImage(imageName)))
				imageClass = Services.Themes.getApplicationClass();
		}

		if (imageClass == null)
			imageClass = Services.Themes.getApplicationClass();

		return imageClass;
	}
}
