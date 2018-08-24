package com.example.opencaching.data.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Volfram on 16.07.2017.
 */

public class Trackable extends RealmObject {

    @SerializedName("code")
    @Expose
    private String code;            //code of the trackable
    @SerializedName("name")
    @Expose
    private String name;            //name of the trackable
    @SerializedName("url")
    @Expose
    private String trackableUrl;    //trackable's own webpage address

    public Trackable() {
    }
}
