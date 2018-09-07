package com.example.opencaching.ui.geocache.info;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.opencaching.api.NetworkService;
import com.example.opencaching.data.models.Error;
import com.example.opencaching.ui.base.BasePresenter;
import com.example.opencaching.data.models.okapi.Geocache;

import com.example.opencaching.utils.ApiUtils;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.Constants.GEOCACHE_INFO_FIELDS;

/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoPresenter extends BasePresenter implements GeocacheInfoContract.Presenter {

    @Inject
    NetworkService networkService;

    private GeocacheInfoContract.View view;
    private Context context;

    public GeocacheInfoPresenter(GeocacheInfoContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGeocacheInfo(String code) {

        Call<Geocache> loginCall = networkService.getGeocacheInfo(code, GEOCACHE_INFO_FIELDS);
        loginCall.enqueue(new Callback<Geocache>() {
            @Override
            public void onResponse(@NonNull Call<Geocache> call, @NonNull Response<Geocache> response) {
                if (response.body() != null){
                    setGeocacheInfo(response.body());
                } else {
                    if (response.errorBody() != null){
                        view.showError(new Error(response.message()));
                    }
                }
                view.hideProgress();
            }
            @Override
            public void onFailure(@NonNull Call<Geocache> call, @NonNull Throwable t) {
                view.hideProgress();
                view.showError(ApiUtils.getErrorSingle(t));
            }
        });
    }

    private void setGeocacheInfo(Geocache geocache) {
        android.util.Log.d("Test", "YEY");
        android.util.Log.d("Test", geocache.getName());
    }

}
