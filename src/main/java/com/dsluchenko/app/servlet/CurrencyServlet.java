package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.CurrencyDto;
import com.dsluchenko.app.entity.Currency;
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


@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    private CurrencyService service;
    private CurrencyMapper mapper;

    @Override
    public void init(ServletConfig config) {
        service = (CurrencyServiceImpl) config.getServletContext().getAttribute(CurrencyServiceImpl.class.getSimpleName());
        mapper = (CurrencyMapperImpl) config.getServletContext().getAttribute(CurrencyMapperImpl.class.getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws RuntimeException, IOException {
        String code = req.getPathInfo()
                         .substring(1);
        Currency currency = service.findByCode(code);
        CurrencyDto currencyDTO = mapper.mapToDTO(currency);
        String data = new Gson().toJson(currencyDTO);
        resp.getWriter().write(data);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        CurrencyDto currencyDTO = new CurrencyDto(name, code, sign);
        Currency newCurrency = mapper.mapToEntity(currencyDTO);
        newCurrency = service.create(newCurrency);
        String responseData = new Gson().toJson(newCurrency);
        resp.getWriter().write(responseData);
    }
}
