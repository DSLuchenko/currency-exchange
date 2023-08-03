package com.dsluchenko.app.dto.response;

import com.dsluchenko.app.model.ExchangeRate;

import java.math.BigDecimal;

public record ExchangeAmountResponse(ExchangeRate rate, BigDecimal amount, BigDecimal convertedAmount) { }
