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

import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.genexus.android.uamp.PlaybackExtras;
import com.genexus.android.uamp.model.MusicProvider;
import com.genexus.android.uamp.utils.LogHelper;
import com.genexus.android.media.model.GxMediaItemFinishedArgs;

/**
 * Manage the interactions among the container service, the queue manager and the actual playback.
 */
public class PlaybackManager implements Playback.Callback {

    private static final String TAG = LogHelper.makeLogTag(PlaybackManager.class);

    private MusicProvider mMusicProvider;
    private QueueManager mQueueManager;
    private Playback mPlayback;
    private PlaybackServiceCallback mServiceCallback;
    private MediaSessionCallback mMediaSessionCallback;

    public PlaybackManager(PlaybackServiceCallback serviceCallback,
                           MusicProvider musicProvider, QueueManager queueManager,
                           Playback playback) {
        mMusicProvider = musicProvider;
        mServiceCallback = serviceCallback;
        mQueueManager = queueManager;
        mMediaSessionCallback = new MediaSessionCallback();
        mPlayback = playback;
        mPlayback.setCallback(this);
    }

    public Playback getPlayback() {
        return mPlayback;
    }

    public MediaSessionCompat.Callback getMediaSessionCallback() {
        return mMediaSessionCallback;
    }

    /**
     * Handle a request to play music
     */
    public void handlePlayRequest() {
        LogHelper.d(TAG, "handlePlayRequest: mState=" + mPlayback.getState());
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
        if (currentMusic != null) {
            mServiceCallback.onPlaybackStart();
            mPlayback.play(currentMusic);
        }
    }

    /**
     * Handle a request to pause music
     */
    public void handlePauseRequest() {
        LogHelper.d(TAG, "handlePauseRequest: mState=" + mPlayback.getState());
        if (mPlayback.isPlaying()) {
            mPlayback.pause();
            mServiceCallback.onPlaybackStop(false);
        }
    }

    /**
     * Handle a request to stop music
     *
     * @param withError Error message in case the stop has an unexpected cause. The error
     *                  message will be set in the PlaybackState and will be visible to
     *                  MediaController clients.
     */
    public void handleStopRequest(String withError) {
        LogHelper.d(TAG, "handleStopRequest: mState=" + mPlayback.getState() + " error=", withError);
        mPlayback.stop(true);
        mServiceCallback.onPlaybackStop(true);
        updatePlaybackState(withError);
    }


    /**
     * Update the current media player state, optionally showing an error message.
     *
     * @param error if not null, error message to present to the user.
     */
    @SuppressWarnings("deprecation")
    public void updatePlaybackState(String error) {
        LogHelper.d(TAG, "updatePlaybackState, playback state=" + mPlayback.getState());
        long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        if (mPlayback != null && mPlayback.isConnected()) {
            position = mPlayback.getCurrentStreamPosition();
        }

        //noinspection ResourceType
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(getAvailableActions());

        int state = mPlayback.getState();

        // If there is an error message, send it to the playback state:
        if (error != null) {
            // Error states are really only supposed to be used for errors that cause playback to
            // stop unexpectedly and persist until the user takes action to fix it.
            stateBuilder.setErrorMessage(error);
            state = PlaybackStateCompat.STATE_ERROR;
        }
        //noinspection ResourceType
        stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime());

        // Set the activeQueueItemId if the current index is valid.
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
        if (currentMusic != null) {
            stateBuilder.setActiveQueueItemId(currentMusic.getQueueId());
        }

        // Set the state that depends on the current queue.
        mQueueManager.updateQueuePlaybackState(stateBuilder);

        mServiceCallback.onPlaybackStateUpdated(stateBuilder.build());

