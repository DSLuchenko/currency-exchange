package com.dsluchenko.app.service;

import java.util.List;
import java.util.Optional;

public interface Service <T>{
    Optional<T> findById(int id);

    List<T> getAll();

    void create(T t);

    void update(T t, String[] params);

    void delete(T t);
}