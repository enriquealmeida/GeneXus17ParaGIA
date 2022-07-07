package com.genexus.android.media.model;

import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.uamp.PlaybackExtras;

/**
 * Media Queue State.
 */
public class GxMediaQueueState
{
	private final int mState;
	private final String mMediaId;
	private final long mQueuePosition;
	private final long mTrackPosition;

	public GxMediaQueueState(PlaybackStateCompat playbackState)
	{
		if (playbackState != null)
		{
			long queuePosition = playbackState.getActiveQueueItemId();
			if (queuePosition == MediaSessionCompat.QueueItem.UNKNOWN_ID)
				queuePosition = 0; // -1 is mapped to 0. "Real" queue item ids are already 1-based.

			long trackPosition = playbackState.getPosition();
			if (trackPosition == PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN)
				trackPosition = 0;

			String mediaId = "";
			Bundle extras = playbackState.getExtras();
			if (extras != null)
				mediaId = extras.getString(PlaybackExtras.EXTRA_MEDIA_ID);

			mState = GxPlaybackState.fromPlaybackState(playbackState);
			mMediaId = mediaId;
			mQueuePosition = queuePosition;
			mTrackPosition = trackPosition;
		}
		else
		{
			mState = GxPlaybackState.NONE;
			mMediaId = "";
			mQueuePosition = 0;
			mTrackPosition = 0;
		}
	}

	public static GxMediaQueueState none()
	{
		return new GxMediaQueueState(null);
	}

	@Override
	public String toString()
	{
		return String.format("MediaQueueState {State:%s, MediaId:%s, QueuePosition:%s, TrackPosition:%s}",
				GxPlaybackState.toString(mState),
				mMediaId,
				mQueuePosition,
				mTrackPosition);
	}

	public Entity toSdt()
	{
		Entity sdt = EntityFactory.newSdt("GeneXus.SD.Media.MediaQueueState");
		sdt.setProperty("State", mState);
		sdt.setProperty("MediaId", mMediaId);
		sdt.setProperty("QueuePosition", mQueuePosition);
		sdt.setProperty("TrackPosition", mTrackPosition);
		return sdt;
	}
}
