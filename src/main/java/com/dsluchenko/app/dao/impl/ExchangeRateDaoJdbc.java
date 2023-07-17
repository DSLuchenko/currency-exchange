package com.dsluchenko.app.dao.impl;

import com.dsluchenko.app.dao.ExchangeRateDao;
import com.dsluchenko.app.entity.ExchangeRate;

import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoJdbc implements ExchangeRateDao {
    @Override
    public Optional<ExchangeRate> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getAll() {
        return null;
    }

    @Override
    public int save(ExchangeRate exchangeRate) {
        return 0;
    }

    @Override
    public int update(ExchangeRate exchangeRate, String[] params) {
        return 0;
    }

    @Override
    public int delete(ExchangeRate exchangeRate) {
        return 0;
    }
}
