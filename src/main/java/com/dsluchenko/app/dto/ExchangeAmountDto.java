package com.dsluchenko.app.dto;

import com.dsluchenko.app.entity.ExchangeRate;

import java.math.BigDecimal;

public record ExchangeAmountDto(ExchangeRate rate, BigDecimal amount, BigDecimal convertedAmount) {
}
