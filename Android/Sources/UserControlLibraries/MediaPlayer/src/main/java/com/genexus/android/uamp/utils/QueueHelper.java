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

package com.genexus.android.uamp.utils;

import java.util.List;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import com.genexus.android.uamp.model.MusicProvider;
import com.genexus.android.media.audio.AudioPlayerBackground;
import com.genexus.android.media.model.GxMediaQueue;

/**
 * Utility class to help on queue related tasks.
 */
public class QueueHelper {

    public static class PlayingQueue
    {
        public List<MediaSessionCompat.QueueItem> items;
        public CharSequence title;
        public int current;

        public PlayingQueue(List<MediaSessionCompat.QueueItem> items, CharSequence title, int current)
        {
            this.items = items;
            this.title = title;
            this.current = current;
        }
    }

    public static PlayingQueue setupPlayingQueue(Context context, MusicProvider musicProvider)
    {
        GxMediaQueue queue = AudioPlayerBackground.getInstance(context).getPlaybackQueue();
        if (queue != null)
        {
            musicProvider.setCurrentMedia(queue.getItems());
            return new PlayingQueue(queue.toMediaSessionQueue(), queue.getTitle(), 0);
        }
        else
            throw new IllegalStateException("AudioPlayerBackground.getPlaybackQueue() returned null?");
    }

    public static boolean applyPlayingQueueSeamlessly(PlayingQueue oldQueue, PlayingQueue newQueue)
    {
        if (newQueue.items.size() == 0)
            return false;

        if (oldQueue == null || oldQueue.items == null || oldQueue.items.size() == 0 ||
                oldQueue.current < 0 || oldQueue.current >= oldQueue.items.size())
            return false;

        MediaSessionCompat.QueueItem oldCurrentItem = oldQueue.items.get(oldQueue.current);
        String oldId = oldCurrentItem.getDescription().getMediaId();
        if (oldId == null || oldId.length() == 0)
            return false;

        // Find the old id in the new queue.
        for (int i = 0; i < newQueue.items.size(); i++)
        {
            MediaSessionCompat.QueueItem newQueueItem = newQueue.items.get(i);
            if (oldId.equalsIgnoreCase(newQueueItem.getDescription().getMediaId()))
            {
                newQueue.current = i;
                return true;
            }
        }

        return false;
    }

    public static int getMusicIndexOnQueue(Iterable<MediaSessionCompat.QueueItem> queue,
             String mediaId) {
        int index = 0;
        for (MediaSessionCompat.QueueItem item : queue) {
            if (mediaId.equals(item.getDescription().getMediaId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static int getMusicIndexOnQueue(Iterable<MediaSessionCompat.QueueItem> queue,
             long queueId) {
        int index = 0;
        for (MediaSessionCompat.QueueItem item : queue) {
            if (queueId == item.getQueueId()) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static boolean isIndexPlayable(int index, List<MediaSessionCompat.QueueItem> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }
}
