package com.example.opencaching.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Volfram on 16.07.2017.
 */

public class Results {

    @SerializedName("results")
    @Expose
    private ArrayList<String> results;

    public ArrayList<String> getResults() {
        return results;
    }

}
