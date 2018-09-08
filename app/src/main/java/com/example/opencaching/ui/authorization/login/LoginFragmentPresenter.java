package com.example.opencaching.ui.authorization.login;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.opencaching.api.OkapiService;
import com.example.opencaching.app.prefs.SessionManager;
import com.example.opencaching.ui.base.BasePresenter;
import com.example.opencaching.utils.ApiUtils;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.StringUtils.getOathToken;
import static com.example.opencaching.utils.StringUtils.getOathTokenSecret;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragmentPresenter extends BasePresenter implements LoginContract.Presenter {

    @Inject
    SessionManager sessionManager;
    @Inject
    OkapiService okapiService;

    private LoginContract.View view;
    private Context context;

    @Inject
    LoginFragmentPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getRequestToken() {
        Call<String> requestTokenCall = okapiService.getRequestToken("oob");
        requestTokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.body() != null) {
                    String oauthToken = getOathToken(response.body());
                    sessionManager.saveOauthToken(oauthToken);
                    sessionManager.saveOauthTokenSecret(getOathTokenSecret(response.body()));
                    if (oauthToken != null) {
                        view.loadUrl("https://opencaching.pl/okapi/services/oauth/authorize?oauth_token=" + oauthToken);
                    } else {
                        view.hideProgress();
                    }
                } else {
                    if (response.errorBody() != null) {
                        view.showError(ApiUtils.getErrorSingle(response.errorBody()));
                        view.hideProgress();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                view.hideProgress();
                view.showError(ApiUtils.getErrorSingle(t));
            }
        });
    }


    @Override
    public void getOauthTokenSecret(String verifier) {
        Call<String> requestTokenCall = okapiService.getAccessToken(verifier);
        requestTokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.body() != null) {
                    sessionManager.saveOauthToken(getOathToken(response.body()));
                    sessionManager.saveOauthTokenSecret(getOathTokenSecret(response.body()));
                    view.hideProgress();
                    view.startMainActivity();
                } else {
                    if (response.errorBody() != null) {
                        view.showError(ApiUtils.getErrorSingle(response.errorBody()));
                        view.hideProgress();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                view.hideProgress();
                view.showError(ApiUtils.getErrorSingle(t));
            }
        });
    }

}
