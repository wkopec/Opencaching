package pl.opencaching.android.ui.geocache.logs;

import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.ui.base.BaseContract;
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
