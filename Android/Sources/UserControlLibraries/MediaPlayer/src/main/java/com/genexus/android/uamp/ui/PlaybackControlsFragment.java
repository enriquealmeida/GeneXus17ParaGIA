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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.DrawableUtils;
import com.genexus.android.core.utils.ThemeUtils;
import com.genexus.android.uamp.AlbumArtCache;
import com.genexus.android.uamp.MusicService;
import com.genexus.android.uamp.R;
import com.genexus.android.uamp.utils.LogHelper;
import com.genexus.android.media.audio.AudioPlayerBackground;
import com.genexus.android.media.customization.AudioPlayerClassExtensionKt;
import com.genexus.android.media.ui.ApplicationAudioResource;

/**
 * A class that shows the Media Queue to the user.
 */
public class PlaybackControlsFragment extends Fragment {

    private static final String TAG = LogHelper.makeLogTag(PlaybackControlsFragment.class);

    private ImageButton mPlayPause;
    private ProgressBar mLoading;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mExtraInfo;
    private ImageView mAlbumArt;
    private String mArtUrl;

    private Drawable mPlayDrawable;
    private Drawable mPauseDrawable;
    private int mButtonColor;

    // Receive callbacks from the MediaControllerCompat. Here we update our state such as which queue
    // is being shown, the current title and description and the PlaybackStateCompat.
    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogHelper.d(TAG, "Received playback state change to state ", state.getState());
            PlaybackControlsFragment.this.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }
            LogHelper.d(TAG, "Received metadata state change to mediaId=",
                    metadata.getDescription().getMediaId(),
                    " song=", metadata.getDescription().getTitle());
            PlaybackControlsFragment.this.onMetadataChanged(metadata);
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);

        mPlayPause = rootView.findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(mButtonListener);
        mPlayPause.setVisibility(View.INVISIBLE);

        mLoading = rootView.findViewById(R.id.progress_loading);
        mLoading.setVisibility(View.INVISIBLE);

        mPlayDrawable = ContextCompat.getDrawable(inflater.getContext(), R.drawable.ic_play_arrow_black_36dp);
        mPauseDrawable = ContextCompat.getDrawable(inflater.getContext(), R.drawable.ic_pause_black_36dp);

        mTitle = rootView.findViewById(R.id.title);
        mSubtitle = rootView.findViewById(R.id.artist);
        mExtraInfo = rootView.findViewById(R.id.extra_info);
        mAlbumArt = rootView.findViewById(R.id.album_art);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullScreenPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MediaMetadataCompat metadata = MediaControllerCompat.getMediaController(getActivity()).getMetadata();

                if (metadata != null) {
                    intent.putExtra(ApplicationAudioResource.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                        metadata.getDescription());
                }
                startActivity(intent);
            }
        });

        mButtonColor = ThemeUtils.getAndroidThemeColor(inflater.getContext(), R.attr.colorAccent, Color.BLACK);

        // TEMP: Set fixed (non scalable) font sizes for the textviews. This should be removed
        // as soon as we support autogrow for this control.
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mExtraInfo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        // END TEMP

        applyCustomizations();

        return rootView;
    }

    private void applyCustomizations()
    {
        Context context = mTitle.getContext();
        ThemeClassDefinition themeClass = AudioPlayerBackground.getInstance(context).getPlayerThemeClass();
        if (themeClass != null)
        {
            ThemeUtils.setFontProperties(mTitle, AudioPlayerClassExtensionKt.getMiniPlayerTitleClass(themeClass));
            ThemeUtils.setFontProperties(mSubtitle, AudioPlayerClassExtensionKt.getMiniPlayerSubtitleClass(themeClass));
            ThemeUtils.setFontProperties(mExtraInfo, AudioPlayerClassExtensionKt.getMiniPlayerSubtitleClass(themeClass));

            ThemeClassDefinition buttonClass = AudioPlayerClassExtensionKt.getMiniPlayerButtonClass(themeClass);
            if (buttonClass != null)
            {
                Integer buttonColor = ThemeUtils.getColorId(buttonClass.getColor());
                if (buttonColor != null)
                    mButtonColor = buttonColor;
            }
        }

        // These are done even if we don't have a theme, to apply the accent color.
        mPlayDrawable = DrawableUtils.applyTint(mPlayDrawable, mButtonColor);
        mPauseDrawable = DrawableUtils.applyTint(mPauseDrawable, mButtonColor);

        Drawable loadingDrawable = mLoading.getIndeterminateDrawable();
        DrawableUtils.applyTint(loadingDrawable, mButtonColor);
        mLoading.setIndeterminateDrawable(loadingDrawable);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart() {
        super.onStart();
        LogHelper.d(TAG, "fragment.onStart");
        if (MediaControllerCompat.getMediaController(getActivity()) != null) {
            onConnected();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStop() {
        super.onStop();
        LogHelper.d(TAG, "fragment.onStop");
        if (MediaControllerCompat.getMediaController(getActivity()) != null) {
			MediaControllerCompat.getMediaController(getActivity()).unregisterCallback(mCallback);
        }
    }

    @SuppressWarnings("deprecation")
    public void onConnected() {
        MediaControllerCompat controller =  MediaControllerCompat.getMediaController(getActivity());
        LogHelper.d(TAG, "onConnected, mediaController==null? ", controller == null);
        if (controller != null) {
            onMetadataChanged(controller.getMetadata());
            onPlaybackStateChanged(controller.getPlaybackState());
            controller.registerCallback(mCallback);
        }
    }

    private void onMetadataChanged(MediaMetadataCompat metadata) {
        LogHelper.d(TAG, "onMetadataChanged ", metadata);
        if (getActivity() == null) {
            LogHelper.w(TAG, "onMetadataChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (metadata == null) {
            return;
        }

        mTitle.setText(metadata.getDescription().getTitle());
        mSubtitle.setText(metadata.getDescription().getSubtitle());
        String artUrl = null;
        if (metadata.getDescription().getIconUri() != null) {
            artUrl = metadata.getDescription().getIconUri().toString();
        }
        if (!TextUtils.equals(artUrl, mArtUrl)) {
            mArtUrl = artUrl;

            if (TextUtils.isEmpty(mArtUrl)) {
                mAlbumArt.setImageBitmap(null);
                return;
            }

            Bitmap art = metadata.getDescription().getIconBitmap();
            AlbumArtCache cache = AlbumArtCache.getInstance();
            if (art == null) {
                art = cache.getIconImage(mArtUrl);
            }
            if (art != null) {
                mAlbumArt.setImageBitmap(art);
            } else {
                cache.fetch(artUrl, new AlbumArtCache.FetchListener() {
                            @Override
                            public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
                                if (icon != null) {
                                    LogHelper.d(TAG, "album art icon of w=", icon.getWidth(),
                                            " h=", icon.getHeight());
                                    if (isAdded()) {
                                        mAlbumArt.setImageBitmap(icon);
                                    }
                                }
                            }
                        }
                );
            }
        }
    }

    public void setExtraInfo(String extraInfo) {
        if (extraInfo == null) {
            mExtraInfo.setVisibility(View.GONE);
        } else {
            mExtraInfo.setText(extraInfo);
            mExtraInfo.setVisibility(View.VISIBLE);
        }
    }

    @SuppressWarnings("deprecation")
    private void onPlaybackStateChanged(PlaybackStateCompat state) {
        LogHelper.d(TAG, "onPlaybackStateChanged ", state);
        if (getActivity() == null) {
            LogHelper.w(TAG, "onPlaybackStateChanged called when getActivity null," +
                    "this should not happen if the callback was properly unregistered. Ignoring.");
            return;
        }
        if (state == null) {
            return;
        }
        boolean enablePlay = false;
        boolean isLoading = false;
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_CONNECTING:
            case PlaybackStateCompat.STATE_BUFFERING:
                isLoading = true;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                enablePlay = true;
                break;
            case PlaybackStateCompat.STATE_ERROR:
                LogHelper.e(TAG, "error playbackstate: ", state.getErrorMessage());
                Toast.makeText(getActivity(), state.getErrorMessage(), Toast.LENGTH_LONG).show();
                break;
        }

        if (isLoading) {
            mLoading.setVisibility(View.VISIBLE);
            mPlayPause.setVisibility(View.INVISIBLE);
        } else {
            mLoading.setVisibility(View.INVISIBLE);
            mPlayPause.setVisibility(View.VISIBLE);

            if (enablePlay) {
                mPlayPause.setImageDrawable(mPlayDrawable);
            } else {
                mPlayPause.setImageDrawable(mPauseDrawable);
            }
        }

        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        String extraInfo = null;
        if (controller != null && controller.getExtras() != null) {
            String castName = controller.getExtras().getString(MusicService.EXTRA_CONNECTED_CAST);
            if (castName != null) {
                extraInfo = getResources().getString(R.string.casting_to_device, castName);
            }
        }
        setExtraInfo(extraInfo);
    }

    @SuppressWarnings("deprecation")
    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlaybackStateCompat stateObj = MediaControllerCompat.getMediaController(getActivity()).getPlaybackState();
            final int state = stateObj == null ?
                    PlaybackStateCompat.STATE_NONE : stateObj.getState();
            LogHelper.d(TAG, "Button pressed, in state " + state);
            if (v.getId() == R.id.play_pause) {
                LogHelper.d(TAG, "Play button pressed, in state " + state);
                if (state == PlaybackStateCompat.STATE_PAUSED ||
                        state == PlaybackStateCompat.STATE_STOPPED ||
                        state == PlaybackStateCompat.STATE_NONE) {
                    playMedia();
                } else if (state == PlaybackStateCompat.STATE_PLAYING ||
                        state == PlaybackStateCompat.STATE_BUFFERING ||
                        state == PlaybackStateCompat.STATE_CONNECTING) {
                    pauseMedia();
                }
            }
        }
    };

    @SuppressWarnings("deprecation")
    private void playMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    @SuppressWarnings("deprecation")
    private void pauseMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }
}
