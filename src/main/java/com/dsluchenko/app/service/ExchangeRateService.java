package com.dsluchenko.app.service;

import com.dsluchenko.app.entity.ExchangeRate;

public interface ExchangeRateService extends Service<ExchangeRate> {

    ExchangeRate findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode);
}
