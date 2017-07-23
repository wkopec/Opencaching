package com.example.opencaching.fragments.map;

import com.example.opencaching.models.Geocache;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Volfram on 15.07.2017.
 */

public interface MapFragmentView {

    void showProgress();

    void hideProgress();

    void showMapInfo(String message);

    void hideMapInfo();


    void addGeocachesOnMap( Map<String, Geocache> newGeocaches);

}
