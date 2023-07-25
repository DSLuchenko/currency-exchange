package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.ExchangeRateDto;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
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
    private ExchangeRateService service;
    private ExchangeRateMapper mapper;
    private ResponseHandler responseHandler;


    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (ExchangeRateServiceImpl) config.getServletContext().getAttribute(ExchangeRateServiceImpl.class.getSimpleName());
        mapper = (ExchangeRateMapperImpl) config.getServletContext().getAttribute(ExchangeRateMapperImpl.class.getSimpleName());
        responseHandler = (ResponseHandler) config.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String baseCurrencyCode = (String) req.getAttribute("baseCode");
        String targetCurrencyCode = (String) req.getAttribute("targetCode");

        ExchangeRate exchangeRate = service.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
        ExchangeRateDto exchangeRateDTO = mapper.mapToDTO(exchangeRate);

        responseHandler.writeResponse(resp, exchangeRateDTO);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        String baseCurrencyCode = (String) req.getAttribute("baseCode");
        String targetCurrencyCode = (String) req.getAttribute("targetCode");
        BigDecimal rate = (BigDecimal) req.getAttribute("rate");

        ExchangeRate exchangeRate = service.changeRate(baseCurrencyCode, targetCurrencyCode, rate);
        ExchangeRateDto exchangeRateDTO = mapper.mapToDTO(exchangeRate);

        responseHandler.writeResponse(resp, exchangeRateDTO);
    }
}
