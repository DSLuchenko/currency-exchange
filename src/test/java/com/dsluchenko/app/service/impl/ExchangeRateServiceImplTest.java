package com.dsluchenko.app.service.impl;

import com.dsluchenko.app.data.dao.ExchangeRateDao;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.model.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;

import com.dsluchenko.app.service.exception.UnavailableExchangeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceImplTest {
    private static final String USD_CURRENCY_CODE = "USD";
    private static final String BASE_CURRENCY_CODE = "EUR";
    private static final String TARGET_CURRENCY_CODE = "TRY";
    private ExchangeRateService service;
    private Currency baseCurrency;
    private Currency targetCurrency;

    @Mock
    private ExchangeRateDao dao;

    @BeforeEach()
    void setUp() {
        service = new ExchangeRateServiceImpl(dao);

        baseCurrency = Currency.builder()
                               .code(BASE_CURRENCY_CODE)
                               .build();
        targetCurrency = Currency.builder()
                                 .code(TARGET_CURRENCY_CODE)
                                 .build();
    }

    @Test
    void getExchangeRate_ShouldReturnDirectRate() {
        ExchangeRate directExchangeRate = ExchangeRate.builder()
                                                      .baseCurrency(baseCurrency)
                                                      .targetCurrency(targetCurrency)
                                                      .rate(BigDecimal.valueOf(2.1234))
                                                      .build();

        when(dao.getByCurrencyCodes(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE)).thenReturn(Optional.of(directExchangeRate));

        ExchangeRate expected = ExchangeRate.builder()
                                            .baseCurrency(baseCurrency)
                                            .targetCurrency(targetCurrency)
                                            .rate(BigDecimal.valueOf(2.1234))
                                            .build();

        ExchangeRate actual = service.getAvailableExchangeRate(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE);

        assertEquals(expected, actual);
    }

    @Test
    void getExchangeRate_ShouldReturnReverseRate() {
        ExchangeRate reverseExchangeRate = ExchangeRate.builder()
                                                       .baseCurrency(targetCurrency)
                                                       .targetCurrency(baseCurrency)
                                                       .rate(BigDecimal.valueOf(2.1459))
                                                       .build();

        when(dao.getByCurrencyCodes(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE)).thenReturn(Optional.empty());
        when(dao.getByCurrencyCodes(TARGET_CURRENCY_CODE, BASE_CURRENCY_CODE)).thenReturn(Optional.of(reverseExchangeRate));

        ExchangeRate expected = ExchangeRate.builder()
                                            .baseCurrency(baseCurrency)
                                            .targetCurrency(targetCurrency)
                                            .rate(BigDecimal.valueOf(0.4660).setScale(4))
                                            .build();

        ExchangeRate actual = service.getAvailableExchangeRate(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE);


        assertEquals(expected, actual);
    }

    @Test
    void getExchangeRate_ShouldReturnCrossRate() {
        Currency usdCurrency = Currency.builder()
                                       .code(USD_CURRENCY_CODE)
                                       .build();
        ExchangeRate usdToBaseExchangeRate = ExchangeRate.builder()
                                                         .baseCurrency(usdCurrency)
                                                         .targetCurrency(baseCurrency)
                                                         .rate(BigDecimal.valueOf(4.1234))
                                                         .build();
        ExchangeRate usdToTargetExchangeRate = ExchangeRate.builder()
                                                           .baseCurrency(usdCurrency)
                                                           .targetCurrency(targetCurrency)
                                                           .rate(BigDecimal.valueOf(2.1234))
                                                           .build();

        when(dao.getByCurrencyCodes(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE)).thenReturn(Optional.empty());
        when(dao.getByCurrencyCodes(TARGET_CURRENCY_CODE, BASE_CURRENCY_CODE)).thenReturn(Optional.empty());
        when(dao.getByCurrencyCodes(USD_CURRENCY_CODE, BASE_CURRENCY_CODE)).thenReturn(Optional.of(usdToBaseExchangeRate));
        when(dao.getByCurrencyCodes(USD_CURRENCY_CODE, TARGET_CURRENCY_CODE)).thenReturn(Optional.of(usdToTargetExchangeRate));

        ExchangeRate expected = ExchangeRate.builder()
                                            .baseCurrency(baseCurrency)
                                            .targetCurrency(targetCurrency)
                                            .rate(BigDecimal.valueOf(0.5150).setScale(4))
                                            .build();

        ExchangeRate actual = service.getAvailableExchangeRate(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE);

        assertEquals(expected, actual);
    }

    @Test
    void getExchangeRate_ShouldThrowUnavailableExchangeExceptionUsdToBaseExchangeRate() {

        when(dao.getByCurrencyCodes(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE)).thenReturn(Optional.empty());
        when(dao.getByCurrencyCodes(TARGET_CURRENCY_CODE, BASE_CURRENCY_CODE)).thenReturn(Optional.empty());
        when(dao.getByCurrencyCodes(USD_CURRENCY_CODE, BASE_CURRENCY_CODE)).thenReturn(Optional.empty());

        assertThrows(UnavailableExchangeException.class, () -> service.getAvailableExchangeRate(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE));
    }

    @Test
    void getExchangeRate_ShouldThrowUnavailableExchangeExceptionWhenNotFoundUsdToTargetExchangeRate() {
        when(dao.getByCurrencyCodes(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE)).thenReturn(Optional.empty());
        when(dao.getByCurrencyCodes(TARGET_CURRENCY_CODE, BASE_CURRENCY_CODE)).thenReturn(Optional.empty());
        when(dao.getByCurrencyCodes(USD_CURRENCY_CODE, BASE_CURRENCY_CODE)).thenReturn(Optional.of(ExchangeRate.builder().build()));
        when(dao.getByCurrencyCodes(USD_CURRENCY_CODE, TARGET_CURRENCY_CODE)).thenReturn(Optional.empty());

        assertThrows(UnavailableExchangeException.class, () -> service.getAvailableExchangeRate(BASE_CURRENCY_CODE, TARGET_CURRENCY_CODE));
    }

    @Test
    void exchange_scaledToNillInfractionalPart() {
        ExchangeRate rate = ExchangeRate.builder()
                                        .baseCurrency(baseCurrency)
                                        .targetCurrency(targetCurrency)
                                        .rate(BigDecimal.valueOf(5.4995))
                                        .build();

        BigDecimal expected = BigDecimal.valueOf(55.00).setScale(2);

        BigDecimal actual = service.exchange(rate, BigDecimal.TEN);

        assertEquals(expected, actual);
    }

    @Test
    void exchange_correctScaled() {
        ExchangeRate rate = ExchangeRate.builder()
                                        .baseCurrency(baseCurrency)
                                        .targetCurrency(targetCurrency)
                                        .rate(BigDecimal.valueOf(2.5921))
                                        .build();

        BigDecimal expected = BigDecimal.valueOf(133.44).setScale(2);

        BigDecimal actual = service.exchange(rate, BigDecimal.valueOf(51.4794));

        assertEquals(expected, actual);
    }
}