package com.dsluchenko.app.web.servlet;

import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.service.CurrencyService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {
    private CurrencyService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = getServiceFromContext(config.getServletContext(), CurrencyService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<Currency> currencies = service.getAll();

        responseHandler.writeResponse(resp, currencies);
    }
}

