package com.example.opencaching.data.repository;

import com.example.opencaching.app.prefs.MapFiltersManager;
import com.example.opencaching.data.models.okapi.Geocache;
import com.example.opencaching.data.repository.base.RealmRepository;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
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
        return getMapFilteredGeocachesQuerry().findAll();
    }

    public RealmResults<Geocache> loadMapFilteredGeocachesIncludes(String[] includesCodes) {
        RealmQuery<Geocache> query = getMapFilteredGeocachesQuerry();
        query.in("code", includesCodes);
        return query.findAll();
    }

    public void clearUnsavedGeocaches() {
        realm.beginTransaction();
        realm.where(Geocache.class).equalTo("isSaved", false).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    private RealmQuery<Geocache> getMapFilteredGeocachesQuerry() {

        RealmQuery<Geocache> query = realm.where(Geocache.class);
        query.beginGroup()
                .equalTo("isFound", mapFiltersManager.isFoundFilter())
                .or()
                .equalTo("isFound", !mapFiltersManager.isNotFoundFilter())
                .endGroup();

        if(!mapFiltersManager.isAvailableFilter()) {
            query.notEqualTo("status", "Available");
        }
        if(!mapFiltersManager.isTempUnavailableFilter()) {
            query.notEqualTo("status", "Temporarily unavailable");
        }
        if(!mapFiltersManager.isArchivedFilter()) {
            query.notEqualTo("status", "Archived");
        }
        if(!mapFiltersManager.isOwnedFilter()) {
            query.notEqualTo("owner.uuid", "qweqwe");
        }
        if(!mapFiltersManager.isIgnoredFilter()) {
            query.notEqualTo("isIgnored", true);
        }
        if(mapFiltersManager.isFTFFilter()) {
            query.equalTo("founds", 0);
        }
        if(mapFiltersManager.isTrackableFilter()) {
            query.notEqualTo("trackablesCount", 0);
        }
//        if(mapFiltersManager.isPowerTrailFilter()) {
//            query.equalTo("isPowerTrail", true);
//        }

        // Geocaches
        if(!mapFiltersManager.isTraditionalFilter()) {
            query.notEqualTo("type", "Traditional");
        }
        if(!mapFiltersManager.isMulticacheFilter()) {
            query.notEqualTo("type", "Multi");
        }
        if(!mapFiltersManager.isQuizFilter()) {
            query.notEqualTo("type", "Quiz");
        }
        if(!mapFiltersManager.isUnknownFilter()) {
            query.notEqualTo("type", "Other");
        }
        if(!mapFiltersManager.isVirtualFilter()) {
            query.notEqualTo("type", "Virtual");
        }
        if(!mapFiltersManager.isEventFilter()) {
            query.notEqualTo("type", "Event");
        }
        if(!mapFiltersManager.isOwncacheFilter()) {
            query.notEqualTo("type", "Own");
        }
        if(!mapFiltersManager.isMovingFilter()) {
            query.notEqualTo("type", "Moving");
        }
        if(!mapFiltersManager.isWebcamFilter()) {
            query.notEqualTo("type", "Webcam");
        }
        return query;
    }

}
