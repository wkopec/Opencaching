package com.example.opencaching.ui.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.example.opencaching.R;
import com.example.opencaching.app.prefs.SessionManager;
import com.example.opencaching.ui.base.BaseActivity;
import com.example.opencaching.ui.main.MainActivity;
import com.example.opencaching.ui.authorization.login.LoginFragment;

import javax.inject.Inject;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginActivity extends BaseActivity {

    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_empty);
        if (sessionManager.isLoggedIn()) {
            startMainActivity();
        } else {
            replaceFragment(new LoginFragment(), false);
        }

    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
