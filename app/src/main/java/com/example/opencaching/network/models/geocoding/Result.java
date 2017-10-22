package com.example.opencaching.network.models.geocoding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Wojtek on 26.07.2017.
 */

public class Result {
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }
}
