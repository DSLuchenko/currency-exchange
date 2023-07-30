package com.dsluchenko.app.web.filter;

import com.dsluchenko.app.dto.request.CurrencyCreateRequest;
import com.dsluchenko.app.web.exception.BadParametersRuntimeException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


@WebFilter(urlPatterns = "/currency/*", servletNames = "CurrencyServlet")
public class CurrencyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();

        switch (method) {
            case "POST":
                validateCurrencyParametersInBody(request);
                String name = request.getParameter("name");
                String code = request.getParameter("code").toUpperCase();
                String sign = request.getParameter("sign");
                request.setAttribute("dto", new CurrencyCreateRequest(name, code, sign));
                break;
            case "GET":
                validateCurrencyCodeInUrl(httpRequest.getPathInfo());
                httpRequest.setAttribute("code", httpRequest.getPathInfo()
                                                            .substring(1));
                break;
            default:
                break;
        }
        chain.doFilter(request, response);
    }

    private void validateCurrencyParametersInBody(ServletRequest request) {
        List<String> currencyFields = Arrays.stream(CurrencyCreateRequest.class.getDeclaredFields())
                                            .toList()
                                            .stream()
                                            .map(Field::getName)
                                            .sorted()
                                            .toList();

        List<String> requestFormFields = Collections.list(request.getParameterNames());

        Collections.sort(requestFormFields);

        if (!currencyFields.equals(requestFormFields))
            throw new BadParametersRuntimeException();


        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        if (code.length() != 3 || sign.length() != 1) {
            throw new BadParametersRuntimeException("Wrong request parameters:" +
                    " 'code' length must be 3 characters" +
                    " and 'sign' length must be 1 character");
        }
    }

    private void validateCurrencyCodeInUrl(String pathInfo) {
        String code = Optional.ofNullable(pathInfo)
                              .orElseThrow(
                                      BadParametersRuntimeException::new)
                              .substring(1);

        if (code.length() != 3)
            throw new BadParametersRuntimeException();
    }
}
