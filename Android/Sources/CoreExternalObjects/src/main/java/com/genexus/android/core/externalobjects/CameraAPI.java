package com.genexus.android.core.externalobjects;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.ManifestHelper;
import com.genexus.android.media.CameraUtils;
import com.genexus.android.media.MediaUtils;
import com.genexus.android.media.actions.MediaAction;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.utils.FileUtils2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CameraAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Media.Camera";

	private static final String METHOD_TAKE_PHOTO = "TakePhoto";
	private static final String METHOD_RECORD_VIDEO = "RecordVideo";

	private Uri mMediaUri;

	public CameraAPI(ApiAction action) {
		super(action);

		List<String> permissions = new ArrayList<>();

		// If the EXTRA_OUTPUT flag is not supported by this device's camera, then we'll have to
		// request the WRITE_EXTERNAL_STORAGE permission in order to delete the taken photo from
		// the external storage and thus avoid that it appears in the gallery app.
		if (!CameraUtils.supportsExtraOutput()) {
			permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}

		// Since Android M, if an app declares that it requires the CAMERA permission and it hasn't
		// yet been granted to the app then if we invoke an Intent that'll make use of the camera
		// (i.e. ACTION_IMAGE_CAPTURE, ACTION_VIDEO_CAPTURE), a SecurityException will be thrown by
		// the system. This is because from the user's standpoint, he hasn't granted permission to
		// access the camera. For more info: https://issuetracker.google.com/issues/37063818
		if (ManifestHelper.hasPermissionDeclared(getActivity(), Manifest.permission.CAMERA)) {
			permissions.add(Manifest.permission.CAMERA);
		}

		if (permissions.isEmpty()) {
			addMethodHandler(METHOD_TAKE_PHOTO, 0, mMethodTakePhoto);
			addMethodHandler(METHOD_RECORD_VIDEO, 0, mMethodRecordVideo);
			addMethodHandler(METHOD_RECORD_VIDEO, 1, mMethodRecordVideo);
		} else {
			addMethodHandlerRequestingPermissions(METHOD_TAKE_PHOTO, 0, permissions.toArray(new String[0]), mMethodTakePhoto);
			addMethodHandlerRequestingPermissions(METHOD_RECORD_VIDEO, 0, permissions.toArray(new String[0]), mMethodRecordVideo);
			addMethodHandlerRequestingPermissions(METHOD_RECORD_VIDEO, 1, permissions.toArray(new String[0]), mMethodRecordVideo);
		}
	}

	private final IMethodInvoker mMethodTakePhoto = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			return execTakeMediaAction(METHOD_TAKE_PHOTO, MediaAction.TAKE_PICTURE);
		}
	};

	private final IMethodInvoker mMethodRecordVideo = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			MediaAction action = (parameters.size() == 0) ?
					MediaAction.CAPTURE_VIDEO :
					MediaAction.captureVideoWithQuality(MediaUtils.mapVideoQualityValue((String) parameters.get(0)));
			return execTakeMediaAction(METHOD_RECORD_VIDEO, action);
		}
	};

	private ExternalApiResult execTakeMediaAction(String methodName, MediaAction mediaAction) {
		Intent intent = mediaAction.buildIntent();
		PackageManager packageManager = getActivity().getPackageManager();

		if (intent.resolveActivity(packageManager) == null)
			return ExternalApiResult.failure(R.string.GXM_NoApplicationAvailable);

		mMediaUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);

		ActivityHelper.registerActionRequestCode(mediaAction.getRequestCode());
		getActivity().startActivityForResult(intent, mediaAction.getRequestCode());
		return ExternalApiResult.SUCCESS_WAIT;
	}

	@Override
	public ExternalApiResult afterActivityResult(int requestCode, int resultCode, Intent result, String method, List<Object> methodParameters) {
		if ((METHOD_TAKE_PHOTO.equalsIgnoreCase(method) || METHOD_RECORD_VIDEO.equalsIgnoreCase(method))
				&& resultCode == Activity.RESULT_OK) {
			if (MediaUtils.isTakeMediaRequest(requestCode)) {
				// When MediaStore.EXTRA_OUTPUT is used, the media file is written to the uri passed and the result intent is NULL.
				// Otherwise, the media file is written to the default gallery directory and its uri is returned in the data field of the result intent.
				// In the latter case, we have to copy the media file to the app's directory.
				Uri mediaUri;
				if (mMediaUri != null) {
					mediaUri = mMediaUri;
					return ExternalApiResult.success(mediaUri);
				}

				mediaUri = (result != null && result.getData() != null) ? result.getData() : null;

				if (mediaUri == null || !FileUtils2.canCopyDataToFile(mediaUri)) {
					return ExternalApiResult.FAILURE;
				}

				// Put the media URI of the taken media in this variable in order to delete it on the copyDataToFileListener.
				mMediaUri = mediaUri;

				FileUtils2.copyDataToFileAsync(getContext(), mediaUri, getContext().getCacheDir(), mCopyDataToFileListener);

				return ExternalApiResult.SUCCESS_WAIT;
			} else if (requestCode == RequestCodes.ACTION) {
				// Continuing action after executing the CopyDataToFileListener.
				if (mMediaUri != null) {
					return ExternalApiResult.success(mMediaUri);
				} else {
					return ExternalApiResult.FAILURE;
				}
			}
		}
		return null;
	}

	private FileUtils2.CopyDataToFileListener mCopyDataToFileListener = new FileUtils2.CopyDataToFileListener() {
		@Override
		public void onCopyDataFinished(boolean successful, @NonNull File destFile) {
			// Delete the taken media from the gallery.
			getContext().getContentResolver().delete(mMediaUri, null, null);

			if (successful) {
				mMediaUri = Uri.fromFile(destFile);
				ActionExecution.continueCurrent(getActivity(), false, getAction());
			} else {
				ActionExecution.cancelCurrent(getAction());
			}
		}
	};
}
