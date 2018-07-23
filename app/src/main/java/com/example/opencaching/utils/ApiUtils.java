package com.example.opencaching.utils;


import com.example.opencaching.R;
import com.example.opencaching.network.models.Error;
import com.example.opencaching.network.models.Errors;
import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

/**
 * Created by Volfram on 16.07.2017.
 */

public class ApiUtils {

    public static Error getErrorSingle(Throwable t){
        t.printStackTrace();
        if (t instanceof ConnectException || t instanceof UnknownHostException){
            return new Error(R.string.check_internet_connection);
        } else if (t instanceof SocketTimeoutException){
            return new Error(R.string.couldn_connect_to_server);
        }
        return Error.DEFAULT;
    }

    public static ArrayList<Error> getErrors(ResponseBody responseBody) {
        ArrayList<Error> errors = new ArrayList<>();
        if (responseBody == null){
            errors.add(Error.DEFAULT);
            return errors;
        }
        Gson gson = new Gson();
        try {
            Errors errors1 = gson.fromJson(responseBody.string(), Errors.class);
            errors = errors1.getErrors();
        } catch (Exception e) {
            errors.add(Error.DEFAULT);
        }
        return errors;
    }

    public static Error getErrorSingle(ResponseBody responseBody){
        ArrayList<Error> errors = getErrors(responseBody);
        if (!errors.isEmpty()){
            return errors.get(0);
        } else {
            return Error.DEFAULT;
        }
    }

}
