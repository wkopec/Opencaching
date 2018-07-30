package com.example.opencaching.ui.geocache.logs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BasePresenter;
import com.example.opencaching.network.models.okapi.GeocacheLog;
import com.example.opencaching.network.api.OpencachingApi;
import com.example.opencaching.utils.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheLogsPresenter extends BasePresenter implements GeocacheLogsContract.Presenter {

    private final static String LOGS_STANDARD_FIELDS = "uuid|date|user|type|comment|images|was_recommended";

    private GeocacheLogsContract.View view;
    private Context context;

    public GeocacheLogsPresenter(GeocacheLogsContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGeocacheLogs(String code) {
        Call<ArrayList<GeocacheLog>> loginCall = OpencachingApi.service(context).getGeocacheLogs(context.getResources().getString(R.string.opencaching_key), code, LOGS_STANDARD_FIELDS, 0, 100);
        loginCall.enqueue(new Callback<ArrayList<GeocacheLog>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GeocacheLog>> call, @NonNull Response<ArrayList<GeocacheLog>> response) {

                if (response.body() != null) {
                    ArrayList<GeocacheLog> geocacheLogs = response.body();
                    if (geocacheLogs != null)
                        view.setLogs(geocacheLogs);
                    else {
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
            public void onFailure(@NonNull Call<ArrayList<GeocacheLog>> call, @NonNull Throwable t) {
                view.hideProgress();
                view.showError(ApiUtils.getErrorSingle(t));
            }
        });
    }

}