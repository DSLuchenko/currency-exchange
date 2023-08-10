package com.dsluchenko.app.web.filter;

import com.dsluchenko.app.dto.request.CurrencyCreateRequest;
import com.dsluchenko.app.web.exception.BadParametersException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.dsluchenko.app.web.filter.ValidationConstants.*;


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
                                                            .substring(START_INDEX_FIRST_CURRENCY_CODE_IN_URL));
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
            throw new BadParametersException(String.format(
                    "expected parameters: %s actual parameters: %s",
                    Arrays.toString(currencyFields.toArray()),
                    Arrays.toString(requestFormFields.toArray())));


        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        if (code.length() != CURRENCY_CODE_LENGTH || sign.length() != SIGN_LENGTH) {
            throw new BadParametersException(String.format("Wrong request parameters: " +
                            "code = %s expected length: %d actual length: %d ," +
                            "sing = %s expected length: %d actual length: %d ",
                    code, CURRENCY_CODE_LENGTH, code.length(),
                    sign, SIGN_LENGTH, sign.length()));
        }
    }

    private void validateCurrencyCodeInUrl(String pathInfo) {

        String code = Optional.ofNullable(pathInfo)
                              .orElseThrow(
                                      BadParametersException::new)
                              .substring(START_INDEX_FIRST_CURRENCY_CODE_IN_URL);

        if (code.length() != CURRENCY_CODE_LENGTH)
            throw new BadParametersException(String.format(
                    "Uncorrected length URI parameter: %s, expected length: %d actual length: %d",
                    code,
                    CURRENCY_CODE_LENGTH,
                    code.length()));
    }
}
