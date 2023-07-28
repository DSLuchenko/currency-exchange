package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.ExchangeAmountDto;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.impl.ExchangeRateServiceImpl;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

@WebServlet("/exchange/*")
public class ExchangeServlet extends BaseServlet {
    private ExchangeRateService service;
    private ResponseHandler responseHandler;


    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (ExchangeRateServiceImpl) config.getServletContext().getAttribute(ExchangeRateServiceImpl.class.getSimpleName());
        responseHandler = (ResponseHandler) config.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String from = (String) req.getAttribute("from");
        String to = (String) req.getAttribute("to");
        BigDecimal amount = (BigDecimal) req.getAttribute("amount");

        ExchangeRate rate = service.getAvailableExchangeRate(from, to);
        BigDecimal convertedAmount = service.exchange(rate, amount);

        ExchangeAmountDto exchangeAmount = new ExchangeAmountDto(rate, amount, convertedAmount);

        responseHandler.writeResponse(resp, exchangeAmount);
    }
}
