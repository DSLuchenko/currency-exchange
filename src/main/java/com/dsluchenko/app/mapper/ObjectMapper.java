package com.dsluchenko.app.mapper;

interface ObjectMapper<D, E> {
    D mapToDTO(E entity);

    E mapToEntity(D dto);
}
