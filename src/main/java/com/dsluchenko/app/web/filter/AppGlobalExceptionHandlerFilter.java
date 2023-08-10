package com.dsluchenko.app.web.filter;

import com.dsluchenko.app.exception.ApplicationRuntimeException;
import com.dsluchenko.app.web.ResponseHandler;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/*")
public class AppGlobalExceptionHandlerFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AppGlobalExceptionHandlerFilter.class.getName());
    private ResponseHandler responseHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        responseHandler = (ResponseHandler) filterConfig.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            logger.info(String.format("REQUEST: URI - %s, method - %s.",
                    ((HttpServletRequest) request).getRequestURI(),
                    ((HttpServletRequest) request).getMethod()));

            chain.doFilter(request, response);
        } catch (ApplicationRuntimeException e) {

            logger.log(Level.WARNING, e.getMessage(), e);
            responseHandler.writeError(response, e);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            responseHandler.writeError(response, new ApplicationRuntimeException());
        }
        logger.info(String.format("RESPONSE: status - %d.",
                ((HttpServletResponse) response).getStatus()));
    }
}
