package com.genexus.android.facebook.controls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.genexus.android.ActivityResourceBase;
import com.genexus.android.ActivityResources;
import com.genexus.android.IActivityResource;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.ui.Coordinator;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.genexus.android.facebook.api.FacebookEntitiesFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class SDFacebookButton extends LoginButton implements IGxEdit {
    public static final String NAME = "SDFacebookButton";

    private static final String PROPERTY_READ_PERMISSIONS = "@SDFacebookButtonReadPermissions";
    private static final String PROPERTY_PUSBLISH_ALLOWED = "@SDFacebookButtonPublishAllowed";

    private static final String EVENT_USER_LOGGEDIN = "OnUserLoggedIn";
    private static final String EVENT_USER_LOGGEDOUT = "OnUserLoggedOut";
    private static final String EVENT_USER_INFOUPDATED = "OnUserInfoUpdated";
    private static final String EVENT_USER_ERROR = "OnError";

    @SuppressWarnings("DoubleBraceInitialization")
    private static final Map<String, String> FIELDS = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("public_profile", "id,name,first_name,middle_name,last_name,gender,picture");
        put("email", "email");
        put("user_birthday", "birthday");
        put("user_location", "location{location}");
    }});

    private String mTag;
    private Coordinator mCoordinator;
    private CallbackManager mCallbackManager;
    private JSONObject mUserInformation;
    private String mReadPermissions;
    private boolean mPublishAllowed;

    public SDFacebookButton(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
        super(context);
        init(coordinator, definition);
    }

    protected void init(Coordinator coordinator, LayoutItemDefinition definition) {
        mCoordinator = coordinator;
        mCallbackManager = CallbackManager.Factory.create();

        if (definition != null && definition.getControlInfo() != null) {
            setProperties(definition.getControlInfo());
        }

        ActivityResources.setResource(mCoordinator.getUIContext().getActivity(), IActivityResource.class, mLifecycleCallbacks);

        registerCallback(mCallbackManager, mLoginCallback);
    }

    private void setProperties(ControlInfo properties) {
        mReadPermissions = properties.optStringProperty(PROPERTY_READ_PERMISSIONS);
        mPublishAllowed = properties.optBooleanProperty(PROPERTY_PUSBLISH_ALLOWED);

		List<String> permissions = Arrays.asList(mReadPermissions.split(","));
        if (mPublishAllowed) {
        	permissions = new ArrayList<>(permissions); // make it mutable
			permissions.add("publish_actions");
        }
		setPermissions(permissions);
    }

    @Override
    public String getGxValue() {
        return mUserInformation != null ? mUserInformation.toString() : Strings.EMPTY;
    }

    @Override
    public void setGxValue(String value) {
    }

    @Override
    public String getGxTag() {
        return mTag;
    }

    @Override
    public void setGxTag(String tag) {
        mTag = tag;
    }

    @Override
    public void setValueFromIntent(Intent data) {
    }

    @Override
    public boolean isEditable() {
        return true; // Because it changes its associated data.
    }

    @Override
    public IGxEdit getViewControl() {
        return this;
    }

    @Override
    public IGxEdit getEditControl() {
        return this;
    }
    private FacebookCallback<LoginResult> mLoginCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Services.Log.debug("Facebook Login Callback: Successful.");
        }

        @Override
        public void onCancel() {
            Services.Log.debug("Facebook Login Callback: Canceled.");
        }

        @Override
        public void onError(FacebookException error) {
            Services.Log.debug(String.format("Facebook Login Callback: Error occourred (%s).", error.getMessage()));
            mCoordinator.runControlEvent(SDFacebookButton.this, EVENT_USER_ERROR);
        }
    };

    // Upon instanciating this class, it starts listening automatically.
    private AccessTokenTracker mAccessTokenTracker = new AccessTokenTracker() {

        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            boolean isLoggedIn = currentAccessToken != null && !currentAccessToken.getPermissions().isEmpty();
            if (isLoggedIn) {
                mCoordinator.runControlEvent(SDFacebookButton.this, EVENT_USER_LOGGEDIN);
            } else {
                mCoordinator.runControlEvent(SDFacebookButton.this, EVENT_USER_LOGGEDOUT);
            }
        }
    };

    // Upon instanciating this class, it starts listening automatically.
    private ProfileTracker mProfileTracker = new ProfileTracker() {

        @Override
        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            if (currentProfile == null) {
                mUserInformation = null;
            } else {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                String fields = null;

                for (String permission : Arrays.asList(mReadPermissions.split(","))) {
                    if (!FIELDS.containsKey(permission)) {
                        continue;
                    }
                    if (TextUtils.isEmpty(fields)) {
                        fields = FIELDS.get(permission);
                    } else {
                        fields += "," + FIELDS.get(permission);
                    }
                }

                GraphRequest request = GraphRequest.newMeRequest(accessToken, mGraphRequestCallback);
                Bundle params = new Bundle();
                params.putString("fields", fields);
                request.setParameters(params);
                request.executeAsync();
            }
        }
    };

    private GraphRequest.GraphJSONObjectCallback mGraphRequestCallback = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            if (object == null) {
                return;
            }
            mUserInformation = FacebookEntitiesFactory.createUserInformation(object, mReadPermissions);
            mCoordinator.runControlEvent(SDFacebookButton.this, EVENT_USER_INFOUPDATED);
        }
    };

    private ActivityResourceBase mLifecycleCallbacks = new ActivityResourceBase() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    };
}
