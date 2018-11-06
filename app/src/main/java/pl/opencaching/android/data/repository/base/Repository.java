package pl.opencaching.android.data.repository.base;

import java.util.List;

import io.realm.Realm;

public interface Repository<T, S extends Specification> {

    void addOrUpdate(T item);

    void addOrUpdate(List<T> items);

    void addOrUpdate(List<T> items, Realm.Transaction.OnSuccess callback);

    void remove(T item);

    void removeList(List<T> items);

    List<T> query(S specification);

}
