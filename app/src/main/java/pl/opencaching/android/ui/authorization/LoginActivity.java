package pl.opencaching.android.ui.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import pl.opencaching.android.R;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.main.MainActivity;
import pl.opencaching.android.ui.authorization.login.LoginFragment;

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

        //TODO: change it to BaseFragmentActivity
        setContentView(R.layout.activity_base_fragment);
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
