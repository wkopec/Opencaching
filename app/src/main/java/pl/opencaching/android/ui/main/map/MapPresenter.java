package pl.opencaching.android.ui.main.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.realm.Realm;
import pl.opencaching.android.R;
import pl.opencaching.android.api.GoogleMapsService;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.app.prefs.MapFiltersManager;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.data.models.okapi.User;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BasePresenter;
import pl.opencaching.android.data.models.CoveredArea;
import pl.opencaching.android.data.models.geocoding.GeocodingResults;
import pl.opencaching.android.data.models.geocoding.Location;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.WaypointResults;

import pl.opencaching.android.utils.ApiUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import io.realm.RealmResults;
import pl.opencaching.android.utils.Constants;
import pl.opencaching.android.utils.IntegerUtils;
import pl.opencaching.android.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.opencaching.android.utils.Constants.GEOCACHES_STANDARD_FIELDS;
import static pl.opencaching.android.utils.Constants.LOGS_STANDARD_FIELDS;

/**
 * Created by Volfram on 15.07.2017.
 */

public class MapPresenter extends BasePresenter implements MapContract.Presenter {

    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    SessionManager sessionManager;
    @Inject
    OkapiService okapiService;
    @Inject
    GoogleMapsService googleMapsService;

    private static final float DEFAULT_LOCATION_ZOOM = 12;
    private static final int GEOCACHE_REQUEST_LIMIT = 500;      //max 500
    private static final int MINIMUM_REQUEST_RADIUS = 10;      //in km

    private MapContract.View view;
    private Context context;
    private LatLng center;
    private ArrayList<String> showedGeocachesCodes;
    private CoveredArea coveredArea;
    private boolean isActive;

    private MapFiltersManager mapFiltersManager;

    @Inject
    public MapPresenter(MapContract.View view, Context context, MapFiltersManager mapFiltersManager) {
        this.view = view;
        this.context = context;
        this.mapFiltersManager = mapFiltersManager;
        showedGeocachesCodes = new ArrayList<>();
        coveredArea = new CoveredArea();
    }

    @Override
    public void downloadGeocaches(LatLng center, int radius) {
        if (!coveredArea.isWithin(center) && !isActive) {
            if (radius < MINIMUM_REQUEST_RADIUS) {
                radius = MINIMUM_REQUEST_RADIUS;
            }
            getWaypoints(center, radius);
        }
    }

