package com.dsluchenko.app.service;

import java.util.List;

interface Service <T>{

    List<T> getAll();

    T create(T t);
}
