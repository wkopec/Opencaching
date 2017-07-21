package com.example.opencaching.fragments.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.opencaching.R;
import com.example.opencaching.interfaces.Presenter;
import com.example.opencaching.models.Geocache;
import com.example.opencaching.models.Results;

import com.example.opencaching.retrofit.OpencachingApi;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.ApiUtils.checkForErrors;
import static com.example.opencaching.utils.Constants.GEOCACHES_STANDARD_FIELDS;
import static com.example.opencaching.utils.StringUtils.getApiFormatedFields;

/**
 * Created by Volfram on 15.07.2017.
 */

public class MapFragmentPresenter implements Presenter {

    private MapFragmentView view;
    private Context context;

    public MapFragmentPresenter(MapFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getWaypoints(String center) {
        Call<Results> loginCall = OpencachingApi.service().getWaypoints(context.getResources().getString(R.string.opencaching_key), center);
        view.showProgress();
        loginCall.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(@NonNull Call<Results> call, @NonNull Response<Results> response) {
                Log.d("Retrofit response code", String.valueOf(response.code()));
                Results waypoints = response.body();
                switch (response.code()) {
                    case 200:
                        if (waypoints != null && !waypoints.getResults().isEmpty()) {
                            Log.d("Test", waypoints.getResults().toString());
                            getGeocaches(getApiFormatedFields(waypoints.getResults()), GEOCACHES_STANDARD_FIELDS);
                        }
                        break;
                    default:
                }
                checkForErrors(response.errorBody());
                view.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<Results> call, @NonNull Throwable t) {
                if(t.getMessage() != null)
                    Log.d("Retrofit fail", t.getMessage());
                view.hideProgress();
            }
        });
    }

    private void getGeocaches(String codes, String fields) {
        Call<Map<String, Geocache>> loginCall = OpencachingApi.service().getGeocaches(context.getResources().getString(R.string.opencaching_key), codes, fields);
        view.showProgress();
        loginCall.enqueue(new Callback<Map<String, Geocache>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Geocache>> call, @NonNull Response<Map<String, Geocache>> response) {
                Log.d("Retrofit response code", String.valueOf(response.code()));
                Map<String, Geocache> geocaches = response.body();
                switch (response.code()) {
                    case 200:
                        if (geocaches != null) {
                            view.showGeocaches(geocaches);
                        }

                        break;
                    default:
                }
                checkForErrors(response.errorBody());
                view.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Geocache>> call, @NonNull Throwable t) {
                if(t.getMessage() != null)
                    Log.d("Retrofit fail", t.getMessage());
                view.hideProgress();
            }
        });
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}