package pl.opencaching.android.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import javax.inject.Inject;

import dagger.android.DaggerBroadcastReceiver;
import io.realm.RealmResults;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.data.repository.LogDraftRepository;

import static pl.opencaching.android.utils.SyncUtils.HAS_PENDING_SYNC;
import static pl.opencaching.android.utils.SyncUtils.isInternetConnection;
import static pl.opencaching.android.utils.SyncUtils.startMergeService;

public class NetworkChangeReceiver extends DaggerBroadcastReceiver {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    LogDraftRepository logDraftRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (isInternetConnection(context)) {
            RealmResults<GeocacheLogDraft> logDraws = logDraftRepository.loadAllLogDrawsBySyncReady(true);
            if (!logDraws.isEmpty() && !sharedPreferences.getBoolean((HAS_PENDING_SYNC), false)) {
                startMergeService(context, sharedPreferences);
            }
        }

    }

}
