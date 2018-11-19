package pl.opencaching.android.sync.tasks;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.models.okapi.NewGeocacheLogResponse;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.data.repository.LogDrawRepository;
import pl.opencaching.android.data.repository.UserRespository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadGeocacheLogsTask implements Runnable {

    private Realm realm;
    private OkapiService okapiService;
    private LogDrawRepository logDrawRepository;
    private GeocacheRepository geocacheRepository;
    private UserRespository userRespository;

    public UploadGeocacheLogsTask(Realm realm, OkapiService okapiService, LogDrawRepository logDrawRepository, GeocacheRepository geocacheRepository, UserRespository userRespository) {
        this.realm = realm;
        this.okapiService = okapiService;
        this.logDrawRepository = logDrawRepository;
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
        RealmResults<GeocacheLogDraw> logDraws = logDrawRepository.loadAllLogDrawsBySyncReady(true);
        for(GeocacheLogDraw geocacheLogDraw : logDraws) {
            okapiService.submitNewGeocacheLog(geocacheLogDraw.getMap()).enqueue(new Callback<NewGeocacheLogResponse>() {
                @Override
                public void onResponse(Call<NewGeocacheLogResponse> call, Response<NewGeocacheLogResponse> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        NewGeocacheLogResponse newLogResponse = response.body();
                        if(newLogResponse.isSuccess()) {
                            GeocacheLog newGeocacheLog = new GeocacheLog(newLogResponse.getLogUuid(), geocacheLogDraw.getGeocacheCode(), geocacheLogDraw.getDate(), userRespository.getLoggedUser(), geocacheLogDraw.getType(), geocacheLogDraw.getComment());
                            Geocache geocache = geocacheRepository.loadGeocacheByCode(newGeocacheLog.getGeocacheCode());

                            realm.beginTransaction();
                            if(geocache != null) {
                                geocache.getGeocacheLogs().add(newGeocacheLog);
                            }
                            geocacheLogDraw.deleteFromRealm();
                            realm.commitTransaction();

                            //TODO: upload images for this log
                            uploadGeocacheImages();
                        } else if(!newLogResponse.isSuccess() && newLogResponse.getMessage() != null) {
                            //TODO: handle error
                        }
                    } else {
                        //TODO: handle error
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
