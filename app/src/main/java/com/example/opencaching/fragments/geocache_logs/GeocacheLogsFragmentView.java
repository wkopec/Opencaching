package com.example.opencaching.fragments.geocache_logs;

import com.example.opencaching.models.okapi.Log;

import java.util.ArrayList;

/**
 * Created by Wojtek on 27.07.2017.
 */

public interface GeocacheLogsFragmentView {

    void setLogs(ArrayList<Log> logs);

    void showError(Error error);

    void showProgress();

    void hideProgress();
}
