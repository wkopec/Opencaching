package com.example.opencaching.utils;

import android.util.Log;

import com.example.opencaching.R;

import java.io.IOException;
import java.net.ConnectException;

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

    public static int getFailureMessage(Throwable t){
        if (t instanceof ConnectException){
            return R.string.check_internet_connection;
        }
        return R.string.something_went_wrong;
    }

}
