package com.dsluchenko.app.web.servlet;

import com.dsluchenko.app.model.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    private ExchangeRateService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = getServiceFromContext(config.getServletContext(), ExchangeRateService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<ExchangeRate> exchangeRates = service.getAll();
        responseHandler.writeResponse(resp, exchangeRates);
    }
}
