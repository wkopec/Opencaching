package com.example.opencaching.ui.base;

import com.example.opencaching.data.models.Error;

/**
 * Created by Wojtek on 22.10.2017.
 */

public abstract class BaseContract {
    public interface Presenter {
        void subscribe();

        void unsubscribe();

        boolean isSubscribed();
    }

    public interface View {
        void showToast(int message);

        void showToast(String message);

        void showError(Error error);

        void showProgress();

        void hideProgress();
    }
}
