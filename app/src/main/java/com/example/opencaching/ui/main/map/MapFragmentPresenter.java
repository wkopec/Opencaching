package com.example.opencaching.ui.main.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.opencaching.R;
import com.example.opencaching.network.models.okapi.User;
import com.example.opencaching.ui.base.BasePresenter;
import com.example.opencaching.network.models.CoveredArea;
import com.example.opencaching.network.models.geocoding.GeocodingResults;
import com.example.opencaching.network.models.geocoding.Location;
import com.example.opencaching.network.models.okapi.Geocache;
import com.example.opencaching.network.models.okapi.WaypointResults;

import com.example.opencaching.network.api.OpencachingApi;
import com.example.opencaching.utils.ApiUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.network.api.GoogleMapsApi.service;
import static com.example.opencaching.utils.Constants.GEOCACHES_STANDARD_FIELDS;
import static com.example.opencaching.utils.IntegerUtils.getDistance;
import static com.example.opencaching.utils.StringUtils.getApiFormatedFields;
import static com.example.opencaching.utils.SyncUtils.isInternetConnection;
import static com.example.opencaching.utils.UserUtils.getUserHomeLocation;
import static com.example.opencaching.utils.UserUtils.setUserHomeLocation;

/**
 * Created by Volfram on 15.07.2017.
 */

public class MapFragmentPresenter extends BasePresenter implements MapContract.Presenter {

    private static final float DEFAULT_LOCATION_ZOOM = 12;
    private static final int GEOCACHE_REQUEST_LIMIT = 500;      //max 500
    private static final int MINIMUM_REQUEST_RADIUS = 10;      //in km

    private MapContract.View view;
    private Context context;
    private LatLng center;
    private Map<String, Geocache> displayedGeocaches;
    private CoveredArea coveredArea;
    private boolean isActive;

    public MapFragmentPresenter(MapContract.View view, Context context) {
        this.view = view;
        this.context = context;
        displayedGeocaches = new HashMap<>();
        coveredArea = new CoveredArea();
    }

    @Override
    public void downloadGeocaches(LatLng center, int radius) {
        if (!coveredArea.isWithin(center) && !isActive) {
            if (radius < MINIMUM_REQUEST_RADIUS)
                radius = MINIMUM_REQUEST_RADIUS;
            getWaypoints(center, radius);
        }
    }

    private void getWaypoints(final LatLng center, int radius) {
        view.showProgress();
        isActive = true;
        this.center = center;
        String centerString = center.latitude + "|" + center.longitude;
        Call<WaypointResults> loginCall = OpencachingApi.service().getWaypoints(context.getResources().getString(R.string.opencaching_key), centerString, GEOCACHE_REQUEST_LIMIT, radius);
        loginCall.enqueue(new Callback<WaypointResults>() {
            @Override
            public void onResponse(@NonNull Call<WaypointResults> call, @NonNull Response<WaypointResults> response) {
                if (response.body() != null) {
                    WaypointResults waypoints = response.body();
                    if (!waypoints.getResults().isEmpty())
                        getGeocaches(getApiFormatedFields(waypoints.getResults()), waypoints.isMore());
                    else {
                        isActive = false;
                        view.showMapInfo(R.string.no_cache_results);
                        view.hideProgress();
                    }
                } else {
                    if (response.errorBody() != null) {
                        view.showError(ApiUtils.getErrorSingle(response.errorBody()));
                        view.hideProgress();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WaypointResults> call, @NonNull Throwable t) {
                isActive = false;
                view.hideProgress();
                view.showError(ApiUtils.getErrorSingle(t));
            }
        });
    }

    private void getGeocaches(String codes, final boolean isMore) {
        Call<Map<String, Geocache>> loginCall = OpencachingApi.service().getGeocaches(context.getResources().getString(R.string.opencaching_key), codes, GEOCACHES_STANDARD_FIELDS);
        loginCall.enqueue(new Callback<Map<String, Geocache>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Geocache>> call, @NonNull Response<Map<String, Geocache>> response) {
                if (response.body() != null) {
                    Map<String, Geocache> geocaches = response.body();
                    if (geocaches != null)
                        addGeocaches(geocaches, isMore);
                    else {
                        view.hideProgress();
                        isActive = false;
                    }
                } else {
                    if (response.errorBody() != null) {
                        view.showError(ApiUtils.getErrorSingle(response.errorBody()));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Geocache>> call, @NonNull Throwable t) {
                isActive = false;
                view.hideProgress();
                view.showError(ApiUtils.getErrorSingle(t));
            }
        });
    }

    private void addGeocaches(Map<String, Geocache> geocaches, boolean isMore) {
        Map<String, Geocache> newGeocaches = new HashMap<>();
        ArrayList<Geocache> newGeocachesArray = new ArrayList<>();
        Iterator iterator = geocaches.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Geocache geocache = (Geocache) pair.getValue();
            if (displayedGeocaches.containsKey(geocache.getCode()))
                continue;
            newGeocaches.put(geocache.getCode(), geocache);
            newGeocachesArray.add(geocache);
            if (!iterator.hasNext())
                coveredArea.addArea(center, getDistance(geocache.getPosition(), center));

            iterator.remove();
        }
        displayedGeocaches.putAll(newGeocaches);
        view.clusterGeocaches(newGeocachesArray);
        if (isMore) {
            view.showMapInfo(R.string.move_map_to_show_more_geocaches);
        } else {
            view.hideMapInfo();
        }
        isActive = false;
        view.hideProgress();
    }

    @Override
    public void getLocation(String address) {
        view.showProgress();
        Call<GeocodingResults> call = service().getLoation(address, context.getString(R.string.google_geocoding_key));
        call.enqueue(new Callback<GeocodingResults>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResults> call, @NonNull Response<GeocodingResults> response) {
                GeocodingResults geocodingResponse = response.body();
                if (geocodingResponse != null) {
                    if (!geocodingResponse.getResults().isEmpty()) {
                        Location location = geocodingResponse.getResults().get(0).getGeometry().getLocation();
                        view.moveMapCamera(new LatLng(location.getLat(), location.getLng()), DEFAULT_LOCATION_ZOOM, 1000);
                        view.hideGeocacheInfo();
                        view.hideProgress();
                    } else {
                        view.showMapInfo(R.string.no_matches_for_location_querry);
                    }
                } else {
                    view.showMapInfo(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResults> call, @NonNull Throwable t) {
                view.showError(ApiUtils.getErrorSingle(t));
                view.hideProgress();
                if (t.getMessage() != null)
                    Log.d("Retrofit fail", t.getMessage());
            }
        });
    }

    @Override
    public void getUserData() {
        Call<User> userCall = OpencachingApi.service(context).getLoggedInUserInfo("home_location");
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                LatLng userHomeLocation = getUserHomeLocation(context);
                if (user != null && (userHomeLocation == null || !userHomeLocation.equals(user.getHomeLocation()))) {
                    setUserHomeLocation(context, user.getRawHomeLocation());
                    view.moveMapCamera(user.getHomeLocation(), 10, 1000);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t.getMessage() != null)
                    Log.d("Retrofit fail", t.getMessage());
            }
        });
    }

    @Override
    public Geocache getGeocache(Marker marker) {
        return displayedGeocaches.get(marker.getSnippet());
    }

}