package com.genexus.android.live_editing.serializers;

import java.lang.reflect.Type;

import com.genexus.android.live_editing.inspector.ControlData;
import com.genexus.android.live_editing.inspector.MasterLayoutData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class MasterLayoutDataSerializer implements JsonSerializer<MasterLayoutData> {

    @Override
    public JsonElement serialize(MasterLayoutData masterLayoutData, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject data = new JsonObject();

        JsonArray controls = new JsonArray();
        for (ControlData controlData : masterLayoutData.getControls()) {
            controls.add(jsonSerializationContext.serialize(controlData));
        }
        data.add("Controls", controls);

        JsonArray windows = new JsonArray();
        JsonObject window = new JsonObject();
        window.addProperty("Class", masterLayoutData.getAppClassName());
        window.addProperty("BGColor", masterLayoutData.getAppBackgroundColor());
        window.addProperty("Height", masterLayoutData.getWindowHeight());
        window.addProperty("Width", masterLayoutData.getWindowWidth());
        windows.add(window);
        data.add("Window", windows);

        return data;
    }
}
