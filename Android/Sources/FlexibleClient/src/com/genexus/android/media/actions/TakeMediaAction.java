package com.genexus.android.media.actions;

import java.io.File;
import java.io.IOException;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.genexus.android.content.FileProviderUtils;
import com.genexus.android.media.CameraUtils;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.StorageHelper;

class TakeMediaAction extends MediaAction {
	private final String mAction;

	public TakeMediaAction(int requestCode, String action, MediaType mediaType) {
		super(requestCode, mediaType);
		mAction = action;
	}

	@Override
	public String getAction() {
		return mAction;
	}

	@Override
	public Intent buildIntent() {
		Intent intent = super.buildIntent();

		if (CameraUtils.supportsExtraOutput()) {
			File outputMediaFile;

			try {
				outputMediaFile = StorageHelper.createNewFileInAppExtStorage(
						Services.Application.getAppContext(),
						mMediaType.getStorageDirectoryName(),
						mMediaType.getDefaultExtension()
				);
			} catch (IOException e) {
				Services.Log.error(String.format("Error when attempting to take media: %s", e.getMessage()));
				return intent;
			}

			if (!outputMediaFile.exists()) {
				Services.Log.error(String.format("File could not be created: %s", outputMediaFile.getAbsolutePath()));
				return intent;
			}

			Uri outputUri = FileProviderUtils.getUriForFile(Services.Application.getAppContext(), outputMediaFile);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			// See: https://commonsware.com/blog/2016/08/31/granting-permissions-uri-intent-extra.html
			intent.setClipData(ClipData.newRawUri(null, outputUri));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		}

		return intent;
	}
}
