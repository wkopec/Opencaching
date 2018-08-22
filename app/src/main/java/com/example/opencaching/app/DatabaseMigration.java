package com.example.opencaching.app;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class DatabaseMigration implements RealmMigration {

    static final int REALM_SCHEMA_VERSION = 0;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
//        RealmSchema schema = realm.getSchema();
//
//        switch ((int) oldVersion) {
//            case 1:
//                schema.get("Object")
//                        .addField("field", String.class);
//
//        }
    }
}
