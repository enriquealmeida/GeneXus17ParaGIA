package com.genexus.android.facebook.api;

import com.genexus.android.core.base.services.Services;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookEntitiesFactory {
    public static JSONObject createFacebookAccessToken(AccessToken accessToken) {
        JSONObject sdt = new JSONObject();

        try {
            sdt.put("AccessToken", accessToken.getToken());
            sdt.put("ApplicationId", accessToken.getApplicationId());
            sdt.put("UserId", accessToken.getUserId());

            JSONArray permissions = new JSONArray(Arrays.asList(accessToken.getPermissions().toArray()));
            JSONArray declinedPermissions = new JSONArray(Arrays.asList(accessToken.getDeclinedPermissions().toArray()));

            sdt.put("Permissions", permissions);
            sdt.put("DeclinedPermissions", declinedPermissions);

            sdt.put("ExpirationDate", Services.Strings.getDateStringForServer(accessToken.getExpires()));
            sdt.put("LastRefreshDate", Services.Strings.getDateStringForServer(accessToken.getLastRefresh()));
        } catch (JSONException e) {
            sdt = null;
        }

        return sdt;
    }

    public static JSONObject createUserInformation(JSONObject userObject, String readPermissions) {
        JSONObject userInfo = new JSONObject();

        try {
            if (readPermissions.contains("public_profile")) {
                userInfo.put("Id", userObject.optString("id"));
                userInfo.put("Name", userObject.optString("name"));
                userInfo.put("FirstName", userObject.optString("first_name"));
                userInfo.put("MiddleName", userObject.optString("middle_name"));
                userInfo.put("LastName", userObject.optString("last_name"));
                userInfo.put("Gender", userObject.optString("gender"));
                JSONObject picture = userObject.optJSONObject("picture");
                if (picture != null) {
                    JSONObject data = picture.optJSONObject("data");
                    if (data != null) {
                        userInfo.put("ProfileImage", data.optString("url"));
                    }
                }
            }

            if (readPermissions.contains("email")) {
                userInfo.put("Email", userObject.optString("email"));
            }

            if (readPermissions.contains("user_birthday")) {
                userInfo.put("Birthday", userObject.optString("birthday"));
            }

            if (readPermissions.contains("user_location")) {
                JSONObject locationPage = userObject.optJSONObject("location");
                if (locationPage != null) {
                    JSONObject location = locationPage.optJSONObject("location");
                    if (location != null) {
                        userInfo.put("Country", location.optString("country"));
                        userInfo.put("City", location.optString("city"));
                        userInfo.put("LocationLatitude", location.optString("latitude"));
                        userInfo.put("LocationLongitude", location.optString("longitude"));
                    }
                }
            }
        } catch (JSONException e) {
            userInfo = null;
        }

        return userInfo;
    }
}
