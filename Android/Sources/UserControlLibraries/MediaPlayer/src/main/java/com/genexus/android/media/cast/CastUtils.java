package com.genexus.android.media.cast;

import org.json.JSONObject;

import android.support.v4.media.MediaMetadataCompat;

import com.genexus.android.core.base.utils.Strings;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.common.images.WebImage;

/**
 * Helper methods for Google Cast functionality.
 */
public class CastUtils
{
	/**
	 * Helper method to convert a {@link android.media.MediaMetadata} to a
	 * {@link com.google.android.gms.cast.MediaInfo} used for sending media to the receiver app.
	 *
	 * @param mediaUri media url.
	 * @param mediaType media type (e.g. MediaMetadata.MEDIA_TYPE_MOVIE).
	 * @param contentType MIME content type (e.g. "audio/mpeg")
	 * @param metadata {@link com.google.android.gms.cast.MediaMetadata}
	 * @param customData custom data specifies the local mediaId used by the player.
	 * @return mediaInfo {@link com.google.android.gms.cast.MediaInfo}
	 */
	public static MediaInfo toCastMediaInfo(String mediaUri, int mediaType, String contentType, MediaMetadataCompat metadata, JSONObject customData)
	{
		MediaMetadata castMetadata = new MediaMetadata(mediaType);
		castMetadata.putString(MediaMetadata.KEY_TITLE, Strings.toString(metadata.getDescription().getTitle(), ""));
		castMetadata.putString(MediaMetadata.KEY_SUBTITLE, Strings.toString(metadata.getDescription().getSubtitle(), ""));

		copyMetadataKey(metadata, castMetadata, MediaMetadataCompat.METADATA_KEY_ARTIST, MediaMetadata.KEY_ARTIST);
		copyMetadataKey(metadata, castMetadata, MediaMetadataCompat.METADATA_KEY_ALBUM, MediaMetadata.KEY_ALBUM_TITLE);
		copyMetadataKey(metadata, castMetadata, MediaMetadataCompat.METADATA_KEY_COMPOSER, MediaMetadata.KEY_COMPOSER);
		copyMetadataKey(metadata, castMetadata, MediaMetadataCompat.METADATA_KEY_DATE, MediaMetadata.KEY_RELEASE_DATE);
		copyMetadataKey(metadata, castMetadata, MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, MediaMetadata.KEY_TRACK_NUMBER);
		copyMetadataKey(metadata, castMetadata, MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, MediaMetadata.KEY_DISC_NUMBER);
		copyMetadataKey(metadata, castMetadata, MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, MediaMetadata.KEY_ALBUM_ARTIST);

		if (metadata.getDescription().getIconUri() != null)
		{
			// First image is used by the receiver for showing the audio album art.
			// Second image is used by on the full screen activity that is shown when the cast dialog is clicked.
			WebImage image = new WebImage(metadata.getDescription().getIconUri());
			castMetadata.addImage(image);
			castMetadata.addImage(image);
		}

		long streamDuration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
		if (streamDuration <= 0)
			streamDuration = MediaInfo.UNKNOWN_DURATION;

		MediaInfo.Builder builder = new MediaInfo.Builder(mediaUri)
				.setStreamType(MediaInfo.STREAM_TYPE_BUFFERED) // TODO: can be STREAM_TYPE_LIVE?
				.setContentType(contentType)
				.setMetadata(castMetadata)
				.setStreamDuration(streamDuration);

		if (customData != null)
			builder.setCustomData(customData);

		return builder.build();

		/*
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();
            jsonObj.put(KEY_DESCRIPTION, subTitle);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to add description to the json object", e);
        }

        return new MediaInfo.Builder(url)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(mimeType)
                .setMetadata(movieMetadata)
                .setMediaTracks(tracks)
                .setStreamDuration(duration * 1000)
                .setCustomData(jsonObj)
                .build();
		*/
	}

	private static void copyMetadataKey(MediaMetadataCompat src, MediaMetadata dest, String srcKey, String destKey)
	{
		String value = src.getString(srcKey);
		if (Strings.hasValue(value))
			dest.putString(destKey, value);
	}
}
