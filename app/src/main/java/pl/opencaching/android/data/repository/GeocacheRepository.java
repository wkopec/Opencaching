package pl.opencaching.android.data.repository;

import io.realm.RealmList;
import pl.opencaching.android.api.OkapiService;
import pl.opencaching.android.app.prefs.MapFiltersManager;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.repository.base.RealmRepository;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class GeocacheRepository extends RealmRepository<Geocache> {

    private MapFiltersManager mapFiltersManager;
    private SessionManager sessionManager;
    private OkapiService okapiService;

    @Inject
    public GeocacheRepository(Realm realm, MapFiltersManager mapFiltersManager, SessionManager sessionManager, OkapiService okapiService) {
        super(realm);
        this.mapFiltersManager = mapFiltersManager;
        this.sessionManager = sessionManager;
        this.okapiService = okapiService;
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

        if (!mapFiltersManager.isFoundFilter()) {
            query.notEqualTo("isFound", true);
        }
        if (!mapFiltersManager.isNotFoundFilter()) {
            query.beginGroup()
                    .notEqualTo("isFound", false)
                    .or()
                    .equalTo("owner.uuid", sessionManager.getUserUuid())
                .endGroup();
        }

        if (!mapFiltersManager.isAvailableFilter()) {
            query.notEqualTo("status", "Available");
        }
        if (!mapFiltersManager.isTempUnavailableFilter()) {
            query.notEqualTo("status", "Temporarily unavailable");
        }
        if (!mapFiltersManager.isArchivedFilter()) {
            query.notEqualTo("status", "Archived");
        }
        if (!mapFiltersManager.isOwnedFilter()) {
            query.notEqualTo("owner.uuid", sessionManager.getUserUuid());
        }
        if (!mapFiltersManager.isIgnoredFilter()) {
            query.notEqualTo("isIgnored", true);
        }
        if (mapFiltersManager.isFTFFilter()) {
            query.equalTo("founds", 0);
        }
        if (mapFiltersManager.isTrackableFilter()) {
            query.notEqualTo("trackablesCount", 0);
        }
//        if(mapFiltersManager.isPowerTrailFilter()) {
//            query.equalTo("isPowerTrail", true);
//        }

        // Geocaches
        if (!mapFiltersManager.isTraditionalFilter()) {
            query.notEqualTo("type", "Traditional");
        }
        if (!mapFiltersManager.isMulticacheFilter()) {
            query.notEqualTo("type", "Multi");
        }
        if (!mapFiltersManager.isQuizFilter()) {
            query.notEqualTo("type", "Quiz");
        }
        if (!mapFiltersManager.isUnknownFilter()) {
            query.notEqualTo("type", "Other");
        }
        if (!mapFiltersManager.isVirtualFilter()) {
            query.notEqualTo("type", "Virtual");
        }
        if (!mapFiltersManager.isEventFilter()) {
            query.notEqualTo("type", "Event");
        }
        if (!mapFiltersManager.isOwncacheFilter()) {
            query.notEqualTo("type", "Own");
        }
        if (!mapFiltersManager.isMovingFilter()) {
            query.notEqualTo("type", "Moving");
        }
        if (!mapFiltersManager.isWebcamFilter()) {
            query.notEqualTo("type", "Webcam");
        }
        return query;
    }

    // Geocache logs

    public RealmList<GeocacheLog> loadLogsByCode(String code) {
        return realm.where(Geocache.class)
                .equalTo("code", code)
                .findFirst().getLatestGeocacheLogs();
    }

}
