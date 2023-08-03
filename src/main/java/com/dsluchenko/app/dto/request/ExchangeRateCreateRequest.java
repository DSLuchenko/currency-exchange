package com.dsluchenko.app.dto.request;

import java.math.BigDecimal;

public record ExchangeRateCreateRequest(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) { }
