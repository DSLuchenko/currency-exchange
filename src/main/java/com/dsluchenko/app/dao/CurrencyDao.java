package com.dsluchenko.app.dao;

import com.dsluchenko.app.entity.Currency;

import java.util.Optional;

public interface CurrencyDao extends Dao<Currency> {
    Optional<Currency> getByCode(final String code);
}
