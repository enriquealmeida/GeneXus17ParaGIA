package com.genexus.android.media.actions;

import android.content.Intent;
import android.provider.MediaStore;

class TakeVideoWithQualityAction extends TakeMediaAction {
	private final int mVideoQualityExtra;

	public TakeVideoWithQualityAction(int videoQualityExtra) {
		super(702, MediaStore.ACTION_VIDEO_CAPTURE, MediaType.Video);
		mVideoQualityExtra = videoQualityExtra;
	}

	@Override
	public Intent buildIntent() {
		Intent intent = super.buildIntent();
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, mVideoQualityExtra);
		return intent;
	}
}
