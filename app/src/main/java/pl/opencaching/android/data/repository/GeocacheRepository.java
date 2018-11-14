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

import static pl.opencaching.android.utils.Constants.GEOCACHE_STATUS_ARCHIVED;
import static pl.opencaching.android.utils.Constants.GEOCACHE_STATUS_AVAILABLE;
import static pl.opencaching.android.utils.Constants.GEOCACHE_STATUS_TEMP_UNAVAILABLE;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_EVENT;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_EWBCAM;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_MOVING;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_MULTI;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_OTHER;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_OWN;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_QUIZ;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_TRADITIONAL;
import static pl.opencaching.android.utils.Constants.GEOCACHE_TYPE_VIRTUAL;

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
            query.notEqualTo("status", GEOCACHE_STATUS_AVAILABLE);
        }
        if (!mapFiltersManager.isTempUnavailableFilter()) {
            query.notEqualTo("status", GEOCACHE_STATUS_TEMP_UNAVAILABLE);
        }
        if (!mapFiltersManager.isArchivedFilter()) {
            query.notEqualTo("status", GEOCACHE_STATUS_ARCHIVED);
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
            query.notEqualTo("type", GEOCACHE_TYPE_TRADITIONAL);
        }
        if (!mapFiltersManager.isMulticacheFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_MULTI);
        }
        if (!mapFiltersManager.isQuizFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_QUIZ);
        }
        if (!mapFiltersManager.isUnknownFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_OTHER);
        }
        if (!mapFiltersManager.isVirtualFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_VIRTUAL);
        }
        if (!mapFiltersManager.isEventFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_EVENT);
        }
        if (!mapFiltersManager.isOwncacheFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_OWN);
        }
        if (!mapFiltersManager.isMovingFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_MOVING);
        }
        if (!mapFiltersManager.isWebcamFilter()) {
            query.notEqualTo("type", GEOCACHE_TYPE_EWBCAM);
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
