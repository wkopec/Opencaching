package com.example.opencaching.ui.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.opencaching.R;
import com.example.opencaching.network.models.Error;
import com.example.opencaching.ui.authorization.LoginActivity;
import com.example.opencaching.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Volfram on 15.07.2017.
 */

public class BaseActivity extends AppCompatActivity implements BaseContract.View{

    public static final int REQUEST_LOCATION = 2;
    private AlertDialog dialog;
    protected OnBackListener onBackListener;
    private PermissionListener permissionListener;
    private boolean isResumed;
    private BaseContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));
        dialog = builder.create();


    }

    public void setBasePresenter(BaseContract.Presenter presenter){
        this.presenter = presenter;
    }

    public void setActionBarTitle(int title){
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

    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isResumed)
                    dialog.show();
            }
        });
    }

    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isResumed)
                    dialog.dismiss();
            }
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

    public boolean requestLocationPermission(PermissionListener listener) {
        permissionListener = listener;
        if (!isLocationPermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return false;
        } else {
            permissionListener.onPermissionGranted();
            return true;
        }
    }

    public void performSessionEnded (){
        UserUtils.setUserToken(this, "");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null){
            presenter.subscribe();
        } else {
            Log.e("MVP " + this.getClass().getSimpleName(), "########### PRESENTER IS IS NULL ###########");
        }
        isResumed = true;
//        if(!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null){
            presenter.unsubscribe();
        } else {
            Log.e("MVP " + this.getClass().getSimpleName(), "########### PRESENTER IS IS NULL ###########");
        }
        isResumed = false;
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionListener.onPermissionGranted();
                } else {
                    permissionListener.onPermissionDenied();
                }
            }
        }
    }

    public void showToast(String message) {
        if (message.equals(getString(R.string.session_has_ended))){
            performSessionEnded();
            Toast.makeText(this, message, Toast.LENGTH_LONG);
        } else {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void showToast(int message){
        showToast(getString(message));
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public interface PermissionListener {
        void onPermissionGranted();

        void onPermissionDenied();
    }

    public interface OnBackListener {
        boolean customOnBackPressed();
    }


}

