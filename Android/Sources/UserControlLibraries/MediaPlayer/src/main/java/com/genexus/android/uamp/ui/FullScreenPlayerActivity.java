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
package com.genexus.android.uamp.ui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.UIActionHelper;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.DrawableUtils;
import com.genexus.android.core.utils.ThemeUtils;
import com.genexus.android.uamp.AlbumArtCache;
import com.genexus.android.uamp.MusicService;
import com.genexus.android.uamp.PlaybackExtras;
import com.genexus.android.uamp.R;
import com.genexus.android.uamp.utils.LogHelper;
import com.genexus.android.media.audio.AudioPlayerBackground;
import com.genexus.android.media.customization.AudioPlayerClassExtensionKt;
import com.genexus.android.media.customization.GxAudioPlayerCustomAction;
import com.genexus.android.media.customization.GxAudioPlayerSettings;
import com.genexus.android.media.ui.ActivityCastResource;
import com.genexus.android.media.ui.ApplicationAudioResource;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * A full screen player that shows the current playing music with a background image
 * depicting the album art. The activity also has controls to seek/pause/play the audio.
 */
public class FullScreenPlayerActivity extends AppCompatActivity {
    private static final String TAG = LogHelper.makeLogTag(FullScreenPlayerActivity.class);
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private static final long SKIP_PREVIOUS_OR_REWIND_THRESHOLD = 3000; // ms

    private ActivityCastResource mCastResource;
    private Toolbar mToolbar;
    private ImageView mSkipPrev;
    private ImageView mSkipNext;
    private ImageView mPlayPause;
    private ImageView mRepeatMode;
    private ImageView mFavorite;
    private ImageView mShuffle;
    private TextView mStart;
    private TextView mEnd;
    private View mSeekbarContainer;
    private SeekBar mSeekbar;
    private TextView mLine1;
    private TextView mLine2;
    private TextView mLine3;
    private ProgressBar mLoading;
    private View mControllers;
    private View mAdvancedControls;
    private Drawable mPauseDrawable;
    private Drawable mPlayDrawable;
    private Drawable mRepeatOneDrawable;
    private Drawable mRepeatQueueDrawable;
    private Drawable mShuffleDrawable;
    private Drawable mFavoriteTrueDrawable;
    private Drawable mFavoriteFalseDrawable;
    private ImageView mBackgroundImage;

    private String mCurrentArtUrl;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private MediaBrowserCompat mMediaBrowser;

