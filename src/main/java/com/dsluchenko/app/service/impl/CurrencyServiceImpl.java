package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.dao.CurrencyDao;
import com.dsluchenko.app.dao.exception.DaoConstraintViolationRuntimeException;
import com.dsluchenko.app.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.exception.IntegrityViolationRuntimeException;
import com.dsluchenko.app.service.exception.CurrencyNotFoundException;
import com.dsluchenko.app.service.exception.ServerRuntimeException;

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
            throw new ServerRuntimeException();
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
            throw new ServerRuntimeException();
        }
    }


    @Override
    public Currency findByCode(String code) {
        try {
            Optional<Currency> currency = dao.getByCode(code);
            if (currency.isEmpty()) throw new CurrencyNotFoundException();
            return currency.get();
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
    }
}
