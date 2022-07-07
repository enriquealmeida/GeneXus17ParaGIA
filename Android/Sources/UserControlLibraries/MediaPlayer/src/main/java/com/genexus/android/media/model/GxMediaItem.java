package com.genexus.android.media.model;

import android.net.Uri;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.StorageHelper;
import com.genexus.android.uamp.PlaybackExtras;
import com.genexus.android.uamp.R;

/**
 * Media item.
 */
public class GxMediaItem
{
	private final String mId;
	private final String mUri;
	private final String mContentType;
	private int mStreamType;
	private String mTitle;
	private String mSubtitle;
	private String mDescription;
	private String mImageUri;
	private long mDuration;
	private boolean mIsFavorite;
	private final GxMediaItemMetadata mMetadata;

	public static final String KEY_DESCRIPTION = MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION;

	public GxMediaItem(String uri, String contentType, String title)
	{
		mId = uri;
		mUri = getFullResourceUri(uri);
		mTitle = title;
		mContentType = contentType;
		mMetadata = new GxMediaItemMetadata();
	}

	public GxMediaItem(Entity sdt)
	{
		String id = sdt.optStringProperty("Id");
		String uri = getFullResourceUri(sdt.optStringProperty("Uri"));

		String title = sdt.optStringProperty("Title");
		if (!Strings.hasValue(title))
			title = Services.Strings.getResource(R.string.GXM_AudioDescription);

		if (!Strings.hasValue(id))
			id = String.valueOf(uri.hashCode()); // Fake a unique id from the hash code of the source.

		mId = id;
		mUri = uri;
		mContentType = sdt.optStringProperty("ContentType");
		mStreamType = sdt.optIntProperty("StreamType");

		mTitle = title;
		mSubtitle = sdt.optStringProperty("Subtitle");
		mDescription = sdt.optStringProperty("Description");
		mImageUri = getFullResourceUri(sdt.optStringProperty("Image"));
		mDuration = sdt.optLongProperty("Duration");
		mIsFavorite = sdt.optBooleanProperty("IsFavorite");

		mMetadata = new GxMediaItemMetadata(sdt.getLevel("Metadata"));
	}

	private static String getFullResourceUri(String uri)
	{
		if (Strings.hasValue(uri))
		{
			// Convert "uri" to a "real uri" (add proper scheme and/or full path if missing).
			if (!uri.contains("://") && !StorageHelper.isLocalFile(uri))
				uri = Services.Application.get().UriMaker.getImageUrl(uri);
		}

		return uri;
	}

	public String getMediaId()
	{
		return mId;
	}

	public String getMediaUri()
	{
		return mUri;
	}

	public String getContentType()
	{
		return mContentType;
	}

	public boolean isFavorite()
	{
		return mIsFavorite;
	}

	public void setFavorite(boolean value)
	{
		mIsFavorite = value;
	}

	public Entity toSdt()
	{
		Entity sdt = EntityFactory.newSdt("GeneXus.SD.Media.MediaItem");
		sdt.setProperty("Id", mId);
		sdt.setProperty("Uri", mUri);
		sdt.setProperty("ContentType", mContentType);
		sdt.setProperty("StreamType", mStreamType);
		sdt.setProperty("Title", mTitle);
		sdt.setProperty("Subtitle", mSubtitle);
		sdt.setProperty("Description", mDescription);
		sdt.setProperty("Image", mImageUri);
		sdt.setProperty("Duration", mDuration);
		sdt.setProperty("IsFavorite", mIsFavorite);
		sdt.putLevel("Metadata", mMetadata.toSdt());
		return sdt;
	}

	public MediaMetadataCompat toMediaMetadata()
	{
		//noinspection WrongConstant
		MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder()
				.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, mUri)
				.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mId)
				.putString(PlaybackExtras.METADATA_KEY_CONTENT_TYPE, mContentType)
				.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, mTitle)
				.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, mSubtitle)
				.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, mDescription)
				.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, mImageUri)
				.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mDuration)
				.putRating(PlaybackExtras.METADATA_KEY_RATING_CUSTOM, RatingCompat.newHeartRating(mIsFavorite));

		// Duplicates needed for RemoteControlClient / CastPlayback.
		// They may be overwritten by custom metadata and that's ok.
		builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, mTitle);
		builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mSubtitle);

		// Custom metadata possibly mapped to Android metadata.
		mMetadata.toMediaMetadata(builder);

		return builder.build();
	}

	private MediaDescriptionCompat toMediaDescription()
	{
		return new MediaDescriptionCompat.Builder()
				.setMediaId(mId)
				.setMediaUri(Uri.parse(mUri))
				.setTitle(mTitle)
				.setSubtitle(mSubtitle)
				.setDescription(mDescription)
				.setIconUri(Strings.hasValue(mImageUri) ? Uri.parse(mImageUri) : null)
				.build();
	}

	public MediaSessionCompat.QueueItem toQueueItem(long id)
	{
		return new MediaSessionCompat.QueueItem(toMediaDescription(), id);
	}
}
