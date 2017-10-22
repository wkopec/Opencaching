package com.example.opencaching.network.models.okapi;

import com.example.opencaching.network.models.Error;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Volfram on 22.10.2017.
 */

public class ErrorResponse {
    @SerializedName("reason_stack")
    private Error error;

    public Error getError() {
        return error;
    }
}
