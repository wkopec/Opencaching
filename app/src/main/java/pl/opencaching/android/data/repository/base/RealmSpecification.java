package pl.opencaching.android.data.repository.base;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public interface RealmSpecification<T extends RealmObject> extends Specification {

    RealmResults<T> toRealmResults(Realm realm);

}
