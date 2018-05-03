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

import com.example.opencaching.R;
import com.example.opencaching.ui.authorization.LoginActivity;
import com.example.opencaching.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.opencaching.utils.StringUtils.getOathToken;
import static com.example.opencaching.utils.StringUtils.getOathVerifier;


/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragment extends BaseFragment implements LoginFragmentContract.View {

    @BindView(R.id.webView)
    WebView webView;
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
        return view;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void startMainActivity() {
        activity.startMainActivity();
    }

    @Override
    public void loadUrl(String url) {
        setWebView();
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
        }
        else cookieManager.removeAllCookie();

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.contains("oauth_verifier=")) {
                //webView.setVisibility(View.GONE);
                presenter.getOauthTokenSecret(getOathVerifier(url), getOathToken(url));
            }
        }
    }

}
