package com.example.opencaching.fragments.map;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opencaching.R;
import com.example.opencaching.activities.MainActivity;
import com.example.opencaching.models.CircleArea;
import com.example.opencaching.models.Geocache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.opencaching.utils.ResourceUtils.getGeocacheIcon;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheSelectedIcon;

/**
 * Created by Volfram on 15.05.2017.
 */

public class MapFragment extends Fragment implements MapFragmentView, OnMapReadyCallback {

    private static final int GEOCACHE_REQUEST_KM_RADIUS = 20;
    private static final float START_MAP_LATITUDE = (float) 51.92;
    private static final float START_MAP_LONGITUDE = (float) 19.15;
    private static final float START_MAP_ZOOM = (float) 5.6;

    @BindView(R.id.mapInfoMessage)
    TextView mapInfoMessage;

    private MapFragmentPresenter presenter;
    private MainActivity activity;
    private GoogleMap mMap;
    private ClusterManager<Geocache> mClusterManager;
    private Map<String, Geocache> displayedGeocaches;
    private Marker lastSelectedMarker;
    private ArrayList<CircleArea> coveredArea;
    private boolean isMapInfoShown;
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
        displayedGeocaches = new HashMap<>();
        coveredArea = new ArrayList<>();
        hideMapInfo();
        return view;
    }

    private void setupActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(START_MAP_LATITUDE, START_MAP_LONGITUDE), START_MAP_ZOOM));

        mClusterManager = new ClusterManager<>(activity, mMap);
        mClusterManager.setRenderer(new OwnIconRendered(activity.getApplicationContext(), mMap, mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        configureMap();
    }

    private void configureMap() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (marker.getSnippet() == null)
                    return null;
                View view = LayoutInflater.from(activity).inflate(R.layout.item_geocache_marker_window, null);
                Geocache geocache = displayedGeocaches.get(marker.getSnippet());
                presenter.setMarkerWindowData(view, geocache);
                return view;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setLastSelectedMarkerIcon();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getSnippet() != null) {
                    setLastSelectedMarkerIcon();
                    lastSelectedMarker = marker;
                    Geocache geocache = displayedGeocaches.get(marker.getSnippet());
                    marker.setIcon(BitmapDescriptorFactory.fromResource(getGeocacheSelectedIcon(geocache.getType())));
                }
                //else
                //TODO: zoom
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mClusterManager.cluster();
                CameraPosition cameraPosition = mMap.getCameraPosition();
                Log.d("Test", String.valueOf(cameraPosition.zoom));
                if (mMap.getCameraPosition().zoom > 10)
                    presenter.getWaypoints(cameraPosition.target.latitude + "|" + cameraPosition.target.longitude, GEOCACHE_REQUEST_KM_RADIUS);
                else
                    showMapInfo(activity.getString(R.string.zoom_map_to_download));
            }
        });
    }

    private void setLastSelectedMarkerIcon() {
        if (lastSelectedMarker != null) {
            try {
                Geocache geocache = displayedGeocaches.get(lastSelectedMarker.getSnippet());
                lastSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(getGeocacheIcon(geocache.getType())));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showMapInfo(String message) {
        mapInfoMessage.setText(message);
        if(!isMapInfoShown){
            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_in_up);
            set.setTarget(mapInfoMessage);
            set.start();
        }
        isMapInfoShown = true;
    }

    @Override
    public void hideMapInfo() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.slide_out_up);
        set.setTarget(mapInfoMessage);
        set.start();
        isMapInfoShown = false;
    }

    @Override
    public void addGeocachesOnMap(Map<String, Geocache> geocaches) {
        Map<String, Geocache> newGeocaches = new HashMap<>();
        Iterator iterator = geocaches.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Geocache geocache = (Geocache) pair.getValue();
            if (displayedGeocaches.containsKey(geocache.getCode()))
                continue;
            newGeocaches.put(geocache.getCode(), geocache);
            mClusterManager.addItem(geocache);
            iterator.remove();
        }
        displayedGeocaches.putAll(newGeocaches);
        mClusterManager.cluster();

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

    private class OwnIconRendered extends DefaultClusterRenderer<Geocache> {

        OwnIconRendered(Context context, GoogleMap map, ClusterManager<Geocache> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Geocache geocache, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(getGeocacheIcon(geocache.getType())));
            markerOptions.snippet(geocache.getSnippet());
            markerOptions.title(geocache.getTitle());

            super.onBeforeClusterItemRendered(geocache, markerOptions);
        }
    }

}
