package com.dsluchenko.app.dao;

import com.dsluchenko.app.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate>{
    Optional<ExchangeRate> getByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode);

    Optional<ExchangeRate> updateByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);
}
