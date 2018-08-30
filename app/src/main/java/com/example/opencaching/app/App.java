package com.example.opencaching.app;

import com.example.opencaching.app.di.DaggerApplicationComponent;

import net.danlew.android.joda.JodaTimeAndroid;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.opencaching.app.DatabaseMigration.REALM_SCHEMA_VERSION;

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
                .schemaVersion(REALM_SCHEMA_VERSION)
                .migration(new DatabaseMigration())
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().create(this);
    }
}
