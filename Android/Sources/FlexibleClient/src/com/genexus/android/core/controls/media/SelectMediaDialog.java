package com.genexus.android.core.controls.media;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityController;
import com.genexus.android.ManifestHelper;
import com.genexus.android.PermissionUtil;
import com.genexus.android.WithPermission;
import com.genexus.android.media.MediaUtils;
import com.genexus.android.media.actions.MediaAction;
import com.genexus.android.core.base.controls.IGxHandleActivityResult;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.FileUtils2;

import java.io.File;

public class SelectMediaDialog implements DialogInterface.OnClickListener, IGxHandleActivityResult, FileUtils2.CopyDataToFileListener {
	private static final int PERMISSION_REQUEST_CODE = 584;
	private final Context mContext;
	private final Coordinator mCoordinator;
	private final IGxMediaEdit mMediaEdit;
	private MediaAction[] mDialogActions;
	private AlertDialog mAlertDialog;
	private Uri mOutputMediaUri;

	public SelectMediaDialog(Context context, Coordinator coordinator, IGxMediaEdit
			mediaEdit) {
		mContext = context;
		mCoordinator = coordinator;
		mMediaEdit = mediaEdit;
	}

	public void show() {
		String[] menuOptions;
		String controlType = mMediaEdit.getControlType();
		switch (controlType) {
			case ControlTypes.PHOTO_EDITOR:
				menuOptions = new String[]{
						Services.Strings.getResource(R.string.GXM_TakePhoto),
						Services.Strings.getResource(R.string.GXM_SelectImage)
				};
				mDialogActions = new MediaAction[]{
						MediaAction.TAKE_PICTURE,
						MediaAction.PICK_IMAGE
				};
				break;
			case ControlTypes.VIDEO_VIEW:
				menuOptions = new String[]{
						Services.Strings.getResource(R.string.GXM_RecordVideo),
						Services.Strings.getResource(R.string.GXM_SelectVideo)
				};
				mDialogActions = new MediaAction[]{
						MediaAction.CAPTURE_VIDEO,
						MediaAction.PICK_VIDEO
				};
				break;
			case ControlTypes.AUDIO_VIEW:
				menuOptions = new String[]{
						Services.Strings.getResource(R.string.GXM_RecordAudio),
						Services.Strings.getResource(R.string.GXM_SelectAudio)
				};
				mDialogActions = new MediaAction[]{
						MediaAction.CAPTURE_AUDIO,
						MediaAction.PICK_AUDIO
				};
				break;
			default:
				throw new IllegalArgumentException("Invalid control type: " + controlType);
		}

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext)
				.setTitle(R.string.GX_BtnSelect)
				.setItems(menuOptions, this);

		mAlertDialog = dialogBuilder.show();
	}

	public void dismiss() {
		if (mAlertDialog != null) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which < 0 || which >= mDialogActions.length) {
			throw new IllegalArgumentException(String.format("Invalid index '%s'.", which));
		}

		ActivityController controller = mCoordinator.getUIContext().getActivityController();
		if (controller != null) {
			controller.setActivityResultHandler(this);
		}

		Activity activity = mCoordinator.getUIContext().getActivity();

		if (ManifestHelper.hasPermissionDeclared(mContext, Manifest.permission.CAMERA)
				&& !PermissionUtil.checkSelfPermissions(mContext, new String[] {Manifest.permission.CAMERA})) {
			// Ask for CAMERA permission
			new WithPermission.Builder<Void>(activity)
					.require(Manifest.permission.CAMERA)
					.setRequestCode(PERMISSION_REQUEST_CODE)
					.attachToActivityController()
					.onSuccess(() -> callMediaAction(mDialogActions[which], activity))
					.build()
					.run();
		} else {
			callMediaAction(mDialogActions[which], activity);
		}
	}

	protected void callMediaAction(MediaAction mediaAction, Activity activity) {
		Intent intent = mediaAction.buildIntent();
		if (intent.resolveActivity(activity.getPackageManager()) == null) {
			String message = Services.Strings.getResource(R.string.GXM_NoApplicationAvailable);
			Services.Messages.showMessage(mContext, message);
		} else {
			// This value was set by the mediaAction.buildIntent() call.
			mOutputMediaUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
			activity.startActivityForResult(intent, mediaAction.getRequestCode());
		}
	}

	@Override
	public boolean handleOnActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (MediaUtils.isPickMediaRequest(requestCode)) {
				Uri uri;
				if (data == null || data.getData() == null) {
					Services.Log.debug("Intent or its data was null.");
					uri = null;
				} else {
					uri = data.getData();

					if (uri.getScheme() == null) {
						uri = Uri.parse(ContentResolver.SCHEME_FILE + "://" + uri.toString());
					}
				}
				Uri mediaUri = uri;
				mediaChanged(mediaUri);
				return true;
			} else if (MediaUtils.isTakeMediaRequest(requestCode)) {
				// When MediaStore.EXTRA_OUTPUT is used, the media file is written to the uri passed and the result intent is NULL.
				// Otherwise, the media file is written to the default gallery directory and its uri is returned in the data field of the result intent.
				// In the latter case, we have to copy the media file to the app's directory.
				Uri mediaUri;
				if (mOutputMediaUri != null) {
					mediaUri = mOutputMediaUri;
				} else {
					mediaUri = (data != null && data.getData() != null) ? data.getData() : null;
				}

				mediaChanged(mediaUri);
				mOutputMediaUri = null;
				return true;
			}
		}
		return false;
	}

	public void mediaChanged(Uri mediaUri) {
		if (mediaUri != null && ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(mediaUri.getScheme())) {
			FileUtils2.copyDataToFileAsync(mContext, mediaUri, mContext.getCacheDir(), this);
			mMediaEdit.setLoadingForCopying(true);
		} else
			mMediaEdit.onMediaChanged(mediaUri, true, true);
	}

	@Override
	public void onCopyDataFinished(boolean successful, @NonNull File destFile) {
		Uri mediaUri = Uri.fromFile(destFile);
		mMediaEdit.onMediaChanged(mediaUri, successful, true);
	}

}
