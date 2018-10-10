package pl.opencaching.android.ui.base;

import android.support.annotation.CallSuper;

/**
 * Created by Wojtek on 22.10.2017.
 */

public abstract class BasePresenter implements BaseContract.Presenter{
    private boolean isSubscribed;

    @CallSuper
    @Override
    public void subscribe() {
        isSubscribed = true;
    }

    @CallSuper
    @Override
    public void unsubscribe() {
        isSubscribed = false;
    }

    @CallSuper
    @Override
    public boolean isSubscribed() {
        return isSubscribed;
    }
}
