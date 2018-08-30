package com.example.opencaching.app.di.modules;

import com.example.opencaching.app.di.ApplicationScope;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

@ApplicationScope
@Module
public class NetworkModule {

//    @Provides
//    @ApplicationScope
//    @Inject
//    TokenAuthInterceptor provideTokenAuthInterceptor(Context context){
//        return new TokenAuthInterceptor(new AuthRepository(context));
//    }
//
//    @Provides
//    @ApplicationScope
//    @Inject
//    Cache provideHttpCache(Context context) {
//        return new Cache(context.getCacheDir(), Config.CACHE_SIZE);
//    }
//
//    @Provides
//    @ApplicationScope
//    Gson provideGson() {
//        return new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .create();
//    }
//
//    @Provides
//    @ApplicationScope
//    OkHttpClient provideOkhttpClient(Cache cache, TokenAuthInterceptor tokenAuthInterceptor) {
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        return new OkHttpClient.Builder()
//                .cache(cache)
//                .addInterceptor(tokenAuthInterceptor)
//                .addInterceptor(loggingInterceptor)
//                .build();
//    }
//
//    @Provides
//    @ApplicationScope
//    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
//        return new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl(Config.BASE_URL)
//                .client(okHttpClient)
//                .build();
//    }
//
//    @Provides
//    @ApplicationScope
//    NetworkService provideNetworkService(Retrofit retrofit){
//        return retrofit.create(NetworkService.class);
//    }
}