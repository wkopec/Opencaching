package pl.opencaching.android.app;

import net.danlew.android.joda.JodaTimeAndroid;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.opencaching.android.app.di.DaggerApplicationComponent;


/**
 * Created by Volfram on 15.07.2017.
 */

public class App extends DaggerApplication {

    private static final String DB_NAME = "OpencachingDB.realm";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        setupRealm();
        JodaTimeAndroid.init(this);
    }

    private void setupRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(DB_NAME)
                .schemaVersion(DatabaseMigration.REALM_SCHEMA_VERSION)
                .migration(new DatabaseMigration())
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().create(this);
    }
}
