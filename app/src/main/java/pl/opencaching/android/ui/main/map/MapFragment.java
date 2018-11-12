package pl.opencaching.android.ui.main.map;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import pl.opencaching.android.R;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.data.models.GeocacheClusterItem;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BaseFragment;
import pl.opencaching.android.ui.dialogs.MapFilterDialog;
import pl.opencaching.android.ui.dialogs.MapTypeDialog;
import pl.opencaching.android.ui.geocache.GeocacheActivity;
import pl.opencaching.android.ui.main.MainActivity;
import pl.opencaching.android.utils.events.MapFilterChangeEvent;
import pl.opencaching.android.utils.events.MapTypeChangeEvent;
import pl.opencaching.android.utils.events.SearchMapEvent;
import pl.opencaching.android.data.models.okapi.Geocache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.RealmResults;
import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.IntegerUtils;
import pl.opencaching.android.utils.StringUtils;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

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
    @BindView(R.id.navigateFloatingButton)
    LinearLayout navigateButton;
    @BindView(R.id.notFoundButton)
    FloatingActionButton notFoundButton;
    @BindView(R.id.foundButton)
    FloatingActionButton foundButton;

    @BindView(R.id.geocacheTopLabel)
    TextView geocacheTopLabel;
    @BindView(R.id.geocacheName)
    TextView geocacheName;
    @BindView(R.id.geocacheType)
    TextView geocacheType;
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
    @BindView(R.id.geocacheLastFound)
    TextView geocacheLastFound;
    @BindView(R.id.difficulty)
    RatingBar difficulty;
    @BindView(R.id.terrain)
    RatingBar terrain;

    @Inject
    MapContract.Presenter presenter;
    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    SessionManager sessionManager;

    private MainActivity activity;
    private GoogleMap mMap;
    private ClusterManager<GeocacheClusterItem> mClusterManager;
    private Marker lastSelectedMarker;
    private boolean isMapInfoShown;
    private boolean isGeocacheInfoShown;
    private BottomSheetBehavior sheetBehavior;
    private Animation fabOpen, fabClose;
    private View locationButton;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, null);
        unbinder = ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        mapFragment.getMapAsync(this);
        activity = (MainActivity) requireActivity();
        activity.setActionBarTitle(R.string.app_name);
        setPresenter(presenter);
        setGeocacheBottomSheet();
        setAnimations();
        presenter.getUserData();
        setHasOptionsMenu(true);
        return view;
    }

    private void setupActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                MapFilterDialog filterDialog = MapFilterDialog.newInstance();
                filterDialog.show(activity.getSupportFragmentManager(), MapFilterDialog.class.getName());
                return false;
            case R.id.action_save:
                return false;
            case R.id.action_list_view:
                return false;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(sessionManager.getMapType());
        if (sessionManager.getUserHomeLocation() != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sessionManager.getUserHomeLocation(), 10));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(START_MAP_LATITUDE, START_MAP_LONGITUDE), START_MAP_ZOOM));
        }
        configureMap();
        presenter.refreshMap(false);
    }

    private void configureMap() {
        setupLocationListener();

        //MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.retro_map);
        //mMap.setMapStyle(style);

        mClusterManager = new ClusterManager<>(activity, mMap);
        final CustomClusterRenderer renderer = new CustomClusterRenderer(activity.getApplicationContext(), mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        mClusterManager.setOnClusterClickListener(
                cluster -> {
                    hideGeocacheInfo();
                    activity.hideSearchView();
                    moveMapCamera(cluster.getPosition(), mMap.getCameraPosition().zoom + 1, 500);
                    return true;
                });
        mClusterManager.setOnClusterItemClickListener(
                clusterItem -> {
                    Marker marker = renderer.getMarker(clusterItem);
                    if (marker.getSnippet() != null) {
                        setLastSelectedMarkerIcon();
                        lastSelectedMarker = marker;
                        marker.setIcon(BitmapDescriptorFactory.fromResource(GeocacheUtils.getGeocacheSelectedIcon(geocacheRepository.loadGeocacheByCode(marker.getSnippet()).getType())));
                        showGeocahceInfo(geocacheRepository.loadGeocacheByCode(marker.getSnippet()));
                        moveMapCamera(marker.getPosition(), mMap.getCameraPosition().zoom, 500);
                    }
                    activity.hideSearchView();
                    return true;
                });

        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setOnMapClickListener(latLng -> {
            activity.hideSearchView();
            hideGeocacheInfo();
        });

        // Classic marker window
        //mMap.setInfoWindowAdapter(new CustomInfoViewAdapter());
        //mMap.setOnInfoWindowClickListener(marker -> startGeocacheActivity(marker.getSnippet()));

        mMap.setOnCameraIdleListener(this::refreshRegion);
    }

    private void setupLocationListener() {

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            mMap.setMyLocationEnabled(true);
            if (locationButton != null) {
                locationButton.setVisibility(View.GONE);
            }

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                    if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        if (locationButton != null) {
                            locationButton.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onProviderDisabled(String provider) {
                    if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(false);
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        }

    }

    @Override
    public void refreshRegion() {
        if(lastSelectedMarker != null && !isVisibleOnMap(lastSelectedMarker.getPosition())) {
            hideGeocacheInfo();
        }
        mClusterManager.cluster();
        CameraPosition cameraPosition = mMap.getCameraPosition();
        hideMapInfo();
        if (mMap.getCameraPosition().zoom >= MINIMUM_REQUEST_ZOOM) {
            LatLng center = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
            int radius = IntegerUtils.getDistance(mMap.getProjection().fromScreenLocation(new Point(0, 0)), center) / 1000;
            presenter.downloadGeocaches(center, radius);
        } else {
            showMapInfo(R.string.zoom_map_to_download);
        }
    }

    private boolean isVisibleOnMap(LatLng latLng) {
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        return vr.latLngBounds.contains(latLng);
    }

    private void setLastSelectedMarkerIcon() {
        try {
            lastSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(GeocacheUtils.getGeocacheIcon(geocacheRepository.loadGeocacheByCode(lastSelectedMarker.getSnippet()).getType())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGeocacheActivity(Geocache geocache) {
        Intent intent = new Intent(activity, GeocacheActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(GeocacheActivity.GEOCACHE, geocache);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showGeocahceInfo(Geocache geocache) {
        setGeocacheInfoView(geocache);

        geocacheBottomSheet.setOnClickListener(view -> startGeocacheActivity(geocache));

        geocacheBottomSheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                geocacheType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                sheetBehavior.setPeekHeight(geocacheType.getTop());
            }
        });

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void setGeocacheInfoView(Geocache geocache) {
        geocacheName.setText(geocache.getName());
        geocacheTopLabel.setText(String.format(getString(R.string.separated_strings), geocache.getCode(), StringUtils.getFormatedCoordinates(geocache.getPosition())));

        geocacheFound.setText(String.valueOf(geocache.getFounds()));
        geocacheNotFound.setText(String.valueOf(geocache.getNotFounds()));
        geocacheRecommendation.setText(String.valueOf(geocache.getRecommendations()));

        geocacheSize.setText(getFormatedGeocacheInfoLabel(R.string.info_size, getString(GeocacheUtils.getGeocacheSize(geocache.getSize()))));
        geocacheOwner.setText(getFormatedGeocacheInfoLabel(R.string.info_owner, geocache.getOwner().getUsername()));
        geocacheType.setText(getFormatedGeocacheInfoLabel(R.string.info_type, getString(GeocacheUtils.getGeocacheType(geocache.getType()))));
        if (geocache.getLastFound() == null) {
            geocacheLastFound.setText(getFormatedGeocacheInfoLabel(R.string.info_empty, getString(R.string.waiting_for_ftf)));
        } else {
            geocacheLastFound.setText(getFormatedGeocacheInfoLabel(R.string.info_last_found, StringUtils.getDateString(geocache.getLastFound(), getContext())));
        }

        difficulty.setRating(geocache.getDifficulty());
        terrain.setRating(geocache.getTerrain());
        if ((int) geocache.getRating() > 0) {
            String[] rates = activity.getResources().getStringArray(R.array.geocache_rates);
            geocacheRate.setText(getFormatedGeocacheInfoLabel(R.string.info_rating, rates[(int) geocache.getRating() - 1]));
        } else {
            geocacheRate.setVisibility(View.GONE);
        }
    }

    private SpannableStringBuilder getFormatedGeocacheInfoLabel(int res, String name) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(String.format(getString(res), name));
        spannableString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), spannableString.length() - name.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void setGeocacheBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(geocacheBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        navigateButton.setScaleX(0);
        navigateButton.setScaleY(0);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    setLastSelectedMarkerIcon();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset < 0) {
                    if (isGeocacheInfoShown) {
                        foundButton.startAnimation(fabClose);
                        notFoundButton.startAnimation(fabClose);
                        foundButton.setClickable(false);
                        notFoundButton.setClickable(false);
                        isGeocacheInfoShown = false;
                    }
                    navigateButton.setScaleX(slideOffset + 1);
                    navigateButton.setScaleY(slideOffset + 1);
                } else {
                    if (!isGeocacheInfoShown) {
                        foundButton.startAnimation(fabOpen);
                        notFoundButton.startAnimation(fabOpen);
                        foundButton.setClickable(true);
                        notFoundButton.setClickable(true);
                        isGeocacheInfoShown = true;
                    }
                }
            }
        });

    }

    private void checkFineLocationPermission() {
        new TedPermission(requireActivity())
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .setDeniedMessage(R.string.permission_denied_message_fine_location)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        setupLocationListener();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .check();
    }

    @OnClick(R.id.myLocationButton)
    public void onMyLocationClick() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkFineLocationPermission();
        } else {
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationButton.callOnClick();
            }else{
                // TODO: showGPSDisabledAlertToUser();
            }

        }
    }

    @OnClick(R.id.layersButton)
    public void onLayersClick() {
        MapTypeDialog mapTypeDialog = MapTypeDialog.newInstance();
        mapTypeDialog.show(activity.getSupportFragmentManager(), MapTypeDialog.class.getName());
    }

    @OnClick(R.id.notFoundButton)
    public void onNotFoundClick() {

    }

    @OnClick(R.id.foundButton)
    public void onFoundClick() {

    }

    @OnClick(R.id.navigateFloatingButton)
    public void onNavigateClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lastSelectedMarker.getPosition().latitude + "," + lastSelectedMarker.getPosition().longitude));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setAnimations() {
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
    }

    @Override
    public void hideGeocacheInfo() {
        setLastSelectedMarkerIcon();
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void clearMap() {
        hideGeocacheInfo();
        mClusterManager.clearItems();
    }

    @Override
    public void showMapInfo(int message) {
        mapInfoMessage.setText(activity.getString(message));
        if (!isMapInfoShown) {
            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(requireActivity(), R.animator.slide_in_up);
            set.setTarget(mapInfoMessage);
            set.start();
        }
        isMapInfoShown = true;
    }

    @Override
    public void hideMapInfo() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(requireActivity(), R.animator.slide_out_up);
        set.setTarget(mapInfoMessage);
        set.start();
        isMapInfoShown = false;
    }

    @Override
    public void moveMapCamera(LatLng latLng, float zoom, int duration) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration, null);
    }

    @Override
    public void addGeocaches(RealmResults<Geocache> geocaches) {
        ArrayList<GeocacheClusterItem> clusterItems = new ArrayList<>();
        for(Geocache geocache : geocaches) {
            clusterItems.add(geocache.getClusterItem());
        }
        mClusterManager.addItems(clusterItems);
        mClusterManager.cluster();
    }

    @Override
    public void removeGeocaches(RealmResults<Geocache> geocaches) {
        for(GeocacheClusterItem geocacheClusterItem : mClusterManager.getAlgorithm().getItems()) {
            for(Geocache geocache : geocaches) {
                if(geocacheClusterItem.getSnippet().equals(geocache.getCode())){
                    mClusterManager.removeItem(geocacheClusterItem);
                }
            }
        }
        mClusterManager.cluster();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSearchMap(SearchMapEvent event) {
        presenter.getLocation(event.getQuerry());
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMapFilter(MapFilterChangeEvent event) {
        presenter.refreshMap(event.isAvailabilityChanged());
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMapType(MapTypeChangeEvent event) {
        switch (event.getMapType()) {
            case MAP_TYPE_NORMAL:
                mMap.setMapType(MAP_TYPE_NORMAL);
                break;
            case MAP_TYPE_SATELLITE:
                mMap.setMapType(MAP_TYPE_SATELLITE);
                break;
            case MAP_TYPE_TERRAIN:
                mMap.setMapType(MAP_TYPE_TERRAIN);
                break;

        }


        EventBus.getDefault().removeStickyEvent(event);
    }

    private class CustomClusterRenderer extends DefaultClusterRenderer<GeocacheClusterItem> {

        CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<GeocacheClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onClusterRendered(Cluster<GeocacheClusterItem> cluster, Marker marker) {
            for(GeocacheClusterItem geocacheClusterItem : cluster.getItems()) {
                if(lastSelectedMarker != null && geocacheClusterItem.getSnippet().equals(lastSelectedMarker.getSnippet())) {
                    hideGeocacheInfo();
                    break;
                }
            }
            super.onClusterRendered(cluster, marker);
        }

        @Override
        protected void onBeforeClusterItemRendered(GeocacheClusterItem geocache, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(GeocacheUtils.getGeocacheIcon(geocache.getType())));
            markerOptions.snippet(geocache.getSnippet());
            markerOptions.title(geocache.getTitle());
            markerOptions.anchor(0.5f, 0.5f);

            super.onBeforeClusterItemRendered(geocache, markerOptions);
        }
    }

//    private class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {
//        @Override
//        public View getInfoWindow(Marker marker) {
//            if (marker.getSnippet() == null)
//                return null;
//            View view = LayoutInflater.from(activity).inflate(R.layout.item_geocache_marker_window, null);
//            Geocache geocache = geocacheRepository.loadGeocacheByCode(marker.getSnippet());
//            TextView nameTextView = view.findViewById(R.id.geocacheName);
//            TextView codeTextView = view.findViewById(R.id.code);
//            TextView sizeTextView = view.findViewById(R.id.geocacheSize);
//            TextView rateTextView = view.findViewById(R.id.geocacheRate);
//            TextView ownerTextView = view.findViewById(R.id.geocacheOwner);
//            TextView foundsTextView = view.findViewById(R.id.geocacheFound);
//            TextView notFoundsTextView = view.findViewById(R.id.geocacheNotFound);
//            TextView recommendationsTextView = view.findViewById(R.id.geocacheRecommendation);
//
//            nameTextView.setText(geocache.getName());
//            codeTextView.setText(geocache.getCode());
//            sizeTextView.setText(activity.getString(R.string.info_size) + " " + activity.getString(getGeocacheSize(geocache.getSize())));
//            ownerTextView.setText(activity.getString(R.string.info_owner) + " " + geocache.getOwner().getUsername());
//            String[] rates = activity.getResources().getStringArray(R.array.geocache_rates);
//            if ((int) geocache.getRating() > 0)
//                rateTextView.setText(activity.getString(R.string.info_rating) + " " + rates[(int) geocache.getRating() - 1]);
//            else
//                rateTextView.setVisibility(View.GONE);
//
//            foundsTextView.setText(geocache.getFounds() + " x " + activity.getString(R.string.found));
//            notFoundsTextView.setText(geocache.getNotFounds() + " x " + activity.getString(R.string.not_found));
//
//            if (geocache.getRecommendations() == 0) {
//                ImageView recommendationImageView = view.findViewById(R.id.recommendationImageView);
//                recommendationImageView.setVisibility(View.GONE);
//                recommendationsTextView.setVisibility(View.GONE);
//            } else {
//                recommendationsTextView.setText(geocache.getRecommendations() + " x " + activity.getString(R.string.recommended));
//            }
//
//            return view;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//    }

}
