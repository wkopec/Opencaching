package com.example.opencaching.api;

import com.example.opencaching.data.models.okapi.Geocache;
import com.example.opencaching.data.models.okapi.GeocacheLog;
import com.example.opencaching.data.models.okapi.User;
import com.example.opencaching.data.models.okapi.WaypointResults;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NetworkService {
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
    Call<WaypointResults> getWaypoints(@Query("consumer_key") String consumerKey, @Query("center") String center, @Query("limit") int limit, @Query("radius") int radius, @Query("status") String status);

    @GET("caches/geocaches")
    Call<Map<String, Geocache>> getGeocaches(@Query("consumer_key") String consumerKey, @Query("cache_codes") String codes, @Query("fields") String fields);

    @GET("logs/logs")
    Call<ArrayList<GeocacheLog>> getGeocacheLogs(@Query("consumer_key") String consumerKey, @Query("cache_code") String code, @Query("fields") String fields, @Query("offset") int offset, @Query("limit") int limit);

    @GET("caches/geocache")
    Call<Geocache> getGeocacheInfo(@Query("cache_code") String code, @Query("fields") String fields);
}
