package pl.opencaching.android.ui.base;

import android.view.View;

import pl.opencaching.android.data.models.Error;

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

        void showMessage(int icon, String title, String message);

        void showMessage(int icon, String message);

        void showMessage(String message);

        void showMessage(String title, String message);

    }
}
