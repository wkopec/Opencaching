package com.example.opencaching.api;

import com.example.opencaching.data.models.geocoding.GeocodingResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Volfram on 08.09.2018.
 */

public interface GoogleMapsService {

    @GET("maps/api/geocode/json")
    Call<GeocodingResults> getLoation(@Query("address") String address, @Query("key") String apiKey);

}
