package pl.opencaching.android.app;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import pl.opencaching.android.BuildConfig;
import pl.opencaching.android.app.di.ApplicationScope;

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
