package com.dsluchenko.app.mapper.impl;

import com.dsluchenko.app.dto.CurrencyResponse;
import com.dsluchenko.app.dto.response.ExchangeRateResponse;
import com.dsluchenko.app.model.ExchangeRate;
import com.dsluchenko.app.mapper.CurrencyMapper;
import com.dsluchenko.app.mapper.ExchangeRateMapper;

public class ExchangeRateMapperImpl implements ExchangeRateMapper {
    private CurrencyMapper currencyMapper;

    public ExchangeRateMapperImpl() {
        this.currencyMapper = new CurrencyMapperImpl();
    }

    @Override
    public ExchangeRateResponse mapToDTO(ExchangeRate entity) {
        CurrencyResponse base = currencyMapper.mapToDTO(entity.getBaseCurrency());
        CurrencyResponse target = currencyMapper.mapToDTO(entity.getTargetCurrency());

        return new ExchangeRateResponse(base, target, entity.getRate());
    }

    @Override
    public ExchangeRate mapToEntity(ExchangeRateResponse dto) {
        return null;
    }

}
