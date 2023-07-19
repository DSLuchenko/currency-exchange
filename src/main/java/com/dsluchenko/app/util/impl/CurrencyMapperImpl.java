package com.dsluchenko.app.util.impl;

import com.dsluchenko.app.dto.CurrencyDto;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.util.CurrencyMapper;

public class CurrencyMapperImpl implements CurrencyMapper {

    @Override
    public CurrencyDto mapToDTO(Currency entity) {
        if (entity == null) throw new RuntimeException();

        return new CurrencyDto(
                entity.getFullName(),
                entity.getCode(),
                entity.getSign()
        );
    }

    @Override
    public Currency mapToEntity(CurrencyDto dto) {
        if (dto == null) throw new RuntimeException();

        return Currency.builder()
                       .id(0)
                       .fullName(dto.name())
                       .code(dto.code())
                       .sign(dto.sign())
                       .build();

    }
}
