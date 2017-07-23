package com.example.opencaching.retrofit;

import com.example.opencaching.models.Geocache;
import com.example.opencaching.models.Results;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Volfram on 16.07.2017.
 */

public class OpencachingApi {

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://opencaching.pl/okapi/")

            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OpencachingApi.Calls service;

    public static OpencachingApi.Calls service() {
        if (service == null) {
            service = retrofit.create(OpencachingApi.Calls.class);
        }
        return service;
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

    public interface Calls {


        @GET("services/caches/search/nearest")
        Call<Results> getWaypoints(@Query("consumer_key") String consumerKey, @Query("center") String center, @Query("limit") int limit, @Query("radius") int radius);

        @GET("services/caches/geocaches")
        Call<Map<String, Geocache>> getGeocaches(@Query("consumer_key") String consumerKey, @Query("cache_codes") String codes, @Query("fields") String fields);

    }




}
