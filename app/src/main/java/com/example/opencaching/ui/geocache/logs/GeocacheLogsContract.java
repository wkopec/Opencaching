package com.example.opencaching.ui.geocache.logs;

import com.example.opencaching.data.models.okapi.GeocacheLog;
import com.example.opencaching.ui.base.BaseContract;

import java.util.ArrayList;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheLogsContract {

    public interface View extends BaseContract.View {

        void setLogs(ArrayList<GeocacheLog> geocacheLogs);

        void showError(Error error);

        void showProgress();

        void hideProgress();

    }

    public interface Presenter extends BaseContract.Presenter {

        void getGeocacheLogs(String code);

    }

}
