package com.dsluchenko.app.service;

import com.dsluchenko.app.model.Currency;

public interface CurrencyService extends Service<Currency> {
    Currency findByCode(final String code);

}
