package com.example.opencaching.fragments.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.opencaching.R;
import com.example.opencaching.activities.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragment extends Fragment implements LoginFragmentView {

    @BindView(R.id.webView)
    WebView webView;
    private Unbinder unbinder;
    private View view;
    private LoginActivity activity;
    private LoginFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (LoginActivity) getActivity();
        presenter = new LoginFragmentPresenter(this, activity);
        setWebView();
        return view;
    }




    private void setWebView() {
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new CustomWebViewClient());
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
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
        webView.loadUrl(url);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.contains("oauth_verifier=")) {
                webView.setVisibility(View.GONE);
                url = url.substring(url.indexOf("oauth_token"));
                String[] pairs = url.split("&");
                String token = pairs[0].replace("oauth_token=", "");
                String verifier = pairs[1].replace("oauth_verifier=", "");
                presenter.getOauthToken(verifier);
            }
        }
    }

}
