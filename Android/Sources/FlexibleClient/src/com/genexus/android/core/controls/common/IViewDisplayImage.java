package com.genexus.android.core.controls.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;

public interface IViewDisplayImage
{
	void setImageBitmap(Bitmap bm);
	void setImageDrawable(Drawable drawable);
	void setImageResource(int resId);

	// For SVG, the setLayerType(View.LAYER_TYPE_SOFTWARE) call is necessary in order to set the
	// View into software rendering mode. The reason is that from ICS onwards, Android enables
	// hardware rendering by default. The hardware renderer does not yet support the
	// Canvas.drawPicture() method, https://bigbadaboom.github.io/androidsvg/use_with_ImageView.html
	void setLayerType(int layerType, @Nullable Paint paint);

	Context getContext();
	String getImageTag();
	void setImageTag(String tag);
	ThemeClassDefinition getThemeClass();
}
