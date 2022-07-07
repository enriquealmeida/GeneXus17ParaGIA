/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.genexus.android.uamp.playback;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.genexus.android.uamp.PlaybackExtras;
import com.genexus.android.uamp.model.MusicProvider;
import com.genexus.android.uamp.utils.LogHelper;
import com.genexus.android.media.cast.CastUtils;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadOptions;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

import static android.support.v4.media.session.MediaSessionCompat.QueueItem;

/**
 * An implementation of Playback that talks to Cast.
 */
@SuppressWarnings("deprecation")
public class CastPlayback implements Playback {

    private static final String TAG = LogHelper.makeLogTag(CastPlayback.class);

    private static final String MIME_TYPE_AUDIO_MPEG = "audio/mpeg";
    private static final String ITEM_ID = "itemId";

    private final MusicProvider mMusicProvider;
    private final Context mAppContext;
    private final RemoteMediaClient mRemoteMediaClient;
    private final RemoteMediaClient.Listener mRemoteMediaClientListener;

    /** The current PlaybackState*/
    private int mState;
    /** Callback for making completion/error calls on */
    private Callback mCallback;
    private volatile int mCurrentPosition;
    private volatile String mCurrentMediaId;
    private boolean mClientListenerRegistered;

    public CastPlayback(Context context, MusicProvider musicProvider) {
        mAppContext = context.getApplicationContext();
        mMusicProvider = musicProvider;

        CastSession castSession = CastContext.getSharedInstance(mAppContext).getSessionManager()
                .getCurrentCastSession();
        mRemoteMediaClient = castSession.getRemoteMediaClient();
        mRemoteMediaClientListener = new CastMediaClientListener();
    }

    @Override
    public void start() {
        if (!mClientListenerRegistered)
        {
            mRemoteMediaClient.addListener(mRemoteMediaClientListener);
            mClientListenerRegistered = true;
        }
    }

