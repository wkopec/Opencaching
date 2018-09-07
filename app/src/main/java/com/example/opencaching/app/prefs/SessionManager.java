package com.example.opencaching.app.prefs;

import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

public class SessionManager {

    private static final String KEY_USER_OAUTH_TOKEN = "oauth_token:";
    private static final String KEY_USER_OAUTH_TOKEN_SECRET = "oauth_token_secret:";
    private static final String KEY_USER_HOME_LOCATION = "home_location:";

    private final SharedPreferences prefs;

    @Inject
    public SessionManager(SharedPreferences preferences) {
        this.prefs = preferences;
    }

    private SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

    public void saveOauthToken(String oauthToken) {
        getEditor().putString(KEY_USER_OAUTH_TOKEN, oauthToken).commit();
    }

    public String getOauthToken() {
        return prefs.getString(KEY_USER_OAUTH_TOKEN, "");
    }

    public void saveOauthTokenSecret(String oauthTokenSecret) {
        getEditor().putString(KEY_USER_OAUTH_TOKEN_SECRET, oauthTokenSecret).commit();
    }

    public String getOauthTokenSecret() {
        return prefs.getString(KEY_USER_OAUTH_TOKEN_SECRET, "");
    }

    public void saveUserHomeLocation(String homeLocation) {
        getEditor().putString(KEY_USER_HOME_LOCATION, homeLocation).apply();
    }

    public LatLng getUserHomeLocation() {
        String stringLatLng = prefs.getString(KEY_USER_HOME_LOCATION, "");
        if(!stringLatLng.equals("")) {
            String[] location = stringLatLng.split("\\|");
            return new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
        }
        return null;
    }

    public boolean isLoggedIn() {
        return !prefs.getString(KEY_USER_OAUTH_TOKEN_SECRET, "").matches("");
    }

}
