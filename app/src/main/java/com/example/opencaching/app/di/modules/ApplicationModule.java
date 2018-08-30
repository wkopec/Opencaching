package com.example.opencaching.app.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.opencaching.BuildConfig;
import com.example.opencaching.app.App;
import com.example.opencaching.app.di.ApplicationScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public abstract class ApplicationModule {

    @Binds
    abstract Context bindContext(App app);

    @Provides
    @ApplicationScope
    public static SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    public static Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

}
