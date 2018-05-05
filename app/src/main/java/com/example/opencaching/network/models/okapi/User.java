package com.example.opencaching.network.models.okapi;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Volfram on 16.07.2017.
 */

public class User {

    @SerializedName("uuid")
    @Expose
    private String uuid;                //geocache owner's user ID
    @SerializedName("username")
    @Expose
    private String username;            //name of the user
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;          //URL of the user profile page
    @SerializedName("is_admin ")
    @Expose
    private boolean isAdmin;            //true if user has admin privileges
    @SerializedName("date_registered")
    @Expose
    private String registeredDate;      //date when the user has registered his Opencaching account
    @SerializedName("caches_found")
    @Expose
    private int foundCaches;            //number of "Found it" and "Attended" log entries
    @SerializedName("caches_notfound")
    @Expose
    private int notFoundCaches;         //number of "Didn't find it" log entries
    @SerializedName("caches_hidden")
    @Expose
    private int hiddenCaches;           //number of caches owned
    @SerializedName("rcmds_given")
    @Expose
    private int givenRecommendations;   //number of recommendations given
    @SerializedName("home_location")
    @Expose
    private String homeLocation;        //home location of the user;

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public int getFoundCaches() {
        return foundCaches;
    }

    public int getNotFoundCaches() {
        return notFoundCaches;
    }

    public int getHiddenCaches() {
        return hiddenCaches;
    }

    public int getGivenRecommendations() {
        return givenRecommendations;
    }

    public LatLng getHomeLocation() {
        if(homeLocation != null) {
            String[] location = homeLocation.split("\\|");
            return new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
        }
        return null;
    }
}
