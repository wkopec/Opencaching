package pl.opencaching.android.ui.geocache.new_log;

import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.ui.base.BaseContract;

public class NewLogContract {

    public interface View extends BaseContract.View {

        void setupGeocache(Geocache geocache);

        void finish();

    }

    public interface Presenter extends BaseContract.Presenter {

        void start(String geocacheCode);

        void submitNewLog(GeocacheLogDraft geocacheLogDraft);

        void saveDraft(GeocacheLogDraft geocacheLogDraft);

    }

}
