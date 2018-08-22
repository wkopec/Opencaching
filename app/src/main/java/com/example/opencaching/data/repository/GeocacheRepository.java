package com.example.opencaching.data.repository;

import com.example.opencaching.data.models.okapi.Geocache;
import com.example.opencaching.data.repository.base.RealmRepository;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeocacheRepository extends RealmRepository<Geocache> {

    public GeocacheRepository(Realm realm) {
        super(realm);
    }

    public RealmResults<Geocache> loadMapFilteredGeocaches() {
        return query(realm -> realm.where(Geocache.class)
                .equalTo("isFound", true)
                //.isNotEmpty("locations")
                .findAll());
    }
}
