package com.example.opencaching.data.repository.base;

import java.util.List;

public interface Repository<T, S extends Specification> {

    void addOrUpdate(T item);

    void addOrUpdate(List<T> items);

    void remove(T item);

    void removeList(List<T> items);

    List<T> query(S specification);

}
