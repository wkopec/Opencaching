package pl.opencaching.android.sync.log.tasks;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.RealmResults;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.models.okapi.NewGeocacheLogResponse;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.data.repository.LogDrawRepository;
import pl.opencaching.android.data.repository.UserRespository;
import retrofit2.Response;

public class UploadGeocacheLogsTask implements Runnable {

    @Inject
    OkapiService okapiService;
    @Inject
    LogDrawRepository logDrawRepository;
    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    UserRespository userRespository;

    @Override
    public void run() {
        try {
            uploadNewGeocacheLogs();
            uploadGeocacheLogsImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void uploadNewGeocacheLogs() throws IOException {
        RealmResults<GeocacheLogDraw> logDraws = logDrawRepository.loadAllLogDrawsBySyncReady(true);
        for(GeocacheLogDraw geocacheLogDraw : logDraws) {
            Response<NewGeocacheLogResponse> response = okapiService.submitNewGeocacheLog(geocacheLogDraw.getMap()).execute();
            if(response.isSuccessful() && response.body() != null) {
                NewGeocacheLogResponse newLogResponse = response.body();
                if(newLogResponse.isSuccess()) {
                    GeocacheLog newGeocacheLog = new GeocacheLog(newLogResponse.getLogUuid(), geocacheLogDraw.getGeocacheCode(), geocacheLogDraw.getDate(), userRespository.getLoggedUser(), geocacheLogDraw.getType(), geocacheLogDraw.getComment());
                    Geocache geocache = geocacheRepository.loadGeocacheByCode(newGeocacheLog.getGeocacheCode());
                    if(geocache != null) {
                        geocache.getGeocacheLogs().add(newGeocacheLog);
                        geocacheRepository.addOrUpdate(geocache);
                    }
                } else if(!newLogResponse.isSuccess() && newLogResponse.getMessage() != null) {
                    //TODO: handle error
                }
            } else {
                //TODO: handle error
            }
        }
    }

    private void uploadGeocacheLogsImages() {

    }

}
