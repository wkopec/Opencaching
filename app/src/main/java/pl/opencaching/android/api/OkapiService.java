package pl.opencaching.android.api;

import java.util.ArrayList;
import java.util.Map;

import pl.opencaching.android.data.models.okapi.Attribute;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.models.okapi.NewGeocacheLogResponse;
import pl.opencaching.android.data.models.okapi.User;
import pl.opencaching.android.data.models.okapi.WaypointResults;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface OkapiService {

    //AUTH

    @GET("oauth/request_token")
    Call<String> getRequestToken(@Query("oauth_callback") String oauthCallback);

    @GET("oauth/access_token")
    Call<String> getAccessToken(@Query("oauth_verifier") String oauthVerifier);

    //ATTRIBUTES

    @GET("attrs/attribute_index")
    Call<Map<String, Attribute>> getAllAttributes(@Query("fields") String fields, @Query("langpref") String langpref, @Query("only_locally_used") boolean isLocallyUsed);

    @GET("attrs/attributes")
    Call<Void> getAttributes(@Query("acodes") String code, @Query("fields") String fields, @Query("langpref") String langpref);

    //USER

    @GET("users/user")
    Call<User> getLoggedInUserInfo(@Query("fields") String fields);

    //GEOCACHE

    @GET("caches/search/nearest")
    Call<WaypointResults> getWaypoints(@Query("center") String center, @Query("limit") int limit, @Query("radius") int radius, @Query("status") String status);

    @GET("caches/geocaches")
    Call<Map<String, Geocache>> getGeocaches(@Query("cache_codes") String codes, @Query("fields") String fields, @Query("log_fields") String logFields, @Query("lpc") int logFieldsLimit );

    @GET("caches/geocache")
    Call<Geocache> getGeocacheInfo(@Query("cache_code") String code, @Query("fields") String fields);

    //LOGS

    @GET("logs/logs")
    Call<ArrayList<GeocacheLog>> getGeocacheLogs(@Query("cache_code") String code, @Query("fields") String fields, @Query("offset") int offset, @Query("limit") int limit);

    @FormUrlEncoded
    @POST("logs/submit")
    Call<NewGeocacheLogResponse> submitNewGeocacheLog(@FieldMap Map<String, String> newLogFieldsMap);

}
