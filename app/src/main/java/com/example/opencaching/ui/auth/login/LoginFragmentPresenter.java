package com.example.opencaching.ui.auth.login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BasePresenter;
import com.example.opencaching.network.api.OpencachingApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;

import static com.example.opencaching.utils.UserUtils.setUserSecretToken;
import static com.example.opencaching.utils.UserUtils.setUserToken;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragmentPresenter extends BasePresenter implements LoginFragmentContract.Presenter {

    private OAuth10aService mService;
    private OAuth1RequestToken requestToken;
    private LoginFragmentContract.View view;
    private Context context;

    //private final String redirectUri = "your://redirecturi";

    public LoginFragmentPresenter(LoginFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;

//        Intent intent = new Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse(OpencachingApi.API_BASE_URL + "/oauth/request_token" + "?client_id=" + context.getString(R.string.opencaching_key) + "&oauth_callback=" + redirectUri));
//        context.startActivity(intent);

        setOAuthService();
        getAccesToken();
    }


    private void setOAuthService() {
        mService = new ServiceBuilder()
                .apiKey(context.getString(R.string.opencaching_key))
                .apiSecret(context.getString(R.string.opencaching_secret_key))
                .build(OpencachingApi.instance());
    }

    private void getAccesToken() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String authUrl = "";
                try {
                    requestToken = mService.getRequestToken();
                    authUrl = mService.getAuthorizationUrl(requestToken);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return authUrl;
            }

            @Override
            protected void onPostExecute(String authUrl) {
                android.webkit.CookieManager cookieManager = CookieManager.getInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                        @Override
                        public void onReceiveValue(Boolean aBoolean) {
                            Log.d("Cookies", "Cookie removed: " + aBoolean);
                        }
                    });
                } else cookieManager.removeAllCookie();
                view.loadUrl(authUrl);

            }
        }.execute();
    }

    @Override
    public void getOauthToken(final String verifier) {
        new AsyncTask<Void, Void, OAuth1AccessToken>() {
            @Override
            protected OAuth1AccessToken doInBackground(Void... params) {
                OAuth1AccessToken accessToken = null;
                try {
                    accessToken = mService.getAccessToken(requestToken, verifier);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return accessToken;
            }

            @Override
            protected void onPostExecute(OAuth1AccessToken accessToken) {
                if (accessToken != null) {
                    String oauthToken = accessToken.getToken();
                    String oauthSecretToken = accessToken.getTokenSecret();
                    Log.d("Oauth Token", oauthToken);
                    Log.d("Oauth Secret Token", oauthSecretToken);
                    setUserToken(context, oauthToken);
                    setUserSecretToken(context, oauthSecretToken);
                    view.startMainActivity();
                }
            }
        }.execute();
    }

//    public class AccessToken {
//
//        private String accessToken;
//        private String tokenType;
//
//        public String getAccessToken() {
//            return accessToken;
//        }
//
//        public String getTokenType() {
//            // OAuth requires uppercase Authorization HTTP header value for token type
//            if (! Character.isUpperCase(tokenType.charAt(0))) {
//                tokenType =
//                        Character
//                                .toString(tokenType.charAt(0))
//                                .toUpperCase() + tokenType.substring(1);
//            }
//
//            return tokenType;
//        }
//    }

}
