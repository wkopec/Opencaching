package com.example.opencaching.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.example.opencaching.R;
import com.example.opencaching.fragments.login.LoginFragment;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.opencaching.utils.UserUtils.isLoggedIn;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_empty);
        if (isLoggedIn(this))
            startMainActivity();
        else
            replaceFragment(new LoginFragment(), false);
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
