package com.dsluchenko.app.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(CurrencyDTO base, CurrencyDTO target, BigDecimal rate) {
}
