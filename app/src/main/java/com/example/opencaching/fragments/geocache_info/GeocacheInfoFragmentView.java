package com.example.opencaching.fragments.geocache_info;

/**
 * Created by Wojtek on 12.08.2017.
 */

public interface GeocacheInfoFragmentView {

    void showError(Error error);

    void showProgress();

    void hideProgress();

}
