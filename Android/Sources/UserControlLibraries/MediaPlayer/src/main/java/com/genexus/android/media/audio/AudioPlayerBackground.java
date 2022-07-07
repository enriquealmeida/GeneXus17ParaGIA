package com.genexus.android.media.audio;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.uamp.MusicService;
import com.genexus.android.uamp.PlaybackExtras;
import com.genexus.android.media.customization.GxAudioPlayerSettings;
import com.genexus.android.media.model.GxMediaItem;
import com.genexus.android.media.model.GxMediaItemFinishedArgs;
import com.genexus.android.media.model.GxMediaQueue;
import com.genexus.android.media.model.GxMediaQueueState;
import com.genexus.android.media.model.GxPlaybackState;

import static com.genexus.android.media.customization.AudioPlayerClassExtensionKt.AUDIO_PLAYER_CLASS_NAME;

/**
 * Wrapper to control the background audio service.
 * There should be only one instance of this class.
 */
public class AudioPlayerBackground implements IAudioPlayer
{
	private static AudioPlayerBackground sInstance;

	private final Context mContext;
	private MediaBrowserCompat mMediaBrowser;
	private MediaControllerCompat mMediaController;
	private GxMediaQueue mMediaQueue;

	private GxAudioPlayerSettings mSettings;

	public static AudioPlayerBackground getInstance(Context context)
	{
		if (sInstance == null)
			sInstance = new AudioPlayerBackground(context.getApplicationContext());

		return sInstance;
	}

	private AudioPlayerBackground(Context context)
	{
		mContext = context;
		mMediaQueue = new GxMediaQueue();

		LocalBroadcastManager.getInstance(context).registerReceiver(mMediaItemFinishedReceiver, MEDIA_ITEM_FINISHED_INTENT_FILTER);
		LocalBroadcastManager.getInstance(context).registerReceiver(mCustomActionEventReceiver, CUSTOM_ACTION_EVENT_INTENT_FILTER);

		// MediaBrowser must be created in the UI thread.
		Services.Device.invokeOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				// Connect a media browser just to get the media session token. There are other ways
				// this can be done, for example by sharing the session token directly.
				mMediaBrowser = new MediaBrowserCompat(mContext, new ComponentName(mContext, MusicService.class), mConnectionCallback, null);
				mMediaBrowser.connect();
			}
		});
	}

	@SuppressWarnings("FieldCanBeLocal")
	private final MediaBrowserCompat.ConnectionCallback mConnectionCallback = new MediaBrowserCompat.ConnectionCallback()
	{
		@Override
		public void onConnected()
		{

			if (mMediaController != null)
				mMediaController.unregisterCallback(mMediaCallback);

			mMediaController = new MediaControllerCompat(mContext, mMediaBrowser.getSessionToken());
			mMediaController.registerCallback(mMediaCallback);

		}
	};

	private final MediaControllerCompat.Callback mMediaCallback = new MediaControllerCompat.Callback()
	{
		@Override
		public void onPlaybackStateChanged(PlaybackStateCompat state)
		{
			super.onPlaybackStateChanged(state);
			AudioApi.onQueueStateChanged(new GxMediaQueueState(state), getActivity());
		}
	};

	public static final IntentFilter MEDIA_ITEM_FINISHED_INTENT_FILTER = new IntentFilter("com.genexus.android.media.audio.MEDIA_ITEM_FINISHED");
	public static final IntentFilter CUSTOM_ACTION_EVENT_INTENT_FILTER = new IntentFilter("com.genexus.android.media.audio.CUSTOM_ACTION_EVENT");

	@SuppressWarnings("FieldCanBeLocal")
	private final BroadcastReceiver mMediaItemFinishedReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			AudioApi.onMediaItemFinished(new GxMediaItemFinishedArgs(intent), getActivity());
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final BroadcastReceiver mCustomActionEventReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String actionId = intent.getStringExtra(PlaybackExtras.EXTRA_CUSTOM_ACTION_ID);
			String mediaId = intent.getStringExtra(PlaybackExtras.EXTRA_ACTION_MEDIA_ID);

			GxMediaItem mediaItem = mMediaQueue.findItem(mediaId);
			if (mediaItem != null)
				AudioApi.onCustomActionEvent(actionId, mediaItem, getActivity());
		}
	};

	private IGxActivity getActivity() {
		return Cast.as(IGxActivity.class, ActivityHelper.getCurrentActivity());
	}

	@Override
	public void play(GxMediaItem audio)
	{
		GxMediaQueue queue = GxMediaQueue.single(audio);
		setPlaybackQueue(queue);
		play();
	}

	@Override
	public void play()
	{
		if (mMediaController != null)
			mMediaController.getTransportControls().play();
	}

	@Override
	public boolean isPlaying()
	{
		//noinspection SimplifiableIfStatement
		if (mMediaController != null)
			return GxPlaybackState.isPlaying(mMediaController.getPlaybackState());
		else
			return false;
	}

	@Override
	public void pause()
	{
		if (mMediaController != null)
			mMediaController.getTransportControls().pause();
	}

	@Override
	public void stop()
	{
		if (mMediaController != null)
			mMediaController.getTransportControls().stop();
	}

	public void setPlaybackQueue(GxMediaQueue queue)
	{
		mMediaQueue = queue;

		if (mMediaController != null)
			mMediaController.getTransportControls().sendCustomAction(PlaybackExtras.ACTION_SET_QUEUE, null);
	}

	public GxMediaQueue getPlaybackQueue()
	{
		return mMediaQueue;
	}

	public GxMediaQueueState getMediaQueueState()
	{
		if (mMediaController != null)
		{
			PlaybackStateCompat playbackState = mMediaController.getPlaybackState();
			return new GxMediaQueueState(playbackState);
		}
		else
			return GxMediaQueueState.none();
	}

	public void setQueueIndex(int index)
	{
		// index is 1-based here.
		if (mMediaController != null && index >= 1 && index <= mMediaQueue.getItems().size())
			mMediaController.getTransportControls().skipToQueueItem(index);
	}

	public void setQueueCurrentId(String mediaId)
	{
		if (mMediaController != null && Strings.hasValue(mediaId))
		{
			Bundle args = new Bundle();
			args.putString(PlaybackExtras.EXTRA_ACTION_MEDIA_ID, mediaId);
			mMediaController.getTransportControls().sendCustomAction(PlaybackExtras.ACTION_SKIP_TO_MEDIA_ID, args);
		}
	}

	public void setPlayerSettings(GxAudioPlayerSettings settings)
	{
		mSettings = settings;
	}

	public @NonNull GxAudioPlayerSettings getPlayerSettings()
	{
		return (mSettings != null ? mSettings : GxAudioPlayerSettings.DEFAULT);
	}

	public @Nullable ThemeClassDefinition getPlayerThemeClass()
	{
		// Cannot be chosen right now, but might be.
		return Services.Themes.getThemeClass(AUDIO_PLAYER_CLASS_NAME);
	}
}
