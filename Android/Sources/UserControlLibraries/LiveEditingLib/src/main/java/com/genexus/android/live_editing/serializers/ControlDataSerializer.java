package com.genexus.android.live_editing.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import android.util.Base64;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.live_editing.inspector.ControlData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class ControlDataSerializer implements JsonSerializer<ControlData> {

    @Override
    public JsonElement serialize(ControlData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject control = new JsonObject();
        control.addProperty("Name", src.getName());
        control.addProperty("Class", src.getThemeClassName());
        control.addProperty("Layout", src.getLayoutId());
        control.addProperty("ObjName", src.getParentObjectName());
        control.addProperty("Visible", src.isVisible());
        control.addProperty("Level", src.getLevel());
        control.addProperty("h", src.getActualHeight());
        control.addProperty("w", src.getActualWidth());
        control.addProperty("x", src.getPosX());
        control.addProperty("y", src.getPosY());
        control.addProperty("z", src.getPosZ());
        control.addProperty("oh", src.getOriginalHeight());
        control.addProperty("oy", src.getOriginalPosY());
        control.addProperty("image", bitmapToBase64(src.getBitmap()));
        return control;
    }

    /**
     * Compress a bitmap to PNG and encode it using Base64.
     */
    private static @Nullable String bitmapToBase64(@Nullable Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
		{
			bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
			return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
		} catch (IOException ex)
		{
			Services.Log.warning("bitmapToBase64", ex.getMessage());
			return null;
		}


    }
}
