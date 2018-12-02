package pl.opencaching.android.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.ui.authorization.AuthorizationActivity;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.main.MainActivity;

public class SplashActivity extends BaseActivity {

    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (sessionManager.isLoggedIn()) {
            startMainActivity();
        } else {
            startAuthorizationActivity();
        }
    }

    public void startAuthorizationActivity() {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
        finish();
    }
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
