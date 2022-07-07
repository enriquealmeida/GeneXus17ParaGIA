package com.genexus.android.media.video;

import java.util.List;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.media.cast.CastUtils;
import com.genexus.android.media.model.GxMediaItem;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadOptions;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

/**
 * Media playback API.
 */

public class VideoApi extends ExternalApi
{
	public static final String OBJECT_NAME = "GxVideo";

	private static final String METHOD_PLAY = "Play";

	public VideoApi(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters)
	{
		if (METHOD_PLAY.equalsIgnoreCase(method) && parameters.size() == 1)
		{
			Entity itemSdt = Cast.as(Entity.class, parameters.get(0));
			if (itemSdt != null)
			{
				playVideo(new GxMediaItem(itemSdt));
				return ExternalApiResult.SUCCESS_CONTINUE;
			}
			else
				return ExternalApiResult.FAILURE;
		}
		else
			return ExternalApiResult.failureUnknownMethod(this, method);
	}

	private void playVideo(GxMediaItem mediaItem)
	{
		String contentType = mediaItem.getContentType();
		if (!Strings.hasValue(contentType))
			contentType = "video/mp4";

		MediaInfo mediaInfo = CastUtils.toCastMediaInfo(mediaItem.getMediaUri(),
				MediaMetadata.MEDIA_TYPE_MOVIE, contentType, mediaItem.toMediaMetadata(), null);

		// If we're connected to Chromecast, play there directly.
		CastSession castSession = CastContext.getSharedInstance(Services.Application.getAppContext()).getSessionManager().getCurrentCastSession();
		if (castSession != null && castSession.isConnected())
		{
			RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
			if (remoteMediaClient != null) {
				MediaLoadOptions optios = new MediaLoadOptions.Builder()
						.setAutoplay(true)
						.build();
				remoteMediaClient.load(mediaInfo, optios);
			}
		}
		else
		{
			// Otherwise show an activity for playback.
			if (getActivity() != null)
			{
				Intent intent = new Intent(getActivity(), LocalVideoPlayerActivity.class);
				intent.putExtra("media", mediaInfo);
				intent.putExtra("shouldStart", true);

				getActivity().startActivity(intent);
			}
		}
	}
}
