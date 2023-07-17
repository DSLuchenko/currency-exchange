package com.dsluchenko.app.servlet.filter;

import com.dsluchenko.app.service.exception.CurrencyIntegrityViolationRuntimeException;
import com.dsluchenko.app.servlet.ResponseHandler;
import com.dsluchenko.app.servlet.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/*", servletNames = "CurrencyServlet")
public class BaseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            chain.doFilter(request, response);
        } catch (BadParametersRuntimeException e) {
            ResponseHandler.writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
