package com.dsluchenko.app.web.filter;

import com.dsluchenko.app.dto.request.ExchangeRateRequest;
import com.dsluchenko.app.web.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@WebFilter("/exchangeRate/*")
public class ExchangeRateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();

        String baseCode, targetCode;

        switch (method) {
            case "POST":
                ExchangeRateRequest exchangeRateRequest = getExchangeRateRequestFromBody(httpRequest);
                request.setAttribute("dto", exchangeRateRequest);
                break;
            case "PATCH":
                validateCurrencyCodesInUrl(httpRequest.getPathInfo());

                baseCode = httpRequest.getPathInfo().substring(1, 4);
                targetCode = httpRequest.getPathInfo().substring(4);
                BigDecimal rate = getRateFromBody(httpRequest);

                request.setAttribute("rate", rate);
                request.setAttribute("baseCode", baseCode);
                request.setAttribute("targetCode", targetCode);
                break;
            case "GET":
                validateCurrencyCodesInUrl(httpRequest.getPathInfo());
                baseCode = httpRequest.getPathInfo().substring(1, 4);
                targetCode = httpRequest.getPathInfo().substring(4);

                request.setAttribute("baseCode", baseCode);
                request.setAttribute("targetCode", targetCode);
                break;
            default:
                break;
        }

        chain.doFilter(request, response);
    }

    private ExchangeRateRequest getExchangeRateRequestFromBody(ServletRequest request) {
        List<String> exchangeRateFields = Arrays.stream(ExchangeRateRequest.class.getDeclaredFields())
                                                .toList()
                                                .stream()
                                                .map(Field::getName)
                                                .sorted()
                                                .toList();

        List<String> requestFormFields = Collections.list(request.getParameterNames());
        Collections.sort(requestFormFields);

        if (!exchangeRateFields.equals(requestFormFields))
            throw new BadParametersRuntimeException();

        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String baseTargetCode = request.getParameter("targetCurrencyCode");
        BigDecimal rate;

        if (baseCurrencyCode.length() != 3 || baseTargetCode.length() != 3)
            throw new BadParametersRuntimeException("Wrong request parameters:" +
                    " 'baseCurrencyCode' and 'baseTargetCode' length must be 3 characters");
        try {
            rate = BigDecimal.valueOf(Double.parseDouble(request.getParameter("rate")));
            return new ExchangeRateRequest(baseCurrencyCode, baseTargetCode, rate);
        } catch (Exception e) {
            throw new BadParametersRuntimeException();
        }
    }

    private void validateCurrencyCodesInUrl(String pathInfo) {
        String currencyCodesFromUrl = Optional.ofNullable(pathInfo)
                                              .orElseThrow(BadParametersRuntimeException::new)
                                              .substring(1);

        if (currencyCodesFromUrl.length() != 6)
            throw new BadParametersRuntimeException();
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
