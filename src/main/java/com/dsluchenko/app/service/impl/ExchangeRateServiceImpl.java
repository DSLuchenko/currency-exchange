package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.dao.ExchangeRateDao;
import com.dsluchenko.app.dao.exception.DaoConstraintViolationRuntimeException;
import com.dsluchenko.app.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.exception.IntegrityViolationRuntimeException;
import com.dsluchenko.app.service.exception.ExchangeRateNotFoundRuntimeException;
import com.dsluchenko.app.service.exception.ServerRuntimeException;
import com.dsluchenko.app.service.exception.UnavailableExchangeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private static final String USD_CURRENCY_CODE = "USD";
    private final ExchangeRateDao dao;

    public ExchangeRateServiceImpl(ExchangeRateDao dao) {
        this.dao = dao;
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

    @Override
    public ExchangeRate getAvailableExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            Optional<ExchangeRate> exchangeRate = findDirectExchangeRate(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate.isPresent()) return exchangeRate.get();

            exchangeRate = findReverseExchangeRate(baseCurrencyCode, targetCurrencyCode);

            if (exchangeRate.isPresent()) {
                BigDecimal baseToTarget = BigDecimal.ONE.divide(exchangeRate.get().getRate(), 4, RoundingMode.HALF_UP);
                return ExchangeRate.builder()
                                   .rate(baseToTarget)
                                   .baseCurrency(exchangeRate.get().getTargetCurrency())
                                   .targetCurrency(exchangeRate.get().getBaseCurrency())
                                   .build();
            }

            return findCrossExchangeRateByUSD(baseCurrencyCode, targetCurrencyCode);

        } catch (DaoRuntimeException e) {
            throw new ServerRuntimeException();
        }
    }

    @Override
    public BigDecimal exchange(ExchangeRate rate, BigDecimal amount) {
        BigDecimal convertedAmount = amount.multiply(rate.getRate()).setScale(2, RoundingMode.HALF_UP);
        return convertedAmount;
    }

    private Optional<ExchangeRate> findDirectExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        return dao.getByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
    }

    private Optional<ExchangeRate> findReverseExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        return dao.getByCurrencyCodes(targetCurrencyCode, baseCurrencyCode);
    }

    private ExchangeRate findCrossExchangeRateByUSD(String baseCurrencyCode, String targetCurrencyCode) {
        ExchangeRate usdToBase = dao.getByCurrencyCodes(USD_CURRENCY_CODE, baseCurrencyCode).orElseThrow(UnavailableExchangeException::new);
        ExchangeRate usdToTarget = dao.getByCurrencyCodes(USD_CURRENCY_CODE, targetCurrencyCode).orElseThrow(UnavailableExchangeException::new);

        BigDecimal rateUsdToBase = usdToBase.getRate();
        BigDecimal rateUsdToTarget = usdToTarget.getRate();
        BigDecimal baseToTarget = rateUsdToTarget.divide(rateUsdToBase, 4);

        return ExchangeRate.builder()
                           .rate(baseToTarget)
                           .baseCurrency(usdToBase.getTargetCurrency())
                           .targetCurrency(usdToTarget.getTargetCurrency())
                           .build();
    }
}
