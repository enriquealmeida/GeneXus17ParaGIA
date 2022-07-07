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

package com.genexus.android.uamp.model;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;

import com.genexus.android.uamp.PlaybackExtras;
import com.genexus.android.media.model.GxMediaItem;

public class MusicProvider {

    private final ConcurrentHashMap<String, GxMediaItem> mCurrentMedia = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MediaMetadataCompat> mCurrentMediaMetadata = new ConcurrentHashMap<>();

    public void setCurrentMedia(List<GxMediaItem> mediaItems)
    {
        mCurrentMedia.clear();
        mCurrentMediaMetadata.clear();

        for (GxMediaItem mediaItem : mediaItems)
            mCurrentMedia.put(mediaItem.getMediaId(), mediaItem);
    }

    public String getUri(String mediaId)
    {
        GxMediaItem mediaItem = mCurrentMedia.get(mediaId);
        if (mediaItem != null)
            return mediaItem.getMediaUri();
        else
            return null;
    }

    public MediaMetadataCompat getMetadata(String mediaId)
    {
        MediaMetadataCompat mediaMetadata = mCurrentMediaMetadata.get(mediaId);
        if (mediaMetadata != null)
        {
            // See if bitmaps have been recycled. This happens with RemoteControlClient!
            if (mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) != null &&
                mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART).isRecycled())
            {
                mediaMetadata = null;
            }
        }

        if (mediaMetadata == null)
        {
            GxMediaItem mediaItem = mCurrentMedia.get(mediaId);
            if (mediaItem != null)
            {
                mediaMetadata = mediaItem.toMediaMetadata();
                mCurrentMediaMetadata.put(mediaId, mediaMetadata);
            }
        }

        return mediaMetadata;
    }

    public void updateMusicArt(String mediaId, Bitmap bitmap, Bitmap icon)
    {
        MediaMetadataCompat metadata = getMetadata(mediaId);
        metadata = new MediaMetadataCompat.Builder(metadata)

                // set high resolution bitmap in METADATA_KEY_ALBUM_ART. This is used, for
                // example, on the lockscreen background when the media session is active.
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)

                // set small version of the album art in the DISPLAY_ICON. This is used on
                // the MediaDescriptionCompat and thus it should be small to be serialized if
                // necessary..
                .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, icon)

                .build();

        mCurrentMediaMetadata.put(mediaId, metadata);
    }

    public void toggleFavorite(String mediaId)
    {
        GxMediaItem mediaItem = mCurrentMedia.get(mediaId);
        if (mediaItem != null)
        {
            mediaItem.setFavorite(!mediaItem.isFavorite());

            MediaMetadataCompat metadata = getMetadata(mediaId);
            if (metadata != null)
            {
                metadata = new MediaMetadataCompat.Builder(metadata)
                        .putRating(PlaybackExtras.METADATA_KEY_RATING_CUSTOM, RatingCompat.newHeartRating(mediaItem.isFavorite()))
                        .build();

                mCurrentMediaMetadata.put(mediaId, metadata);
            }
        }
    }
}
