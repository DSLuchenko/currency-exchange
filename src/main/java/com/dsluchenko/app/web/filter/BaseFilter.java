package com.dsluchenko.app.web.filter;

import com.dsluchenko.app.service.exception.ApplicationRuntimeException;
import com.dsluchenko.app.web.ResponseHandler;
import com.dsluchenko.app.web.exception.BadParametersRuntimeException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/*")
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
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
        try {
            logger.info(String.format("REQUEST: URI - %s, method - %s.",
                    ((HttpServletRequest) request).getRequestURI(),
                    ((HttpServletRequest) request).getMethod()));

            chain.doFilter(request, response);
        } catch (BadParametersRuntimeException e) {
            logger.log(Level.WARNING, e.getMessage(), e);

            responseHandler.writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);

            responseHandler.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
        logger.info(String.format("RESPONSE: status - %d.",
                ((HttpServletResponse) response).getStatus()));
    }
}
