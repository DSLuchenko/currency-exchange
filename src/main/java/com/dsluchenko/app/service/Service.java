package com.dsluchenko.app.service;

import java.util.List;

public interface Service <T>{

    List<T> getAll();

    T create(T t);
}
