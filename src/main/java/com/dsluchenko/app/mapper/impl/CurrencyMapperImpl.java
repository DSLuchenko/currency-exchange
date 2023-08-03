package com.dsluchenko.app.mapper.impl;

import com.dsluchenko.app.dto.request.CurrencyCreateRequest;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.mapper.CurrencyMapper;

public class CurrencyMapperImpl implements CurrencyMapper {

    @Override
    public CurrencyCreateRequest mapToDTO(Currency entity) {

        return new CurrencyCreateRequest(
                entity.getFullName(),
                entity.getCode(),
                entity.getSign()
        );
    }

    @Override
    public Currency mapToEntity(CurrencyCreateRequest dto) {

        return Currency.builder()
                       .id(0)
                       .fullName(dto.name())
                       .code(dto.code())
                       .sign(dto.sign())
                       .build();
    }
}
