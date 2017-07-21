package com.example.opencaching.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Volfram on 16.07.2017.
 */

public class ApiUtils {

    public static void checkForErrors(ResponseBody response) {
        try {
            if(response != null)
                Log.d("Retrofit error", response.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
