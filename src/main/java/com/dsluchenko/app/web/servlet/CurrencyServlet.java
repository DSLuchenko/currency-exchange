package com.dsluchenko.app.web.servlet;

import com.dsluchenko.app.dto.request.CurrencyCreateRequest;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.mapper.CurrencyMapper;
import com.dsluchenko.app.mapper.impl.CurrencyMapperImpl;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/currency/*", name = "CurrencyServlet")
public class CurrencyServlet extends BaseServlet {
    private CurrencyService service;
    private CurrencyMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = getServiceFromContext(config.getServletContext(), CurrencyService.class);
        mapper = new CurrencyMapperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String code = (String) req.getAttribute("code");
        Currency currency = service.findByCode(code);
        responseHandler.writeResponse(resp, currency);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        CurrencyCreateRequest currencyRequest = (CurrencyCreateRequest) req.getAttribute("dto");
        Currency newCurrency = mapper.mapToEntity(currencyRequest);
        newCurrency = service.create(newCurrency);

        responseHandler.writeResponse(resp, newCurrency);
    }
}
