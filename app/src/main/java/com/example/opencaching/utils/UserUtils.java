package com.example.opencaching.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class UserUtils {
    private static String PREF_USER_TOKEN = "pref-user-token";
    private static String PREF_USER_SECRET_TOKEN = "pref-user-secret-token";

    public static void setUserToken(Context context, String token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_USER_TOKEN, token).apply();
    }
    public static void setUserSecretToken(Context context, String secretToken){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_USER_SECRET_TOKEN, secretToken).apply();
    }


    public static String getUserToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_USER_TOKEN, "");
    }

    public static String getUserSecretToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_USER_SECRET_TOKEN, "");
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return !preferences.getString(PREF_USER_TOKEN, "").matches("");
    }

}
