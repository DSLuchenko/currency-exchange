package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.ExchangeRateDto;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.impl.ExchangeRateServiceImpl;
import com.dsluchenko.app.util.ExchangeRateMapper;
import com.dsluchenko.app.util.impl.ExchangeRateMapperImpl;

import com.google.gson.Gson;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    private ExchangeRateService service;
    private ExchangeRateMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (ExchangeRateServiceImpl) config.getServletContext().getAttribute(ExchangeRateServiceImpl.class.getSimpleName());
        mapper = (ExchangeRateMapperImpl) config.getServletContext().getAttribute(ExchangeRateMapperImpl.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getPathInfo()
                                     .substring(1, 4);
        String targetCurrencyCode = req.getPathInfo()
                                       .substring(4);
        ExchangeRate exchangeRate = service.findByCurrencyCodes(baseCurrencyCode, targetCurrencyCode);
        ExchangeRateDto exchangeRateDTO = mapper.mapToDTO(exchangeRate);
        String data = new Gson().toJson(exchangeRateDTO);
        resp.getWriter().write(data);
    }
}
