package com.dineconnect.backend.exception.util;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class ErrorResponseWriter {

    public static void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("""
            {
                "status": "error",
                "message": "%s"
            }
        """, message));
    }
}

