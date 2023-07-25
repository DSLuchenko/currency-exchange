package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.CurrencyDto;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.impl.CurrencyServiceImpl;
import com.dsluchenko.app.util.CurrencyMapper;
import com.dsluchenko.app.util.impl.CurrencyMapperImpl;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {
    private CurrencyService service;
    private CurrencyMapper mapper;
    private ResponseHandler responseHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (CurrencyServiceImpl) config.getServletContext().getAttribute(CurrencyServiceImpl.class.getSimpleName());
        mapper = (CurrencyMapperImpl) config.getServletContext().getAttribute(CurrencyMapperImpl.class.getSimpleName());
        responseHandler = (ResponseHandler) config.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<CurrencyDto> currencies = service.getAll()
                                              .stream()
                                              .map(mapper::mapToDTO)
                                              .toList();

        responseHandler.writeResponse(resp, currencies);
    }
}

