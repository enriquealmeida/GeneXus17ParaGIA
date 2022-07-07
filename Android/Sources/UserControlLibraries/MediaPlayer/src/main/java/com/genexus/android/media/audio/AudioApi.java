package com.genexus.android.media.audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.ActivityResourceBase;
import com.genexus.android.ActivityResources;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.media.customization.GxAudioPlayerSettings;
import com.genexus.android.media.model.GxMediaItem;
import com.genexus.android.media.model.GxMediaItemFinishedArgs;
import com.genexus.android.media.model.GxMediaQueue;
import com.genexus.android.media.model.GxMediaQueueState;
import com.genexus.android.media.ui.ApplicationAudioResource;

public class AudioApi extends ExternalApi
{
	public static final String OBJECT_NAME = "GeneXus.SD.Media.Audio";

	private static final String METHOD_PLAY = "Play";
	private static final String METHOD_PLAY_BACKGROUND = "PlayBackground";
	private static final String METHOD_STOP = "Stop";
	private static final String METHOD_IS_PLAYING = "IsPlaying";

	// Queue-related methods.
	private static final String METHOD_SET_QUEUE = "SetQueue";
	private static final String METHOD_GET_QUEUE = "GetQueue";
	private static final String METHOD_GET_QUEUE_STATE = "GetQueueState";
	private static final String METHOD_PLAY_QUEUE = "PlayQueue";
	private static final String METHOD_PAUSE_QUEUE = "PauseQueue";
	private static final String METHOD_SET_QUEUE_INDEX = "SetQueueCurrentIndex";
	private static final String METHOD_SET_QUEUE_ITEM = "SetQueueCurrentItem";
	private static final String METHOD_SET_PLAYER_SETTINGS = "SetPlayerSettings";

	private static final String EVENT_QUEUE_STATE_CHANGED = "QueueStateChanged";
	private static final String EVENT_QUEUE_ITEM_FINISHED = "QueueItemFinished";
	private static final String EVENT_CUSTOM_ACTION = "CustomActionEvent";

	private static final String TYPE_BACKGROUND = "1";
	private static final String TYPE_FOREGROUND = "(foreground)";
	private static final String TYPE_MIX = "2";
	private static final String TYPE_SOLO = "3";

