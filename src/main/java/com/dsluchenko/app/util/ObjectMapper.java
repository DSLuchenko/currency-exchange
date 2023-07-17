package com.dsluchenko.app.util;

public interface ObjectMapper<D, E> {
    D mapToDTO(E entity);

    E mapToEntity(D dto);
}
