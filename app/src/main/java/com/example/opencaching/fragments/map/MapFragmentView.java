package com.example.opencaching.fragments.map;

import com.example.opencaching.models.Geocache;

import java.util.Map;

/**
 * Created by Volfram on 15.07.2017.
 */

public interface MapFragmentView {

    void showProgress();

    void hideProgress();

    void showGeocaches(Map<String, Geocache> geocaches);

}
