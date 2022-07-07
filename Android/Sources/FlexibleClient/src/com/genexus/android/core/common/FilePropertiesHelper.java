package com.genexus.android.core.common;

import android.net.Uri;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.FileUtils2;

public class FilePropertiesHelper {

	private static final String PROPERTY_URI = "FileURI";
	private static final String PROPERTY_NAME = "FileName";
	private static final String PROPERTY_TYPE = "FileType";

	public static Expression.Value handleFileProperty(Expression.Value value, String property) {
		if (value == null)
			return Expression.Value.newString(Strings.EMPTY);

		String fileUri = value.coerceToString();
		if (fileUri == null || fileUri.isEmpty()) {
			Services.Log.error(String.format("File is empty for property %s", property));
			return Expression.Value.newString(Strings.EMPTY);
		}

		String fileNameWExtension = fileUri;
		try {
			fileNameWExtension = "/" + FileUtils2.getFileNameWithExtension(ActivityHelper.getCurrentActivity(), Uri.parse(fileUri));
		} catch (IllegalArgumentException exception) {
			Services.Log.warning(String.format("File URI '%s' has an incorrect format", fileUri), exception);
		}

		if (PROPERTY_URI.equalsIgnoreCase(property))
			return Expression.Value.newString(fileUri);

		if (PROPERTY_NAME.equalsIgnoreCase(property)) {
			String name = FileUtils2.getFileName(fileNameWExtension);
			return Expression.Value.newString(name);
		}

		if (PROPERTY_TYPE.equalsIgnoreCase(property)) {
			String type = FileUtils2.getFileExtension(fileNameWExtension);
			return Expression.Value.newString(type);
		}

		throw new IllegalArgumentException(String.format("Unknown property ('%s').", property));
	}

}
