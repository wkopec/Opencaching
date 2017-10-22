package com.example.opencaching.ui.auth.login;

import com.example.opencaching.ui.base.BaseContract;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragmentContract {

    public interface View extends BaseContract.View {

        void showProgress();

        void hideProgress();

        void startMainActivity();

        void loadUrl(String url);

    }

    public interface Presenter extends BaseContract.Presenter {

        void getOauthToken(final String verifier);

    }


}
