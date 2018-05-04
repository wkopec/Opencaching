package com.example.opencaching.ui.authorization.login;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.opencaching.R;
import com.example.opencaching.ui.authorization.LoginActivity;
import com.example.opencaching.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.opencaching.utils.StringUtils.getOathToken;
import static com.example.opencaching.utils.StringUtils.getOathVerifier;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragment extends BaseFragment implements LoginFragmentContract.View {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.mask)
    View mask;

    private WebView webView;
    private LoginActivity activity;
    private LoginFragmentContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, view);
        activity = (LoginActivity) getActivity();
        presenter = new LoginFragmentPresenter(this, activity);
        setPresenter(presenter);
        setWebView();
        return view;
    }

    @Override
    public void showProgress() {
        loginButton.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
        mask.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loginButton.setClickable(true);
        progressBar.setVisibility(View.GONE);
        mask.setVisibility(View.GONE);
    }

    @Override
    public void startMainActivity() {
        activity.startMainActivity();
    }

    @Override
    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    private void setWebView() {
        android.webkit.CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("Cookies", "Cookie removed: " + aBoolean);
                }
            });
        } else {
            cookieManager.removeAllCookie();
        }
        webView = new WebView(activity);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(url.contains("/login.php")) {
                hideProgress();
            } else if (url.contains("oauth_verifier=")) {
                presenter.getOauthTokenSecret(getOathVerifier(url), getOathToken(url));
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(url.contains("authorize?oauth_token")) {
                acceptOauthApplicationAccess();
            } else if(url.contains("https://opencaching.pl/UserAuthorization/login")) {
                Toast.makeText(activity, getString(R.string.invalid_username_or_password), Toast.LENGTH_LONG).show();
                hideProgress();
            }
        }
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick() {
        showProgress();
        webView.loadUrl("javascript: {" +
                "document.getElementById('userName').value = '" + username.getText() + "';" +
                "document.getElementById('password').value = '" + password.getText() + "';" +
                "var frms = document.getElementsByName('login_form');" +
                "frms[0].submit(); };");

    }

    private void acceptOauthApplicationAccess() {
        webView.loadUrl("javascript:var x = document.getElementById('authform_result').setAttribute('value', 'granted'); document.forms['authform'].submit();");

    }
}
