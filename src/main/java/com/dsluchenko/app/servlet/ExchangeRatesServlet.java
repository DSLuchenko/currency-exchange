package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.ExchangeRateDto;
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
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    private ExchangeRateService service;
    private ExchangeRateMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = (ExchangeRateServiceImpl) config.getServletContext().getAttribute(ExchangeRateServiceImpl.class.getSimpleName());
        mapper = (ExchangeRateMapperImpl) config.getServletContext().getAttribute(ExchangeRateMapperImpl.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateDto> exchangeRates = service.getAll().stream()
                                                     .map(mapper::mapToDTO)
                                                     .toList();

        String respData = new Gson().toJson(exchangeRates);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(respData);
    }
}
