package com.dineconnect.backend.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dineconnect.backend.dto.AuthRequest;
import com.dineconnect.backend.dto.AuthResponse;
import com.dineconnect.backend.security.service.JwtService;
import com.dineconnect.backend.user.model.User;
import com.dineconnect.backend.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/admin/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Authentication", description = "Endpoints for admin authentication")
public class AdminAuthController {

    private final UserService userService;
    private final JwtService jwtService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new admin", description = "Creates a new admin user and returns a JWT token")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerAdmin(@RequestBody  AuthRequest authRequest) {
        User user = userService.createUser(authRequest.email(), authRequest.username(), authRequest.password(), true);
        return new AuthResponse(jwtService.generateToken(user.getUsername(),true));
    }
}
