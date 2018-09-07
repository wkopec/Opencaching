package com.example.opencaching.app.di.modules;

import com.example.opencaching.api.NetworkService;
import com.example.opencaching.api.OAuthInterceptor;
import com.example.opencaching.app.di.ApplicationScope;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@ApplicationScope
@Module
public class NetworkModule {

    private static final String API_BASE_URL = "http://opencaching.pl/okapi/services/";

    @Provides
    @ApplicationScope
    @Inject
    HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @ApplicationScope
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @ApplicationScope
    OkHttpClient provideOkhttpClient(HttpLoggingInterceptor httpLoggingInterceptor, OAuthInterceptor oAuthInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(oAuthInterceptor)
                .build();
    }

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @ApplicationScope
    NetworkService provideNetworkService(Retrofit retrofit){
        return retrofit.create(NetworkService.class);
    }
}