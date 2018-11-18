package pl.opencaching.android.data.repository;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import pl.opencaching.android.data.models.okapi.GeocacheLog;
import pl.opencaching.android.data.repository.base.RealmRepository;

public class GeocacheLogRepository extends RealmRepository<GeocacheLog> {

    @Inject
    public GeocacheLogRepository(Realm realm) {
        super(realm);
    }

    public RealmResults<GeocacheLog> loadLogsByCode(String code) {
        return realm
                .where(GeocacheLog.class)
                .equalTo("geocacheCode", code)
                .sort("date", Sort.DESCENDING)
                .findAll();
    }

}
