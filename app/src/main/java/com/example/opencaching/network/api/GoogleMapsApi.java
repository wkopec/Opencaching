package com.example.opencaching.network.api;

import android.util.Log;

import com.example.opencaching.BuildConfig;
import com.example.opencaching.network.models.geocoding.GeocodingResults;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Wojtek on 26.07.2017.
 */

public class GoogleMapsApi {
    private static Calls service;
    public static Calls service(){
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getClient())
                    .build();
            service = retrofit.create(Calls.class);
        }
        return service;
    }

    private static OkHttpClient getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i("GOOGLE LOGGER", message);
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
        return httpClient.build();
    }

    public interface Calls {

        @GET("maps/api/geocode/json")
        Call<GeocodingResults> getLoation(@Query("address") String address, @Query("key") String apiKey);

    }
}
