package pl.opencaching.android.sync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.data.repository.LogDrawRepository;
import pl.opencaching.android.data.repository.UserRespository;
import pl.opencaching.android.sync.tasks.UploadGeocacheLogsTask;
import timber.log.Timber;

import static pl.opencaching.android.utils.SyncUtils.HAS_PENDING_SYNC;

public class SyncService extends DaggerIntentService {

    private static final String LOG_TAG = SyncService.class.getSimpleName();

    public static final String ACTION_LOG_SYNC = "action_log_sync:";

    @Inject
    Realm realm;
    @Inject
    OkapiService okapiService;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    LogDrawRepository logDrawRepository;
    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    UserRespository userRespository;

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String serviceAction = intent.getAction();
        switch (serviceAction) {
            case ACTION_LOG_SYNC:
                if(!sharedPreferences.getBoolean((HAS_PENDING_SYNC), false)) {
                    Timber.d(LOG_TAG, "Starting log sync data action");
                    new Handler(getMainLooper()).post(new UploadGeocacheLogsTask(realm, okapiService, logDrawRepository, geocacheRepository, userRespository) {
                        @Override
                        public void run() {
                            super.run();
                        }
                    });
                }
                break;
            default:
                Timber.d(LOG_TAG, "Action not handled: %s", serviceAction);
                break;
        }
    }
}
