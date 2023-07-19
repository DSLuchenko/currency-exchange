package com.dsluchenko.app.servlet.filter;

import com.dsluchenko.app.servlet.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;


@WebFilter("/exchangeRate/*")
public class ExchangeRateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();

        switch (method) {
            case "POST":
                //TODO checkReqCreateExchangeRate
                break;
            case "GET":
                checkReqGetByCurrencyCodes(httpRequest);
                break;
            default:
                break;
        }

        chain.doFilter(request, response);
    }

    private void checkReqGetByCurrencyCodes(HttpServletRequest servletRequest) {
        String pathInfo = servletRequest.getPathInfo();

        if (pathInfo == null || pathInfo.substring(1)
                                        .length() != 6)
            throw new BadParametersRuntimeException();
    }
}
