package com.dsluchenko.app.dto.response;

import com.dsluchenko.app.dto.CurrencyResponse;

import java.math.BigDecimal;

public record ExchangeRateResponse(CurrencyResponse base, CurrencyResponse target, BigDecimal rate) {
}
