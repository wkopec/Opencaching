package pl.opencaching.android.ui.geocache.new_log;

import android.content.Context;

import javax.inject.Inject;

import io.realm.Realm;
import pl.opencaching.android.R;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.models.okapi.NewGeocacheLogResponse;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.data.repository.LogDrawRepository;
import pl.opencaching.android.data.repository.UserRespository;
import pl.opencaching.android.ui.base.BasePresenter;
import pl.opencaching.android.utils.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewLogPresenter extends BasePresenter implements NewLogContract.Presenter{

    @Inject
    Context context;
    @Inject
    Realm realm;
    @Inject
    OkapiService okapiService;
    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    LogDrawRepository logDrawRepository;
    @Inject
    UserRespository userRespository;
    @Inject
    SessionManager sessionManager;

    private NewLogContract.View view;
    private Geocache geocache;

    @Inject
    NewLogPresenter(NewLogContract.View view) {
        this.view = view;
    }

    @Override
    public void start(String geocacheCode) {
        geocache = geocacheRepository.loadGeocacheByCode(geocacheCode);
        view.setupGeocache(geocache);
    }

    @Override
    public void submitNewLog(GeocacheLogDraw geocacheLogDraw) {
              okapiService.submitNewGeocacheLog(geocacheLogDraw.getMap()).enqueue(new Callback<NewGeocacheLogResponse>() {
            @Override
            public void onResponse(Call<NewGeocacheLogResponse> call, Response<NewGeocacheLogResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    NewGeocacheLogResponse newLogResponse = response.body();
                    if(newLogResponse.isSuccess()) {
                        GeocacheLog newGeocacheLog = new GeocacheLog(newLogResponse.getLogUuid(), geocacheLogDraw.getGeocacheCode(), geocacheLogDraw.getDate(), userRespository.getLoggedUser(), geocacheLogDraw.getType(), geocacheLogDraw.getComment());
                        realm.beginTransaction();
                        geocache.getGeocacheLogs().add(newGeocacheLog);
                        realm.commitTransaction();
                        view.finish();
                    } else if(!newLogResponse.isSuccess() && newLogResponse.getMessage() != null) {
                        view.showMessage(newLogResponse.getMessage());
                    }
                } else {
                    view.showMessage(context.getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<NewGeocacheLogResponse> call, Throwable t) {
                createGeocacheDraft(geocacheLogDraw, true);
                view.showMessage(context.getString(ApiUtils.getErrorSingle(t).getMessage()), context.getString(R.string.new_log_auto_submit_message));
            }
        });
    }

    @Override
    public void createDraft(GeocacheLogDraw geocacheLogDraw) {
        createGeocacheDraft(geocacheLogDraw, false);
        view.finish();
    }

    private void createGeocacheDraft(GeocacheLogDraw geocacheLogDraw, boolean isReadyToSync) {
        geocacheLogDraw.setReadyToSync(isReadyToSync);
        geocacheLogDraw.setUser(userRespository.getLoggedUser());
        logDrawRepository.addOrUpdate(geocacheLogDraw);
    }
}
