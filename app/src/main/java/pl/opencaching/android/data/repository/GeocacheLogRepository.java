package pl.opencaching.android.data.repository;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.repository.base.RealmRepository;

public class GeocacheLogRepository extends RealmRepository<GeocacheLog> {

    @Inject
    public GeocacheLogRepository(Realm realm) {
        super(realm);
    }

    public RealmList<GeocacheLog> loadLogsByCode(String code) {
        Geocache geocache = realm.where(Geocache.class)
                .equalTo("code", code)
                .findFirst();
        return geocache != null ? geocache.getGeocacheLogs() : null;
    }

}
