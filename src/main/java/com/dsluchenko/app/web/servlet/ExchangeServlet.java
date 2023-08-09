package com.dsluchenko.app.web.servlet;

import com.dsluchenko.app.dto.response.ExchangeAmountResponse;
import com.dsluchenko.app.model.ExchangeRate;
import com.dsluchenko.app.service.ExchangeRateService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

@WebServlet(urlPatterns = "/exchange/*", name = "ExchangeServlet")
public class ExchangeServlet extends BaseServlet {
    private ExchangeRateService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = getServiceFromContext(config.getServletContext(), ExchangeRateService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String from = (String) req.getAttribute("from");
        String to = (String) req.getAttribute("to");
        BigDecimal amount = (BigDecimal) req.getAttribute("amount");

        ExchangeRate rate = service.getAvailableExchangeRate(from, to);
        BigDecimal convertedAmount = service.exchange(rate, amount);

        ExchangeAmountResponse exchangeAmount = new ExchangeAmountResponse(rate, amount, convertedAmount);
        responseHandler.writeResponse(resp, exchangeAmount);
    }
}
