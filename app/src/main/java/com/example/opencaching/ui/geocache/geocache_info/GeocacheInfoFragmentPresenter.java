package com.example.opencaching.ui.geocache.geocache_info;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.opencaching.R;
import com.example.opencaching.network.models.Error;
import com.example.opencaching.ui.base.BasePresenter;
import com.example.opencaching.network.models.okapi.Geocache;
import com.example.opencaching.network.api.OpencachingApi;
import com.example.opencaching.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.Constants.GEOCACHE_INFO_FIELDS;

/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoFragmentPresenter extends BasePresenter implements GeocacheInfoContract.Presenter {

    private GeocacheInfoContract.View view;
    private Context context;

    public GeocacheInfoFragmentPresenter(GeocacheInfoContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGeocacheInfo(String code) {

        Call<Geocache> loginCall = OpencachingApi.service(context).getGeocacheInfo(code, GEOCACHE_INFO_FIELDS);
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
