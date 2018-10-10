package pl.opencaching.android.ui.authorization.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import pl.opencaching.android.R;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.ui.authorization.LoginActivity;
import pl.opencaching.android.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.opencaching.android.utils.StringUtils;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.loginButton)
    FrameLayout loginButton;
    @BindView(R.id.mask)
    View mask;
    @BindView(R.id.termsOfService)
    TextView termsOfService;
    @BindView(R.id.background)
    ImageView background;

    private WebView webView;
    private LoginActivity activity;

    @Inject
    LoginContract.Presenter presenter;
    @Inject
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, view);
        activity = (LoginActivity) getActivity();
        setPresenter(presenter);
        setWebView();
        setTermsOfService();
        setOnDoneClickListener();
        presenter.getRequestToken();
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
            if (url.contains("/login.php")) {
                hideProgress();
            } else if (url.contains("oauth_verifier=")) {
                sessionManager.saveOauthToken(StringUtils.getOathToken(url));
                presenter.getOauthTokenSecret(StringUtils.getOathVerifier(url));
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.contains("authorize?oauth_token")) {
                acceptOauthApplicationAccess();
            } else if (url.contains("https://opencaching.pl/UserAuthorization/login")) {
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

    private void setTermsOfService() {
        SpannableString ss = new SpannableString(String.format(getString(R.string.agree_terms_of_service), getString(R.string.terms_of_service)));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.terms_of_service_website)));
                activity.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(activity, android.R.color.white));
            }
        };
        ss.setSpan(clickableSpan, ss.length() - getString(R.string.terms_of_service).length() - 1, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsOfService.setText(ss);
        termsOfService.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setOnDoneClickListener() {
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onLoginButtonClick();
                }
                return false;
            }
        });
    }
}
