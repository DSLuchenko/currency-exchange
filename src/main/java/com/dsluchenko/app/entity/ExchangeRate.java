package com.dsluchenko.app.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRate {
    private final int id;
    private final Currency baseCurrency;
    private final Currency targetCurrency;
    private final BigDecimal rate;
}
