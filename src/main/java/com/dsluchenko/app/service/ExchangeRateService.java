package com.dsluchenko.app.service;

import com.dsluchenko.app.entity.ExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateService extends Service<ExchangeRate> {

    ExchangeRate findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode);

    ExchangeRate changeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    ExchangeRate getAvailableExchangeRate(String baseCurrencyCode, String targetCurrencyCode);

    BigDecimal exchange(ExchangeRate rate, BigDecimal amount);
}
