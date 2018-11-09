package pl.opencaching.android.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import pl.opencaching.android.R;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.data.models.Error;
import pl.opencaching.android.ui.authorization.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import pl.opencaching.android.ui.dialogs.MessageDialog;

/**
 * Created by Volfram on 15.07.2017.
 */

public abstract class BaseActivity extends DaggerAppCompatActivity implements BaseContract.View {

    @Inject
    SessionManager sessionManager;

    public static final int REQUEST_LOCATION = 2;
    private AlertDialog dialog;
    protected OnBackListener onBackListener;
    private boolean isResumed;
    private BaseContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));
        dialog = builder.create();

    }

    public void setBasePresenter(BaseContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setActionBarTitle(int title) {
        setActionBarTitle(getString(title));
    }

    public void setActionBarTitle(String string) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(string);
        }
    }

    @Override
    public void showError(Error error) {
        showToast(error.getMessage());
    }

    @Override
    public void showMessage(String message) {
        MessageDialog messageDialog = MessageDialog.newInstance( message);
        messageDialog.show(getSupportFragmentManager(), MessageDialog.class.getName());
    }

    @Override
    public void showMessage(int icon, String message) {
        MessageDialog messageDialog = MessageDialog.newInstance(icon, message);
        messageDialog.show(getSupportFragmentManager(), MessageDialog.class.getName());
    }

    @Override
    public void showMessage(int icon, String title, String message) {
        MessageDialog messageDialog = MessageDialog.newInstance(icon, title, message);
        messageDialog.show(getSupportFragmentManager(), MessageDialog.class.getName());
    }

    public void showProgress() {
        runOnUiThread(() -> {
            if (isResumed)
                dialog.show();
        });
    }

    public void hideProgress() {
        runOnUiThread(() -> {
            if (isResumed)
                dialog.dismiss();
        });
    }

    public void setOnBackListener(OnBackListener listener) {
        onBackListener = listener;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (onBackListener != null) {
            onBackListener.customOnBackPressed();
            return;
        }
        goBack();
    }

    public void goBack() {
        onBackListener = null;
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void performSessionEnded() {
        //setOauthTokenSecret(this, "");
        sessionManager.saveOauthTokenSecret("");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
        } else {
            Log.e("MVP " + this.getClass().getSimpleName(), "########### PRESENTER IS IS NULL ###########");
        }
        isResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.unsubscribe();
        } else {
            Log.e("MVP " + this.getClass().getSimpleName(), "########### PRESENTER IS IS NULL ###########");
        }
        isResumed = false;
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    public void showToast(String message) {
        if (message.equals(getString(R.string.session_has_ended))) {
            performSessionEnded();
            Toast.makeText(this, message, Toast.LENGTH_LONG);
        } else {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void showToast(int message) {
        showToast(getString(message));
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public interface OnBackListener {
        boolean customOnBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

