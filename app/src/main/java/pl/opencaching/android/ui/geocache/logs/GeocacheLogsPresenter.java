package pl.opencaching.android.ui.geocache.logs;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.repository.GeocacheLogRepository;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.data.repository.LogDrawRepository;
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
    OkapiService okapiService;
    @Inject
    GeocacheLogRepository geocacheLogRepository;
    @Inject
    LogDrawRepository logDrawRepository;
    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    Realm realm;

    private GeocacheLogsContract.View view;

    @Inject
    GeocacheLogsPresenter(GeocacheLogsContract.View view) {
        this.view = view;
    }

    @Override
    public void getGeocacheLogs(String code) {
        view.setLogs(new ArrayList<>(getLogs(code)));

        Call<ArrayList<GeocacheLog>> loginCall = okapiService.getGeocacheLogs(code, LOGS_STANDARD_FIELDS, 0, 500);
        loginCall.enqueue(new Callback<ArrayList<GeocacheLog>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GeocacheLog>> call, @NonNull Response<ArrayList<GeocacheLog>> response) {
                if (response.body() != null) {
                    ArrayList<GeocacheLog> fetchedGocacheLogs = response.body();

                    for( GeocacheLog log : fetchedGocacheLogs) {
                        log.setGeocacheCode(code);
                    }

                    Geocache geocache = geocacheRepository.loadGeocacheByCode(code);
                    realm.beginTransaction();
                    geocache.getGeocacheLogs().addAll(fetchedGocacheLogs);
                    realm.commitTransaction();

                    view.setLogs(new ArrayList<>(getLogs(code)));
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

    private ArrayList<GeocacheLogInterface> getLogs(String code) {
        ArrayList<GeocacheLogInterface> geocacheLogs = new ArrayList<>();
        geocacheLogs.addAll(geocacheLogRepository.loadLogsByCode(code));
        geocacheLogs.addAll(logDrawRepository.loadLogDrawsForGeocache(code));
        return geocacheLogs;
    }


}
