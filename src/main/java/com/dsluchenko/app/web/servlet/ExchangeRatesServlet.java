package com.dsluchenko.app.web.servlet;

import com.dsluchenko.app.model.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.impl.ExchangeRateServiceImpl;
import com.dsluchenko.app.web.ResponseHandler;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    private ExchangeRateService service;
    private ResponseHandler responseHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (ExchangeRateServiceImpl) config.getServletContext().getAttribute(ExchangeRateServiceImpl.class.getSimpleName());
        responseHandler = (ResponseHandler) config.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        List<ExchangeRate> exchangeRates = service.getAll();

        responseHandler.writeResponse(resp, exchangeRates);
    }
}
