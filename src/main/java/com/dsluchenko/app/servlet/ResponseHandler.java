package com.dsluchenko.app.servlet;

import com.dsluchenko.app.dto.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class ResponseHandler {
    public static void writeError(ServletResponse resp, int errorCode, String errorMessage) throws IOException {
        ((HttpServletResponse) resp).setStatus(errorCode);
        ErrorResponse error = new ErrorResponse(errorMessage);
        String errorJson = new Gson().toJson(error);
        resp.getWriter().write(errorJson);
        resp.getWriter().close();
    }
}
