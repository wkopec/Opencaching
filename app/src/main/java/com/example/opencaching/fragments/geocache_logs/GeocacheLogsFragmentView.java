package com.example.opencaching.fragments.geocache_logs;

import com.example.opencaching.models.okapi.GeocacheLog;

import java.util.ArrayList;

/**
 * Created by Wojtek on 27.07.2017.
 */

public interface GeocacheLogsFragmentView {

    void setLogs(ArrayList<GeocacheLog> geocacheLogs);

    void showError(Error error);

    void showProgress();

    void hideProgress();
}
