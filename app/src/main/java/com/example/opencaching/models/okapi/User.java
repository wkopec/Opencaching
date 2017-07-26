package com.example.opencaching.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Volfram on 16.07.2017.
 */

public class User {

    @SerializedName("uuid")
    @Expose
    private String uuid;            //geocache owner's user ID
    @SerializedName("username")
    @Expose
    private String username;        //name of the user
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;      //URL of the user profile page

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
