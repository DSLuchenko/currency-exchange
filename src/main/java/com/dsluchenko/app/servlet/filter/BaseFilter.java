package com.dsluchenko.app.servlet.filter;

import com.dsluchenko.app.servlet.ResponseHandler;
import com.dsluchenko.app.servlet.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/*", dispatcherTypes = DispatcherType.REQUEST)
public class BaseFilter implements Filter {
    private static final Logger logger = Logger.getLogger(BaseFilter.class.getName());
    private ResponseHandler responseHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        responseHandler = (ResponseHandler) filterConfig.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            chain.doFilter(request, response);
        } catch (BadParametersRuntimeException e) {
            responseHandler.writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            responseHandler.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
