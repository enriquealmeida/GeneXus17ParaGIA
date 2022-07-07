package com.genexus.android.facebook.api;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.FileDownloader;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.facebook.AccessToken;
import com.facebook.share.model.ShareContent;
import com.facebook.share.widget.ShareDialog;
import com.genexus.android.facebook.ShareContentFactory;

import java.util.List;

import static com.genexus.android.core.utils.FileUtils2.SCHEME_HTTP;


public class FacebookAPI extends ExternalApi {
    public static final String OBJECT_NAME = "GeneXus.Social.Facebook";

    // API Property Names
    private static final String PROPERTY_FACEBOOK_ACCESS_TOKEN	= "AccessToken";
    // API Method Names
    private static final String METHOD_POST = "PostToWall"; // Deprecated!
    private static final String METHOD_SHARE_LINK = "ShareLink";
    private static final String METHOD_SHARE_IMAGE = "ShareImage";
    private static final String METHOD_SHARE_VIDEO = "ShareVideo";

    public FacebookAPI(ApiAction action) {
        super(action);
        addReadonlyPropertyHandler(PROPERTY_FACEBOOK_ACCESS_TOKEN, mGetFacebookAccessTokenProperty);
        addMethodHandler(METHOD_POST, 5, mPostToWallMethod);
        addMethodHandler(METHOD_SHARE_LINK, 1, mShareLinkMethod);
        addMethodHandler(METHOD_SHARE_IMAGE, 1, mShareImageMethod);
        addMethodHandler(METHOD_SHARE_VIDEO, 1, mShareVideoMethod);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mGetFacebookAccessTokenProperty = parameters -> {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null ?
                ExternalApiResult.success(FacebookEntitiesFactory.createFacebookAccessToken(accessToken))
                : ExternalApiResult.SUCCESS_CONTINUE;
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mPostToWallMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
            final String link = (String) parameters.get(3);
            final String picture = (String) parameters.get(4);

            ShareContent shareContent;

            if (!TextUtils.isEmpty(link)) {
                shareContent = ShareContentFactory.createShareLinkContent(Uri.parse(link));
            } else if (!TextUtils.isEmpty(picture)) {
                if (picture.startsWith(SCHEME_HTTP)) {
                    FileDownloader.enqueue(Uri.parse(picture), getContext(), mFileDownloadListener);
                    return ExternalApiResult.SUCCESS_WAIT;
                }
                shareContent = ShareContentFactory.createSharePhotoContent(Uri.parse(picture));
            } else {
                Services.Log.debug(String.format("%s.%s was called with both link and picture parameters empty.", OBJECT_NAME, METHOD_POST));
                return ExternalApiResult.FAILURE;
            }

            showDialog(shareContent);

            return ExternalApiResult.SUCCESS_WAIT;
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mShareLinkMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
            final String link = (String) parameters.get(0);

            if (TextUtils.isEmpty(link)) {
                Services.Log.debug(String.format("%s.%s was called with empty parameter.", OBJECT_NAME, METHOD_SHARE_LINK));
                return ExternalApiResult.FAILURE;
            }

            ShareContent shareContent = ShareContentFactory.createShareLinkContent(Uri.parse(link));
            showDialog(shareContent);

            return ExternalApiResult.SUCCESS_WAIT;
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mShareImageMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
            final String image = (String) parameters.get(0);

            if (TextUtils.isEmpty(image)) {
                Services.Log.debug(String.format("%s.%s was called with empty parameter.", OBJECT_NAME, METHOD_SHARE_IMAGE));
                return ExternalApiResult.FAILURE;
            }

            if (image.startsWith(SCHEME_HTTP)) {
                FileDownloader.enqueue(Uri.parse(image), getContext(), mFileDownloadListener);
                return ExternalApiResult.SUCCESS_WAIT;
            }

            ShareContent shareContent = ShareContentFactory.createSharePhotoContent(Uri.parse(image));
            showDialog(shareContent);

            return ExternalApiResult.SUCCESS_WAIT;
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mShareVideoMethod = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
            final String video = (String) parameters.get(0);

            if (TextUtils.isEmpty(video)) {
                Services.Log.debug(String.format("%s.%s was called with empty parameter.", OBJECT_NAME, METHOD_SHARE_VIDEO));
                return ExternalApiResult.FAILURE;
            }

            if (video.startsWith(SCHEME_HTTP)) {
                FileDownloader.enqueue(Uri.parse(video), getContext(), mFileDownloadListener);
                return ExternalApiResult.SUCCESS_WAIT;
            }

            ShareContent shareContent = ShareContentFactory.createShareVideoContent(Uri.parse(video));
            showDialog(shareContent);

            return ExternalApiResult.SUCCESS_WAIT;
        }
    };

    @Override
    public ExternalApiResult afterActivityResult(int requestCode, int resultCode, Intent result, String method, List<Object> methodParameters) {
        if (METHOD_POST.equals(method) || METHOD_SHARE_LINK.equals(method) ||
                METHOD_SHARE_IMAGE.equals(method) || METHOD_SHARE_VIDEO.equals(method)) {
            return (resultCode == Activity.RESULT_OK) ? ExternalApiResult.SUCCESS_CONTINUE : ExternalApiResult.FAILURE;
        }
        return null;
    }

    private void showDialog(ShareContent shareContent) {
        ShareDialog shareDialog = new ShareDialog(getActivity());
        ActivityHelper.registerActionRequestCode(shareDialog.getRequestCode());
        shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
    }

    private FileDownloader.FileDownloaderListener mFileDownloadListener = new FileDownloader.FileDownloaderListener() {
		@Override
		public void onDownloadProgressUpdate(int progressPercentage) {

		}

		@Override
        public void onDownloadSuccessful(Uri fileUri, String fileName) {
            String methodName = getAction().getMethodName();

            ShareContent shareContent;
            if (METHOD_SHARE_IMAGE.equals(methodName)) {
                shareContent = ShareContentFactory.createSharePhotoContent(fileUri);
            } else if (METHOD_SHARE_VIDEO.equals(methodName)) {
                shareContent = ShareContentFactory.createShareVideoContent(fileUri);
            } else {
                ActionExecution.cancelCurrent(getAction());
                return;
            }

            showDialog(shareContent);
        }

        @Override
        public void onDownloadFailed() {
            ActionExecution.cancelCurrent(getAction());
        }
    };
}
