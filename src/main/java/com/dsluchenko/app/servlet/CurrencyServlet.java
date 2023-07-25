package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.CurrencyDto;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.service.CurrencyService;
import com.dsluchenko.app.service.impl.CurrencyServiceImpl;
import com.dsluchenko.app.util.CurrencyMapper;
import com.dsluchenko.app.util.impl.CurrencyMapperImpl;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

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
        String code = (String) req.getAttribute("code");

        Currency currency = service.findByCode(code);
        CurrencyDto currencyDTO = mapper.mapToDTO(currency);

        responseHandler.writeResponse(resp, currencyDTO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        CurrencyDto currencyDTO = (CurrencyDto) req.getAttribute("dto");
        Currency newCurrency = mapper.mapToEntity(currencyDTO);
        newCurrency = service.create(newCurrency);

        responseHandler.writeResponse(resp, newCurrency);
    }
}
