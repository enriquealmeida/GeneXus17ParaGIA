package com.genexus.android.core.externalobjects;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.content.FileProviderUtils;
import com.genexus.android.media.MediaUtils;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.StorageHelper;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

public class ShareAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Social.Share";

	private static final String METHOD_SHARE_TEXT = "ShareText";
	private static final String METHOD_SHARE_IMAGE = "ShareImage";
	private static final String METHOD_SHARE_FILE = "ShareFile";

	public ShareAPI(ApiAction action) {
		super(action);
		addMethodHandler(METHOD_SHARE_TEXT, 3, new IMethodInvoker() {
			@Override
			public @NonNull ExternalApiResult invoke(List<Object> parameters) {
				List<String> values = ExternalApi.toString(parameters);
				shareText(values.get(2), values.get(0), values.get(1));
				return ExternalApiResult.SUCCESS_WAIT;
			}
		});

		addMethodHandler(METHOD_SHARE_IMAGE, 4, new IMethodInvoker() {
			@Override
			public @NonNull ExternalApiResult invoke(List<Object> parameters) {
				List<String> values = ExternalApi.toString(parameters);
				shareImage(values.get(3), values.get(1), values.get(2), values.get(0));
				return ExternalApiResult.SUCCESS_WAIT;
			}
		});

		addMethodHandler(METHOD_SHARE_FILE, 1, mShareFileHandler);
		addMethodHandler(METHOD_SHARE_FILE, 3, mShareFileHandler);
	}

	private final IMethodInvoker mShareFileHandler = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String text = Strings.EMPTY;
			String title = Strings.EMPTY;
			List<String> values = ExternalApi.toString(parameters);
			if (values.size() >= 3)
			{
				text = values.get(1);
				title = values.get(2);
			}
			if (shareFile(values.get(0), text, title))
				return ExternalApiResult.SUCCESS_WAIT;
			else
				return ExternalApiResult.FAILURE;
		}
	};


	private void shareText(String title, String text, String url) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, getSharedText(text, url));

		startShareActivity(intent);
	}

	private void shareImage(String title, String text, String url, String image) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, getSharedText(text, url));
		intent.putExtra(Intent.EXTRA_STREAM, Services.Images.getSharedImage(getContext(), image));

		startShareActivity(intent);
	}

	private boolean shareFile(String uri, String text, String title) {
		Intent intent = new Intent(Intent.ACTION_SEND);

		if (!Strings.hasValue(uri)) {
			Services.Log.error("Error sharing file, empty Uri");
			return false;
		}

		String mimeType = MediaUtils.getMimeType(uri);
		if (mimeType==null) {
			Services.Log.error("Error sharing file, empty Mime Type");
			return false;
		}

		Uri fileUri = getSharedFileUri(getContext(), uri);

		if (fileUri==null) {
			Services.Log.error("Error sharing file, fileUri cannot be shared : " + uri);
			return false;
		}
		
		intent.setType(mimeType);
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.putExtra(Intent.EXTRA_STREAM, fileUri);

		startShareActivity(intent);
		return true;
	}

	private void startShareActivity(Intent intent) {
		getActivity().startActivityForResult(Intent.createChooser(intent, null), RequestCodes.ACTION_ALWAYS_SUCCESSFUL);
	}

	private @NonNull String getSharedText(String text, String url) {
		if (Strings.hasValue(text) && Strings.hasValue(url))
			return text + Strings.NEWLINE + url;
		else if (Strings.hasValue(text))
			return text;
		else if (Strings.hasValue(url))
			return url;
		else
			return Strings.EMPTY;
	}

	private Uri getSharedFileUri(Context context, String uri) {
		// 1) local file => add scheme if not have.
		final String SCHEME_FILE = "file://";
		// add file scheme if missing
		if (StorageHelper.isLocalFile(uri) && !Strings.starsWithIgnoreCase(uri, SCHEME_FILE))
			uri = SCHEME_FILE + uri;

		Uri fileUri = Uri.parse(uri);
		// 2) file to share , convert to provider uri if local file
		if (Strings.starsWithIgnoreCase(uri, SCHEME_FILE)) {
			File localFile = new File(uri.substring(SCHEME_FILE.length()));
			try {
				fileUri = FileProviderUtils.getSharedUriFromFile(context, localFile);
			} catch (IOException e) {
				Services.Log.error("Error sharing file", e);
				return null;
			}
		}

		// Share URI that can be shared (content://)
		return fileUri;
	}

}
