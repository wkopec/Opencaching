package com.example.opencaching.app.prefs;

import android.content.SharedPreferences;

import com.example.opencaching.data.models.okapi.User;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

public class SessionManager {

    private static final String KEY_USER_OAUTH_TOKEN = "oauth_token:";
    private static final String KEY_USER_OAUTH_TOKEN_SECRET = "oauth_token_secret:";


    private static final String KEY_USER_UUID = "user_uuid:";
    private static final String KEY_USER_USERNAME = "username:";
    private static final String KEY_USER_REGISTER_DATE = "date_registered:";
    private static final String KEY_USER_CACHES_FOUND = "caches_found:";
    private static final String KEY_USER_CACHES_NOT_FOUND = "caches_notfound:";
    private static final String KEY_USER_CACHES_HIDDEN = "caches_hidden:";
    private static final String KEY_USER_RECOMMENDATIONS_GIVEN = "recommendations_given:";
    private static final String KEY_USER_HOME_LOCATION = "home_location:";



    private final SharedPreferences prefs;

    @Inject
    public SessionManager(SharedPreferences preferences) {
        this.prefs = preferences;
    }

    private SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

    public void setLoggedUserData(User user) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(KEY_USER_UUID, user.getUuid());
        editor.putString(KEY_USER_USERNAME, user.getUsername());
        editor.putString(KEY_USER_REGISTER_DATE, user.getRegisteredDate());
        editor.putInt(KEY_USER_CACHES_FOUND, user.getFoundCaches());
        editor.putInt(KEY_USER_CACHES_NOT_FOUND, user.getNotFoundCaches());
        editor.putInt(KEY_USER_CACHES_HIDDEN, user.getHiddenCaches());
        editor.putInt(KEY_USER_RECOMMENDATIONS_GIVEN, user.getGivenRecommendations());
        editor.putString(KEY_USER_HOME_LOCATION, user.getRawHomeLocation());
        editor.apply();
    }

    public void clearLoggedUserData() {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(KEY_USER_OAUTH_TOKEN);
        editor.remove(KEY_USER_OAUTH_TOKEN_SECRET);
        editor.remove(KEY_USER_UUID);
        editor.remove(KEY_USER_USERNAME);
        editor.remove(KEY_USER_REGISTER_DATE);
        editor.remove(KEY_USER_CACHES_FOUND);
        editor.remove(KEY_USER_CACHES_NOT_FOUND);
        editor.remove(KEY_USER_CACHES_HIDDEN);
        editor.remove(KEY_USER_RECOMMENDATIONS_GIVEN);
        editor.remove(KEY_USER_HOME_LOCATION);
        editor.apply();
    }

    //Authorization data

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

    public boolean isLoggedIn() {
        return !prefs.getString(KEY_USER_OAUTH_TOKEN_SECRET, "").matches("");
    }

    //User data

    public String getUserUuid() {
        return prefs.getString(KEY_USER_UUID, "");
    }

    public String getUsername() {
        return prefs.getString(KEY_USER_USERNAME, "");
    }

    public String getRegisterDate() {
        return prefs.getString(KEY_USER_REGISTER_DATE, "");
    }

    public int getFoundGeocachesCount() {
        return prefs.getInt(KEY_USER_CACHES_FOUND, -1);
    }

    public int getNotFoundGeocachesCount() {
        return prefs.getInt(KEY_USER_CACHES_NOT_FOUND, -1);
    }

    public int getHiddenGeocachesCount() {
        return prefs.getInt(KEY_USER_CACHES_HIDDEN, -1);
    }

    public int getRecommendationGivenCount() {
        return prefs.getInt(KEY_USER_RECOMMENDATIONS_GIVEN, -1);
    }

    public LatLng getUserHomeLocation() {
        String stringLatLng = prefs.getString(KEY_USER_HOME_LOCATION, "");
        if(!stringLatLng.equals("")) {
            String[] location = stringLatLng.split("\\|");
            return new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
        }
        return null;
    }
    
    //User settings

}
