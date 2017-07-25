package com.example.opencaching.fragments.map;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opencaching.R;
import com.example.opencaching.activities.MainActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

import static com.example.opencaching.utils.IntegerUtils.getDistance;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheIcon;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheSelectedIcon;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheSize;

/**
 * Created by Volfram on 15.05.2017.
 */

public class MapFragment extends Fragment implements MapFragmentView, OnMapReadyCallback {


    private static final float MINIMUM_REQUEST_ZOOM = (float) 10;
    private static final float START_MAP_LATITUDE = (float) 51.92;
    private static final float START_MAP_LONGITUDE = (float) 19.15;
    private static final float START_MAP_ZOOM = (float) 5.6;

    @BindView(R.id.mapInfoMessage)
    TextView mapInfoMessage;
    @BindView(R.id.progressBar)
    SmoothProgressBar progressBar;

    private MapFragmentPresenter presenter;
    private MainActivity activity;
    private GoogleMap mMap;
    private ClusterManager<Geocache> mClusterManager;
    private Marker lastSelectedMarker;
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
        mMap.setInfoWindowAdapter(new MarkerAdapter());
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
                    marker.setIcon(BitmapDescriptorFactory.fromResource(getGeocacheSelectedIcon(presenter.getGeocache(marker).getType())));
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
                if (mMap.getCameraPosition().zoom > MINIMUM_REQUEST_ZOOM) {
                    LatLng center = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    int radius = getDistance(mMap.getProjection().fromScreenLocation(new Point(0, 0)), center) / 1000;
                    presenter.downloadGeocaches(center, radius);
                } else
                    showMapInfo(R.string.zoom_map_to_download);
            }
        });
    }


    private void setLastSelectedMarkerIcon() {
        if (lastSelectedMarker != null) {
            try {
                lastSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(getGeocacheIcon(presenter.getGeocache(lastSelectedMarker).getType())));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showMapInfo(int message) {
        mapInfoMessage.setText(activity.getString(message));
        if (!isMapInfoShown) {
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
    public void clusterGeocaches(ArrayList<Geocache> geocaches) {
        mClusterManager.addItems(geocaches);
        mClusterManager.cluster();
        hideProgress();
    }

    @Override
    public void showProgress() {
        hideMapInfo();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.progressiveStart();

    }

    @Override
    public void hideProgress() {
        progressBar.progressiveStop();
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

    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (marker.getSnippet() == null)
                return null;
            View view = LayoutInflater.from(activity).inflate(R.layout.item_geocache_marker_window, null);
            Geocache geocache = presenter.getGeocache(marker);
            TextView nameTextView = (TextView) view.findViewById(R.id.name);
            TextView codeTextView = (TextView) view.findViewById(R.id.code);
            TextView sizeTextView = (TextView) view.findViewById(R.id.sizeTextView);
            TextView rateTextView = (TextView) view.findViewById(R.id.rateTextView);
            TextView ownerTextView = (TextView) view.findViewById(R.id.ownerTextView);
            TextView foundsTextView = (TextView) view.findViewById(R.id.foundTextView);
            TextView notFoundsTextView = (TextView) view.findViewById(R.id.notFoundTextView);
            TextView recommendationsTextView = (TextView) view.findViewById(R.id.recommendationTextView);

            nameTextView.setText(geocache.getName());
            codeTextView.setText(geocache.getCode());
            sizeTextView.setText(activity.getString(R.string.marker_window_size) + " " + activity.getString(getGeocacheSize(geocache.getSize())));
            ownerTextView.setText(activity.getString(R.string.marker_window_owner) + " " + geocache.getOwner().getUsername());
            String[] rates = activity.getResources().getStringArray(R.array.geocache_rates);
            if ((int) geocache.getRating() > 0)
                rateTextView.setText(activity.getString(R.string.marker_window_rating) + " " + rates[(int) geocache.getRating() - 1]);
            else
                rateTextView.setVisibility(View.GONE);

            foundsTextView.setText(geocache.getFounds() + " x " + activity.getString(R.string.found));
            notFoundsTextView.setText(geocache.getNotfounds() + " x " + activity.getString(R.string.not_found));

            if (geocache.getRecommendations() == 0) {
                ImageView recommendationImageView = (ImageView) view.findViewById(R.id.recommendationImageView);
                recommendationImageView.setVisibility(View.GONE);
                recommendationsTextView.setVisibility(View.GONE);
            } else {
                recommendationsTextView.setText(geocache.getRecommendations() + " x " + activity.getString(R.string.recommended));
            }

            return view;
        }
    }

}
