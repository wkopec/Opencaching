package com.example.opencaching.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Wojtek on 03.05.2018.
 */

public class UserUtils {

    private static String PREF_SESSION = "pref_session";
    private static String OAUTH_TOKEN = "oauth_token";
    private static String OAUTH_TOKEN_SECRET = "oauth_token_secret";
    private static String HOME_LOCATION = "home_location";

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE);
    }

    public static String getOauthToken(Context context) {
        return getSharedPreferences(context).getString(OAUTH_TOKEN, "");
    }

    public static void setOauthToken(Context context, String oauthToken) {
        getSharedPreferences(context).edit().putString(OAUTH_TOKEN, oauthToken).apply();
    }

    public static String getOauthTokenSecret(Context context) {
        return getSharedPreferences(context).getString(OAUTH_TOKEN_SECRET, "");
    }

    public static void setOauthTokenSecret(Context context, String oauthTokenSecret) {
        getSharedPreferences(context).edit().putString(OAUTH_TOKEN_SECRET, oauthTokenSecret).apply();
    }

    public static void setUserHomeLocation(Context context, String homeLocation) {
        getSharedPreferences(context).edit().putString(HOME_LOCATION, homeLocation).apply();
    }

    public static LatLng getUserHomeLocation(Context context) {
        String stringLatLng = getSharedPreferences(context).getString(HOME_LOCATION, "");
        if(!stringLatLng.equals("")) {
            String[] location = stringLatLng.split("\\|");
            return new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
        }
        return null;
    }

    public static boolean isLoggedIn(Context context) {
        return !getSharedPreferences(context).getString(OAUTH_TOKEN_SECRET, "").matches("");
    }

}
