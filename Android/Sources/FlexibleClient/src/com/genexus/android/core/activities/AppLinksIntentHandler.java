package com.genexus.android.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

import com.genexus.android.R;
import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.DynamicCallAction;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.genexus.android.core.utils.FileUtils2.isHttp;

public class AppLinksIntentHandler implements IIntentHandler {

    private static final String NAME = "GeneXus.SD.DeepLink";
    private static final String EVENT_HANDLE = "Handle";

    @Override
    public boolean tryHandleIntent(final UIContext context, Intent intent, final Entity entity) {
        final Uri uri = intent.getData();
        if (uri != null && validAppLink(uri)) {
            if (isGamOtpUrl(uri))
                Services.Log.debug("GAM OTP Url isn't automatically handled yet.");

            ExternalObjectEvent event = new ExternalObjectEvent(NAME, EVENT_HANDLE);
            if (!event.fire(Arrays.asList(uri.toString(), false), new ExternalObjectEvent.IEntityEventListener() {
                @Override
                public void onEndEvent(boolean successful, List<Object> parameters) {
                    if (parameters.size() > 1 && parameters.get(1) instanceof String && Services.Strings.tryParseBoolean((String) parameters.get(1), false)) {
                        // Handled by GeneXus user code, ignore success because event may be cancel if the panel was exited with back, &Handled should be set before the call
                    } else {
                        callAutomaticOrFinish(uri, context, entity);
                    }
                }
            })) {
                callAutomaticOrFinish(uri, context, entity);
            }
            return true;
        }

        return false;
    }

    private void callAutomaticOrFinish(Uri uri, UIContext context, Entity entity) {
        if (!callAutomatic(uri, context, entity) && context.getActivity() != null) {
            Activity activity = context.getActivity();

            // Call browser
            String scheme = uri.getScheme().toLowerCase();
            if (isHttp(scheme)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                List<Intent> targetedShareIntentList = new ArrayList<>();
                List<ResolveInfo> resolveInfoList = activity.getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo resolveInfo : resolveInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    if (!packageName.equals(activity.getPackageName())) {
                        Intent targetedShareIntent = new Intent(Intent.ACTION_VIEW, uri);
                        targetedShareIntent.setPackage(packageName);
                        targetedShareIntentList.add(targetedShareIntent);
                    }
                }
                if (targetedShareIntentList.size() == 1) {
                    activity.startActivity(targetedShareIntentList.get(0));
                }
                else if (targetedShareIntentList.size() > 1) {
                    Intent chooserIntent = Intent.createChooser(targetedShareIntentList.remove(0), Services.Strings.getResource(R.string.GXM_OpenWith));
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntentList.toArray(new Parcelable[targetedShareIntentList.size()]));
                    activity.startActivity(chooserIntent);
                }
            }
        }
    }

    private boolean callAutomatic(Uri uri, UIContext context, Entity entity) {
        String dynamicCall = createDynamicCall(uri);
        if (dynamicCall != null) {
            DynamicCallAction action = DynamicCallAction.redirect(context, entity, dynamicCall, true);
            if (action != null) {
                ActionExecution exec = new ActionExecution(action);
                exec.executeAction(); // Will also finish() the current activity.
                return true;
            }
        }
        return false;
    }

    private boolean isGamOtpUrl(Uri uri) {
        if (!uri.toString().contains("/oauth/gam/signin?uotp=auth&state="))
            return false;

        String otpCode = getOtpCode(uri);
        return otpCode != null && !otpCode.isEmpty();
    }

    private String getOtpCode(Uri uri) {
        return uri.getQueryParameter("otp");
    }

    private boolean validAppLink(Uri uri) {
        String urlList = Services.Application.get().getMainProperties().optStringProperty("DeepLinkBaseURL");
        for (String urlString : urlList.split(";", -1)) {
            if (uri.toString().startsWith(urlString))
                return true;
        }
        return false;
    }

    private String createDynamicCall(Uri uri) {
        String link = getDeepLinkName(uri);
        String objectName = mapObject(link);
        if (objectName == null)
            return null;

        if (Strings.hasValue(uri.getQuery()))
            return objectName + "?" + uri.getQuery().replace('&', ','); // It would be more verbose to use getQueryParameters() but this way the dynamic call code is used to know the parameters position
        else
            return objectName;
    }

    private String getDeepLinkName(Uri uri) {
        String urlList = Services.Application.get().getMainProperties().optStringProperty("DeepLinkBaseURL");
        for (String urlString : urlList.split(";", -1)) {
            if (uri.toString().startsWith(urlString)) {
                String link = uri.toString().substring(urlString.length());
                link = link.replaceFirst("^/+", "");
                int index = link.indexOf('?');
                if (index >= 0)
                    link = link.substring(0, index);
                return link;
            }
        }
        return null;
    }

    private String mapObject(String link) {
        return Services.Application.getDefinition().getDeepLink(link);
    }
}
