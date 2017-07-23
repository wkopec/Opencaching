package com.example.opencaching.fragments.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opencaching.R;
import com.example.opencaching.interfaces.Presenter;
import com.example.opencaching.models.Geocache;
import com.example.opencaching.models.Results;

import com.example.opencaching.retrofit.OpencachingApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.opencaching.utils.ApiUtils.checkForErrors;
import static com.example.opencaching.utils.Constants.GEOCACHES_STANDARD_FIELDS;
import static com.example.opencaching.utils.ResourceUtils.getGeocacheSize;
import static com.example.opencaching.utils.StringUtils.getApiFormatedFields;

/**
 * Created by Volfram on 15.07.2017.
 */

public class MapFragmentPresenter implements Presenter {

    private static final int GEOCACHE_REQUEST_LIMIT = 500;      //max 500

    private MapFragmentView view;
    private Context context;
    private String center;
    private Map<String, Geocache> displayedDeocaches;


    public MapFragmentPresenter(MapFragmentView view, Context context) {
        this.view = view;
        this.context = context;
        displayedDeocaches = new HashMap<>();
    }

    public void getWaypoints(String center, int radius) {
        this.center = center;
        Call<Results> loginCall = OpencachingApi.service().getWaypoints(context.getResources().getString(R.string.opencaching_key), center, GEOCACHE_REQUEST_LIMIT, radius);
        //view.showProgress();
        loginCall.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(@NonNull Call<Results> call, @NonNull Response<Results> response) {
                Log.d("Retrofit response code", String.valueOf(response.code()));
                Results waypoints = response.body();
                switch (response.code()) {
                    case 200:
                        if (waypoints != null && !waypoints.getResults().isEmpty()) {
                            getGeocaches(getApiFormatedFields(waypoints.getResults()), waypoints.isMore());
                        }
                        break;
                    default:
                }
                checkForErrors(response.errorBody());
                view.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<Results> call, @NonNull Throwable t) {
                if(t.getMessage() != null)
                    Log.d("Retrofit fail", t.getMessage());
                view.hideProgress();
            }
        });
    }

    private void getGeocaches(String codes, final boolean isMore) {
        Call<Map<String, Geocache>> loginCall = OpencachingApi.service().getGeocaches(context.getResources().getString(R.string.opencaching_key), codes, GEOCACHES_STANDARD_FIELDS);
        //view.showProgress();
        loginCall.enqueue(new Callback<Map<String, Geocache>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Geocache>> call, @NonNull Response<Map<String, Geocache>> response) {
                Log.d("Retrofit response code", String.valueOf(response.code()));
                Map<String, Geocache> geocaches = response.body();
                switch (response.code()) {
                    case 200:
                        if (geocaches != null) {
                            view.addGeocachesOnMap(geocaches);
                            if(isMore)
                                view.showMapInfo(context.getString(R.string.move_map_to_show_more_geocaches));
                            else
                                view.hideMapInfo();
                        }
                        break;
                    default:
                }
                checkForErrors(response.errorBody());
                view.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Geocache>> call, @NonNull Throwable t) {
                if(t.getMessage() != null)
                    Log.d("Retrofit fail", t.getMessage());
                view.hideProgress();
            }
        });
    }




    public void setMarkerWindowData(View view, Geocache geocache) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView codeTextView = (TextView) view.findViewById(R.id.code);
        TextView sizeTextView = (TextView) view.findViewById(R.id.sizeTextView);
        TextView rateTextView = (TextView) view.findViewById(R.id.rateTextView);
        TextView ownerTextView = (TextView) view.findViewById(R.id.ownerTextView);
        TextView foundsTextView = (TextView) view.findViewById(R.id.foundTextView);
        TextView notFoundsTextView = (TextView) view.findViewById(R.id.notFoundTextView);
        TextView recommendationsTextView = (TextView) view.findViewById(R.id.recommendationTextView);

        nameTextView.setText(geocache.getName());
        codeTextView.setText(geocache.getCode());
        sizeTextView.setText(context.getString(R.string.marker_window_size) + " " + context.getString(getGeocacheSize(geocache.getSize())));
        ownerTextView.setText(context.getString(R.string.marker_window_owner) + " " + geocache.getOwner().getUsername());
        String[] rates = context.getResources().getStringArray(R.array.geocache_rates);
        if((int)geocache.getRating() > 0 )
            rateTextView.setText(context.getString(R.string.marker_window_rating) + " " + rates[(int)geocache.getRating() - 1]);
        else
            rateTextView.setVisibility(View.GONE);

        foundsTextView.setText(geocache.getFounds() + " x " + context.getString(R.string.found));
        notFoundsTextView.setText(geocache.getNotfounds() + " x " + context.getString(R.string.not_found));

        if(geocache.getRecommendations() == 0) {
            ImageView recommendationImageView = (ImageView) view.findViewById(R.id.recommendationImageView);
            recommendationImageView.setVisibility(View.GONE);
            recommendationsTextView.setVisibility(View.GONE);
        } else {
            recommendationsTextView.setText(geocache.getRecommendations() + " x " + context.getString(R.string.recommended));
        }
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {

    }
}