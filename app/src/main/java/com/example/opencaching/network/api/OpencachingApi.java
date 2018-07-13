package com.example.opencaching.network.api;

import android.content.Context;

import com.example.opencaching.network.models.okapi.Geocache;
import com.example.opencaching.network.models.okapi.GeocacheLog;
import com.example.opencaching.network.models.okapi.User;
import com.example.opencaching.network.models.okapi.WaypointResults;
import com.example.opencaching.utils.UserUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.opencaching.utils.Constants.OPENCACHING_CONSUMER_KEY;
import static com.example.opencaching.utils.Constants.OPENCACHING_CONSUMER_KEY_SECRET;
import static com.example.opencaching.utils.StringUtils.REQUET_DATE_FORMAT;

/**
 * Created by Volfram on 16.07.2017.
 */

public class OpencachingApi {

    public static final String API_BASE_URL = "http://opencaching.pl/okapi/services/";

    public static OpencachingApi.Calls service(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();

        OpencachingApi.Calls service = retrofit.create(OpencachingApi.Calls.class);
        return service;
    }

    public static OpencachingApi.Calls service(Context context) {
        return service(OPENCACHING_CONSUMER_KEY, OPENCACHING_CONSUMER_KEY_SECRET, UserUtils.getOauthToken(context), UserUtils.getOauthTokenSecret(context));
    }

    public static OpencachingApi.Calls service(String consumerKey, String consumerSecret, String tokenKey, String tokenSecret) {

        OAuthInterceptor oAuthInterceptor = new OAuthInterceptor.Builder()
                .consumerKey(consumerKey)
                .consumerSecret(consumerSecret)
                .tokenKey(tokenKey)
                .tokenSecret(tokenSecret)
                .build();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(oAuthInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat(REQUET_DATE_FORMAT)
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(Calls.class);
    }

    public interface Calls {

        //AUTH
        @GET("oauth/request_token")
        Call<String> getRequestToken(@Query("oauth_callback") String oauthCallback);

        @GET("oauth/access_token")
        Call<String> getAccessToken(@Query("oauth_verifier") String oauthVerifier);

        //USER

        @GET("users/user")
        Call<User> getLoggedInUserInfo(@Query("fields") String fields);

        //GEOCACHE

        @GET("caches/search/nearest")
        Call<WaypointResults> getWaypoints(@Query("consumer_key") String consumerKey, @Query("center") String center, @Query("limit") int limit, @Query("radius") int radius);

        @GET("caches/geocaches")
        Call<Map<String, Geocache>> getGeocaches(@Query("consumer_key") String consumerKey, @Query("cache_codes") String codes, @Query("fields") String fields);

        @GET("logs/logs")
        Call<ArrayList<GeocacheLog>> getGeocacheLogs(@Query("consumer_key") String consumerKey, @Query("cache_code") String code, @Query("fields") String fields, @Query("offset") int offset, @Query("limit") int limit);

        @GET("caches/geocache")
        Call<Geocache> getGeocacheInfo(@Query("cache_code") String code, @Query("fields") String fields);


    }

}
