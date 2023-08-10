package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.data.dao.ExchangeRateDao;
import com.dsluchenko.app.data.dao.exception.ConstraintViolationException;
import com.dsluchenko.app.data.dao.exception.DaoApplicationException;
import com.dsluchenko.app.model.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.exception.IntegrityViolationException;
import com.dsluchenko.app.service.exception.ExchangeRateNotFoundException;
import com.dsluchenko.app.exception.ApplicationRuntimeException;
import com.dsluchenko.app.service.exception.UnavailableExchangeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private static final Logger logger = Logger.getLogger(ExchangeRateServiceImpl.class.getName());
    private static final String USD_CURRENCY_CODE = "USD";
    private final ExchangeRateDao dao;

    public ExchangeRateServiceImpl(ExchangeRateDao dao) {
        this.dao = dao;
    }

    @Override
    public List<ExchangeRate> getAll() {
        try {
            return dao.getAll();
        } catch (DaoApplicationException e) {
            throw new ApplicationRuntimeException();
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
        } catch (ConstraintViolationException e) {
            throw new IntegrityViolationException();
        } catch (DaoApplicationException e) {
            throw new ApplicationRuntimeException();
        }
    }

    @Override
    public ExchangeRate findByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            Optional<ExchangeRate> exchangeRate = dao.getByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
            return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);
        } catch (DaoApplicationException e) {
            throw new ApplicationRuntimeException();
        }
    }

    @Override
    public ExchangeRate changeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        try {
            Optional<ExchangeRate> exchangeRate = dao.updateByCurrencyCodes(baseCurrencyCode, targetCurrencyCode, rate);
            return exchangeRate.orElseThrow(ExchangeRateNotFoundException::new);
        } catch (DaoApplicationException e) {
            throw new ApplicationRuntimeException();
        }
    }

    @Override
    public ExchangeRate getAvailableExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            Optional<ExchangeRate> exchangeRate = findDirectExchangeRate(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate.isPresent()) return exchangeRate.get();

            logger.info(String.format(
                    "Direct rate %s_%s not founded", baseCurrencyCode, targetCurrencyCode));
            exchangeRate = findReverseExchangeRate(baseCurrencyCode, targetCurrencyCode);


            if (exchangeRate.isPresent()) {
                BigDecimal baseToTarget = BigDecimal.ONE.divide(exchangeRate.get().getRate(), 4, RoundingMode.HALF_UP);
                return ExchangeRate.builder()
                                   .rate(baseToTarget)
                                   .baseCurrency(exchangeRate.get().getTargetCurrency())
                                   .targetCurrency(exchangeRate.get().getBaseCurrency())
                                   .build();
            }
            logger.info(String.format(
                    "Reverse rate %s_%s not founded", targetCurrencyCode, baseCurrencyCode));
            return findCrossExchangeRateByUSD(baseCurrencyCode, targetCurrencyCode);

        } catch (DaoApplicationException e) {
            logger.info(String.format(
                    "Cross rate USD_%s and USD_%s  not founded", baseCurrencyCode, targetCurrencyCode));
            throw new ApplicationRuntimeException();
        }
    }

    @Override
    public BigDecimal exchange(ExchangeRate rate, BigDecimal amount) {
        BigDecimal convertedAmount = amount.multiply(rate.getRate()).setScale(2, RoundingMode.HALF_UP);
        return convertedAmount;
    }

    private Optional<ExchangeRate> findDirectExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        logger.info(String.format(
                "Search direct rate %s_%s", baseCurrencyCode, targetCurrencyCode));
        return dao.getByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
    }

    private Optional<ExchangeRate> findReverseExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        logger.info(String.format(
                "Search reverse rate %s_%s", targetCurrencyCode, baseCurrencyCode));
        return dao.getByCurrencyCodes(targetCurrencyCode, baseCurrencyCode);
    }

    private ExchangeRate findCrossExchangeRateByUSD(String baseCurrencyCode, String targetCurrencyCode) {
        logger.info(String.format(
                "Search cross rate USD_%s and USD_%s", baseCurrencyCode, targetCurrencyCode));
        ExchangeRate usdToBase = dao.getByCurrencyCodes(USD_CURRENCY_CODE, baseCurrencyCode).orElseThrow(UnavailableExchangeException::new);
        ExchangeRate usdToTarget = dao.getByCurrencyCodes(USD_CURRENCY_CODE, targetCurrencyCode).orElseThrow(UnavailableExchangeException::new);

        BigDecimal rateUsdToBase = usdToBase.getRate();
        BigDecimal rateUsdToTarget = usdToTarget.getRate();
        BigDecimal baseToTarget = rateUsdToTarget.divide(rateUsdToBase, 4, RoundingMode.HALF_UP);

        return ExchangeRate.builder()
                           .rate(baseToTarget)
                           .baseCurrency(usdToBase.getTargetCurrency())
                           .targetCurrency(usdToTarget.getTargetCurrency())
                           .build();
    }
}
