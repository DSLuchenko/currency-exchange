package com.dsluchenko.app.data.dao;

import com.dsluchenko.app.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate>{
    Optional<ExchangeRate> getByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode);

    Optional<ExchangeRate> updateByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);
}
