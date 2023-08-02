package com.dsluchenko.app.web.filter;

import com.dsluchenko.app.dto.request.ExchangeRateCreateRequest;
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

import static com.dsluchenko.app.web.filter.ValidationConstants.*;

@WebFilter(urlPatterns = "/exchangeRate/*", servletNames = "ExchangeRateServlet")
public class ExchangeRateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();

        String baseCode, targetCode;

        switch (method) {
            case "POST":
                ExchangeRateCreateRequest exchangeRateRequest = getExchangeRateRequestFromBody(httpRequest);
                request.setAttribute("dto", exchangeRateRequest);
                break;
            case "PATCH":
                validateCurrencyCodesInUrl(httpRequest.getPathInfo());

                baseCode = httpRequest.getPathInfo().substring(
                        START_INDEX_FIRST_CURRENCY_CODE_IN_URL,
                        END_INDEX_FIRST_CURRENCY_CODE_IN_URL);
                targetCode = httpRequest.getPathInfo().substring(END_INDEX_FIRST_CURRENCY_CODE_IN_URL);
                BigDecimal rate = getRateFromBody(httpRequest);

                request.setAttribute("rate", rate);
                request.setAttribute("baseCode", baseCode);
                request.setAttribute("targetCode", targetCode);
                break;
            case "GET":
                validateCurrencyCodesInUrl(httpRequest.getPathInfo());
                baseCode = httpRequest.getPathInfo().substring(
                        START_INDEX_FIRST_CURRENCY_CODE_IN_URL,
                        END_INDEX_FIRST_CURRENCY_CODE_IN_URL);
                targetCode = httpRequest.getPathInfo().substring(END_INDEX_FIRST_CURRENCY_CODE_IN_URL);

                request.setAttribute("baseCode", baseCode);
                request.setAttribute("targetCode", targetCode);
                break;
            default:
                break;
        }

        chain.doFilter(request, response);
    }

    private ExchangeRateCreateRequest getExchangeRateRequestFromBody(ServletRequest request) {
        List<String> exchangeRateFields = Arrays.stream(ExchangeRateCreateRequest.class.getDeclaredFields())
                                                .toList()
                                                .stream()
                                                .map(Field::getName)
                                                .sorted()
                                                .toList();

        List<String> requestFormFields = Collections.list(request.getParameterNames());
        Collections.sort(requestFormFields);

        if (!exchangeRateFields.equals(requestFormFields))
            throw new BadParametersRuntimeException(String.format(
                    "expected parameters: %s actual parameters: %s",
                    Arrays.toString(exchangeRateFields.toArray()),
                    Arrays.toString(requestFormFields.toArray())));

        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        BigDecimal rate;

        if (baseCurrencyCode.length() != CURRENCY_CODE_LENGTH || targetCurrencyCode.length() != CURRENCY_CODE_LENGTH)
            throw new BadParametersRuntimeException(String.format(
                    "Wrong request parameters: " +
                            "baseCurrencyCode = %s expected length: %d actual length: %d ," +
                            "targetCurrencyCode = %s expected length: %d actual length: %d ",
                    baseCurrencyCode, CURRENCY_CODE_LENGTH, baseCurrencyCode.length(),
                    targetCurrencyCode, CURRENCY_CODE_LENGTH, targetCurrencyCode.length()));
        try {
            rate = BigDecimal.valueOf(Double.parseDouble(request.getParameter("rate")));
            return new ExchangeRateCreateRequest(baseCurrencyCode, targetCurrencyCode, rate);
        } catch (NumberFormatException e) {
            throw new BadParametersRuntimeException(String.format(
                    "Uncorrected rate data type: %s, expected double",
                    request.getParameter("rate")
            ));
        }
    }

    private void validateCurrencyCodesInUrl(String pathInfo) {
        String currencyCodesFromUrl = Optional.ofNullable(pathInfo)
                                              .orElseThrow(BadParametersRuntimeException::new)
                                              .substring(START_INDEX_FIRST_CURRENCY_CODE_IN_URL);

        if (currencyCodesFromUrl.length() != CURRENCY_CODE_LENGTH * 2)
            throw new BadParametersRuntimeException(String.format(
                    "Uncorrected length URI parameter: %s, expected length: %d actual length: %d",
                    currencyCodesFromUrl,
                    CURRENCY_CODE_LENGTH * 2,
                    currencyCodesFromUrl.length()));
    }

    private BigDecimal getRateFromBody(HttpServletRequest servletRequest) {
        try (BufferedReader reader = servletRequest.getReader()) {

            String data = reader.readLine();
            String[] body = data.split("=");
            String parameterName = body[0];
            String parameterValue = body[1];

            if (!parameterName.equals("rate")) throw new BadParametersRuntimeException(String.format(
                    "expected parameter name: rate, actual parameter: %s", parameterName));

            BigDecimal rate = BigDecimal.valueOf(
                    Double.parseDouble(parameterValue));

            return rate;

        } catch (NumberFormatException | IOException e) {
            throw new BadParametersRuntimeException(String.format(
                    "Uncorrected rate data type, expected double"
            ));
        }
    }
}
