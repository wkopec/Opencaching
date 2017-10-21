package com.example.opencaching.fragments.login;

/**
 * Created by Wojtek on 13.08.2017.
 */

public interface LoginFragmentView {

    void showProgress();

    void hideProgress();

    void startMainActivity();

    void loadUrl(String url);

}
