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

import java.util.ArrayList;
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
        List<Currency> currencies = new ArrayList<>();
        try {
            currencies = dao.getAll();
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
        return currencies;
    }

    @Override
    public Currency create(Currency currency) {
        Currency savedCurrency = Currency.builder()
                                         .build();
        try {
            int idSavedCurrency = dao.save(currency);
            savedCurrency = Currency.builder()
                                    .id(idSavedCurrency)
                                    .code(currency.getCode())
                                    .fullName(currency.getFullName())
                                    .sign(currency.getSign())
                                    .build();
        } catch (DaoConstraintViolationRuntimeException e) {
            throw new CurrencyIntegrityViolationRuntimeException();

        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
        return savedCurrency;
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
        Optional<Currency> currency = Optional.empty();
        try {
            currency = dao.getByCode(code);
            if (currency.isEmpty()) throw new CurrencyNotFoundException();
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }

        return currency.get();
    }
}
