package pl.opencaching.android.ui.base;

import android.util.Log;
import dagger.android.support.DaggerFragment;
import pl.opencaching.android.data.models.Error;

/**
 * Created by Wojtek on 22.10.2017.
 */

public abstract class BaseFragment extends DaggerFragment implements BaseContract.View {


    private BaseContract.Presenter presenter;

    public void setPresenter(BaseContract.Presenter basePresenter){
        this.presenter = basePresenter;
    }

    @Override
    public void showToast(int message) {
        ((BaseActivity)getActivity()).showToast(message);
    }

    @Override
    public void showToast(String message) {
        ((BaseActivity)getActivity()).showToast(message);
    }

    @Override
    public void showError(Error error) {
        ((BaseActivity)getActivity()).showError(error);
    }

    @Override
    public void showProgress() {
        ((BaseActivity)getActivity()).showProgress();
    }

    @Override
    public void hideProgress() {
        ((BaseActivity)getActivity()).hideProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null){
            presenter.subscribe();
        } else {
            Log.e("MVP " + this.getClass().getSimpleName(), "########### PRESENTER IS IS NULL ###########");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null){
            presenter.unsubscribe();
        } else {
            Log.e("MVP " + this.getClass().getSimpleName(), "########### PRESENTER IS IS NULL ###########");
        }    }
}
