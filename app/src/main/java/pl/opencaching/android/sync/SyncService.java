package pl.opencaching.android.sync;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import javax.inject.Inject;

import io.realm.RealmResults;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.repository.LogDrawRepository;
import pl.opencaching.android.sync.log.LogSyncFactory;
import timber.log.Timber;

import static pl.opencaching.android.utils.SyncUtils.HAS_PENDING_SYNC;

public class SyncService extends IntentService {

    private static final String LOG_TAG = SyncService.class.getSimpleName();

    public static final String ACTION_LOG_SYNC = "action_log_sync:";

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String serviceAction = intent.getAction();
        switch (serviceAction) {
            case ACTION_LOG_SYNC:
                //RealmResults<GeocacheLogDraw> logDraws = logDrawRepository.loadAllLogDrawsBySyncReady(true);
                //if(!logDraws.isEmpty() && sharedPreferences.getBoolean((HAS_PENDING_SYNC), false)) {
                    Timber.d(LOG_TAG, "Starting log sync data action");
                    new LogSyncFactory().execute();
                //}

                break;

            default:
                Timber.d(LOG_TAG, "Action not handled: %s", serviceAction);
                break;
        }
    }
}
