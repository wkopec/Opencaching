package pl.opencaching.android.ui.geocache.new_log;

import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BaseContract;

public class NewLogContract {

    public interface View extends BaseContract.View {

        void setupView(Geocache geocache);

    }

    public interface Presenter extends BaseContract.Presenter {

        void start(String geocacheCode);

    }

}