package com.example.opencaching.fragments.map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.opencaching.R;
import com.example.opencaching.activities.MainActivity;
import com.example.opencaching.models.Geocache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Volfram on 15.05.2017.
 */

public class MapFragment extends Fragment implements MapFragmentView, OnMapReadyCallback {


    private MapFragmentPresenter presenter;
    private MainActivity activity;
    private GoogleMap mMap;
    private Map<String, Geocache> geocaches;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        unbinder = ButterKnife.bind(this, view);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        activity = (MainActivity) getActivity();
        activity.setActionBarTitle(R.string.app_name);
        presenter = new MapFragmentPresenter(this, activity);

        presenter.getWaypoints("50.20|19.30");

        return view;
    }

    private void setupActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

//    private void showLocationSettingsDialog() {
//        new AlertDialog.Builder(activity)
//                .setTitle(R.string.warning)
//                .setMessage(R.string.message_location_settings)
//                .setNegativeButton(R.string.cancel, null)
//                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(intent);
//                    }
//                })
//                .show();
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float mapLat = (float) 51.92;
        float mapLang = (float) 19.15;
        float mapZoom = (float) 5.6;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapLat, mapLang), mapZoom));
        configureInfoWindowAdapter();

    }

    private void configureInfoWindowAdapter() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view =  LayoutInflater.from(activity).inflate(R.layout.item_geocache_marker_window, null);
                TextView textView = (TextView) view.findViewById(R.id.name);
                Geocache geocache = geocaches.get(marker.getSnippet());
                //Log.d("Test", geocache.getCode());
                //textView.setText(geocache.getName());
                return view;
            }

        });
    }

    @Override
    public void showGeocaches(Map<String, Geocache> geocaches) {
        this.geocaches = geocaches;
        Iterator iterator = geocaches.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Geocache geocache = (Geocache) pair.getValue();
            String[] location = geocache.getLocation().split("\\|");
            LatLng latLang = new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
            MarkerOptions marker = new MarkerOptions().position(latLang).title(geocache.getName());
            marker.snippet(geocache.getCode());
            mMap.addMarker(marker);

            iterator.remove();
        }
    }


    @Override
    public void showProgress() {
        activity.showProgress();
    }

    @Override
    public void hideProgress() {
        activity.hideProgress();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupActionBar();
        presenter.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
