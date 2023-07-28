package com.dsluchenko.app.servlet.filter;

import com.dsluchenko.app.servlet.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;

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
            if (from.length() != 3 || to.length() != 3)
                throw new BadParametersRuntimeException();

            Double.parseDouble(amount);
        } catch (Exception e) {
            throw new BadParametersRuntimeException();
        }

    }
}
