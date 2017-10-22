package com.example.opencaching.ui.geocache.geocache_info;

import com.example.opencaching.ui.base.BaseContract;

/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoContract {

    public interface View extends BaseContract.View{

        void showError(Error error);

        void showProgress();

        void hideProgress();

    }

    public interface Presenter extends BaseContract.Presenter{

        void getGeocacheInfo(String code);

    }

}
