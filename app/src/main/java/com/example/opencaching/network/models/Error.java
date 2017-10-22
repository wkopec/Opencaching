package com.example.opencaching.network.models;

import com.example.opencaching.R;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Volfram on 22.10.2017.
 */

public class Error {
    public static final Error DEFAULT = new Error(R.string.something_went_wrong);
    public static final String UNAUTHORIZED = "Unauthorized";

    @SerializedName("message")
    private String message = "";
    private int messageRes;
    private String field;

    public Error(int message) {
        messageRes = message;
    }
    public Error(String message) {
        this.message = message;
    }

    public String getCode() {
        return message;
    }

    public int getMessage() {
        if (messageRes != 0) {
            return messageRes;
        } else {
            return getMessageRes();
        }
    }

    private int getMessageRes() {
        if (message != null)
            switch (message) {
                case UNAUTHORIZED:
                    return R.string.session_has_ended;
            }
        return R.string.something_went_wrong;
    }

    public String getField() {
        return field;
    }

}