    @Override
    public void stop(boolean notifyListeners) {
        if (mRemoteMediaClient.hasMediaSession())
            mRemoteMediaClient.stop();

        if (mClientListenerRegistered)
        {
            mRemoteMediaClient.removeListener(mRemoteMediaClientListener);
            mClientListenerRegistered = false;
        }

        mState = PlaybackStateCompat.STATE_STOPPED;
        mCurrentPosition = 0;
        if (notifyListeners && mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    @Override
    public void setState(int state) {
        this.mState = state;
    }

    @Override
    public int getCurrentStreamPosition() {
        if (!isConnected()) {
            return mCurrentPosition;
        }
        return (int) mRemoteMediaClient.getApproximateStreamPosition();
    }

    @Override
    public void setCurrentStreamPosition(int pos) {
        this.mCurrentPosition = pos;
    }

    @Override
    public void updateLastKnownStreamPosition() {
        mCurrentPosition = getCurrentStreamPosition();
    }

    @Override
    public void play(QueueItem item) {
        try {
            start(); // Register client if necessary.
            loadMedia(item.getDescription().getMediaId(), true);
            mState = PlaybackStateCompat.STATE_BUFFERING;
            if (mCallback != null) {
                mCallback.onPlaybackStatusChanged(mState);
            }
        } catch (JSONException e) {
            LogHelper.e(TAG, "Exception loading media ", e, null);
            if (mCallback != null) {
                mCallback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void pause() {
        try {
            if (mRemoteMediaClient.hasMediaSession()) {
                mRemoteMediaClient.pause();
                mCurrentPosition = (int) mRemoteMediaClient.getApproximateStreamPosition();
            } else {
                loadMedia(mCurrentMediaId, false);
            }
        } catch (JSONException e) {
            LogHelper.e(TAG, e, "Exception pausing cast playback");
            if (mCallback != null) {
                mCallback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void seekTo(int position) {
        if (mCurrentMediaId == null) {
            if (mCallback != null) {
                mCallback.onError("seekTo cannot be calling in the absence of mediaId.");
            }
            return;
        }
        try {
            if (mRemoteMediaClient.hasMediaSession()) {
                mRemoteMediaClient.seek(position);
                mCurrentPosition = position;
            } else {
                mCurrentPosition = position;
                loadMedia(mCurrentMediaId, false);
            }
        } catch (JSONException e) {
            LogHelper.e(TAG, e, "Exception pausing cast playback");
            if (mCallback != null) {
                mCallback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        this.mCurrentMediaId = mediaId;
    }

    @Override
    public String getCurrentMediaId() {
        return mCurrentMediaId;
    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public boolean isConnected() {
        CastSession castSession = CastContext.getSharedInstance(mAppContext).getSessionManager()
                .getCurrentCastSession();
        return (castSession != null && castSession.isConnected());
    }

    @Override
    public boolean isPlaying() {
        return isConnected() && mRemoteMediaClient.isPlaying();
    }

    @Override
    public int getState() {
        return mState;
    }

    private void loadMedia(String mediaId, boolean autoPlay) throws JSONException {
        String mediaUri = mMusicProvider.getUri(mediaId);
        MediaMetadataCompat track = mMusicProvider.getMetadata(mediaId);
        if (mediaUri == null || track == null) {
            throw new IllegalArgumentException("Invalid mediaId " + mediaId);
        }

        if (mRemoteMediaClient.hasMediaSession() && TextUtils.equals(mediaId, mCurrentMediaId))
        {
            mRemoteMediaClient.play();
        }
        else
        {
            if (!TextUtils.equals(mediaId, mCurrentMediaId))
            {
                mCurrentMediaId = mediaId;
                mCurrentPosition = 0;
            }
            JSONObject customData = new JSONObject();
            customData.put(ITEM_ID, mediaId);
            MediaInfo media = toCastMediaMetadata(mediaUri, track, customData);
            MediaLoadOptions options = new MediaLoadOptions.Builder()
                    .setAutoplay(autoPlay)
                    .setPlayPosition(mCurrentPosition)
                    .setCustomData(customData)
                    .build();
            mRemoteMediaClient.load(media, options);
        }
    }

    /**
     * Helper method to convert a {@link android.media.MediaMetadata} to a
     * {@link com.google.android.gms.cast.MediaInfo} used for sending media to the receiver app.
     *
     * @param track {@link com.google.android.gms.cast.MediaMetadata}
     * @param customData custom data specifies the local mediaId used by the player.
     * @return {@link com.google.android.gms.cast.MediaInfo}
     */
    private static MediaInfo toCastMediaMetadata(String mediaUri, MediaMetadataCompat track, JSONObject customData)
    {
        //noinspection WrongConstant
        String contentType = track.getString(PlaybackExtras.METADATA_KEY_CONTENT_TYPE);
        if (TextUtils.isEmpty(contentType))
            contentType = MIME_TYPE_AUDIO_MPEG;

        return CastUtils.toCastMediaInfo(mediaUri, MediaMetadata.MEDIA_TYPE_MUSIC_TRACK,
                contentType, track, customData);
    }

    private void setMetadataFromRemote() {
        // Sync: We get the customData from the remote media information and update the local
        // metadata if it happens to be different from the one we are currently using.
        // This can happen when the app was either restarted/disconnected + connected, or if the
        // app joins an existing session while the Chromecast was playing a queue.
        try {
            MediaInfo mediaInfo = mRemoteMediaClient.getMediaInfo();
            if (mediaInfo == null) {
                return;
            }
            JSONObject customData = mediaInfo.getCustomData();

            if (customData != null && customData.has(ITEM_ID)) {
                String remoteMediaId = customData.getString(ITEM_ID);
                if (!TextUtils.equals(mCurrentMediaId, remoteMediaId)) {
                    mCurrentMediaId = remoteMediaId;
                    if (mCallback != null) {
                        mCallback.setCurrentMediaId(remoteMediaId);
                    }
                    updateLastKnownStreamPosition();
                }
            }
        } catch (JSONException e) {
            LogHelper.e(TAG, e, "Exception processing update metadata");
        }

    }

    private void updatePlaybackState() {
        int status = mRemoteMediaClient.getPlayerState();
        int idleReason = mRemoteMediaClient.getIdleReason();

        LogHelper.d(TAG, "onRemoteMediaPlayerStatusUpdated ", status);

        // Convert the remote playback states to media playback states.
        switch (status) {
            case MediaStatus.PLAYER_STATE_IDLE:
                if (idleReason == MediaStatus.IDLE_REASON_FINISHED) {
                    if (mCallback != null) {
                        mCallback.onCompletion();
                    }
                }
                break;
            case MediaStatus.PLAYER_STATE_BUFFERING:
                mState = PlaybackStateCompat.STATE_BUFFERING;
                if (mCallback != null) {
                    mCallback.onPlaybackStatusChanged(mState);
                }
                break;
            case MediaStatus.PLAYER_STATE_PLAYING:
                mState = PlaybackStateCompat.STATE_PLAYING;
                setMetadataFromRemote();
                if (mCallback != null) {
                    mCallback.onPlaybackStatusChanged(mState);
                }
                break;
            case MediaStatus.PLAYER_STATE_PAUSED:
                mState = PlaybackStateCompat.STATE_PAUSED;
                setMetadataFromRemote();
                if (mCallback != null) {
                    mCallback.onPlaybackStatusChanged(mState);
                }
                break;
            default: // case unknown
                LogHelper.d(TAG, "State default : ", status);
                break;
        }
    }

    private class CastMediaClientListener implements RemoteMediaClient.Listener {

        @Override
        public void onMetadataUpdated() {
            LogHelper.d(TAG, "RemoteMediaClient.onMetadataUpdated");
            setMetadataFromRemote();
        }

        @Override
        public void onStatusUpdated() {
            LogHelper.d(TAG, "RemoteMediaClient.onStatusUpdated");
            updatePlaybackState();
        }

        @Override
        public void onSendingRemoteMediaRequest() {
        }

        @Override
        public void onQueueStatusUpdated() {
        }

        @Override
        public void onPreloadStatusUpdated() {
        }

        @Override
        public void onAdBreakStatusUpdated() {
        }
    }
}
