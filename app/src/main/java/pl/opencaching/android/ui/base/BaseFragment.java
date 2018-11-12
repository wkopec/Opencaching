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
        ((BaseActivity)requireActivity()).showToast(message);
    }

    @Override
    public void showToast(String message) {
        ((BaseActivity)requireActivity()).showToast(message);
    }

    @Override
    public void showError(Error error) {
        ((BaseActivity)requireActivity()).showError(error);
    }

    @Override
    public void showProgress() {
        ((BaseActivity)requireActivity()).showProgress();
    }

    @Override
    public void hideProgress() {
        ((BaseActivity)requireActivity()).hideProgress();
    }

    @Override
    public void showMessage(String message) {
        ((BaseActivity)requireActivity()).showMessage(message);
    }

    @Override
    public void showMessage(int icon, String message) {
        ((BaseActivity)requireActivity()).showMessage(icon, message);
    }

    @Override
    public void showMessage(int icon, String title, String message) {
        ((BaseActivity)requireActivity()).showMessage(icon, title, message);
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
