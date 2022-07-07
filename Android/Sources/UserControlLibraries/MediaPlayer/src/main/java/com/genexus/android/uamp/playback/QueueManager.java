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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.genexus.android.uamp.AlbumArtCache;
import com.genexus.android.uamp.PlaybackExtras;
import com.genexus.android.uamp.model.MusicProvider;
import com.genexus.android.uamp.utils.LogHelper;
import com.genexus.android.uamp.utils.QueueHelper;

/**
 * Simple data provider for queues. Keeps track of a current queue and a current index in the
 * queue. Also provides methods to set the current queue based on common queries, relying on a
 * given MusicProvider to provide the actual media metadata.
 */
public class QueueManager {
    private static final String TAG = LogHelper.makeLogTag(QueueManager.class);

    static final int SKIP_TO_NEXT_AUTOMATIC = 0;

    static final int UPDATED_QUEUE_SEAMLESSLY = 1;
    static final int UPDATED_QUEUE_STOP = 2;

    private final Context mContext;
    private final MusicProvider mMusicProvider;
    private final MetadataUpdateListener mListener;

    // "Now playing" queue:
    private List<MediaSessionCompat.QueueItem> mPlayingQueue;
    private List<Integer> mPlayingQueueIndexes;
    private int mCurrentIndexPointer;

    private int mRepeatMode;
    private boolean mShuffle;

    public QueueManager(@NonNull Context context, @NonNull MusicProvider musicProvider,
                        @NonNull MetadataUpdateListener listener) {
        this.mContext = context;
        this.mMusicProvider = musicProvider;
        this.mListener = listener;

        mPlayingQueue = Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        mPlayingQueueIndexes = Collections.synchronizedList(new ArrayList<Integer>());
        mCurrentIndexPointer = 0;
    }

    public int updatePlayingQueue(boolean attemptSeamless)
    {
        QueueHelper.PlayingQueue oldQueue = new QueueHelper.PlayingQueue(mPlayingQueue, null, getCurrentIndex());
        QueueHelper.PlayingQueue newQueue = QueueHelper.setupPlayingQueue(mContext, mMusicProvider);
        int returnCode;

        // Reset shuffle and repeat modes.
        mRepeatMode = PlaybackExtras.REPEAT_MODE_NONE;
        mShuffle = false;

        if (attemptSeamless && QueueHelper.applyPlayingQueueSeamlessly(oldQueue, newQueue))
        {
            // Don't stop playback, locate the current song in the new queue and continue.
            setPlayingQueue(newQueue.items, newQueue.current);
            returnCode = UPDATED_QUEUE_SEAMLESSLY;
        }
        else
        {
            // Wholly replace the previous queue with the new one.
            setPlayingQueue(newQueue.items, 0);
            returnCode = UPDATED_QUEUE_STOP;
        }

        mListener.onQueueUpdated(newQueue.title, mPlayingQueue);
        return returnCode;
    }

    private void setPlayingQueue(List<MediaSessionCompat.QueueItem> queueItems, int currentIndex)
    {
        if (mShuffle)
            throw new IllegalStateException("Cannot call this method when shuffle == true!");

        mPlayingQueue.clear();
        mPlayingQueue.addAll(queueItems);

        mPlayingQueueIndexes.clear();
        for (int i = 0; i < mPlayingQueue.size(); i++)
            mPlayingQueueIndexes.add(i);

        mCurrentIndexPointer = currentIndex;
    }

    public void toggleRepeat()
    {
        final int COUNT_REPEAT_MODES = 3;
        mRepeatMode = (mRepeatMode + 1) % COUNT_REPEAT_MODES;
    }

    public void toggleShuffle()
    {
        int currentIndex = getCurrentIndex();
        boolean newShuffle = !mShuffle;
        ArrayList<Integer> newIndexes = new ArrayList<>();
        int newPointer;

        if (newShuffle)
        {
            // Shuffle queue indexes, making the CURRENT item the FIRST one
            newIndexes.add(currentIndex);
            for (int i = 0; i < mPlayingQueue.size(); i++)
            {
                if (i != currentIndex)
                    newIndexes.add(i);
            }

            Collections.shuffle(newIndexes.subList(1, newIndexes.size()));
            newPointer = 0;
        }
        else
        {
            // Reset to original queue order.
            for (int i = 0; i < mPlayingQueue.size(); i++)
                newIndexes.add(i);

            newPointer = currentIndex;
        }

        mShuffle = newShuffle;
        mPlayingQueueIndexes.clear();
        mPlayingQueueIndexes.addAll(newIndexes);
        mCurrentIndexPointer = newPointer;
    }

    public boolean setCurrentQueueItem(long queueId)
    {
        // set the current index on queue from the queue Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, queueId);
        return setCurrentQueueIndex(index);
    }

    public boolean setCurrentQueueItem(String mediaId)
    {
        // set the current index on queue from the music Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, mediaId);
        return setCurrentQueueIndex(index);
    }

    private boolean setCurrentQueueIndex(int index)
    {
        if (index >= 0 && index < mPlayingQueue.size())
        {
            int pointerToIndex = mPlayingQueueIndexes.indexOf(index);
            if (pointerToIndex != -1)
            {
                mCurrentIndexPointer = pointerToIndex;
                mListener.onCurrentQueueIndexUpdated(mCurrentIndexPointer);
                return true;
            }
        }

        return false;
    }