    private GxAudioPlayerSettings mPlayerSettings;
    private int mPlayPauseButtonColor;
    private int mOtherButtonDefaultColor;
    private int mOtherButtonActiveColor;

    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private final ScheduledExecutorService mExecutorService =
        Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> mScheduleFuture;
    private PlaybackStateCompat mLastPlaybackState;
    private Bitmap mLastMediaImage;

    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogHelper.d(TAG, "onPlaybackstate changed", state);
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            updateFromMetadata(metadata);
        }
    };

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            LogHelper.d(TAG, "onConnected");
            try {
                connectToSession(mMediaBrowser.getSessionToken());
            } catch (RemoteException e) {
                LogHelper.e(TAG, e, "could not connect media controller");
            }
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_player);

        mCastResource = new ActivityCastResource(this);
        mCastResource.onCreate(this, savedInstanceState);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mBackgroundImage = findViewById(R.id.background_image);
        mPauseDrawable = ContextCompat.getDrawable(this, R.drawable.uamp_ic_pause_white_48dp);
        mPlayDrawable = ContextCompat.getDrawable(this, R.drawable.uamp_ic_play_arrow_white_48dp);
        mRepeatQueueDrawable = ContextCompat.getDrawable(this, R.drawable.ic_repeat_white_24dp);
        mRepeatOneDrawable = ContextCompat.getDrawable(this, R.drawable.ic_repeat_one_white_24dp);
        mShuffleDrawable = ContextCompat.getDrawable(this, R.drawable.ic_shuffle_white_24dp);
        mFavoriteTrueDrawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp);
        mFavoriteFalseDrawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp);
        mPlayPause = findViewById(R.id.play_pause);
        mSkipNext = findViewById(R.id.next);
        mSkipPrev = findViewById(R.id.prev);
        mRepeatMode = findViewById(R.id.repeat);
        mFavorite = findViewById(R.id.favorite);
        mShuffle = findViewById(R.id.shuffle);
        mStart = findViewById(R.id.startText);
        mEnd = findViewById(R.id.endText);
        mSeekbarContainer = findViewById(R.id.seekBarContainer);
        mSeekbar = findViewById(R.id.seekBar1);
        mLine1 = findViewById(R.id.line1);
        mLine2 = findViewById(R.id.line2);
        mLine3 = findViewById(R.id.line3);
        mLoading = findViewById(R.id.progressBar1);
        mAdvancedControls = findViewById(R.id.advancedControls);
        mControllers = findViewById(R.id.controllers);

        mSkipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaControllerCompat.TransportControls controls = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls();
                controls.skipToNext();
            }
        });

        mSkipPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaControllerCompat.TransportControls controls = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls();
                PlaybackStateCompat playbackState = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getPlaybackState();
                if (playbackState.getPosition() > SKIP_PREVIOUS_OR_REWIND_THRESHOLD)
                {
                    // Rewind to start.
                    // Also reset Seekbar because we might be paused and we won't get a PlaybackStateChanged.
                    controls.seekTo(0);
                    mSeekbar.setProgress(0);
                    mLastPlaybackState = null;
                }
                else
                    controls.skipToPrevious();
            }
        });

        mRepeatMode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MediaControllerCompat.TransportControls controls = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls();
                controls.sendCustomAction(PlaybackExtras.ACTION_TOGGLE_REPEAT, new Bundle());
            }
        });

        mFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MediaControllerCompat.TransportControls controls = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls();
                controls.sendCustomAction(PlaybackExtras.ACTION_TOGGLE_FAVORITE, new Bundle());
            }
        });

        mShuffle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MediaControllerCompat.TransportControls controls = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls();
                controls.sendCustomAction(PlaybackExtras.ACTION_TOGGLE_SHUFFLE, new Bundle());
            }
        });

        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaybackStateCompat state = MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getPlaybackState();
                if (state != null) {
                    MediaControllerCompat.TransportControls controls =
							MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls();
                    switch (state.getState()) {
                        case PlaybackStateCompat.STATE_PLAYING: // fall through
                        case PlaybackStateCompat.STATE_BUFFERING:
                            controls.pause();
                            stopSeekbarUpdate();
                            break;
                        case PlaybackStateCompat.STATE_PAUSED:
                        case PlaybackStateCompat.STATE_STOPPED:
                            controls.play();
                            scheduleSeekbarUpdate();
                            break;
                        default:
                            LogHelper.d(TAG, "onClick with state ", state.getState());
                    }
                }
            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mStart.setText(DateUtils.formatElapsedTime(progress / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopSeekbarUpdate();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
				MediaControllerCompat.getMediaController(FullScreenPlayerActivity.this).getTransportControls().seekTo(seekBar.getProgress());
                scheduleSeekbarUpdate();
            }
        });

        // Only update from the intent if we are not recreating from a config change:
        if (savedInstanceState == null) {
            updateFromParams(getIntent());
        }

        mMediaBrowser = new MediaBrowserCompat(this,
            new ComponentName(this, MusicService.class), mConnectionCallback, null);

        applyCustomizations();
    }

    private void applyCustomizations()
    {
        mPlayerSettings = AudioPlayerBackground.getInstance(this).getPlayerSettings();

        mRepeatMode.setVisibility(mPlayerSettings.getSupportRepeat() ? VISIBLE : GONE);
        mFavorite.setVisibility(mPlayerSettings.getSupportFavorite() ? VISIBLE : GONE);
        mShuffle.setVisibility(mPlayerSettings.getSupportShuffle() ? VISIBLE : GONE);

        // Hide the advanced controls bar if none of them is visible.
        if (!mPlayerSettings.getSupportRepeat() && !mPlayerSettings.getSupportFavorite() && ! mPlayerSettings.getSupportShuffle())
            mAdvancedControls.setVisibility(View.GONE);

        mPlayPauseButtonColor = Color.WHITE;
        mOtherButtonDefaultColor = Color.WHITE;
        Integer accentColor = ThemeUtils.getAndroidThemeColorId(this, R.attr.colorAccent);
        if (accentColor != null)
            mOtherButtonActiveColor = accentColor;

        ThemeClassDefinition themeClass = AudioPlayerBackground.getInstance(this).getPlayerThemeClass();
        if (themeClass != null)
        {
            ThemeUtils.setFontProperties(mLine1, AudioPlayerClassExtensionKt.getFullScreenPlayerTitleClass(themeClass));
            ThemeUtils.setFontProperties(mLine2, AudioPlayerClassExtensionKt.getFullScreenPlayerSubtitleClass(themeClass));
            ThemeUtils.setFontProperties(mLine3, AudioPlayerClassExtensionKt.getFullScreenPlayerSubtitleClass(themeClass));

            ThemeClassDefinition playButtonClass = AudioPlayerClassExtensionKt.getFullScreenPlayerPlayPauseButtonClass(themeClass);
            if (playButtonClass != null)
            {
                Integer buttonColor = ThemeUtils.getColorId(playButtonClass.getColor());
                if (buttonColor != null)
                    mPlayPauseButtonColor = buttonColor;

                mPlayDrawable = DrawableUtils.applyTint(mPlayDrawable, mPlayPauseButtonColor);
                mPauseDrawable = DrawableUtils.applyTint(mPauseDrawable, mPlayPauseButtonColor);
            }

            ThemeClassDefinition otherButtonClass = AudioPlayerClassExtensionKt.getFullScreenPlayerButtonsClass(themeClass);
            if (otherButtonClass != null)
            {
                Integer defaultButtonColor = ThemeUtils.getColorId(otherButtonClass.getColor());
                if (defaultButtonColor != null)
                    mOtherButtonDefaultColor = defaultButtonColor;

                Integer activeButtonColor = ThemeUtils.getColorId(otherButtonClass.getHighlightedColor());
                if (activeButtonColor != null)
                    mOtherButtonActiveColor = activeButtonColor;

                mSkipPrev.setImageDrawable(DrawableUtils.applyTint(mSkipPrev.getDrawable(), mOtherButtonDefaultColor));
                mSkipNext.setImageDrawable(DrawableUtils.applyTint(mSkipNext.getDrawable(), mOtherButtonDefaultColor));
            }

            ThemeClassDefinition imageClass = AudioPlayerClassExtensionKt.getFullScreenPlayerImageClass(themeClass);
            if (imageClass != null)
            {
                ThemeUtils.setPadding(mBackgroundImage, imageClass);
                ThemeUtils.setBackgroundBorderProperties(mBackgroundImage, imageClass, BackgroundOptions.DEFAULT);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)mBackgroundImage.getLayoutParams();
                ThemeUtils.setMargins(lp, imageClass);

                if (imageClass.getImageScaleType(ImageScaleType.FILL_KEEPING_ASPECT) == ImageScaleType.FIT)
                {
                    // Reparent the background image to show as square above the song title.
                    mBackgroundImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    ((RelativeLayout)mBackgroundImage.getParent()).removeView(mBackgroundImage);
                    RelativeLayout newParent = (RelativeLayout)mLine1.getParent();
                    lp.addRule(RelativeLayout.ABOVE, mLine1.getId());
                    lp.topMargin += mToolbar.getLayoutParams().height;
                    newParent.addView(mBackgroundImage);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(
                FullScreenPlayerActivity.this, token);
        if (mediaController.getMetadata() == null) {
            finish();
            return;
        }
		MediaControllerCompat.setMediaController(this, mediaController);
        mediaController.registerCallback(mCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();
        updatePlaybackState(state);
        updateFromMetadata(mediaController.getMetadata());
        updateProgress();
        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
            scheduleSeekbarUpdate();
        }
    }

    private void updateFromParams(Intent intent) {
        if (intent != null) {
            MediaDescriptionCompat description = intent.getParcelableExtra(
                    ApplicationAudioResource.EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if (description != null) {
                updateMediaDescription(description);
            }
        }
    }

    private void scheduleSeekbarUpdate() {
        stopSeekbarUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mCastResource.onCreateOptionsMenu(menu);

        int menuPosition = 1;
        for (GxAudioPlayerCustomAction action : mPlayerSettings.getCustomActions())
        {
            MenuItem menuItem = menu.add(Menu.NONE, menuPosition++, Menu.NONE, action.getTitle());
			menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            Drawable image = Services.Images.getStaticImage(getBaseContext(), action.getImage());
            if (image != null)
                menuItem.setIcon(image);
            else
                UIActionHelper.setStandardMenuItemImage(this, menuItem, action.getId());
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        else if (item.getItemId() >= 1 && item.getItemId() <= mPlayerSettings.getCustomActions().size())
        {
            // Fire the custom action.
            GxAudioPlayerCustomAction action = mPlayerSettings.getCustomActions().get(item.getItemId() - 1);

            Bundle extras = new Bundle();
            extras.putString(PlaybackExtras.EXTRA_CUSTOM_ACTION_ID, action.getId());

            MediaControllerCompat.TransportControls controls = MediaControllerCompat.getMediaController(this).getTransportControls();
            controls.sendCustomAction(PlaybackExtras.ACTION_CUSTOM, extras);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowser != null) {
            mMediaBrowser.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityHelper.onResume(this);
        mCastResource.onResume(this);
    }

    @Override
    protected void onPause() {
        mCastResource.onPause(this);
        ActivityHelper.onPause(this);
        super.onPause();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowser != null) {
            mMediaBrowser.disconnect();
        }
        if (MediaControllerCompat.getMediaController(this) != null) {
			MediaControllerCompat.getMediaController(this).unregisterCallback(mCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSeekbarUpdate();
        mExecutorService.shutdown();
    }

    private void fetchImageAsync(@NonNull MediaDescriptionCompat description) {
        if (description.getIconUri() == null) {
            return;
        }
        String artUrl = description.getIconUri().toString();
        mCurrentArtUrl = artUrl;
        AlbumArtCache cache = AlbumArtCache.getInstance();
        Bitmap art = cache.getBigImage(artUrl);
        if (art == null) {
            art = description.getIconBitmap();
        }
        if (art != null) {
            // if we have the art cached or from the MediaDescription, use it:
            setMediaImageWithCrossFade(art);
        } else {
            // otherwise, fetch a high res version and update:
            cache.fetch(artUrl, new AlbumArtCache.FetchListener() {
                @Override
                public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
                    // sanity check, in case a new fetch request has been done while
                    // the previous hasn't yet returned:
                    if (artUrl.equals(mCurrentArtUrl)) {
                        setMediaImageWithCrossFade(bitmap);
                    }
                }
            });
        }
    }

    private void setMediaImageWithCrossFade(@NonNull Bitmap bitmap)
    {
        if (mLastMediaImage != null)
        {
            Drawable[] images = new Drawable[2];
            images[0] = new BitmapDrawable(getResources(), mLastMediaImage);
            images[1] = new BitmapDrawable(getResources(), bitmap);

            TransitionDrawable crossfader = new TransitionDrawable(images);
            mBackgroundImage.setImageDrawable(crossfader);
            crossfader.startTransition(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        }
        else
            mBackgroundImage.setImageBitmap(bitmap);

        mLastMediaImage = bitmap;
    }

    private void updateFromMetadata(MediaMetadataCompat metadata)
    {
        if (metadata != null)
        {
            MediaDescriptionCompat description = metadata.getDescription();
            if (description != null)
                updateMediaDescription(description);

            updateFavorite(metadata);
            updateDuration(metadata);
        }
    }

    private void updateMediaDescription(MediaDescriptionCompat description) {
        if (description == null) {
            return;
        }
        LogHelper.d(TAG, "updateMediaDescription called ");
        mLine1.setText(description.getTitle());
        mLine2.setText(description.getSubtitle());
        fetchImageAsync(description);
    }

    private void updateFavorite(MediaMetadataCompat metadata)
    {
        if (mFavorite.getVisibility() == VISIBLE)
        {
            //noinspection WrongConstant
            RatingCompat rating = metadata.getRating(PlaybackExtras.METADATA_KEY_RATING_CUSTOM);
            boolean isFavorite = rating != null && rating.hasHeart();

            if (isFavorite)
                mFavorite.setImageDrawable(DrawableUtils.applyTint(mFavoriteTrueDrawable, mOtherButtonActiveColor));
            else
                mFavorite.setImageDrawable(DrawableUtils.applyTint(mFavoriteFalseDrawable, mOtherButtonDefaultColor));
        }
    }

    private void updateDuration(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        LogHelper.d(TAG, "updateDuration called ");
        int duration = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);

        int previousDuration = mSeekbar.getMax();
        mSeekbar.setMax(duration);
        mEnd.setText(DateUtils.formatElapsedTime(duration/1000));

        if (duration != previousDuration)
            mSeekbar.setProgress(0);

    }

    @SuppressWarnings("deprecation")
    private void updatePlaybackState(PlaybackStateCompat state) {
        if (state == null) {
            return;
        }
        mLastPlaybackState = state;
        if (MediaControllerCompat.getMediaController(this) != null && MediaControllerCompat.getMediaController(this).getExtras() != null) {
            String castName = MediaControllerCompat.getMediaController(this)
                    .getExtras().getString(MusicService.EXTRA_CONNECTED_CAST);
            String line3Text = castName == null ? "" : getResources()
                        .getString(R.string.casting_to_device, castName);
            mLine3.setText(line3Text);
        }

        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                mLoading.setVisibility(INVISIBLE);
                mPlayPause.setVisibility(VISIBLE);
                mPlayPause.setImageDrawable(mPauseDrawable);
                mControllers.setVisibility(VISIBLE);
                mSeekbarContainer.setVisibility(VISIBLE);
                scheduleSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                mControllers.setVisibility(VISIBLE);
                mLoading.setVisibility(INVISIBLE);
                mPlayPause.setVisibility(VISIBLE);
                mPlayPause.setImageDrawable(mPlayDrawable);
                mSeekbarContainer.setVisibility(VISIBLE);
                stopSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                mControllers.setVisibility(VISIBLE);
                mLoading.setVisibility(INVISIBLE);
                mPlayPause.setVisibility(VISIBLE);
                mPlayPause.setImageDrawable(mPlayDrawable);
                mSeekbarContainer.setVisibility(INVISIBLE);
                stopSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_NONE:
                mLoading.setVisibility(INVISIBLE);
                mPlayPause.setVisibility(VISIBLE);
                mPlayPause.setImageDrawable(mPlayDrawable);
                mSeekbarContainer.setVisibility(INVISIBLE);
                stopSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                mPlayPause.setVisibility(INVISIBLE);
                mLoading.setVisibility(VISIBLE);
                mLine3.setText(R.string.loading);
                mSeekbarContainer.setVisibility(VISIBLE);
                stopSeekbarUpdate();
                break;
            default:
                LogHelper.d(TAG, "Unhandled state ", state.getState());
        }

        updateSkipButtonsVisibility(state, state.getPosition());
        updatePlaybackActions(state);
    }

    private void updateSkipButtonsVisibility(PlaybackStateCompat state, long currentPosition)
    {
        // Force the "previous song" visible for rewinding if after the threshold.
        // NOTE: Do not use state.getPosition(), that value is stale when called by updateProgress()
        boolean isPrevButtonVisible = (currentPosition > SKIP_PREVIOUS_OR_REWIND_THRESHOLD ||
                (state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0);

        boolean isNextButtonVisible = (state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0;

        mSkipPrev.setVisibility(isPrevButtonVisible ? VISIBLE : INVISIBLE);
        mSkipNext.setVisibility(isNextButtonVisible ? VISIBLE : INVISIBLE);
    }

    private void updatePlaybackActions(PlaybackStateCompat state)
    {
        Bundle extras = state.getExtras();
        if (extras == null)
            return;

        if (mRepeatMode.getVisibility() == VISIBLE)
        {
            Drawable repeatDrawable;
            int repeatMode = extras.getInt(PlaybackExtras.EXTRA_REPEAT_MODE, PlaybackExtras.REPEAT_MODE_NONE);

            if (repeatMode == PlaybackExtras.REPEAT_MODE_QUEUE)
                repeatDrawable = DrawableUtils.applyTint(mRepeatQueueDrawable, mOtherButtonActiveColor);
            else if (repeatMode == PlaybackExtras.REPEAT_MODE_SINGLE)
                repeatDrawable = DrawableUtils.applyTint(mRepeatOneDrawable, mOtherButtonActiveColor);
            else
                repeatDrawable = DrawableUtils.applyTint(mRepeatQueueDrawable, mOtherButtonDefaultColor);

            mRepeatMode.setImageDrawable(repeatDrawable);
        }

        if (mShuffle.getVisibility() == VISIBLE)
        {
            Drawable shuffleDrawable;
            boolean shuffle = extras.getBoolean(PlaybackExtras.EXTRA_SHUFFLE_MODE, false);

            if (shuffle)
                shuffleDrawable = DrawableUtils.applyTint(mShuffleDrawable, mOtherButtonActiveColor);
            else
                shuffleDrawable = DrawableUtils.applyTint(mShuffleDrawable, mOtherButtonDefaultColor);

            mShuffle.setImageDrawable(shuffleDrawable);
        }
    }

    private void updateProgress() {
        if (mLastPlaybackState == null) {
            return;
        }
        long currentPosition = mLastPlaybackState.getPosition();
        if (mLastPlaybackState.getState() != PlaybackStateCompat.STATE_PAUSED) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += Math.round(timeDelta * mLastPlaybackState.getPlaybackSpeed());
        }
        mSeekbar.setProgress((int) currentPosition);

        updateSkipButtonsVisibility(mLastPlaybackState, currentPosition);
    }
}
