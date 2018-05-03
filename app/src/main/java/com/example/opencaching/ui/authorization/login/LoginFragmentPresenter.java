package com.example.opencaching.ui.authorization.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BasePresenter;
import com.example.opencaching.network.api.OpencachingApi;
import com.example.opencaching.utils.ApiUtils;
import com.example.opencaching.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.StringUtils.getOathToken;
import static com.example.opencaching.utils.StringUtils.getOathTokenSecret;

/**
 * Created by Wojtek on 13.08.2017.
 */

public class LoginFragmentPresenter extends BasePresenter implements LoginFragmentContract.Presenter {

    private LoginFragmentContract.View view;
    private Context context;
    private String oauthToken;
    private String oauthTokenSecret;

    public LoginFragmentPresenter(LoginFragmentContract.View view, Context context) {
        this.view = view;
        this.context = context;

        getRequestToken();

    }

    private void getRequestToken() {
        Call<String> requestTokenCall = OpencachingApi.service(context.getResources().getString(R.string.opencaching_key), context.getResources().getString(R.string.opencaching_secret_key), "", "").getRequestToken("oob");
        requestTokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.body() != null) {
                    oauthToken = getOathToken(response.body());
                    oauthTokenSecret = getOathTokenSecret(response.body());
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
    public void getOauthTokenSecret(String verifier, String oauthToken) {
        Call<String> requestTokenCall = OpencachingApi.service(context.getResources().getString(R.string.opencaching_key), context.getResources().getString(R.string.opencaching_secret_key), oauthToken, oauthTokenSecret).getAccessToken(verifier);
        requestTokenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.body() != null) {
                    SessionManager.setOauthToken(context, getOathToken(response.body()));
                    SessionManager.setOauthTokenSecret(context, getOathTokenSecret(response.body()));
                    view.startMainActivity();
                    view.hideProgress();

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
