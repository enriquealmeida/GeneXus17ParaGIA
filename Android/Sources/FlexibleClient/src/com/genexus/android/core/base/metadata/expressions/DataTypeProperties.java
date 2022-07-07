package com.genexus.android.core.base.metadata.expressions;

import java.util.Set;

import com.genexus.android.core.base.utils.Strings;

/**
 * Standard properties of GX basic data types.
 */

final class DataTypeProperties
{
	private static final String AUDIO_URI = "AudioUri";
	private static final String IMAGE_URI = "ImageUri";
	private static final String VIDEO_URI = "VideoUri";
	private static final String BLOBFILE_URI = "FileUri";

	static final Set<String> MEDIA_URIS = Strings.newSet(AUDIO_URI, IMAGE_URI, VIDEO_URI, BLOBFILE_URI);

}
