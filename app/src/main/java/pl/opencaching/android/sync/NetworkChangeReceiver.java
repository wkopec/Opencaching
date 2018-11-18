package pl.opencaching.android.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.RealmResults;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.repository.LogDrawRepository;

import static pl.opencaching.android.utils.SyncUtils.HAS_PENDING_SYNC;
import static pl.opencaching.android.utils.SyncUtils.isInternetConnection;
import static pl.opencaching.android.utils.SyncUtils.startMergeService;

public class NetworkChangeReceiver extends BroadcastReceiver {

    SharedPreferences sharedPreferences;

    public NetworkChangeReceiver() { }

    @Inject
    public NetworkChangeReceiver(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isInternetConnection(context)) {
//            RealmResults<GeocacheLogDraw> logDraws = logDrawRepository.loadAllLogDrawsBySyncReady(true);
//            if(!logDraws.isEmpty() && sharedPreferences.getBoolean((HAS_PENDING_SYNC), false)) {
            if(!sharedPreferences.getBoolean((HAS_PENDING_SYNC), false)) {

                //Sometimes Internet connection is not performed immediately after onReceive
                Completable.timer(3, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> startMergeService(context, sharedPreferences));

            }
        }
    }

}