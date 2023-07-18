package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.CurrencyDTO;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.impl.CurrencyServiceImpl;
import com.dsluchenko.app.util.CurrencyMapper;
import com.dsluchenko.app.util.impl.CurrencyMapperImpl;

import com.google.gson.Gson;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {
    private CurrencyService service;
    private CurrencyMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = (CurrencyServiceImpl) config.getServletContext().getAttribute(CurrencyServiceImpl.class.getName());
        mapper = (CurrencyMapperImpl) config.getServletContext().getAttribute(CurrencyMapperImpl.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CurrencyDTO> currencies = service.getAll()
                                              .stream()
                                              .map(mapper::mapToDTO)
                                              .toList();

        String respData = new Gson().toJson(currencies);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(respData);
    }
}

