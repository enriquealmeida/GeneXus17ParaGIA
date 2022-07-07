package com.genexus.android.media.actions;

import android.content.Intent;
import android.os.Build;

class PickMediaAction extends MediaAction {

	public PickMediaAction(int requestCode, MediaType mediaType) {
		super(requestCode, mediaType);
	}

	@Override
	public Intent buildIntent() {
		Intent intent = super.buildIntent();
		intent.setType(mMimeType);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		return intent;
	}

	@Override
	public String getAction() {
        // TODO: Careful with the access scope of GET_CONTENT vs OPEN_DOCUMENT.
        // The first one is used to retrieve files and it's given access to the provided URI for a VERY short time.
        // The second one is used to have access to a file in order to open it, usually used when we just want to display something.
		// Check: https://developer.android.com/guide/components/intents-common.html

		// Xiaomi apps don't implement the ACTION_OPEN_DOCUMENT correctly (e.g. the Gallery doesn't handle this intent).
		return !Build.MANUFACTURER.equals("Xiaomi") ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT;
	}
}
