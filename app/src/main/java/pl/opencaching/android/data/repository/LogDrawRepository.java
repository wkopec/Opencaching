package pl.opencaching.android.data.repository;

import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;
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

    @Override
    public void addOrUpdate(GeocacheLogDraw item) {
        checkNotNull(item.isReadyToSync());
        super.addOrUpdate(item);
    }

    @Override
    public void addOrUpdate(List<GeocacheLogDraw> items) {
        for(GeocacheLogDraw logDraw : items) {
            checkNotNull(logDraw.isReadyToSync());
        }
        super.addOrUpdate(items);
    }

    @Override
    public void addOrUpdate(List<GeocacheLogDraw> items, Realm.Transaction.OnSuccess callback) {
        for(GeocacheLogDraw logDraw : items) {
            checkNotNull(logDraw.isReadyToSync());
        }
        super.addOrUpdate(items, callback);
    }
}
