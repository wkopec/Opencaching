package com.example.opencaching.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import static com.example.opencaching.utils.IntegerUtils.getDistance;

/**
 * Created by Volfram on 22.07.2017.
 */

public class CircleArea {
    private LatLng center;
    private int radius;

    public CircleArea(LatLng center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public boolean isWithin(LatLng point) {
        return getDistance(center, point) < radius;
    }

}
