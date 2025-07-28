package com.dineconnect.backend.user.service;

import com.dineconnect.backend.user.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

    String getCurrentUsername();

    CustomUserDetails loadUserByUsername(String username);
}
