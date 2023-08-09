package com.dsluchenko.app.web.servlet;

import com.dsluchenko.app.service.Service;
import com.dsluchenko.app.service.exception.IntegrityViolationRuntimeException;
import com.dsluchenko.app.service.exception.CurrencyNotFoundRuntimeException;
import com.dsluchenko.app.service.exception.ExchangeRateNotFoundRuntimeException;
import com.dsluchenko.app.service.exception.ApplicationRuntimeException;

import com.dsluchenko.app.web.ResponseHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;


class BaseServlet extends HttpServlet {
    protected ResponseHandler responseHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        responseHandler = (ResponseHandler) config.getServletContext().getAttribute(ResponseHandler.class.getSimpleName());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            if (req.getMethod().equalsIgnoreCase("PATCH")) {
                doPatch(req, resp);
            } else {
                super.service(req, resp);
            }
        } catch (IntegrityViolationRuntimeException e) {
            responseHandler.writeError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (CurrencyNotFoundRuntimeException | ExchangeRateNotFoundRuntimeException e) {
            responseHandler.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (ApplicationRuntimeException e) {
            responseHandler.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            responseHandler.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
    }

    protected <T extends Service> T getServiceFromContext(ServletContext sc, Class<T> type) {
        var services = (Map<String, Service>) sc.getAttribute("services");
        var service = services.get(type.getSimpleName());
        return type.cast(service);
    }
}
