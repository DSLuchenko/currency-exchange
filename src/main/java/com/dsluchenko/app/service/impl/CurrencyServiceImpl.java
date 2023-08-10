package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.data.dao.CurrencyDao;
import com.dsluchenko.app.data.dao.exception.ConstraintViolationException;
import com.dsluchenko.app.data.dao.exception.DaoApplicationException;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.exception.IntegrityViolationException;
import com.dsluchenko.app.service.exception.CurrencyNotFoundException;
import com.dsluchenko.app.exception.ApplicationRuntimeException;

import java.util.List;
import java.util.Optional;


public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDao dao;

    public CurrencyServiceImpl(CurrencyDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Currency> getAll() {
        try {
            List<Currency> currencies = dao.getAll();
            return currencies;
        } catch (DaoApplicationException e) {
            throw new ApplicationRuntimeException();
        }
    }

    @Override
    public Currency create(Currency currency) {
        try {
            int idSavedCurrency = dao.save(currency);
            Currency savedCurrency = Currency.builder()
                                             .id(idSavedCurrency)
                                             .code(currency.getCode())
                                             .fullName(currency.getFullName())
                                             .sign(currency.getSign())
                                             .build();
            return savedCurrency;
        } catch (ConstraintViolationException e) {
            throw new IntegrityViolationException();
        } catch (DaoApplicationException e) {
            throw new ApplicationRuntimeException();
        }
    }


    @Override
    public Currency findByCode(String code) {
        try {
            Optional<Currency> currency = dao.getByCode(code);
            if (currency.isEmpty()) throw new CurrencyNotFoundException();
            return currency.get();
        } catch (DaoApplicationException e) {
            throw new ApplicationRuntimeException();
        }
    }
}
