package pl.opencaching.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SyncUtils {

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

}
