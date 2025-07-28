package com.dineconnect.backend.user.service;

import com.dineconnect.backend.user.model.CustomUserDetails;
import com.dineconnect.backend.user.model.Role;
import com.dineconnect.backend.user.model.User;
import com.dineconnect.backend.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (Objects.equals(email, adminEmail)){
            return new User(
                    adminEmail,
                    adminUsername,
                    passwordEncoder.encode(adminPassword),
                    Role.ADMIN
            );
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public String getCurrentUsername() {
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();
    }
}
