package com.genexus.android.media.audio;

import com.genexus.android.media.model.GxMediaItem;

public interface IAudioPlayer
{
	/** Starts audio playing of the specified URI */
	void play(GxMediaItem audio);

	/** Continues audio playing, if paused. */
	void play();

	/** Returns whether the player is currently playing. */
	boolean isPlaying();

	/** Pauses audio playing. */
	void pause();

	/** Stops audio playing. */
	void stop();

	/** Releases the audio player resources. */
//	void release();
}
