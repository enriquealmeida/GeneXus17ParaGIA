package com.genexus.android.facebook;

import android.net.Uri;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;

public class ShareContentFactory {
    public static ShareContent createShareLinkContent(Uri link) {
        return new ShareLinkContent.Builder()
                .setContentUrl(link)
                .build();
    }

    public static ShareContent createSharePhotoContent(Uri photoUrl) {
        SharePhoto photo = new SharePhoto.Builder()
                .setImageUrl(photoUrl)
                .build();

        return new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
    }

    public static ShareContent createShareVideoContent(Uri videoUrl) {
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(videoUrl)
                .build();

        return new ShareVideoContent.Builder()
                .setVideo(video)
                .build();
    }
}
