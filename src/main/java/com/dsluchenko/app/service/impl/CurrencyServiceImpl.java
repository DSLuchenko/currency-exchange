package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.data.dao.CurrencyDao;
import com.dsluchenko.app.data.dao.exception.DaoConstraintViolationRuntimeException;
import com.dsluchenko.app.data.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.exception.IntegrityViolationRuntimeException;
import com.dsluchenko.app.service.exception.CurrencyNotFoundRuntimeException;
import com.dsluchenko.app.service.exception.ApplicationRuntimeException;

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
        } catch (DaoRuntimeException e) {
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
        } catch (DaoConstraintViolationRuntimeException e) {
            throw new IntegrityViolationRuntimeException();
        } catch (DaoRuntimeException e) {
            throw new ApplicationRuntimeException();
        }
    }


    @Override
    public Currency findByCode(String code) {
        try {
            Optional<Currency> currency = dao.getByCode(code);
            if (currency.isEmpty()) throw new CurrencyNotFoundRuntimeException();
            return currency.get();
        } catch (DaoRuntimeException e) {
            throw new ApplicationRuntimeException();
        }
    }
}
