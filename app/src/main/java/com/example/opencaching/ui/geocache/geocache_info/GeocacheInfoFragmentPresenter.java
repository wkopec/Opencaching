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

import static com.example.opencaching.utils.UserUtils.getUserSecretToken;
import static com.example.opencaching.utils.UserUtils.getUserToken;

/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoFragmentPresenter extends BasePresenter implements GeocacheInfoContract.Presenter {

    private final static String INFO_STANDARD_FIELDS = "name|location|type|status|url|owner|gc_code|is_found|is_not_found|is_watched|founds|notfounds|willattends|size2|difficulty|terrain|trip_time|trip_distance|rating|rating_votes|recommendations|req_passwd|short_description|description|hint2|images|attr_acodes|my_notes|trackables_count|trackables|date_created|date_hidden";

    private GeocacheInfoContract.View view;
    private Context context;

    public GeocacheInfoFragmentPresenter(GeocacheInfoContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGeocacheInfo(String code) {

        Call<Geocache> loginCall = OpencachingApi.service(context.getResources().getString(R.string.opencaching_key), context.getResources().getString(R.string.opencaching_secret_key), getUserToken(context), getUserSecretToken(context)).getGeocacheInfo(code, INFO_STANDARD_FIELDS);
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
