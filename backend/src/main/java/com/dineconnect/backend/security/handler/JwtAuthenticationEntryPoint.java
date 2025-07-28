package com.dineconnect.backend.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.dineconnect.backend.exception.util.ErrorResponseWriter;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorResponseWriter.write(
            response, 
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized access - Invalid or missing token");
    }
}

