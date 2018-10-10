package pl.opencaching.android.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Wojtek on 22.07.2017.
 */

public class IntegerUtils {

    public static int getDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results);
        return (int) results[0];
    }

}
