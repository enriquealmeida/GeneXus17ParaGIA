package com.genexus.android.media.model;

import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Playback State.
 */
public class GxPlaybackState
{
	public static final int NONE = 0;
	public static final int STOPPED = 1;
	public static final int PAUSED = 2;
	public static final int PLAYING = 3;
	public static final int ERROR = 4;
	public static final int TRANSITIONAL = 5;

	public static int fromPlaybackState(PlaybackStateCompat playbackState)
	{
		if (playbackState == null)
			return NONE;

		switch (playbackState.getState())
		{
			case PlaybackStateCompat.STATE_NONE :
				return NONE;

			case PlaybackStateCompat.STATE_STOPPED :
				return STOPPED;

			case PlaybackStateCompat.STATE_PAUSED :
				return PAUSED;

			case PlaybackStateCompat.STATE_PLAYING :
				return PLAYING;

			case PlaybackStateCompat.STATE_ERROR :
				return ERROR;

			case PlaybackStateCompat.STATE_BUFFERING :
			case PlaybackStateCompat.STATE_FAST_FORWARDING :
			case PlaybackStateCompat.STATE_REWINDING :
			case PlaybackStateCompat.STATE_CONNECTING :
			case PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS :
			case PlaybackStateCompat.STATE_SKIPPING_TO_NEXT :
			case PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM :
				return TRANSITIONAL;

			default :
				throw new IllegalArgumentException("Unexpected PlaybackStateCompat state: " + playbackState.getState());
		}
	}

	public static boolean isPlaying(PlaybackStateCompat playbackState)
	{
		// For the purposes of this method, "buffering == playing".
		if (playbackState != null)
		{
			int state = playbackState.getState();
			return (state == PlaybackStateCompat.STATE_PLAYING || state == PlaybackStateCompat.STATE_BUFFERING);
		}
		else
			return false;
	}

	static String toString(int gxPlaybackState)
	{
		switch (gxPlaybackState)
		{
			case NONE : return "NONE";
			case STOPPED : return "STOPPED";
			case PAUSED : return "PAUSED";
			case PLAYING : return "PLAYING";
			case ERROR : return "ERROR";
			case TRANSITIONAL : return "TRANSITIONAL";
			default : return "(?)";
		}
	}
}
