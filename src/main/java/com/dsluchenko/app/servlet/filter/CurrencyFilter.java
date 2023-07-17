package com.dsluchenko.app.servlet.filter;

import com.dsluchenko.app.dto.CurrencyDTO;
import com.dsluchenko.app.servlet.exception.BadParametersRuntimeException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@WebFilter(urlPatterns = "/currency/*", servletNames = "CurrencyServlet")
public class CurrencyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String method = httpRequest.getMethod();

        switch (method) {
            case "POST":
                checkReqCreateCurrency(httpRequest);
                break;
            case "GET":
                checkReqGetCurrencyByCode(httpRequest);
                break;
            default:
                break;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void checkReqCreateCurrency(HttpServletRequest servletRequest) {

        List<String> currencyFields = Arrays.stream(CurrencyDTO.class.getDeclaredFields())
                                            .toList()
                                            .stream()
                                            .map(f -> f.getName())
                                            .sorted()
                                            .toList();

        List<String> requestFormFields = Collections.list(servletRequest.getParameterNames());

        Collections.sort(requestFormFields);

        if (!currencyFields.equals(requestFormFields))
            throw new BadParametersRuntimeException();
    }

    private void checkReqGetCurrencyByCode(HttpServletRequest servletRequest) {
        String pathInfo = servletRequest.getPathInfo();

        if (pathInfo == null || pathInfo.substring(1)
                                        .length() != 3)
            throw new BadParametersRuntimeException();

    }
}
