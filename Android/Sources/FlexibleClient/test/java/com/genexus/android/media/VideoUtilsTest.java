package com.genexus.android.media;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class VideoUtilsTest {
	private static final String VIDEO_ID = "NLqAF9hrVbY";
	private static final String SHORT_URL = String.format("https://youtu.be/%s", VIDEO_ID);
	private static final String IFRAME_URL = String.format("https://www.youtube.com/embed/%s", VIDEO_ID);
	private static final String IFRAME_WITH_PARAM_URL = String.format("https://www.youtube.com/embed/%s?rel=0", VIDEO_ID);
	private static final String WATCH_URL = String.format("http://www.youtube.com/watch?v=%s", VIDEO_ID);

	@Test
	public void testIsYouTubeUrl() {
		assertThat(VideoUtils.isYouTubeUrl(SHORT_URL)).isTrue();
		assertThat(VideoUtils.isYouTubeUrl(IFRAME_URL)).isTrue();
		assertThat(VideoUtils.isYouTubeUrl(IFRAME_WITH_PARAM_URL)).isTrue();
		assertThat(VideoUtils.isYouTubeUrl(WATCH_URL)).isTrue();
	}

	@Test
	public void testGetYouTubeVideoId() {
		assertThat(VideoUtils.getYouTubeVideoId(SHORT_URL)).isEqualTo(VIDEO_ID);
		assertThat(VideoUtils.getYouTubeVideoId(IFRAME_URL)).isEqualTo(VIDEO_ID);
		assertThat(VideoUtils.getYouTubeVideoId(IFRAME_WITH_PARAM_URL)).isEqualTo(VIDEO_ID);
		assertThat(VideoUtils.getYouTubeVideoId(WATCH_URL)).isEqualTo(VIDEO_ID);
	}
}