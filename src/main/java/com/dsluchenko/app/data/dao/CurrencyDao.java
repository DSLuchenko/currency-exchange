package com.dsluchenko.app.data.dao;

import com.dsluchenko.app.model.Currency;

import java.util.Optional;

public interface CurrencyDao extends Dao<Currency> {
    Optional<Currency> getByCode(final String code);
}