    public boolean skipQueuePosition(int amount)
    {
        if (amount == SKIP_TO_NEXT_AUTOMATIC)
        {
            // Take REPEAT_MODE into account.
            if (mRepeatMode != PlaybackExtras.REPEAT_MODE_SINGLE)
                amount = 1;
            else
                amount = 0;
        }

        int nextIndex = mCurrentIndexPointer + amount;
        boolean continuePlaying = true;

        if (nextIndex < 0)
        {
            // Skipped backwards before first song. Either go to last (if repeating) or stay here.
            if (mRepeatMode == PlaybackExtras.REPEAT_MODE_QUEUE)
                nextIndex = mPlayingQueue.size() - 1;
            else
                nextIndex = 0;
        }
        else if (nextIndex >= mPlayingQueue.size())
        {
            // Skipped forward from last song. Either cycle back (if repeating) or stop playback.
            if (mRepeatMode == PlaybackExtras.REPEAT_MODE_QUEUE)
                nextIndex = (nextIndex % mPlayingQueue.size());
            else
            {
                nextIndex = 0;
                continuePlaying = false;
            }
        }

        if (!QueueHelper.isIndexPlayable(nextIndex, mPlayingQueue)) {
            LogHelper.e(TAG, "Cannot increment queue index by ", amount,
                    ". Current=", mCurrentIndexPointer, " queue length=", mPlayingQueue.size());
            return false;
        }

        mCurrentIndexPointer = nextIndex;
        return continuePlaying;
    }

    public void setQueueFromMusic(String mediaId)
    {
        LogHelper.d(TAG, "setQueueFromMusic", mediaId);

        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, mediaId);
        if (index != -1)
        {
            // Queue is the current one, just update index. If not, should we recreate it?
            mCurrentIndexPointer = index;
            updateMetadata();
        }
    }

    public MediaSessionCompat.QueueItem getCurrentMusic() {
        int index = getCurrentIndex();
        if (!QueueHelper.isIndexPlayable(index, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(index);
    }

    private String getCurrentMediaId()
    {
        MediaSessionCompat.QueueItem currentMedia = getCurrentMusic();
        return (currentMedia != null ? currentMedia.getDescription().getMediaId() : "");
    }

    private int getCurrentIndex()
    {
        if (mPlayingQueue.size() != mPlayingQueueIndexes.size())
            throw new IllegalStateException("mPlayingQueue and mPlayingQueueIndexes have different lengths.");

        if (mCurrentIndexPointer >= 0 && mCurrentIndexPointer < mPlayingQueueIndexes.size())
            return mPlayingQueueIndexes.get(mCurrentIndexPointer);
        else
            return 0;
    }

    /*
    protected void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue) {
        setCurrentQueue(title, newQueue, null);
    }

    protected void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue,
                                   String initialMediaId) {
        mPlayingQueue = newQueue;
        int index = 0;
        if (initialMediaId != null) {
            index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, initialMediaId);
        }
        mCurrentIndex = Math.max(index, 0);
        mListener.onQueueUpdated(title, newQueue);
    }
    */

    public void updateMetadata() {
        if (mPlayingQueue.size() == 0) {
            mListener.onMetadataChanged(null);
            return;
        }

        MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
        if (currentMusic == null) {
            mListener.onMetadataRetrieveError();
            return;
        }

        final String musicId = currentMusic.getDescription().getMediaId();
        if (musicId == null)
            throw new IllegalStateException("Null mediaId in currentMusic.getDescription()");

        MediaMetadataCompat metadata = mMusicProvider.getMetadata(musicId);
        if (metadata == null) {
            throw new IllegalArgumentException("Invalid musicId " + musicId);
        }

        mListener.onMetadataChanged(metadata);

        // Set the proper album artwork on the media session, so it can be shown in the
        // locked screen and in other places.
        if (metadata.getDescription().getIconBitmap() == null &&
                metadata.getDescription().getIconUri() != null) {
            String albumUri = metadata.getDescription().getIconUri().toString();
            AlbumArtCache.getInstance().fetch(albumUri, new AlbumArtCache.FetchListener() {
                @Override
                public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
                    mMusicProvider.updateMusicArt(musicId, bitmap, icon);

                    // If we are still playing the same music, notify the listeners:
                    MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
                    if (currentMusic == null) {
                        return;
                    }
                    String currentPlayingId = currentMusic.getDescription().getMediaId();
                    if (musicId.equals(currentPlayingId)) {
                        mListener.onMetadataChanged(mMusicProvider.getMetadata(currentPlayingId));
                    }
                }
            });
        }
    }

    void updateQueuePlaybackState(PlaybackStateCompat.Builder stateBuilder)
    {
        Bundle extras = new Bundle();
        extras.putString(PlaybackExtras.EXTRA_MEDIA_ID, getCurrentMediaId());
        extras.putInt(PlaybackExtras.EXTRA_REPEAT_MODE, mRepeatMode);
        extras.putBoolean(PlaybackExtras.EXTRA_SHUFFLE_MODE, mShuffle);

        stateBuilder.setExtras(extras);
    }

    long updateQueueAvailableActions(long actions)
    {
        if (mCurrentIndexPointer > 0 || mRepeatMode == PlaybackExtras.REPEAT_MODE_QUEUE)
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;

        if (mCurrentIndexPointer < mPlayingQueue.size() - 1 || mRepeatMode == PlaybackExtras.REPEAT_MODE_QUEUE)
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_NEXT;

        return actions;
    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);
        void onMetadataRetrieveError();
        void onCurrentQueueIndexUpdated(int queueIndex);
        void onQueueUpdated(CharSequence title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
