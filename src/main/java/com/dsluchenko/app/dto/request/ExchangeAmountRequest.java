package com.dsluchenko.app.dto.request;

import com.dsluchenko.app.model.ExchangeRate;

import java.math.BigDecimal;

public record ExchangeAmountRequest(ExchangeRate rate, BigDecimal amount, BigDecimal convertedAmount) {
}
