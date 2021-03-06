package pl.opencaching.android.sync.tasks;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.data.models.okapi.NewGeocacheLogResponse;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.data.repository.LogDraftRepository;
import pl.opencaching.android.data.repository.UserRespository;
import pl.opencaching.android.utils.events.LogSyncEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.opencaching.android.utils.events.LogSyncEvent.SyncStatus.COMPLETED;
import static pl.opencaching.android.utils.events.LogSyncEvent.SyncStatus.FAILED;
import static pl.opencaching.android.utils.events.LogSyncEvent.SyncStatus.WITH_ERRORS;

public class UploadGeocacheLogsTask implements Runnable {

    private Realm realm;
    private OkapiService okapiService;
    private LogDraftRepository logDraftRepository;
    private GeocacheRepository geocacheRepository;
    private UserRespository userRespository;

    public UploadGeocacheLogsTask(Realm realm, OkapiService okapiService, LogDraftRepository logDraftRepository, GeocacheRepository geocacheRepository, UserRespository userRespository) {
        this.realm = realm;
        this.okapiService = okapiService;
        this.logDraftRepository = logDraftRepository;
        this.geocacheRepository = geocacheRepository;
        this.userRespository = userRespository;
    }

    @Override
    public void run() {
        try {
            uploadNewGeocacheLogs();
            //uploadGeocacheLogsImages();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void uploadNewGeocacheLogs() throws IOException {
        RealmResults<GeocacheLogDraft> logDraws = logDraftRepository.loadAllLogDrawsBySyncReadyAscending(true);
        for(GeocacheLogDraft geocacheLogDraft : logDraws) {
            okapiService.submitNewGeocacheLog(geocacheLogDraft.getMap()).enqueue(new Callback<NewGeocacheLogResponse>() {
                @Override
                public void onResponse(Call<NewGeocacheLogResponse> call, Response<NewGeocacheLogResponse> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        NewGeocacheLogResponse newLogResponse = response.body();
                        if(newLogResponse.isSuccess()) {
                            GeocacheLog newGeocacheLog = new GeocacheLog(newLogResponse.getLogUuid(), geocacheLogDraft.getGeocacheCode(), geocacheLogDraft.getDate(), userRespository.getLoggedUser(), geocacheLogDraft.getType(), geocacheLogDraft.getComment());
                            Geocache geocache = geocacheRepository.loadGeocacheByCode(geocacheLogDraft.getGeocacheCode());

                            realm.beginTransaction();
                            if(geocache != null) {
                                geocache.getGeocacheLogs().add(newGeocacheLog);
                            }
                            geocacheLogDraft.deleteFromRealm();
                            realm.commitTransaction();

                            //TODO: upload images for this log
                            uploadGeocacheImages();

                            EventBus.getDefault().post(new LogSyncEvent(COMPLETED));

                        } else if(!newLogResponse.isSuccess() && newLogResponse.getMessage() != null) {
                            realm.beginTransaction();
                            geocacheLogDraft.setReadyToSync(false);
                            geocacheLogDraft.setUploadErrorMessage(newLogResponse.getMessage());
                            realm.commitTransaction();
                            EventBus.getDefault().post(new LogSyncEvent(WITH_ERRORS));
                        }
                    } else {
                        EventBus.getDefault().post(new LogSyncEvent(FAILED));
                    }
                }

                @Override
                public void onFailure(Call<NewGeocacheLogResponse> call, Throwable t) {

                }
            });
        }

    }

    private void uploadGeocacheImages() {

    }

}
