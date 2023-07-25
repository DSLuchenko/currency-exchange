package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.ExchangeRateDto;
import com.dsluchenko.app.service.ExchangeRateService;
import com.dsluchenko.app.service.impl.ExchangeRateServiceImpl;
import com.dsluchenko.app.util.ExchangeRateMapper;
import com.dsluchenko.app.util.impl.ExchangeRateMapperImpl;

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
    private ResponseHandler responseHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (ExchangeRateServiceImpl) config.getServletContext().getAttribute(ExchangeRateServiceImpl.class.getSimpleName());
        mapper = (ExchangeRateMapperImpl) config.getServletContext().getAttribute(ExchangeRateMapperImpl.class.getSimpleName());
        responseHandler = (ResponseHandler) config.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        List<ExchangeRateDto> exchangeRates = service.getAll().stream()
                                                     .map(mapper::mapToDTO)
                                                     .toList();

        responseHandler.writeResponse(resp, exchangeRates);
    }
}
