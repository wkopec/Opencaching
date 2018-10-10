package com.example.opencaching.ui.geocache.info;

import com.example.opencaching.data.models.okapi.Geocache;
import com.example.opencaching.ui.base.BaseContract;

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
