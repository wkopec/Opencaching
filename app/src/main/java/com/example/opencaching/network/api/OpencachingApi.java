package com.example.opencaching.network.api;

import com.example.opencaching.network.models.okapi.Geocache;
import com.example.opencaching.network.models.okapi.GeocacheLog;
import com.example.opencaching.network.models.okapi.WaypointResults;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Volfram on 16.07.2017.
 */

public class OpencachingApi extends DefaultApi10a {

    public static final String API_BASE_URL = "http://opencaching.pl/okapi/services/";

    public static OpencachingApi.Calls service(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();

        OpencachingApi.Calls service = retrofit.create(OpencachingApi.Calls.class);
        return service;
    }

    public static OpencachingApi.Calls service(String consumerKey, String consumerSecret, String tokenKey, String tokenSecret) {

        OAuthInterceptor oauth1Woocommerce = new OAuthInterceptor.Builder()
                .consumerKey(consumerKey)
                .consumerSecret(consumerSecret)
                .tokenKey(tokenKey)
                .tokenSecret(tokenSecret)
                .build();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(oauth1Woocommerce)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        OpencachingApi.Calls service = retrofit.create(OpencachingApi.Calls.class);
        return service;
    }

    public interface Calls {

        //AUTH

        @GET("oauth/request_token")
        Call<String> get(@Query("consumer_key") String consumerKey, @Query("center") String center, @Query("limit") int limit, @Query("radius") int radius);


        //GEOCACHE

        @GET("caches/search/nearest")
        Call<WaypointResults> getWaypoints(@Query("consumer_key") String consumerKey, @Query("center") String center, @Query("limit") int limit, @Query("radius") int radius);

        @GET("caches/geocaches")
        Call<Map<String, Geocache>> getGeocaches(@Query("consumer_key") String consumerKey, @Query("cache_codes") String codes, @Query("fields") String fields);

        @GET("logs/logs")
        Call<ArrayList<GeocacheLog>> getGeocacheLogs(@Query("consumer_key") String consumerKey, @Query("cache_code") String code, @Query("fields") String fields, @Query("offset") int offset, @Query("limit") int limit);

        @GET("caches/geocache")
        Call<Geocache> getGeocacheInfo(@Query("cache_code") String code, @Query("fields") String fields);
        //Call<Geocache> getGeocacheInfo(@Query("consumer_key") String consumerKey, @Query("cache_code") String code, @Query("fields") String fields);

    }

    //OAUTH

    private static final String AUTHORIZE_URL = "http://opencaching.pl/okapi/services/oauth/authorize?oauth_token=%s";

    private static class InstanceHolder {
        private static final OpencachingApi INSTANCE = new OpencachingApi();
    }

    public static OpencachingApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://opencaching.pl/okapi/services/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://opencaching.pl/okapi/services/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

}
