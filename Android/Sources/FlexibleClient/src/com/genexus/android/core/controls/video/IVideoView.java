package com.genexus.android.core.controls.video;

import android.net.Uri;

interface IVideoView {

	/**
	 * Starts video playback.
	 */
	void startVideo();

	/**
	 * Pauses video playback.
	 */
	void pauseVideo();

	/**
	 * Seeks video playback to the beginning.
	 */
	void stopVideo();

	/**
	 * Seeks video playback to the desired position in seconds.
	 */
	void seekTo(int position);

	/**
	 * Sets the video source.
	 */
	void setVideoUri(Uri uri);

	/**
	 * Returns the current video playback position in seconds.
	 */
	int getCurrentPosition();

	/**
	 * Sets the video AutoPlay property. If the video is paused, it is then resumed.
	 */
	void setAutoPlay(boolean autoPlay);

	/**
	 * Sets the video PlaybackRate property. Only supported on API 23+
	 */
	void setPlaybackRate(int playbackRate);

	/**
	 * Executes the actions needed prior to removing the view from the screen.
	 */
	void destroy();
}
