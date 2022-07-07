package com.genexus.android.live_editing.serializers;

import java.lang.reflect.Type;

import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.services.Services;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class INodeCollectionDeserializer implements JsonDeserializer<INodeCollection> {

	@Override
	public INodeCollection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Services.Serializer.createCollection(json.toString());
	}
}
