package pl.opencaching.android.data.repository;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.repository.base.RealmRepository;

public class LogDrawRepository extends RealmRepository<GeocacheLogDraw> {

    @Inject
    LogDrawRepository(Realm realm) {
        super(realm);
    }

    public RealmResults<GeocacheLogDraw> loadLogDrawsForGeocache(String geocacheCode) {
        return realm.where(GeocacheLogDraw.class)
                .equalTo("geocacheCode", geocacheCode)
                .findAll();
    }

    public RealmResults<GeocacheLogDraw> loadAllLogDraws() {
        return realm.where(GeocacheLogDraw.class)
                .findAll();
    }

    public RealmResults<GeocacheLogDraw> loadAllLogDrawsBySyncReady(boolean isReadyToSync) {
        return realm.where(GeocacheLogDraw.class)
                .equalTo("isReadyToSync", isReadyToSync)
                .findAll();
    }

}
