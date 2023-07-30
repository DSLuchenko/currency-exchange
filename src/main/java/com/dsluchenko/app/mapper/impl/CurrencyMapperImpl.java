package com.dsluchenko.app.mapper.impl;

import com.dsluchenko.app.dto.CurrencyResponse;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.mapper.CurrencyMapper;

public class CurrencyMapperImpl implements CurrencyMapper {

    @Override
    public CurrencyResponse mapToDTO(Currency entity) {
        if (entity == null) throw new RuntimeException();

        return new CurrencyResponse(
                entity.getFullName(),
                entity.getCode(),
                entity.getSign()
        );
    }

    @Override
    public Currency mapToEntity(CurrencyResponse dto) {
        if (dto == null) throw new RuntimeException();

        return Currency.builder()
                       .id(0)
                       .fullName(dto.name())
                       .code(dto.code())
                       .sign(dto.sign())
                       .build();

    }
}
