package com.dsluchenko.app.data.dao;

import java.util.List;
import java.util.Optional;

interface Dao<T> {
    Optional<T> get(int id);

    List<T> getAll();

    int save(T t);

    int update(T t, String[] params);

    int delete(T t);
}
