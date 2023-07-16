package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.dao.CurrencyDao;
import com.dsluchenko.app.dao.impl.JdbcCurrencyDao;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.exception.CurrencyNotFoundException;

import java.util.List;
import java.util.Optional;



public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDao dao;

    public CurrencyServiceImpl() {
        dao = new JdbcCurrencyDao();
    }

    @Override
    public Optional<Currency> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Currency> getAll() {
        return dao.getAll();
    }

    @Override
    public void create(Currency currency) {

    }

    @Override
    public void update(Currency currency, String[] params) {

    }

    @Override
    public void delete(Currency currency) {

    }

    @Override
    public Currency findByCode(String code) {
        Optional<Currency> currency = dao.getByCode(code);
        if (currency.isEmpty()) throw new CurrencyNotFoundException();

        return currency.get();
    }
}
