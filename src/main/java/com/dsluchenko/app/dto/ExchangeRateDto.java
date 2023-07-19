package com.dsluchenko.app.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(CurrencyDto base, CurrencyDto target, BigDecimal rate) {
}
