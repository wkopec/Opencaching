package com.example.opencaching.fragments.map;

import com.example.opencaching.models.okapi.Geocache;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Volfram on 15.07.2017.
 */

public interface MapFragmentView {

    void showProgress();

    void hideProgress();

    void showMapInfo(int message);

    void hideMapInfo();

    void moveMapCamera(LatLng latLng, float zoom);

    void clusterGeocaches(ArrayList<Geocache> geocaches);
}
