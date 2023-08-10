package com.dsluchenko.app.web.servlet;

import com.dsluchenko.app.exception.ApplicationRuntimeException;
import com.dsluchenko.app.service.Service;

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
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
    }

    protected <T extends Service> T getServiceFromContext(ServletContext sc, Class<T> type) {
        try {
            var services = (Map<String, Service>) sc.getAttribute("services");
            var service = services.get(type.getSimpleName());
            return type.cast(service);
        } catch (Exception e) {
            throw new ApplicationRuntimeException();
        }
    }
}
