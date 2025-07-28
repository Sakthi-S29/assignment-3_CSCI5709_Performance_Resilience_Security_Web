package com.dineconnect.backend.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.dineconnect.backend.exception.util.ErrorResponseWriter;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        ErrorResponseWriter.write(
            response,
            HttpServletResponse.SC_FORBIDDEN,
            "Access denied - You do not have permission to access this resource"
        );
    }
}
