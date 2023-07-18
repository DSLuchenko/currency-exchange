package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.dao.CurrencyDao;
import com.dsluchenko.app.dao.exception.DaoConstraintViolationRuntimeException;
import com.dsluchenko.app.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.dao.impl.CurrencyDaoJdbc;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.exception.CurrencyIntegrityViolationRuntimeException;
import com.dsluchenko.app.service.exception.CurrencyNotFoundException;
import com.dsluchenko.app.service.exception.ServerRuntimeException;

import java.util.List;
import java.util.Optional;


public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDao dao;

    public CurrencyServiceImpl() {
        dao = new CurrencyDaoJdbc();
    }

    @Override
    public Optional<Currency> findById(int id) {
        return Optional.empty();
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
            throw new CurrencyIntegrityViolationRuntimeException();
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
    }

    @Override
    public Currency update(Currency currency, String[] params) {
        return null;
    }

    @Override
    public void delete(Currency currency) {

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
