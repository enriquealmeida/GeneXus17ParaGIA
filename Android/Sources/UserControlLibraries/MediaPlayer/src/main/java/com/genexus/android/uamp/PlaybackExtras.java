package com.genexus.android.uamp;

/**
 * Extra information passed as a bundle in PlaybackState, or as parameters of
 * custom actions sent via TransportControls.
 */
public class PlaybackExtras
{
	public static final String EXTRA_REPEAT_MODE = "com.genexus.android.media.audio.REPEAT_MODE";
	public static final String EXTRA_SHUFFLE_MODE = "com.genexus.android.media.audio.SHUFFLE_MODE";
	public static final String EXTRA_MEDIA_ID = "com.genexus.android.media.audio.MEDIA_ID";

	public static final String METADATA_KEY_CONTENT_TYPE = "com.genexus.android.media.audio.CONTENT_TYPE";
	public static final String METADATA_KEY_RATING_CUSTOM = "com.genexus.android.media.audio.MEDIA_RATING";

	public static final int REPEAT_MODE_NONE = 0;
	public static final int REPEAT_MODE_QUEUE = 1;
	public static final int REPEAT_MODE_SINGLE = 2;

	public static final String ACTION_SET_QUEUE = "com.genexus.android.media.audio.ACTION_SET_QUEUE";
	public static final String ACTION_TOGGLE_REPEAT = "com.genexus.android.media.audio.ACTION_TOGGLE_REPEAT";
	public static final String ACTION_TOGGLE_SHUFFLE = "com.genexus.android.media.audio.ACTION_TOGGLE_SHUFFLE";
	public static final String ACTION_TOGGLE_FAVORITE = "com.genexus.android.media.audio.ACTION_TOGGLE_FAVORITE";
	public static final String ACTION_SKIP_TO_MEDIA_ID = "com.genexus.android.media.audio.ACTION_SKIP_TO_MEDIA_ID";

	public static final String EXTRA_ACTION_MEDIA_ID = "com.genexus.android.media.audio.EXTRA_ACTION_MEDIA_ID";

	public static final String ACTION_CUSTOM = "com.genexus.android.media.audio.ACTION_CUSTOM";
	public static final String EXTRA_CUSTOM_ACTION_ID = "com.genexus.android.media.audio.CUSTOM_ACTION_ID";

	public static final String CUSTOM_ACTION_ID_FAVORITE = "GXFavoriteStatusChanged";
}
