package com.genexus.android.twitter;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.genexus.android.core.base.services.Services;

import twitter4j.TwitterException;

import static com.genexus.android.core.utils.FileUtils2.SCHEME_HTTP;

class TwitterUtils {

	static boolean isValidTwitterUsername(String userName) {
		return (!TextUtils.isEmpty(userName) && userName.trim().matches("\\w{1,15}"));
	}

	static boolean isValidTwitterStatusId(String statusId) {
		return (!TextUtils.isEmpty(statusId) && statusId.trim().matches("\\d{1,19}"));
	}

	// Receives a path with scheme.
	static boolean isValidImagePath(String imagePath) {
		if (TextUtils.isEmpty(imagePath)) {
			return false;
		}
	
		String fileExt;
	
		if (!imagePath.startsWith(SCHEME_HTTP)) {
			File imageFile = new File(imagePath);
	
			if (!imageFile.exists()) {
				return false;
			}
	
			fileExt = FilenameUtils.getExtension(imageFile.getName());
		} else {
			fileExt = MimeTypeMap.getFileExtensionFromUrl(imagePath);
		}
	
		String fileMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt);

		if (fileMimeType == null || !fileMimeType.startsWith("image/")) {
			return false;
		}

		return true;
	}
	
	static void showErrorMessageOnApp(Context context, TwitterException te) {
		if (te.isCausedByNetworkIssue()) {
			Services.Messages.showMessage(context, R.string.GXM_TwitterNetworkIssue);
		} else if (te.isErrorMessageAvailable()) {
			Services.Messages.showMessage(context, te.getErrorMessage());
		} else {
			Services.Messages.showMessage(context, R.string.GXM_TwitterOperationError);
		}
	}

}
