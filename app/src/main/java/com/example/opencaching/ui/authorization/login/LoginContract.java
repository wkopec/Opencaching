package com.example.opencaching.ui.authorization.login;

import com.example.opencaching.ui.base.BaseContract;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginContract {

    public interface View extends BaseContract.View {

        void showProgress();

        void hideProgress();

        void startMainActivity();

        void loadUrl(String url);

    }

    public interface Presenter extends BaseContract.Presenter {

        void getOauthTokenSecret(String verifier);

        void getRequestToken();

    }


}
