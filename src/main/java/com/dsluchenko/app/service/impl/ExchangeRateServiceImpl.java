package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.dao.ExchangeRateDao;
import com.dsluchenko.app.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.dao.impl.ExchangeRateDaoJdbc;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.exception.ServerRuntimeException;

import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDao dao;

    public ExchangeRateServiceImpl() {
        this.dao = new ExchangeRateDaoJdbc();
    }

    @Override
    public Optional<ExchangeRate> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getAll() {
        try {
            return dao.getAll();
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate, String[] params) {
        return null;
    }

    @Override
    public void delete(ExchangeRate exchangeRate) {

    }
}
