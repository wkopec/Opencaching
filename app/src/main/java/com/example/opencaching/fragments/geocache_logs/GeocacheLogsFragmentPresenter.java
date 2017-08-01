package com.example.opencaching.fragments.geocache_logs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.opencaching.R;
import com.example.opencaching.interfaces.Presenter;
import com.example.opencaching.models.okapi.Geocache;
import com.example.opencaching.models.okapi.Log;
import com.example.opencaching.retrofit.OpencachingApi;
import com.example.opencaching.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.ApiUtils.checkForErrors;
import static com.example.opencaching.utils.Constants.GEOCACHES_STANDARD_FIELDS;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheLogsFragmentPresenter implements Presenter {

    private final static String LOGS_STANDARD_FIELDS = "";

    private GeocacheLogsFragmentView view;
    private Context context;

    public GeocacheLogsFragmentPresenter(GeocacheLogsFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGeocacheLogs(String code) {
        Call<ArrayList<Log>> loginCall = OpencachingApi.service().getGeocacheLogs(context.getResources().getString(R.string.opencaching_key), code, LOGS_STANDARD_FIELDS, 0, 100);
        loginCall.enqueue(new Callback<ArrayList<Log>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Log>> call, @NonNull Response<ArrayList<Log>> response) {
                android.util.Log.d("Retrofit response code", String.valueOf(response.code()));
                ArrayList<Log> logs = response.body();
                switch (response.code()) {
                    case 200:
                        if (logs != null)
                            view.setLogs(logs);
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
            public void onFailure(@NonNull Call<ArrayList<Log>> call, @NonNull Throwable t) {
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
