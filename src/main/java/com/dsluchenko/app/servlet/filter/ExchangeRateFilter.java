package com.dsluchenko.app.servlet.filter;

import com.dsluchenko.app.servlet.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;


@WebFilter("/exchangeRate/*")
public class ExchangeRateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();

        String pathInfo = Optional.ofNullable(httpRequest.getPathInfo())
                                  .orElseThrow(
                                          BadParametersRuntimeException::new);

        String currencyCodesFromUrl = pathInfo.substring(1);

        if (currencyCodesFromUrl.length() != 6)
            throw new BadParametersRuntimeException();

        String baseCode = currencyCodesFromUrl.substring(0, 3);
        String targetCode = currencyCodesFromUrl.substring(3);

        switch (method) {
            case "POST":
                //TODO checkReqCreateExchangeRate
                break;
            case "PATCH":
                BigDecimal rate = getRateFromBody(httpRequest);
                request.setAttribute("rate", rate);
                request.setAttribute("baseCode", baseCode);
                request.setAttribute("targetCode", targetCode);
                break;
            case "GET":
                request.setAttribute("baseCode", baseCode);
                request.setAttribute("targetCode", targetCode);
                break;
            default:
                break;
        }

        chain.doFilter(request, response);
    }

    private BigDecimal getRateFromBody(HttpServletRequest servletRequest) {
        try (BufferedReader reader = servletRequest.getReader()) {

            String data = reader.readLine();
            String[] body = data.split("=");
            String parameterName = body[0];
            String parameterValue = body[1];

            if (!parameterName.equals("rate")) throw new BadParametersRuntimeException();

            BigDecimal rate = BigDecimal.valueOf(
                    Double.parseDouble(parameterValue));

            return rate;

        } catch (Exception e) {
            throw new BadParametersRuntimeException();
        }
    }
}
