package pl.opencaching.android.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.opencaching.android.app.di.ApplicationScope;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@ApplicationScope
@Module
public class NetworkModule {

    private static final String OKAPI_BASE_URL = "http://opencaching.pl/okapi/services/";
    private static final String GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com";

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
    OkHttpClient provideOkhttpClient(HttpLoggingInterceptor httpLoggingInterceptor, OkapiInterceptor okapiInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(okapiInterceptor)
                .build();
    }

    @Provides
    @ApplicationScope
    OkapiService provideNetworkService(Gson gson, OkHttpClient okHttpClient){
        return  new Retrofit.Builder()
                .baseUrl(OKAPI_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(OkapiService.class);
    }

    @Provides
    @ApplicationScope
    GoogleMapsService provideGoogleMapsService(Gson gson, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(GOOGLE_MAPS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build().create(GoogleMapsService.class);
    }

}