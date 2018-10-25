package pl.opencaching.android.ui.geocache.logs;

import android.content.Context;
import android.support.annotation.NonNull;

import pl.opencaching.android.R;

import java.util.ArrayList;

import javax.inject.Inject;

import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BasePresenter;
import pl.opencaching.android.utils.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheLogsPresenter extends BasePresenter implements GeocacheLogsContract.Presenter {

    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    OkapiService okapiService;

    private final static String LOGS_STANDARD_FIELDS = "uuid|date|user|type|comment|images|was_recommended";

    private GeocacheLogsContract.View view;
    private Context context;

    @Inject
    public GeocacheLogsPresenter(GeocacheLogsContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getGeocacheLogs(String code) {
        Call<ArrayList<GeocacheLog>> loginCall = okapiService.getGeocacheLogs(context.getResources().getString(R.string.opencaching_key), code, LOGS_STANDARD_FIELDS, 0, 500);
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
