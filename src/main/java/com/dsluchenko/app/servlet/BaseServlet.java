package com.dsluchenko.app.servlet;

import com.dsluchenko.app.service.exception.CurrencyIntegrityViolationRuntimeException;
import com.dsluchenko.app.service.exception.CurrencyNotFoundException;
import com.dsluchenko.app.service.exception.ExchangeRateNotFoundRuntimeException;
import com.dsluchenko.app.service.exception.ServerRuntimeException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            super.service(req, resp);
        } catch (CurrencyIntegrityViolationRuntimeException e) {
            ResponseHandler.writeError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (CurrencyNotFoundException e) {
            ResponseHandler.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (ExchangeRateNotFoundRuntimeException e) {
            ResponseHandler.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (ServerRuntimeException e) {
            ResponseHandler.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
