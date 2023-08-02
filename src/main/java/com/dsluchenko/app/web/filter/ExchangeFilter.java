package com.dsluchenko.app.web.filter;

import com.dsluchenko.app.web.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;

import static com.dsluchenko.app.web.filter.ValidationConstants.*;

@WebFilter(urlPatterns = "/exchange/*", servletNames = "ExchangeServlet")
public class ExchangeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();

        switch (method) {
            case "GET":
                validateExchangeParametersInUrl(request);
                String from = request.getParameter("from").toUpperCase();
                String to = request.getParameter("to").toUpperCase();
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));

                request.setAttribute("from", from);
                request.setAttribute("to", to);
                request.setAttribute("amount", amount);
                break;
            default:
                break;
        }

        chain.doFilter(request, response);
    }

    private void validateExchangeParametersInUrl(ServletRequest request) {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amount = request.getParameter("amount");

        try {
            if (from.length() != CURRENCY_CODE_LENGTH || to.length() != CURRENCY_CODE_LENGTH)
                throw new BadParametersRuntimeException(String.format(
                        "Wrong request parameters: " +
                                "from = %s expected length: %d actual length: %d ," +
                                "to = %s expected length: %d actual length: %d ",
                        from, CURRENCY_CODE_LENGTH, from.length(),
                        to, CURRENCY_CODE_LENGTH, to.length()));

            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw new BadParametersRuntimeException(String.format(
                    "Uncorrected amount data type: %s, expected double",
                    amount
            ));
        } catch (NullPointerException e) {
            throw new BadParametersRuntimeException("Uncorrected expected query parameters:<from>&<to>&<amount>");
        }
    }
}
