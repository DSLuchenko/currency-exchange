package com.dsluchenko.app.dao;

import com.dsluchenko.app.entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate>{
    Optional<ExchangeRate> getByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode);
}
