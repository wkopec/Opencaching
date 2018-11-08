package pl.opencaching.android.ui.geocache.new_log;

import pl.opencaching.android.data.models.okapi.Geocache;

import pl.opencaching.android.ui.base.BaseContract;

public class NewLogContract {

    public interface View extends BaseContract.View {

//        void setLogs(ArrayList<GeocacheLog> geocacheLogs);
//
//        void showError(Error error);
//
//        void showProgress();
//
//        void hideProgress();

    }

    public interface Presenter extends BaseContract.Presenter {

        void start(Geocache geocache);

    }

}
