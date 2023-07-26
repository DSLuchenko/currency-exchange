package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.dao.ExchangeRateDao;
import com.dsluchenko.app.dao.exception.DaoConstraintViolationRuntimeException;
import com.dsluchenko.app.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.dao.impl.ExchangeRateDaoJdbc;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.exception.IntegrityViolationRuntimeException;
import com.dsluchenko.app.service.exception.ExchangeRateNotFoundRuntimeException;
import com.dsluchenko.app.service.exception.ServerRuntimeException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDao dao;

    public ExchangeRateServiceImpl() {
        this.dao = new ExchangeRateDaoJdbc();
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
        try {
            int id = dao.save(exchangeRate);
            ExchangeRate createdExchangeRate = ExchangeRate.builder()
                                                           .id(id)
                                                           .baseCurrency(exchangeRate.getBaseCurrency())
                                                           .targetCurrency(exchangeRate.getTargetCurrency())
                                                           .rate(exchangeRate.getRate())
                                                           .build();

            return createdExchangeRate;
        } catch (DaoConstraintViolationRuntimeException e) {
            throw new IntegrityViolationRuntimeException();
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
    }

    @Override
    public ExchangeRate findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            Optional<ExchangeRate> exchangeRate = dao.getByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
            return exchangeRate.orElseThrow(ExchangeRateNotFoundRuntimeException::new);
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
    }

    @Override
    public ExchangeRate changeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        try {
            Optional<ExchangeRate> exchangeRate = dao.updateByCurrencyCodes(baseCurrencyCode, targetCurrencyCode, rate);
            return exchangeRate.orElseThrow(ExchangeRateNotFoundRuntimeException::new);
        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
    }
}
