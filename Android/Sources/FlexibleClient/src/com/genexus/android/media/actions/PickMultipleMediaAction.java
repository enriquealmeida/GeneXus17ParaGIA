package com.genexus.android.media.actions;

import android.content.Intent;

class PickMultipleMediaAction extends PickMediaAction {
	public PickMultipleMediaAction(int requestCode, MediaType mediaType) {
		super(requestCode, mediaType);
	}

	@Override
	public Intent buildIntent() {
		Intent intent = super.buildIntent();
		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		return intent;
	}
}
