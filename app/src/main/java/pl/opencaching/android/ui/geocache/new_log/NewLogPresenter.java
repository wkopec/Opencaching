package pl.opencaching.android.ui.geocache.new_log;

import android.util.Log;

import javax.inject.Inject;

import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.NewGeocacheLog;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BasePresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewLogPresenter extends BasePresenter implements NewLogContract.Presenter{

    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    OkapiService okapiService;

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
    public void submitNewLog(NewGeocacheLog newGeocacheLog) {
        okapiService.submitNewGeocacheLog(newGeocacheLog.getGeocacheCode(), newGeocacheLog.getLogType(), newGeocacheLog.getComment()).enqueue(new Callback<String>() {
        //okapiService.submitNewGeocacheLog(newGeocacheLog).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.d("Test", "yey " + response.body());
                } else {
                    Log.d("Test", "yey " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Test", "not yey " + t.getMessage());
            }
        });
    }

}
