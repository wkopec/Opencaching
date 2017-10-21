package com.example.opencaching.fragments.geocache_info;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.opencaching.R;
import com.example.opencaching.interfaces.Presenter;
import com.example.opencaching.models.okapi.Geocache;
import com.example.opencaching.retrofit.OpencachingApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.ApiUtils.checkForErrors;
import static com.example.opencaching.utils.UserUtils.getUserSecretToken;
import static com.example.opencaching.utils.UserUtils.getUserToken;

/**
 * Created by Wojtek on 12.08.2017.
 */

public class GeocacheInfoFragmentPresenter implements Presenter {

    private final static String INFO_STANDARD_FIELDS = "name|location|type|status|url|owner|gc_code|is_found|is_not_found|is_watched|founds|notfounds|willattends|size2|difficulty|terrain|trip_time|trip_distance|rating|rating_votes|recommendations|req_passwd|short_description|description|hint2|images|attr_acodes|my_notes|trackables_count|trackables|date_created|date_hidden";

    private GeocacheInfoFragmentView view;
    private Context context;

    public GeocacheInfoFragmentPresenter(GeocacheInfoFragmentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGeocacheInfo(String code) {

        Call<Geocache> loginCall = OpencachingApi.service(context.getResources().getString(R.string.opencaching_key), context.getResources().getString(R.string.opencaching_secret_key), getUserToken(context), getUserSecretToken(context)).getGeocacheInfo(code, INFO_STANDARD_FIELDS);
        loginCall.enqueue(new Callback<Geocache>() {
            @Override
            public void onResponse(@NonNull Call<Geocache> call, @NonNull Response<Geocache> response) {
                android.util.Log.d("Retrofit response code", String.valueOf(response.code()));
                Geocache geocache = response.body();
                if(geocache != null) {
                    setGeocacheInfo(geocache);
                }
                view.hideProgress();
                checkForErrors(response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<Geocache> call, @NonNull Throwable t) {
                if (t.getMessage() != null)
                    android.util.Log.d("Retrofit fail", t.getMessage());
            }
        });
    }

    private void setGeocacheInfo(Geocache geocache) {
        android.util.Log.d("Test", "YEY");
        android.util.Log.d("Test", geocache.getName());
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
