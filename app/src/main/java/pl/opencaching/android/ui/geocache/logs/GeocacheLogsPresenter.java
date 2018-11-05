package pl.opencaching.android.ui.geocache.logs;

import android.content.Context;
import android.support.annotation.NonNull;

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

import static pl.opencaching.android.utils.Constants.LOGS_STANDARD_FIELDS;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheLogsPresenter extends BasePresenter implements GeocacheLogsContract.Presenter {

    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    OkapiService okapiService;

    private GeocacheLogsContract.View view;

    @Inject
    public GeocacheLogsPresenter(GeocacheLogsContract.View view) {
        this.view = view;
    }

    @Override
    public void getGeocacheLogs(String code) {
        view.setLogs(new ArrayList<>(geocacheRepository.loadLogsByCode(code)));

        Call<ArrayList<GeocacheLog>> loginCall = okapiService.getGeocacheLogs(code, LOGS_STANDARD_FIELDS, 0, 500);
        loginCall.enqueue(new Callback<ArrayList<GeocacheLog>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GeocacheLog>> call, @NonNull Response<ArrayList<GeocacheLog>> response) {
                if (response.body() != null) {
                    ArrayList<GeocacheLog> geocacheLogs = response.body();
                    view.setLogs(geocacheLogs);
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
