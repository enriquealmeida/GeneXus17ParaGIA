package com.genexus.android.media.audio;

import java.util.ArrayList;

import android.content.Context;

import com.genexus.android.media.model.GxMediaItem;

/**
 * Support for concurrent (mixing) audio players.
 */
public class AudioPlayerMix implements IAudioPlayer
{
	private final Context mContext;
	private final ArrayList<LocalPlaybackWrapper> mPlayers;
	private final Object mLock = new Object();

	public AudioPlayerMix(Context context)
	{
		mContext = context;
		mPlayers = new ArrayList<>();
	}

	@Override
	public void play(GxMediaItem audio)
	{
		synchronized (mLock)
		{
			// If the same audio is already playing, skip. Otherwise start a new playback.
			for (LocalPlaybackWrapper playback : new ArrayList<>(mPlayers))
			{
				if (playback.isPlaying(audio))
					return;
			}

			MixPlayback playback = new MixPlayback();
			mPlayers.add(playback);
			playback.play(audio);
		}
	}

	@Override
	public void play()
	{
		synchronized (mLock)
		{
			// Resume all.
			for (LocalPlaybackWrapper player : new ArrayList<>(mPlayers))
				player.play();
		}
	}

	@Override
	public boolean isPlaying()
	{
		synchronized (mLock)
		{
			// Are any channels playing?
			for (LocalPlaybackWrapper player : new ArrayList<>(mPlayers))
				if (player.isPlaying())
					return true;

			// None.
			return false;
		}
	}

	@Override
	public void pause()
	{
		synchronized (mLock)
		{
			// Pause all.
			for (LocalPlaybackWrapper player : new ArrayList<>(mPlayers))
				player.pause();
		}
	}

	@Override
	public void stop()
	{
		synchronized (mLock)
		{
			// Stop all.
			for (LocalPlaybackWrapper player : new ArrayList<>(mPlayers))
				player.stop();

			mPlayers.clear();
		}
	}

	private class MixPlayback extends LocalPlaybackWrapper
	{
		public MixPlayback()
		{
			// Mixing doesn't use/respect audio focus.
			super(mContext, false);
		}

		@Override
		protected void finish()
		{
			super.finish();
			synchronized (mLock)
			{
				mPlayers.remove(this);
			}
		}
	}
}
