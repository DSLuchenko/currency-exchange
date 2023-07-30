package com.dsluchenko.app.web;

import com.dsluchenko.app.dto.response.ErrorResponse;
import com.dsluchenko.app.service.exception.ApplicationRuntimeException;
import com.google.gson.Gson;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public final class ResponseHandler {
    public void writeError(ServletResponse resp, int errorCode, String errorMessage) {
        try (PrintWriter writer = resp.getWriter()) {
            ((HttpServletResponse) resp).setStatus(errorCode);
            ErrorResponse error = new ErrorResponse(errorMessage);
            String errorJson = new Gson().toJson(error);
            writer.write(errorJson);
        } catch (Exception e) {
            throw new ApplicationRuntimeException();
        }
    }

    public void writeResponse(ServletResponse resp, Object data) {
        try (PrintWriter writer = resp.getWriter()) {
            ((HttpServletResponse) resp).setStatus(HttpServletResponse.SC_OK);
            String responseData = new Gson().toJson(data);
            writer.write(responseData);
        } catch (Exception e) {
            throw new ApplicationRuntimeException();
        }
    }
}
