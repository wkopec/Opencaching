package com.example.opencaching.data.repository.base;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmRepository<T extends RealmObject> implements Repository<T, RealmSpecification<T>> {

    protected Realm realm;

    public RealmRepository(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void addOrUpdate(T item) {
        realm.executeTransaction(realm1 -> {
            realm1.copyToRealmOrUpdate(item);
        });
    }

    @Override
    public void addOrUpdate(List<T> items) {
        realm.executeTransaction(realm1 -> {
            realm1.copyToRealmOrUpdate(items);
        });
    }

    @Override
    public void remove(T item) {
        realm.executeTransaction(realm1 -> {
            item.deleteFromRealm();
        });
    }

    @Override
    public void removeList(List<T> items) {
        realm.executeTransaction(realm1 -> {
            for (int i = 0; i < items.size(); i++)
                items.get(i).deleteFromRealm();
        });
    }

    @Override
    public RealmResults<T> query(RealmSpecification<T> specification) {
        return specification.toRealmResults(realm);
    }
}
