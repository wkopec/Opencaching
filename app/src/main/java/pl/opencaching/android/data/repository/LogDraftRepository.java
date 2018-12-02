package pl.opencaching.android.data.repository;

import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.data.repository.base.RealmRepository;

public class LogDraftRepository extends RealmRepository<GeocacheLogDraft> {

    @Inject
    LogDraftRepository(Realm realm) {
        super(realm);
    }

    public GeocacheLogDraft loadLogDrawByUuid(String logDrawUuid) {
        return realm.where(GeocacheLogDraft.class)
                .equalTo("uuid", logDrawUuid)
                .findFirst();
    }

    public RealmResults<GeocacheLogDraft> loadLogDrawsForGeocache(String geocacheCode) {
        return realm.where(GeocacheLogDraft.class)
                .equalTo("geocacheCode", geocacheCode)
                .findAll();
    }

    public RealmResults<GeocacheLogDraft> loadAllLogDraws() {
        return realm.where(GeocacheLogDraft.class)
                .findAll();
    }

    public RealmResults<GeocacheLogDraft> loadAllLogDrawsBySyncReadyAscending(boolean isReadyToSync) {
        return realm.where(GeocacheLogDraft.class)
                .equalTo("isReadyToSync", isReadyToSync)
                .sort("date", Sort.ASCENDING)
                .findAll();
    }

    @Override
    public void addOrUpdate(GeocacheLogDraft item) {
        checkNotNull(item.isReadyToSync());
        super.addOrUpdate(item);
    }

    @Override
    public void addOrUpdate(List<GeocacheLogDraft> items) {
        for(GeocacheLogDraft logDraw : items) {
            checkNotNull(logDraw.isReadyToSync());
        }
        super.addOrUpdate(items);
    }

    @Override
    public void addOrUpdate(List<GeocacheLogDraft> items, Realm.Transaction.OnSuccess callback) {
        for(GeocacheLogDraft logDraw : items) {
            checkNotNull(logDraw.isReadyToSync());
        }
        super.addOrUpdate(items, callback);
    }
}
