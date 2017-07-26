package com.example.opencaching.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Volfram on 16.07.2017.
 */

public class Log {

    @SerializedName("uuid")
    @Expose
    private String uuid;        //log ID
    @SerializedName("date")
    @Expose
    private String date;        //date and time when the log was added
    @SerializedName("user")
    @Expose
    private User user;          //authot of the log
    @SerializedName("type")
    @Expose
    private String type;        //type of a log
    @SerializedName("comment")
    @Expose
    private String comment;     //full comment of a log


}
