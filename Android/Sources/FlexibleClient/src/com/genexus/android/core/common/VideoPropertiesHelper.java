package com.genexus.android.core.common;

import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.FileUtils2;

public class VideoPropertiesHelper {

	private static final String PROPERTY_URI = "VideoURI";
	private static final String PROPERTY_NAME = "VideoName";
	private static final String PROPERTY_TYPE = "VideoType";

	public static Expression.Value handleVideoProperty(Expression.Value value, String property) {
		if (value == null)
			return Expression.Value.newString(Strings.EMPTY);

		String videoUri = value.coerceToString();
		if (videoUri == null || videoUri.isEmpty()) {
			Services.Log.error(String.format("Video is empty for property %s", property));
			return Expression.Value.newString(Strings.EMPTY);
		}

		if (PROPERTY_URI.equalsIgnoreCase(property))
			return Expression.Value.newString(videoUri);

		if (PROPERTY_NAME.equalsIgnoreCase(property)) {
			String name = FileUtils2.getFileName(videoUri);
			return Expression.Value.newString(name);
		}

		if (PROPERTY_TYPE.equalsIgnoreCase(property)) {
			String type = FileUtils2.getFileExtension(videoUri);
			return Expression.Value.newString(type);
		}

		throw new IllegalArgumentException(String.format("Unknown property ('%s').", property));
	}
}
