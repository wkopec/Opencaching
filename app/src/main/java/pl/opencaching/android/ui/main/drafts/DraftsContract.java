package pl.opencaching.android.ui.main.drafts;

import java.util.List;

import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.ui.base.BaseContract;

public class DraftsContract {

    public interface View extends BaseContract.View {
        void setGeocacheDraws(List<GeocacheLogDraw> geocacheLogDraws);
    }

    public interface Presenter extends BaseContract.Presenter {

        void onStart();

    }
}
