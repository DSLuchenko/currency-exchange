package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.ExchangeRateDto;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.impl.CurrencyServiceImpl;
import com.dsluchenko.app.service.impl.ExchangeRateServiceImpl;
import com.dsluchenko.app.util.ExchangeRateMapper;
import com.dsluchenko.app.util.impl.ExchangeRateMapperImpl;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    private ExchangeRateService rateService;
    private CurrencyService currencyService;
    private ExchangeRateMapper mapper;
    private ResponseHandler responseHandler;


    @Override
    public void init(ServletConfig config) throws ServletException {
        rateService = (ExchangeRateServiceImpl) config.getServletContext().getAttribute(ExchangeRateServiceImpl.class.getSimpleName());
        currencyService = (CurrencyServiceImpl) config.getServletContext().getAttribute(CurrencyServiceImpl.class.getSimpleName());
        mapper = (ExchangeRateMapperImpl) config.getServletContext().getAttribute(ExchangeRateMapperImpl.class.getSimpleName());
        responseHandler = (ResponseHandler) config.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String baseCurrencyCode = (String) req.getAttribute("baseCode");
        String targetCurrencyCode = (String) req.getAttribute("targetCode");

        ExchangeRate exchangeRate = rateService.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
        ExchangeRateDto exchangeRateDTO = mapper.mapToDTO(exchangeRate);

        responseHandler.writeResponse(resp, exchangeRateDTO);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        String baseCurrencyCode = (String) req.getAttribute("baseCode");
        String targetCurrencyCode = (String) req.getAttribute("targetCode");
        BigDecimal rate = (BigDecimal) req.getAttribute("rate");

        ExchangeRate exchangeRate = rateService.changeRate(baseCurrencyCode, targetCurrencyCode, rate);
        ExchangeRateDto exchangeRateDTO = mapper.mapToDTO(exchangeRate);

        responseHandler.writeResponse(resp, exchangeRateDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String baseCurrencyCode = (String) req.getAttribute("baseCode");
        String targetCurrencyCode = (String) req.getAttribute("targetCode");
        BigDecimal rate = (BigDecimal) req.getAttribute("rate");

        Currency baseCurrency = currencyService.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.findByCode(targetCurrencyCode);

        ExchangeRate newExchangeRate = ExchangeRate.builder()
                                                   .baseCurrency(baseCurrency)
                                                   .targetCurrency(targetCurrency)
                                                   .rate(rate)
                                                   .build();

        newExchangeRate = rateService.create(newExchangeRate);
        responseHandler.writeResponse(resp, newExchangeRate);
    }
}
