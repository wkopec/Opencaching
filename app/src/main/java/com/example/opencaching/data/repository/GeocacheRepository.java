package com.example.opencaching.data.repository;

import com.example.opencaching.app.prefs.MapFiltersManager;
import com.example.opencaching.data.models.okapi.Geocache;
import com.example.opencaching.data.repository.base.RealmRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeocacheRepository extends RealmRepository<Geocache> {

    private MapFiltersManager mapFiltersManager;

    @Inject
    public GeocacheRepository(Realm realm, MapFiltersManager mapFiltersManager) {
        super(realm);
        this.mapFiltersManager = mapFiltersManager;
    }

    public Geocache loadGeocacheByCode(String code) {
        return realm.where(Geocache.class)
                .equalTo("code", code)
                .findFirst();
    }

    public RealmResults<Geocache> loadMapFilteredGeocaches() {
        RealmResults<Geocache> results = query(realm -> realm.where(Geocache.class)
                .equalTo("isFound", mapFiltersManager.isFoundFilter())
                //.equalTo("isFound", mapFiltersManager.isNotFoundFilter())
                .findAll());

        if(!mapFiltersManager.isTraditionalFilter()) {
            results.where().notEqualTo("type", "Traditional").findAll();
        }

        //FIXME
        ArrayList<Geocache> geocaches = new ArrayList<>();
        for(Geocache geocache : results) {
            try {
                geocaches.add((Geocache) geocache.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        //return new ArrayList<>(results);

        return results;

    }
}
