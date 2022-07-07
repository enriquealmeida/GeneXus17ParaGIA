package com.genexus.android.media.audio;

import android.content.Context;

import com.genexus.android.media.model.GxMediaItem;

/**
 * AudioPlayer wrapper (using audio focus).
 */
public class AudioPlayerSolo implements IAudioPlayer
{
	private final Context mContext;
	private LocalPlaybackWrapper mPlayer;

	public AudioPlayerSolo(Context context)
	{
		mContext = context;
	}

	@Override
	public void play(GxMediaItem audio)
	{
		if (mPlayer == null)
			mPlayer = new LocalPlaybackWrapper(mContext, true); // Solo uses and respects audio focus.

		mPlayer.play(audio);
	}

	@Override
	public void play()
	{
		if (mPlayer != null)
			mPlayer.play();
	}

	@Override
	public boolean isPlaying()
	{
		return (mPlayer != null && mPlayer.isPlaying());
	}

	@Override
	public void pause()
	{
		if (mPlayer != null)
			mPlayer.pause();
	}

	@Override
	public void stop()
	{
		if (mPlayer != null)
			mPlayer.stop();
	}
}
