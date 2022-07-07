package com.genexus.android.media.audio;

import android.content.Context;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.uamp.playback.LocalPlayback;
import com.genexus.android.uamp.playback.Playback;
import com.genexus.android.media.model.GxMediaItem;

/**
 * Local Playback Helper (makes pause/play easier).
 */
class LocalPlaybackWrapper implements IAudioPlayer
{
	private final LocalPlayback mPlayback;
	private GxMediaItem mAudio;

	private static final int SINGLE_QUEUE_ID = 0;

	public LocalPlaybackWrapper(Context context, boolean useAudioFocus)
	{
		mPlayback = new LocalPlayback(context, useAudioFocus);
		mPlayback.setCallback(mCallback);
	}

	@Override
	public void play(GxMediaItem audio)
	{
		mAudio = audio;
		mPlayback.play(audio.toQueueItem(SINGLE_QUEUE_ID));
	}

	@Override
	public void play()
	{
		if (mAudio != null)
			play(mAudio); // This will resume playback because media_id is the same.
	}

	@Override
	public boolean isPlaying()
	{
		return mPlayback.isPlaying();
	}

	public boolean isPlaying(GxMediaItem audio)
	{
		return mAudio != null && mAudio.getMediaId().equals(audio.getMediaId());
	}

	@Override
	public void pause()
	{
		mPlayback.pause();
	}

	@Override
	public void stop()
	{
		finish();
	}

	protected void finish()
	{
		mAudio = null;
		mPlayback.stop(false);
	}

	@SuppressWarnings("FieldCanBeLocal")
	private final Playback.Callback mCallback = new Playback.Callback()
	{
		@Override
		public void onCompletion()
		{
			finish();
		}

		@Override
		public void onError(String error)
		{
			Services.Log.error(error);
			finish();
		}

		@Override
		public void onPlaybackStatusChanged(int state) { }

		@Override
		public void setCurrentMediaId(String mediaId)
		{
			Services.Log.debug("setCurrentMediaId " + mediaId);
		}
	};
}
