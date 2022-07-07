package com.genexus.android.facebook.api;

import android.content.Context;

import com.genexus.android.core.activities.IntentHandlers;
import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;
import com.genexus.android.facebook.controls.SDFacebookButton;

public class FacebookModule implements GenexusModule {

    @Override
    public void initialize(Context context) {
        ExternalApiFactory.addApi(new ExternalApiDefinition(FacebookAPI.OBJECT_NAME, FacebookAPI.class));
        UcFactory.addControl(new UserControlDefinition(SDFacebookButton.NAME, SDFacebookButton.class));

        AppLinksGx fbAppLinks = new AppLinksGx();
        fbAppLinks.initialize(context);
        IntentHandlers.addHandler(fbAppLinks);
    }
}
