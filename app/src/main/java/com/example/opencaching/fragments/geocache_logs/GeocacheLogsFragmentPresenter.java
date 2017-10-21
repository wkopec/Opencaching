package com.example.opencaching.fragments.geocache_logs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.opencaching.R;
import com.example.opencaching.interfaces.Presenter;
import com.example.opencaching.models.okapi.GeocacheLog;
import com.example.opencaching.retrofit.OpencachingApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.ApiUtils.checkForErrors;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheLogsFragmentPresenter implements Presenter {

    private final static String LOGS_STANDARD_FIELDS = "uuid|date|user|type|comment|images|was_recommended";

    private GeocacheLogsFragmentView view;
    private Context context;

    public GeocacheLogsFragmentPresenter(GeocacheLogsFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGeocacheLogs(String code) {
        Call<ArrayList<GeocacheLog>> loginCall = OpencachingApi.service().getGeocacheLogs(context.getResources().getString(R.string.opencaching_key), code, LOGS_STANDARD_FIELDS, 0, 100);
        loginCall.enqueue(new Callback<ArrayList<GeocacheLog>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GeocacheLog>> call, @NonNull Response<ArrayList<GeocacheLog>> response) {
                android.util.Log.d("Retrofit response code", String.valueOf(response.code()));
                ArrayList<GeocacheLog> geocacheLogs = response.body();
                switch (response.code()) {
                    case 200:
                        if (geocacheLogs != null)
                            view.setLogs(geocacheLogs);
                        else {
                            view.hideProgress();
                        }
                        break;
                    default:
                        view.hideProgress();
                }
                checkForErrors(response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<GeocacheLog>> call, @NonNull Throwable t) {
                if (t.getMessage() != null)
                    android.util.Log.d("Retrofit fail", t.getMessage());
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
