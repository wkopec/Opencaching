package com.example.opencaching.ui.main.map;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BaseFragment;
import com.example.opencaching.ui.geocache.GeocacheActivity;
import com.example.opencaching.ui.main.MainActivity;
import com.example.opencaching.utils.events.SearchMapEvent;
import com.example.opencaching.network.models.okapi.Geocache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

import static com.example.opencaching.ui.geocache.GeocacheActivity.GEOCACHE_WAYPOINT;
import static com.example.opencaching.utils.IntegerUtils.getDistance;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheIcon;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheSelectedIcon;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheSize;
import static com.example.opencaching.utils.UserUtils.getUserHomeLocation;

/**
 * Created by Volfram on 15.05.2017.
 */

public class MapFragment extends BaseFragment implements MapContract.View, OnMapReadyCallback {

    private static final float MINIMUM_REQUEST_ZOOM = (float) 10;
    private static final float START_MAP_LATITUDE = (float) 51.92;
    private static final float START_MAP_LONGITUDE = (float) 19.15;
    private static final float START_MAP_ZOOM = (float) 5.6;

    @BindView(R.id.mapInfoMessage)
    TextView mapInfoMessage;
    @BindView(R.id.progressBar)
    SmoothProgressBar progressBar;
    @BindView(R.id.geocacheBottomSheet)
    LinearLayout geocacheBottomSheet;
    @BindView(R.id.geocacheInfoHeader)
    LinearLayout geocacheInfoHeader;
    @BindView(R.id.geocacheName)
    TextView geocacheName;
    @BindView(R.id.geocacheSize)
    TextView geocacheSize;
    @BindView(R.id.geocacheRate)
    TextView geocacheRate;
    @BindView(R.id.geocacheOwner)
    TextView geocacheOwner;
    @BindView(R.id.geocacheFound)
    TextView geocacheFound;
    @BindView(R.id.geocacheNotFound)
    TextView geocacheNotFound;
    @BindView(R.id.geocacheRecommendation)
    TextView geocacheRecommendation;

