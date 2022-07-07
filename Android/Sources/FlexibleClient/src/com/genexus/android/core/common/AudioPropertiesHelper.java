package com.genexus.android.core.common;

import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.FileUtils2;

public class AudioPropertiesHelper {

	private static final String PROPERTY_URI = "AudioURI";
	private static final String PROPERTY_NAME = "AudioName";
	private static final String PROPERTY_TYPE = "AudioType";

	public static Expression.Value handleAudioProperty(Expression.Value value, String property) {
		if (value == null)
			return Expression.Value.newString(Strings.EMPTY);

		String audioUri = value.coerceToString();
		if (audioUri == null || audioUri.isEmpty()) {
			Services.Log.error(String.format("Audio is empty for property %s", property));
			return Expression.Value.newString(Strings.EMPTY);
		}

		if (PROPERTY_URI.equalsIgnoreCase(property))
			return Expression.Value.newString(audioUri);

		if (PROPERTY_NAME.equalsIgnoreCase(property)) {
			String name = FileUtils2.getFileName(audioUri);
			return Expression.Value.newString(name);
		}

		if (PROPERTY_TYPE.equalsIgnoreCase(property)) {
			String type = FileUtils2.getFileExtension(audioUri);
			return Expression.Value.newString(type);
		}

		throw new IllegalArgumentException(String.format("Unknown property ('%s').", property));
	}

}
