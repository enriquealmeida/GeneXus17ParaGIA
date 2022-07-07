package com.genexus.android.media.actions;

import android.content.Intent;
import android.provider.MediaStore;

public abstract class MediaAction {
	public static final MediaAction TAKE_PICTURE = new TakeMediaAction(701, MediaStore.ACTION_IMAGE_CAPTURE, MediaType.Image);
	public static final MediaAction CAPTURE_VIDEO = new TakeMediaAction(702, MediaStore.ACTION_VIDEO_CAPTURE, MediaType.Video);
	public static final MediaAction CAPTURE_AUDIO = new TakeMediaAction(703, MediaStore.Audio.Media.RECORD_SOUND_ACTION, MediaType.Audio);
	public static final MediaAction PICK_IMAGE = new PickMediaAction(801, MediaType.Image);
	public static final MediaAction PICK_VIDEO = new PickMediaAction(802, MediaType.Video);
	public static final MediaAction PICK_AUDIO = new PickMediaAction(803, MediaType.Audio);
	public static final MediaAction PICK_IMAGES = new PickMultipleMediaAction(801, MediaType.Image);
	public static final MediaAction PICK_VIDEOS = new PickMultipleMediaAction(802, MediaType.Video);
	public static final MediaAction PICK_AUDIOS = new PickMultipleMediaAction(803, MediaType.Audio);

	public static MediaAction captureVideoWithQuality(int videoQualityExtra) {
		return new TakeVideoWithQualityAction(videoQualityExtra);
	}

	private final int mRequestCode;
	protected final String mMimeType;
	protected final MediaType mMediaType;

	protected MediaAction(int requestCode, MediaType mediaType) { // TODO: Consider if requestCode should be passed here.
		mRequestCode = requestCode;
		mMimeType = mediaType.getMimeType();
		mMediaType = mediaType;
	}

	public final int getRequestCode() {
		return mRequestCode;
	}

	public abstract String getAction();

	@SuppressWarnings("deprecation")
	public Intent buildIntent() {
		Intent intent = new Intent(getAction());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		return intent;
	}
}