    private MapContract.Presenter presenter;
    private MainActivity activity;
    private GoogleMap mMap;
    private ClusterManager<Geocache> mClusterManager;
    private Marker lastSelectedMarker;
    private boolean isMapInfoShown;
    private BottomSheetBehavior sheetBehavior;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, null);
        unbinder = ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        activity = (MainActivity) getActivity();
        activity.setActionBarTitle(R.string.app_name);
        presenter = new MapFragmentPresenter(this, activity);
        setPresenter(presenter);
        setGeocacheBottomSheet();

        return view;
    }

    private void setupActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(getUserHomeLocation(activity) != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getUserHomeLocation(activity), 10));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(START_MAP_LATITUDE, START_MAP_LONGITUDE), START_MAP_ZOOM));
        }
        configureMap();
        presenter.getUserData();
    }

    private void configureMap() {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.retro_map);
        mMap.setMapStyle(style);

        mClusterManager = new ClusterManager<>(activity, mMap);
        final CustomClusterRenderer renderer = new CustomClusterRenderer(activity.getApplicationContext(), mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        mClusterManager.setOnClusterClickListener(
                cluster -> {
                    hideGeocacheInfo();
                    moveMapCamera(cluster.getPosition(), mMap.getCameraPosition().zoom + 1);
                    return true;
                });
        mClusterManager.setOnClusterItemClickListener(
                clusterItem -> {
                    Marker marker = renderer.getMarker(clusterItem);
                    if (marker.getSnippet() != null) {
                        setLastSelectedMarkerIcon();
                        lastSelectedMarker = marker;
                        marker.setIcon(BitmapDescriptorFactory.fromResource(getGeocacheSelectedIcon(presenter.getGeocache(marker).getType())));
                        showGeocahceInfo(presenter.getGeocache(marker));
                        moveMapCamera(marker.getPosition(), mMap.getCameraPosition().zoom);
                    }
                    return true;
                });

        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setOnMapClickListener(latLng -> {
            activity.hideSearchView();
            //setLastSelectedMarkerIcon();
            hideGeocacheInfo();
        });

        // Classic marker window
        //mMap.setInfoWindowAdapter(new CustomInfoViewAdapter());
        //mMap.setOnInfoWindowClickListener(marker -> startGeocacheActivity(marker.getSnippet()));
        mMap.setOnCameraIdleListener(() -> {
            mClusterManager.cluster();
            CameraPosition cameraPosition = mMap.getCameraPosition();
            hideMapInfo();
            if (mMap.getCameraPosition().zoom >= MINIMUM_REQUEST_ZOOM) {
                LatLng center = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                int radius = getDistance(mMap.getProjection().fromScreenLocation(new Point(0, 0)), center) / 1000;
                presenter.downloadGeocaches(center, radius);
            } else
                showMapInfo(R.string.zoom_map_to_download);
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

    private void startGeocacheActivity(String geocacheWaypoint) {
        Intent intent = new Intent(activity, GeocacheActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GEOCACHE_WAYPOINT, geocacheWaypoint);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showGeocahceInfo(Geocache geocache) {
        geocacheName.setText(geocache.getName());
        geocacheSize.setText(String.format(getString(R.string.info_size), geocache.getSize()));
        geocacheOwner.setText(String.format(getString(R.string.info_owner), geocache.getOwner().getUsername()));
        geocacheFound.setText(String.format(getString(R.string.info_found), geocache.getFounds()));
        geocacheNotFound.setText(String.format(getString(R.string.info_not_found), geocache.getNotFounds()));
        geocacheRecommendation.setText(String.format(getString(R.string.info_recommended), geocache.getRecommendations()));
        if ((int) geocache.getRating() > 0) {
            String[] rates = activity.getResources().getStringArray(R.array.geocache_rates);
            geocacheRate.setText(String.format(activity.getString(R.string.info_rating), rates[(int) geocache.getRating() - 1]));
        } else {
            geocacheRate.setVisibility(View.GONE);
        }

        geocacheBottomSheet.setOnClickListener(view -> startGeocacheActivity(geocache.getCode()));

        //sheetBehavior.setPeekHeight(geocacheInfoHeader.getHeight());
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private void setGeocacheBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(geocacheBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    private void hideGeocacheInfo(){
        setLastSelectedMarkerIcon();
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSearchMap(SearchMapEvent event) {
        presenter.getLocation(event.getQuerry());
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void showMapInfo(int message) {
        hideProgress();
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
    public void moveMapCamera(LatLng latLng, float zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
    }

    @Override
    public void clusterGeocaches(ArrayList<Geocache> geocaches) {
        mClusterManager.addItems(geocaches);
        mClusterManager.cluster();
        hideProgress();
    }

    @Override
    public void showToast(int message) {
        showMapInfo(message);
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupActionBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class CustomClusterRenderer extends DefaultClusterRenderer<Geocache> {

        CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<Geocache> clusterManager) {
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

    private class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            if (marker.getSnippet() == null)
                return null;
            View view = LayoutInflater.from(activity).inflate(R.layout.item_geocache_marker_window, null);
            Geocache geocache = presenter.getGeocache(marker);
            TextView nameTextView = view.findViewById(R.id.geocacheName);
            TextView codeTextView = view.findViewById(R.id.code);
            TextView sizeTextView = view.findViewById(R.id.geocacheSize);
            TextView rateTextView = view.findViewById(R.id.geocacheRate);
            TextView ownerTextView = view.findViewById(R.id.geocacheOwner);
            TextView foundsTextView = view.findViewById(R.id.geocacheFound);
            TextView notFoundsTextView = view.findViewById(R.id.geocacheNotFound);
            TextView recommendationsTextView = view.findViewById(R.id.geocacheRecommendation);

            nameTextView.setText(geocache.getName());
            codeTextView.setText(geocache.getCode());
            sizeTextView.setText(activity.getString(R.string.info_size) + " " + activity.getString(getGeocacheSize(geocache.getSize())));
            ownerTextView.setText(activity.getString(R.string.info_owner) + " " + geocache.getOwner().getUsername());
            String[] rates = activity.getResources().getStringArray(R.array.geocache_rates);
            if ((int) geocache.getRating() > 0)
                rateTextView.setText(activity.getString(R.string.info_rating) + " " + rates[(int) geocache.getRating() - 1]);
            else
                rateTextView.setVisibility(View.GONE);

            foundsTextView.setText(geocache.getFounds() + " x " + activity.getString(R.string.found));
            notFoundsTextView.setText(geocache.getNotFounds() + " x " + activity.getString(R.string.not_found));

            if (geocache.getRecommendations() == 0) {
                ImageView recommendationImageView = view.findViewById(R.id.recommendationImageView);
                recommendationImageView.setVisibility(View.GONE);
                recommendationsTextView.setVisibility(View.GONE);
            } else {
                recommendationsTextView.setText(geocache.getRecommendations() + " x " + activity.getString(R.string.recommended));
            }

            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

}
