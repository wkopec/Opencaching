package com.example.opencaching.network.models.okapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Volfram on 16.07.2017.
 */

public class WaypointResults {

    @SerializedName("results")
    @Expose
    private ArrayList<String> results;
    @SerializedName("more")
    @Expose
    private boolean more;

    public ArrayList<String> getResults() {
        return results;
    }

    public boolean isMore() {
        return more;
    }
}