    private void getWaypoints(final LatLng center, int radius) {
        view.showProgress();
        isActive = true;
        this.center = center;
        String centerString = center.latitude + "|" + center.longitude;
        Call<WaypointResults> loginCall = okapiService.getWaypoints(centerString, GEOCACHE_REQUEST_LIMIT, radius, getSelectedStatus());
        loginCall.enqueue(new Callback<WaypointResults>() {
            @Override
            public void onResponse(@NonNull Call<WaypointResults> call, @NonNull Response<WaypointResults> response) {
                if (response.body() != null) {
                    WaypointResults waypoints = response.body();
                    if (!waypoints.getResults().isEmpty())
                        getGeocaches(StringUtils.getApiFormatedFields(waypoints.getResults()), waypoints.isMore());
                    else {
                        isActive = false;
                        view.showMapInfo(R.string.no_cache_results);
                        view.hideProgress();
                    }
                } else {
                    if (response.errorBody() != null) {
                        view.showMapInfo(ApiUtils.getErrorSingle(response.errorBody()).getMessage());
                        view.hideProgress();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WaypointResults> call, @NonNull Throwable t) {
                isActive = false;
                view.hideProgress();
                view.showMapInfo(ApiUtils.getErrorSingle(t).getMessage());
            }
        });
    }

    private void getGeocaches(String codes, final boolean isMore) {
        Call<Map<String, Geocache>> loginCall = okapiService.getGeocaches(codes, GEOCACHES_STANDARD_FIELDS, LOGS_STANDARD_FIELDS);
        loginCall.enqueue(new Callback<Map<String, Geocache>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Geocache>> call, @NonNull Response<Map<String, Geocache>> response) {
                if (response.body() != null) {
                    Map<String, Geocache> geocaches = response.body();
                    storeAndShowGeocaches(geocaches, Completable.fromAction(() -> {
                        if (isMore) {
                            view.showMapInfo(R.string.move_map_to_show_more_geocaches);
                        } else {
                            view.hideMapInfo();
                        }
                        view.hideProgress();
                    }));
                    isActive = false;
                } else if (response.errorBody() != null) {
                    view.showMapInfo(ApiUtils.getErrorSingle(response.errorBody()).getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Geocache>> call, @NonNull Throwable t) {
                isActive = false;
                view.hideProgress();
                view.showMapInfo(ApiUtils.getErrorSingle(t).getMessage());
            }
        });
    }

    private void storeAndShowGeocaches(Map<String, Geocache> geocaches, Completable completable) {
        ArrayList<Geocache> downloadedGeocachesArray = new ArrayList<>();
        ArrayList<String> newGeocachesCodes = new ArrayList<>();

        Iterator iterator = geocaches.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Geocache geocache = (Geocache) pair.getValue();
            if(geocache.isPasswordRequired()) {
                geocache.getAttributeCodes().add("A999");
            }
            //geocache.setApiRequestCounter();
            downloadedGeocachesArray.add(geocache);
            if (!iterator.hasNext()) {
                coveredArea.addArea(center, IntegerUtils.getDistance(geocache.getPosition(), center));
            }
            if (showedGeocachesCodes.contains(geocache.getCode())) {
                continue;
            }
            newGeocachesCodes.add(geocache.getCode());

            iterator.remove();
        }
        geocacheRepository.addOrUpdate(downloadedGeocachesArray, () -> {
            showedGeocachesCodes.addAll(newGeocachesCodes);
            view.addGeocaches(geocacheRepository.loadMapFilteredGeocachesIncludes(newGeocachesCodes.toArray(new String[]{})));
            completable.subscribe();
        });
    }

    @Override
    public void getLocation(String address) {
        view.showProgress();
        Call<GeocodingResults> call = googleMapsService.getLoation(address, context.getString(R.string.google_geocoding_key));
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
                view.showMapInfo(ApiUtils.getErrorSingle(t).getMessage());
                view.hideProgress();
                if (t.getMessage() != null) {
                    Log.d("Retrofit fail", t.getMessage());
                }
            }
        });
    }

    @Override
    public void getUserData() {
        Call<User> userCall = okapiService.getLoggedInUserInfo(Constants.USERNAME_FIELDS);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user != null) {
                    LatLng userHomeLocation = sessionManager.getUserHomeLocation();
                    if ((userHomeLocation == null || !userHomeLocation.equals(user.getHomeLocation()))) {
                        view.moveMapCamera(user.getHomeLocation(), 10, 1000);
                    }
                    sessionManager.setLoggedUserData(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t.getMessage() != null) {
                    Log.d("Retrofit fail", t.getMessage());
                }
            }
        });
    }

    @Override
    public void refreshMap(boolean isClearGeocaches) {
        if (isClearGeocaches) {
            clearStoredGeocaches();
            view.refreshRegion();
        } else {
            view.clearMap();
            RealmResults<Geocache> geocaches = geocacheRepository.loadMapFilteredGeocaches();
            showedGeocachesCodes = getGeocachesCodes(geocaches);
            view.addGeocaches(geocaches);
        }
    }

    public void clearStoredGeocaches() {
        coveredArea.clear();
        showedGeocachesCodes.clear();
        geocacheRepository.clearUnsavedGeocaches();
        view.clearMap();
    }

    private ArrayList<String> getGeocachesCodes(RealmResults<Geocache> geocaches) {
        ArrayList<String> geocachesCodes = new ArrayList<>();
        for(Geocache geocache : geocaches) {
            geocachesCodes.add(geocache.getCode());
        }
        return geocachesCodes;
    }

    private String getSelectedStatus() {
        StringBuilder status = new StringBuilder();
        if (mapFiltersManager.isAvailableFilter()) {
            status.append("Available");
        }
        if (mapFiltersManager.isTempUnavailableFilter()) {
            if (status.length() != 0) {
                status.append("|");
            }
            status.append("Temporarily unavailable");
        }
        if (mapFiltersManager.isArchivedFilter()) {
            if (status.length() != 0) {
                status.append("|");
            }
            status.append("Archived");
        }

        return status.toString();
    }

}