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

package com.genexus.android.uamp;

import java.io.IOException;

import android.graphics.Bitmap;
import androidx.core.graphics.BitmapCompat;
import androidx.collection.LruCache;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.TaskRunner;
import com.genexus.android.uamp.utils.BitmapHelper;
import com.genexus.android.uamp.utils.LogHelper;

/**
 * Implements a basic cache of album arts, with async loading support.
 */
public final class AlbumArtCache {
    private static final String TAG = LogHelper.makeLogTag(AlbumArtCache.class);

    private static final int MAX_ALBUM_ART_CACHE_SIZE = 12*1024*1024;  // 12 MB
    private static final int MAX_ART_WIDTH = 800;  // pixels
    private static final int MAX_ART_HEIGHT = 480;  // pixels

    // Resolution reasonable for carrying around as an icon (generally in
    // MediaDescription.getIconBitmap). This should not be bigger than necessary, because
    // the MediaDescription object should be lightweight. If you set it too high and try to
    // serialize the MediaDescription, you may get FAILED BINDER TRANSACTION errors.
    private static final int MAX_ART_WIDTH_ICON = 128;  // pixels
    private static final int MAX_ART_HEIGHT_ICON = 128;  // pixels

    private static final int BIG_BITMAP_INDEX = 0;
    private static final int ICON_BITMAP_INDEX = 1;

    private final LruCache<String, Bitmap[]> mCache;

    private static final AlbumArtCache INSTANCE = new AlbumArtCache();

    public static AlbumArtCache getInstance() {
        return INSTANCE;
    }

    private AlbumArtCache() {
        // Holds no more than MAX_ALBUM_ART_CACHE_SIZE bytes, bounded by maxmemory/4 and
        // Integer.MAX_VALUE:
        int maxSize = Math.min(MAX_ALBUM_ART_CACHE_SIZE,
            (int) Math.min(Integer.MAX_VALUE, Runtime.getRuntime().maxMemory()/4));
        mCache = new LruCache<String, Bitmap[]>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap[] value) {
                return BitmapCompat.getAllocationByteCount(value[BIG_BITMAP_INDEX])
                    + BitmapCompat.getAllocationByteCount(value[ICON_BITMAP_INDEX]);
            }
        };
    }

    public Bitmap getBigImage(String artUrl) {
        Bitmap[] result = mCache.get(artUrl);
        return result == null ? null : result[BIG_BITMAP_INDEX];
    }

    public Bitmap getIconImage(String artUrl) {
        Bitmap[] result = mCache.get(artUrl);
        return result == null ? null : result[ICON_BITMAP_INDEX];
    }

    public void fetch(final String artUrl, final FetchListener listener) {
        // WARNING: for the sake of simplicity, simultaneous multi-thread fetch requests
        // are not handled properly: they may cause redundant costly operations, like HTTP
        // requests and bitmap rescales. For production-level apps, we recommend you use
        // a proper image loading library, like Glide.
        Bitmap[] bitmap = mCache.get(artUrl);
        if (bitmap != null) {
            if (bitmap[BIG_BITMAP_INDEX].isRecycled() || bitmap[ICON_BITMAP_INDEX].isRecycled()) {
                LogHelper.d(TAG, "getOrFetch: album art is in cache but recycled :(", artUrl);
                // Dont recycle images, because in general one is and one not.
				// Fail at least in 4.4 because the one not recycled is in use.
                mCache.remove(artUrl);
            } else {
                LogHelper.d(TAG, "getOrFetch: album art is in cache, using it", artUrl);
                listener.onFetched(artUrl, bitmap[BIG_BITMAP_INDEX], bitmap[ICON_BITMAP_INDEX]);
                return;
            }
        }
        LogHelper.d(TAG, "getOrFetch: starting TaskRunner to fetch ", artUrl);

		TaskRunner.execute(new TaskRunner.BaseTask<Bitmap[]>() {
            @Override
            public Bitmap[] doInBackground() {
                Bitmap[] bitmaps;
                try {
                    Bitmap bitmap = BitmapHelper.fetchAndRescaleBitmap(artUrl, MAX_ART_WIDTH, MAX_ART_HEIGHT);
                    if (bitmap == null)  {
						Services.Log.error(String.format("Cannot download bitmap from %s for AlbumArt", artUrl));
                    	return null;
					}

                    Bitmap icon = BitmapHelper.scaleBitmap(bitmap,
                        MAX_ART_WIDTH_ICON, MAX_ART_HEIGHT_ICON);
                    bitmaps = new Bitmap[] {bitmap, icon};
                    mCache.put(artUrl, bitmaps);
                } catch (IOException e) {
                    return null;
                }
                LogHelper.d(TAG, "doInBackground: putting bitmap in cache. cache size=" +
                    mCache.size());
                return bitmaps;
            }

            @Override
            public void onPostExecute(Bitmap[] bitmaps) {
                if (bitmaps == null) {
                    listener.onError(artUrl, new IllegalArgumentException("got null bitmaps"));
                } else {
                    listener.onFetched(artUrl,
                        bitmaps[BIG_BITMAP_INDEX], bitmaps[ICON_BITMAP_INDEX]);
                }
            }
        });
    }

    public abstract static class FetchListener {
        public abstract void onFetched(String artUrl, Bitmap bigImage, Bitmap iconImage);
        public void onError(String artUrl, Exception e) {
            LogHelper.e(TAG, e, "AlbumArtFetchListener: error while downloading " + artUrl);
        }
    }
}
