package com.dsluchenko.app.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(int id);

    List<T> getAll();

    int save(T t);

    int update(T t, String[] params);

    int delete(T t);
}
