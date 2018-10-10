package pl.opencaching.android.ui.geocache.info;

import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BaseContract;

/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoContract {

    public interface View extends BaseContract.View{
        void setGeocacheData(Geocache geocache);
        void showError(Error error);
        void showProgress();
        void hideProgress();

    }

    public interface Presenter extends BaseContract.Presenter{

        void getGeocacheInfo(String code);

    }

}
