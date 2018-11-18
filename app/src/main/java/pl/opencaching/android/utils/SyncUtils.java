package pl.opencaching.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import pl.opencaching.android.sync.SyncService;

public class SyncUtils {

    public static String HAS_PENDING_SYNC = "has_pending_sync";

    public static boolean isInternetConnection(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return wifi.isConnected() || mobile.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void startMergeService(Context context, SharedPreferences sharedPreferences) {
        if (isInternetConnection(context) && !sharedPreferences.getBoolean((HAS_PENDING_SYNC), false)) {
            Intent intent = new Intent(context, SyncService.class);
            intent.setAction(SyncService.ACTION_LOG_SYNC);
            context.startService(intent);
        }
    }

}
