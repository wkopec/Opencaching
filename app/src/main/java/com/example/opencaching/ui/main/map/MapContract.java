package com.example.opencaching.ui.main.map;

import com.example.opencaching.network.models.okapi.Geocache;
import com.example.opencaching.ui.base.BaseContract;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by Volfram on 15.07.2017.
 */

public class MapContract{

    public interface View extends BaseContract.View {

        void showProgress();

        void hideProgress();

        void showMapInfo(int message);

        void hideMapInfo();

        void moveMapCamera(LatLng latLng, float zoom);

        void clusterGeocaches(ArrayList<Geocache> geocaches);

    }

    public interface Presenter extends BaseContract.Presenter {

        void downloadGeocaches(LatLng center, int radius);

        void getLocation(String address);

        void getUserData();

        Geocache getGeocache(Marker marker);

    }


}
