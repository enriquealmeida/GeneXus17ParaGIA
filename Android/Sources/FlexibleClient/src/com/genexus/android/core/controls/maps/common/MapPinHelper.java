package com.genexus.android.core.controls.maps.common;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.utils.BitmapUtils;
import com.genexus.android.core.utils.Cast;

public class MapPinHelper {
	public static class ResourceOrBitmap {
		public final Integer resourceId;
		public final Bitmap bitmap;

		public ResourceOrBitmap(int resourceId) {
			this.resourceId = resourceId;
			this.bitmap = null;
		}

		public ResourceOrBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
			this.resourceId = null;
		}

		boolean isNull() {
			return (resourceId == null && bitmap == null);
		}
	}

	private final Context mContext;
	private final GxMapViewDefinition mMapDefinition;

	public MapPinHelper(Context context, GxMapViewDefinition mapDefinition) {
		mContext = context;
		mMapDefinition = mapDefinition;
	}

	public @NonNull
	ResourceOrBitmap getPinImage(Entity item) {
		ResourceOrBitmap pin = loadPinResourceOrBitmap(item);
		return applyPinImageClass(pin);
	}

	private @NonNull
	ResourceOrBitmap loadPinResourceOrBitmap(Entity item) {
		// Try for item's custom  pin image.
		if (Strings.hasValue(mMapDefinition.getPinImageExpression())) {
			String imageValue = Cast.as(String.class, item.getProperty(mMapDefinition.getPinImageExpression()));
			if (Strings.hasValue(imageValue)) {
				// 1) From resources.
				int resourceId = Services.Resources.getDataImageResourceId(imageValue);
				if (resourceId != 0)
					return new ResourceOrBitmap(resourceId);

				// 2) From a bitmap file (either local or in the server).
				Bitmap pinImageBitmap = Services.Images.getBitmap(mContext, imageValue);
				if (pinImageBitmap != null)
					return new ResourceOrBitmap(pinImageBitmap);
			}
		}

		// Try for generic pin item image.
		int pinImageResourceId = mMapDefinition.getPinImageResourceId();
		if (pinImageResourceId != 0)
			return new ResourceOrBitmap(pinImageResourceId);

		// No pin, default will be used.
		return new ResourceOrBitmap(null);
	}

	@SuppressWarnings("deprecation")
	private @NonNull
	ResourceOrBitmap applyPinImageClass(@NonNull ResourceOrBitmap pin) {
		if (pin.isNull())
			return pin;

		GxMapViewDefinition.PinImageProperties properties = mMapDefinition.getPinImageProperties();
		if (properties.width != 0 && properties.height != 0 && properties.scaleType != null) {
			// We need a bitmap to resize, either the bitmap we already had or read it from resources with its id.
			Bitmap originalBitmap = pin.bitmap;
			if (originalBitmap == null)
				originalBitmap = BitmapUtils.createFromDrawable(ContextCompat.getDrawable(mContext, pin.resourceId));

			if (originalBitmap != null) {
				Bitmap newBitmap = BitmapUtils.createScaledBitmap(mContext.getResources(), originalBitmap, properties.width, properties.height, properties.scaleType);
				if (newBitmap != null)
					pin = new ResourceOrBitmap(newBitmap);
			}
		}

		return pin;
	}
}