	public AudioApi(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters)
	{
		if (METHOD_PLAY.equalsIgnoreCase(method) && parameters.size() == 2)
		{
			play(Strings.toString(parameters.get(0)), Strings.toString(parameters.get(1)), null);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_PLAY_BACKGROUND.equalsIgnoreCase(method) && parameters.size() >= 1)
		{
			String uri = Strings.toString(parameters.get(0));
			String description = (parameters.size() >= 2 ? Strings.toString(parameters.get(1)) : null);

			play(uri, TYPE_BACKGROUND, description);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_STOP.equalsIgnoreCase(method))
		{
			String type = (parameters.size() >= 1 ? Strings.toString(parameters.get(0)) : null);
			getActivityAudio().stop(type);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_IS_PLAYING.equalsIgnoreCase(method))
		{
			String type = (parameters.size() >= 1 ? Strings.toString(parameters.get(0)) : null);
			Boolean result = getActivityAudio().isPlaying(type);
			return ExternalApiResult.success(result.toString());
		}
		else if (METHOD_SET_QUEUE.equalsIgnoreCase(method) && parameters.size() >= 1)
		{
			// As per spec, this stores a COPY of the supplied queue SDT, not the actual one.
			Entity queueSdt = Cast.as(Entity.class, parameters.get(0));
			GxMediaQueue mediaQueue = new GxMediaQueue(queueSdt);

			getBackgroundAudio().setPlaybackQueue(mediaQueue);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_GET_QUEUE.equalsIgnoreCase(method))
		{
			// As per spec, this returns a COPY of the local queue, not the actual one.
			GxMediaQueue mediaQueue = getBackgroundAudio().getPlaybackQueue();
			return ExternalApiResult.success(mediaQueue.toSdt());
		}
		else if (METHOD_GET_QUEUE_STATE.equalsIgnoreCase(method))
		{
			GxMediaQueueState mediaQueueState = getBackgroundAudio().getMediaQueueState();
			return ExternalApiResult.success(mediaQueueState.toSdt());
		}
		else if (METHOD_PLAY_QUEUE.equalsIgnoreCase(method))
		{
			getBackgroundAudio().play();
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_PAUSE_QUEUE.equalsIgnoreCase(method))
		{
			getBackgroundAudio().pause();
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_SET_QUEUE_INDEX.equalsIgnoreCase(method) && parameters.size() >= 1)
		{
			Integer index = Services.Strings.tryParseInt(Strings.toString(parameters.get(0)));
			if (index != null)
				getBackgroundAudio().setQueueIndex(index);

			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_SET_QUEUE_ITEM.equalsIgnoreCase(method) && parameters.size() >= 1)
		{
			getBackgroundAudio().setQueueCurrentId(Strings.toString(parameters.get(0)));
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else if (METHOD_SET_PLAYER_SETTINGS.equalsIgnoreCase(method) && parameters.size() >= 1)
		{
			Entity settingsSdt = Cast.as(Entity.class, parameters.get(0));
			GxAudioPlayerSettings settings = new GxAudioPlayerSettings(settingsSdt);

			getBackgroundAudio().setPlayerSettings(settings);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
		else
			return ExternalApiResult.failureUnknownMethod(this, method);
	}

	private void play(String uri, String type, String description)
	{
		if (Strings.hasValue(uri))
		{
			// Assume "mp3" (audio/mpeg) for audio file.
			GxMediaItem audio = new GxMediaItem(uri, "audio/mpeg", description);
			getActivityAudio().play(audio, type);
		}
	}

	static void onQueueStateChanged(GxMediaQueueState state, IGxActivity activity)
	{
		// Log.d(EVENT_QUEUE_STATE_CHANGED, state.toString());
		fireEvent(activity, EVENT_QUEUE_STATE_CHANGED, state.toSdt());
	}

	static void onMediaItemFinished(GxMediaItemFinishedArgs finishedArgs, IGxActivity activity)
	{
		// Log.d(EVENT_QUEUE_ITEM_FINISHED, finishedArgs.toString());
		fireEvent(activity, EVENT_QUEUE_ITEM_FINISHED, finishedArgs.toSdt());
	}

	static void onCustomActionEvent(String actionId, GxMediaItem mediaItem, IGxActivity activity)
	{
		fireEvent(activity, EVENT_CUSTOM_ACTION, actionId, mediaItem.toSdt());
	}

	private static void fireEvent(IGxActivity activity, String eventName, Object... eventArgs)
	{
		ExternalObjectEvent event = new ExternalObjectEvent(OBJECT_NAME, eventName);
		Coordinator coordinator = activity == null ? null : event.getFormCoordinatorForEvent(activity);
		event.fire(Arrays.asList(eventArgs), coordinator, null);
	}

	private ActivityAudio getActivityAudio()
	{
		Activity activity = getActivity();
		ApplicationAudioResource.getInstance().setCurrentMediaPlayerActivity(activity);

		return ActivityResources.getResource(activity, ActivityAudio.class,
			new Function<Activity, ActivityAudio>()
			{
				@Override
				public ActivityAudio run(Activity activity) { return new ActivityAudio(activity); }
			});
	}

	private AudioPlayerBackground getBackgroundAudio()
	{
		if (getActivity() != null)
			return getActivityAudio().mPlayerBackground;
		else
			return AudioPlayerBackground.getInstance(Services.Application.getAppContext());
	}

	private static class ActivityAudio extends ActivityResourceBase
	{
		private final AudioPlayerBackground mPlayerBackground;
		private final IAudioPlayer mPlayerSolo;
		private final IAudioPlayer mPlayerMix;

		private ActivityAudio(Activity activity)
		{
			// TODO: Create an intent that will open this activity only if it was closed.
			// As a temporary hack, set FLAG_ACTIVITY_SINGLE_TOP. This will return to the application
			// but most likely not create a new activity, since our activities are almost all the same class.
			Intent notificationIntent = new Intent(activity.getIntent());
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

			// Create all audio players with the Application context to prevent memory leaks
			// (e.g. a background audio player will most surely live longer than the activity that starts it).
			Context context = activity.getApplicationContext();

			mPlayerBackground = AudioPlayerBackground.getInstance(context);
			mPlayerSolo = new AudioPlayerSolo(context);
			mPlayerMix = new AudioPlayerMix(context);
		}

		public synchronized void play(GxMediaItem audio, String type)
		{
			if (type == null)
				type = TYPE_BACKGROUND;

			// Note: Solo + Mix mixes, but Mix + Solo stops mix. Account for this second case.
			if (TYPE_SOLO.equalsIgnoreCase(type) && mPlayerMix.isPlaying())
				mPlayerMix.stop();

			//noinspection LoopStatementThatDoesntLoop
			for (IAudioPlayer player : getPlayers(type))
			{
				// Should be only one, but break after starting just in case.
				player.play(audio);
				break;
			}
		}

		public synchronized void stop(String type)
		{
			for (IAudioPlayer player : getPlayers(type))
				player.stop();
		}

		public synchronized boolean isPlaying(String type)
		{
			for (IAudioPlayer player : getPlayers(type))
				if (player.isPlaying())
					return true;

			return false;
		}

		private ArrayList<IAudioPlayer> getPlayers(String type)
		{
			// If type is undefined, return all.
			// Foreground comprises both mix and solo.
			ArrayList<IAudioPlayer> players = new ArrayList<>();

			if (type == null || TYPE_MIX.equalsIgnoreCase(type) || TYPE_FOREGROUND.equalsIgnoreCase(type))
				players.add(mPlayerMix);
			if (type == null || TYPE_SOLO.equalsIgnoreCase(type) || TYPE_FOREGROUND.equalsIgnoreCase(type))
				players.add(mPlayerSolo);
			if (type == null || TYPE_BACKGROUND.equalsIgnoreCase(type))
				players.add(mPlayerBackground);

			return players;
		}

		@Override
		public synchronized void onResume(Activity activity)
		{
			// Resume foreground audio when activity is resumed.
			for (IAudioPlayer player : getPlayers(TYPE_FOREGROUND))
				player.play();
		}

		@Override
		public synchronized void onPause(Activity activity)
		{
			// Pause foreground audio when activity is paused.
			for (IAudioPlayer player : getPlayers(TYPE_FOREGROUND))
				player.pause();
		}

		@Override
		public synchronized void onDestroy(Activity activity)
		{
			// Release all foreground audio resources when destroying an activity.
			for (IAudioPlayer player : getPlayers(TYPE_FOREGROUND))
				player.stop();
		}
	}
}