        if (state == PlaybackStateCompat.STATE_PLAYING ||
                state == PlaybackStateCompat.STATE_PAUSED) {
            mServiceCallback.onNotificationRequired();
        }
    }

    private long getAvailableActions() {
        long actions = PlaybackStateCompat.ACTION_PLAY;
//                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
//                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
//                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
//                PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        if (mPlayback.isPlaying()) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        }

        actions = mQueueManager.updateQueueAvailableActions(actions);
        return actions;
    }

    /**
     * Implementation of the Playback.Callback interface
     */
    @Override
    public void onCompletion() {
        // The media player finished playing the current song, so we go ahead
        // and start the next.
        onItemPlaybackFinished(GxMediaItemFinishedArgs.FINISH_REASON_PLAYBACK_COMPLETE);

        if (mQueueManager.skipQueuePosition(QueueManager.SKIP_TO_NEXT_AUTOMATIC)) {
            handlePlayRequest();
        } else {
            // If skipping was not possible, we stop and release the resources:
            handleStopRequest(null);
        }

        mQueueManager.updateMetadata();
    }

    @Override
    public void onPlaybackStatusChanged(int state) {
        updatePlaybackState(null);
    }

    @Override
    public void onError(String error) {
        updatePlaybackState(error);
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        LogHelper.d(TAG, "setCurrentMediaId", mediaId);
        mQueueManager.setQueueFromMusic(mediaId);
    }

    /**
     * Switch to a different Playback instance, maintaining all playback state, if possible.
     *
     * @param playback switch to this playback
     */
    public void switchToPlayback(Playback playback, boolean resumePlaying) {
        if (playback == null) {
            throw new IllegalArgumentException("Playback cannot be null");
        }
        // suspend the current one.
        int oldState = mPlayback.getState();
        int pos = mPlayback.getCurrentStreamPosition();
        String currentMediaId = mPlayback.getCurrentMediaId();
        mPlayback.stop(false);
        playback.setCallback(this);
        playback.setCurrentStreamPosition(pos < 0 ? 0 : pos);
        playback.setCurrentMediaId(currentMediaId);
        playback.start();
        // finally swap the instance
        mPlayback = playback;
        switch (oldState) {
            case PlaybackStateCompat.STATE_BUFFERING:
            case PlaybackStateCompat.STATE_CONNECTING:
            case PlaybackStateCompat.STATE_PAUSED:
                mPlayback.pause();
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
                if (resumePlaying && currentMusic != null) {
                    mPlayback.play(currentMusic);
                } else if (!resumePlaying) {
                    mPlayback.pause();
                } else {
                    mPlayback.stop(true);
                }
                break;
            case PlaybackStateCompat.STATE_NONE:
                break;
            default:
                LogHelper.d(TAG, "Default called. Old state is ", oldState);
        }
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            LogHelper.d(TAG, "play");
            if (mQueueManager.getCurrentMusic() != null) {
                handlePlayRequest();
                mQueueManager.updateMetadata(); //matiash
            }
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
            LogHelper.d(TAG, "OnSkipToQueueItem with queueId:" + queueId);

            boolean wasPlaying = isPlaying(false);
            if (wasPlaying)
                onItemPlaybackFinished(GxMediaItemFinishedArgs.FINISH_REASON_SKIPPED);

            if (mQueueManager.setCurrentQueueItem(queueId))
            {
                if (wasPlaying)
                    handlePlayRequest();

                mQueueManager.updateMetadata();
            }
        }

        private void onSkipToQueueItem(String mediaId)
        {
            LogHelper.d(TAG, "OnSkipToQueueItem with mediaId:" + mediaId);

            boolean wasPlaying = isPlaying(false);
            if (wasPlaying)
                onItemPlaybackFinished(GxMediaItemFinishedArgs.FINISH_REASON_SKIPPED);

            if (mQueueManager.setCurrentQueueItem(mediaId))
            {
                if (wasPlaying)
                    handlePlayRequest();

                mQueueManager.updateMetadata();
            }
        }

        @Override
        public void onSeekTo(long position) {
            LogHelper.d(TAG, "onSeekTo:", position);
            mPlayback.seekTo((int) position);
        }

        /*
        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            LogHelper.d(TAG, "playFromMediaId mediaId:", mediaId, "  extras=", extras);
            mQueueManager.setQueueFromMusic(mediaId);
            handlePlayRequest();
        }
        */

        @Override
        public void onPause() {
            LogHelper.d(TAG, "pause. current state=" + mPlayback.getState());
            handlePauseRequest();
        }

        @Override
        public void onStop() {
            LogHelper.d(TAG, "stop. current state=" + mPlayback.getState());
            onItemPlaybackFinished(GxMediaItemFinishedArgs.FINISH_REASON_STOPPED);

            handleStopRequest(null);
        }

        @Override
        public void onSkipToNext() {
            LogHelper.d(TAG, "skipToNext");
            onItemPlaybackFinished(GxMediaItemFinishedArgs.FINISH_REASON_SKIPPED);

            if (mQueueManager.skipQueuePosition(1)) {
                handlePlayRequest();
            } else {
                handleStopRequest("Cannot skip");
            }
            mQueueManager.updateMetadata();
        }

        @Override
        public void onSkipToPrevious() {
            LogHelper.d(TAG, "skipToPrevious");
            onItemPlaybackFinished(GxMediaItemFinishedArgs.FINISH_REASON_SKIPPED);

            if (mQueueManager.skipQueuePosition(-1)) {
                handlePlayRequest();
            } else {
                handleStopRequest("Cannot skip");
            }
            mQueueManager.updateMetadata();
        }

        @Override
        public void onCustomAction(@NonNull String action, Bundle extras)
        {
            if (PlaybackExtras.ACTION_SET_QUEUE.equals(action))
            {
                // Get state to preserve current position if currently playing track is present in new queue.
                MediaSessionCompat.QueueItem currentItem = mQueueManager.getCurrentMusic();
                boolean wasPlaying = isPlaying(true);

                int updateType = mQueueManager.updatePlayingQueue(wasPlaying);
                if (updateType == QueueManager.UPDATED_QUEUE_SEAMLESSLY)
                {
                    updatePlaybackState(null); // To refresh next/previous action availability.
                }
                else
                {
                    if (wasPlaying)
                        onItemPlaybackFinished(currentItem, GxMediaItemFinishedArgs.FINISH_REASON_QUEUE_REPLACED);

                    handleStopRequest(null);
                    mQueueManager.updateMetadata();
                }
            }
            else if (PlaybackExtras.ACTION_SKIP_TO_MEDIA_ID.equalsIgnoreCase(action))
            {
                String mediaId = extras.getString(PlaybackExtras.EXTRA_ACTION_MEDIA_ID);
                onSkipToQueueItem(mediaId);
            }
            else if (PlaybackExtras.ACTION_TOGGLE_REPEAT.equalsIgnoreCase(action))
            {
                mQueueManager.toggleRepeat();
                updatePlaybackState(null);
            }
            else if (PlaybackExtras.ACTION_TOGGLE_SHUFFLE.equalsIgnoreCase(action))
            {
                mQueueManager.toggleShuffle();
                updatePlaybackState(null);
            }
            else if (PlaybackExtras.ACTION_TOGGLE_FAVORITE.equalsIgnoreCase(action))
            {
                MediaSessionCompat.QueueItem currentItem = mQueueManager.getCurrentMusic();
                if (currentItem != null)
                {
                    mMusicProvider.toggleFavorite(currentItem.getDescription().getMediaId());
                    mQueueManager.updateMetadata();

                    // Also fire a specific "custom action".
                    mServiceCallback.onCustomAction(PlaybackExtras.CUSTOM_ACTION_ID_FAVORITE, currentItem);
                }
            }
            else if (PlaybackExtras.ACTION_CUSTOM.equalsIgnoreCase(action))
            {
                String actionId = extras.getString(PlaybackExtras.EXTRA_CUSTOM_ACTION_ID);
                MediaSessionCompat.QueueItem currentItem = mQueueManager.getCurrentMusic();

                if (currentItem != null && !TextUtils.isEmpty(actionId))
                    mServiceCallback.onCustomAction(actionId, currentItem);
            }
            else
                LogHelper.e(TAG, "Unsupported action: ", action);
        }

        /**
         * Handle free and contextual searches.
         * <p/>
         * All voice searches on Android Auto are sent to this method through a connected
         * {@link android.support.v4.media.session.MediaControllerCompat}.
         * <p/>
         * Threads and async handling:
         * Search, as a potentially slow operation, should run in another thread.
         * <p/>
         * Since this method runs on the main thread, most apps with non-trivial metadata
         * should defer the actual search to another thread (for example, by using
         * an {@link TaskRunner} as we do here).
         **/
        /*
        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
            LogHelper.d(TAG, "playFromSearch  query=", query, " extras=", extras);

            mPlayback.setState(PlaybackStateCompat.STATE_CONNECTING);
            mQueueManager.setQueueFromSearch(query, extras);
            handlePlayRequest();
            mQueueManager.updateMetadata();
        }
        */
    }

    private boolean isPlaying(boolean orPaused)
    {
        int state = mPlayback.getState();
        MediaSessionCompat.QueueItem currentItem = mQueueManager.getCurrentMusic();

        return (currentItem != null &&
                (orPaused || state != PlaybackStateCompat.STATE_PAUSED) &&
                state != PlaybackStateCompat.STATE_NONE &&
                state != PlaybackStateCompat.STATE_STOPPED &&
                state != PlaybackStateCompat.STATE_ERROR);
    }

    private void onItemPlaybackFinished(int reason)
    {
        onItemPlaybackFinished(mQueueManager.getCurrentMusic(), reason);
    }

    private void onItemPlaybackFinished(MediaSessionCompat.QueueItem item, int reason)
    {
        if (item != null)
            mServiceCallback.onItemPlaybackFinished(item, reason);
    }


    public interface PlaybackServiceCallback {
        void onPlaybackStart();

        void onNotificationRequired();

        void onPlaybackStop(boolean fullStop);

        void onPlaybackStateUpdated(PlaybackStateCompat newState);
        void onItemPlaybackFinished(MediaSessionCompat.QueueItem item, int reason);
        void onCustomAction(String actionId, MediaSessionCompat.QueueItem item);
    }
}
