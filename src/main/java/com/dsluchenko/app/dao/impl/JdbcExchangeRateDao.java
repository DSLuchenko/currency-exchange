package com.dsluchenko.app.dao.impl;

import com.dsluchenko.app.dao.ExchangeRateDao;
import com.dsluchenko.app.entity.ExchangeRate;

import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateDao implements ExchangeRateDao {
    @Override
    public Optional<ExchangeRate> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getAll() {
        return null;
    }

    @Override
    public void save(ExchangeRate exchangeRate) {

    }

    @Override
    public void update(ExchangeRate exchangeRate, String[] params) {

    }

    @Override
    public void delete(ExchangeRate exchangeRate) {

    }
}
