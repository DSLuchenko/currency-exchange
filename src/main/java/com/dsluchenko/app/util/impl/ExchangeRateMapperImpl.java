package com.dsluchenko.app.util.impl;

import com.dsluchenko.app.dto.CurrencyDTO;
import com.dsluchenko.app.dto.ExchangeRateDto;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.util.CurrencyMapper;
import com.dsluchenko.app.util.ExchangeRateMapper;

public class ExchangeRateMapperImpl implements ExchangeRateMapper {
    private CurrencyMapper currencyMapper;

    public ExchangeRateMapperImpl() {
        this.currencyMapper = new CurrencyMapperImpl();
    }

    @Override
    public ExchangeRateDto mapToDTO(ExchangeRate entity) {
        CurrencyDTO base = currencyMapper.mapToDTO(entity.getBaseCurrency());
        CurrencyDTO target = currencyMapper.mapToDTO(entity.getTargetCurrency());

        return new ExchangeRateDto(base, target, entity.getRate());
    }

    @Override
    public ExchangeRate mapToEntity(ExchangeRateDto dto) {
        return null;
    }

}
